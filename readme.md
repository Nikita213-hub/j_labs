# Lab 1 Bug Tracker API

Spring Boot REST API for reporting and tracking software bugs by QA engineers and developers.

## Endpoints
- `GET /api/v1/bugs` – return all bugs or filter by `status` query parameter (OPEN/IN_PROGRESS/IN_REVIEW/RESOLVED).
- `GET /api/v1/bugs/{id}` – fetch a single bug by UUID.
- `POST /api/v1/bugs` – create a new bug report (title, description, priority, reporter, optional assignee).

## Tooling
- Run `mvn -DskipTests=false verify sonar:sonar` after configuring your Sonar token to send reports to SonarCloud (project dashboard: https://sonarcloud.io/summary/new_code?id=Nikita213-hub_j_labs&branch=main).
