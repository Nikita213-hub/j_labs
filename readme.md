# Lab 1 Books API

Spring Boot REST API for managing an in-memory library of books.

## Endpoints
- `GET /api/v1/books` – return all books.
- `GET /api/v1/books?author={name}` – filter books by author substring.
- `GET /api/v1/books/{id}` – fetch a single book by UUID.
- `POST /api/v1/books` – create a book from JSON payload.

## Tooling
- Run `mvn -DskipTests=false verify sonar:sonar` after configuring your Sonar token to send reports to SonarCloud (project dashboard: https://sonarcloud.io/project/configuration?id=Nikita213-hub_j_labs).
