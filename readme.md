# Bug Tracker API

Spring Boot 3.3 REST API for reporting and tracking software bugs backed by PostgreSQL.

## Data model
- `Project` – root aggregate, one project contains many `Bug`s (`@OneToMany` with `CascadeType.ALL`/`FetchType.LAZY`).
- `Bug` – owns status/priority and links to reporter & assignee (`UserAccount`), tags and comments.
- `UserAccount` – QA/Developer/Manager users (`@ManyToOne` from bugs/comments).
- `Tag` – reusable labels, linked to bugs via a `@ManyToMany` join table.
- `Comment` – child entity of a bug with cascade from `Bug.comments`.

Lazy loading is used for collections to avoid loading large graphs by default. Fetch plans are controlled through `@EntityGraph` for reporting scenarios and targeted fetch joins (see `/api/v1/bugs/details`).


## REST endpoints
| Method | Path | Description |
| --- | --- | --- |
| `GET` | `/api/v1/bugs?status=OPEN` | List bugs (optional status filter). |
| `GET` | `/api/v1/bugs/{id}` | Fetch a single bug summary. |
| `GET` | `/api/v1/bugs/{id}/details` | Fetch full bug graph (project, users, tags, comments). |
| `GET` | `/api/v1/bugs/details?optimized=true` | List details for many bugs. `optimized=false` purposefully causes N+1 queries, `true` uses `@EntityGraph` to solve it. |
| `POST` | `/api/v1/bugs` | Create new bug (title, description, priority, projectId, reporterId, optional assignee/tagIds). |
| `PUT` | `/api/v1/bugs/{id}` | Update an existing bug. |
| `DELETE` | `/api/v1/bugs/{id}` | Delete a bug. |
| `POST` | `/api/v1/demo/bulk?transactional={true|false}` | Creates a project with multiple related bugs using the supplied `BulkProjectRequest`. When `failAfterFirstBug=true` the non-transactional call leaves partial data while the transactional variant fully rolls back. |

`DatabaseSeeder` loads demo data (users/projects/tags/bugs) on startup so you can exercise the API immediately.

## N+1 illustration
- Call `GET /api/v1/bugs/details?optimized=false` with SQL logging enabled (default in `application.yml`) to see separate select statements per bug for project, reporter, tags and comments.
- Call the same endpoint with `optimized=true` to see a single select built with `@EntityGraph`, eliminating the N+1 explosion.

## Transactional save demo
`BulkProjectService` exposes two write paths through `/api/v1/demo/bulk`:
1. `transactional=false` → no surrounding transaction. When `failAfterFirstBug=true`, the project and the first bug are stored even though the method fails.
2. `transactional=true` → method annotated with `@Transactional`. Raising the same error rolls back both project and bug inserts.

Inspect the database contents between the two calls to observe the difference.

## Tooling
- Build & test: `mvn -DskipTests=false verify`
- Static analysis: `mvn -DskipTests=false verify sonar:sonar` (configure your Sonar token; project dashboard: https://sonarcloud.io/summary/new_code?id=Nikita213-hub_j_labs&branch=main)
