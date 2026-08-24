# Engineering Intelligence Hub Backend

Spring Boot backend for an engineering operations hub. The API manages users, teams, projects, project documents, incidents, audit logs, and an admin dashboard. Authentication is cookie-based JWT with Spring Security method-level authorization on the service layer.

## Tech Stack

- Java 21
- Spring Boot 3.5.16
- Spring Web
- Spring Security
- Spring Data JPA / Hibernate
- Bean Validation
- MySQL
- JJWT 0.12.6
- Lombok
- Maven Wrapper

## Core Domain

The application is built around these entities:

- `User`: account with unique `username`, unique `email`, BCrypt password, a `Role`, and many-to-many team membership.
- `Team`: unique team name, members, and projects.
- `Project`: belongs to one team and owns documents and incidents.
- `Document`: project-scoped knowledge item uploaded by a user.
- `Incident`: project-scoped work item assigned to a user and tracked through a status lifecycle.
- `AuditLog`: immutable audit history containing event type, username, description, and creation time.

Roles are:

- `ADMIN`: global administration across users, teams, projects, documents, incidents, audits, and dashboard metrics.
- `MANAGER`: can create projects and incidents for their teams, and close resolved incidents for their teams.
- `ENGINEER`: can access team projects, upload documents, and progress incidents assigned to them.

Incident statuses are:

```text
OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED
```

## Project Structure

```text
src/main/java/com/pranav/engineering_intelligence_hub
|-- config          Security and CORS configuration
|-- controller      REST controllers
|-- dto             Request and response DTO records
|-- entity          JPA entities and enums
|-- exceptions      Custom exceptions and global exception mapping
|-- mapper          Entity/DTO mapping components
|-- repository      Spring Data JPA repositories
|-- security        JWT filter, JWT service, and UserDetails integration
|-- service         Business logic and authorization rules
|-- util            Cookie helper for JWT cookies
```

## Prerequisites

- JDK 21
- MySQL running locally or reachable from the application
- Maven Wrapper from this repository

Create the database expected by `application.properties`:

```sql
CREATE DATABASE engineering_hub;
```

Hibernate is configured with `spring.jpa.hibernate.ddl-auto=update`, so tables are created and updated automatically from the JPA entities.

## Configuration

The application reads database, JWT, and cookie settings from environment variables:

| Variable | Purpose | Local example |
| --- | --- | --- |
| `DB_USERNAME` | MySQL username | `root` |
| `DB_PASSWORD` | MySQL password | `password` |
| `JWT_SECRET` | HMAC signing secret for JWTs. Use at least 32 ASCII characters. | `change-this-to-a-long-32-char-secret` |
| `COOKIE_SECURE` | Whether the JWT cookie is marked `Secure` | `false` |
| `COOKIE_SAME_SITE` | SameSite policy for the JWT cookie | `Lax` |

Current datasource settings:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/engineering_hub
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

For local frontend development, CORS allows `http://localhost:3000` with credentials enabled.

## Running Locally

Windows PowerShell:

```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="password"
$env:JWT_SECRET="change-this-to-a-long-32-char-secret"
$env:COOKIE_SECURE="false"
$env:COOKIE_SAME_SITE="Lax"
.\mvnw.cmd spring-boot:run
```

macOS/Linux:

```bash
export DB_USERNAME=root
export DB_PASSWORD=password
export JWT_SECRET=change-this-to-a-long-32-char-secret
export COOKIE_SECURE=false
export COOKIE_SAME_SITE=Lax
./mvnw spring-boot:run
```

The API starts on Spring Boot's default port:

```text
http://localhost:8080
```

## Tests

Run the test suite:

```powershell
.\mvnw.cmd test
```

The current test coverage contains a Spring context-load test. It uses the main application configuration, so the same database, JWT, and cookie environment variables must be available when tests run.

## Authentication and CSRF Flow

Security is stateless. The server stores no HTTP session and authenticates requests with an HttpOnly `jwt` cookie.

1. Call `GET /api/auth/csrf` to receive a CSRF token and the `XSRF-TOKEN` cookie.
2. Send mutating requests with credentials/cookies and the `X-XSRF-TOKEN` header.
3. Call `POST /api/auth/register` or `POST /api/auth/login`.
4. The server responds with a `Set-Cookie` header for the HttpOnly `jwt` cookie.
5. Authenticated requests rely on that `jwt` cookie.
6. Call `POST /api/auth/logout` to clear the `jwt` cookie.

Only `/api/auth/**` is publicly accessible. All other routes require authentication. Most role restrictions are enforced in service methods with `@PreAuthorize`.

New registrations are created as `ENGINEER`. To bootstrap the first admin in a local database, register a user and update their role manually:

```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'your_username';
```

## API Reference

### Auth

| Method | Endpoint | Auth | Description |
| --- | --- | --- | --- |
| `GET` | `/api/auth/csrf` | Public | Returns the current CSRF token. |
| `POST` | `/api/auth/register` | Public plus CSRF | Creates an `ENGINEER` account and sets the JWT cookie. |
| `POST` | `/api/auth/login` | Public plus CSRF | Authenticates username/password and sets the JWT cookie. |
| `POST` | `/api/auth/logout` | Public plus CSRF | Clears the JWT cookie. |

Request bodies:

```json
{
  "username": "pranav",
  "email": "pranav@example.com",
  "password": "Pranav123"
}
```

```json
{
  "username": "pranav",
  "password": "Pranav123"
}
```

### Users and Admin

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/users/{id}` | Authenticated | Returns a user's `id`, `username`, and `email`. |
| `POST` | `/api/admin/users/{id}/promote` | `ADMIN` | Promotes an `ENGINEER` to `MANAGER`. |
| `POST` | `/api/admin/users/{id}/demote` | `ADMIN` | Demotes a `MANAGER` to `ENGINEER`. |
| `POST` | `/api/admin/users/{userId}/teams/{teamId}` | `ADMIN` | Adds a user to a team. |
| `DELETE` | `/api/admin/users/{userId}/teams/{teamId}` | `ADMIN` | Removes a user from a team. |
| `GET` | `/api/admin/dashboard` | `ADMIN` | Returns aggregate counts for users, teams, projects, documents, and incidents by status. |

### Teams

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `POST` | `/api/teams` | `ADMIN` | Creates a team with a unique name. |
| `DELETE` | `/api/teams/{id}` | `ADMIN` | Deletes a team. |

Request body:

```json
{
  "name": "Platform"
}
```

### Projects

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `POST` | `/api/projects` | `ADMIN`, `MANAGER` | Creates a project for a team. Managers must belong to that team. |
| `GET` | `/api/projects/{id}` | `ADMIN`, team member | Returns one project. |
| `GET` | `/api/projects/teams/{teamId}` | `ADMIN`, team member | Returns all projects for a team. |

Request body:

```json
{
  "name": "Observability Upgrade",
  "description": "Improve dashboards and alert coverage.",
  "teamId": 1
}
```

### Documents

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `POST` | `/api/documents/projects/{projectId}` | `ADMIN`, team member | Creates a document for a project. Document titles are globally unique. |
| `GET` | `/api/documents/projects/{projectId}` | `ADMIN`, team member | Lists documents for a project. |
| `DELETE` | `/api/documents/{id}` | `ADMIN`, uploader | Deletes a document. |

Request body:

```json
{
  "title": "Runbook",
  "content": "Steps to diagnose and recover the service."
}
```

### Incidents

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `POST` | `/api/incidents/projects/{projectId}` | `ADMIN`, `MANAGER` | Creates an `OPEN` incident. Managers must belong to the project team. Assigned user must also belong to the project team. |
| `GET` | `/api/incidents/projects/{projectId}` | `ADMIN`, team member | Lists incidents for a project. |
| `DELETE` | `/api/incidents/{id}` | `ADMIN`, assigned user | Deletes an incident. |
| `PATCH` | `/api/incidents/{id}/start` | `ADMIN`, assigned user | Moves an incident from `OPEN` to `IN_PROGRESS`. |
| `PATCH` | `/api/incidents/{id}/resolve` | `ADMIN`, assigned user | Moves an incident from `IN_PROGRESS` to `RESOLVED`. |
| `PATCH` | `/api/incidents/{id}/close` | `ADMIN`, team manager | Moves an incident from `RESOLVED` to `CLOSED`. |

Request body:

```json
{
  "title": "Database latency spike",
  "description": "P95 latency exceeded the threshold.",
  "assignedEngineerUsername": "pranav"
}
```

### Audits

| Method | Endpoint | Role | Description |
| --- | --- | --- | --- |
| `GET` | `/api/audits` | `ADMIN` | Returns audit entries ordered newest first. |

Audited actions include team creation, project creation, document creation/deletion, incident lifecycle changes, user team assignment/removal, and user promotion/demotion.

## Error Handling

The global exception handler maps common failures to HTTP responses:

| Status | When it is used |
| --- | --- |
| `400 Bad Request` | Bean validation failures or invalid incident status transitions. |
| `403 Forbidden` | Authenticated user is not allowed to access or change the target resource. |
| `404 Not Found` | Missing user, team, project, document, or incident. |
| `409 Conflict` | Duplicate user username/email, duplicate team name, or duplicate document title. |

Validation errors return a field-to-message JSON object. Other handled domain errors return a plain string message.

## Implementation Notes

- `SecurityConfig` enables method security, disables form login and HTTP Basic, and installs `JwtAuthenticationFilter` before `UsernamePasswordAuthenticationFilter`.
- `CookieCsrfTokenRepository.withHttpOnlyFalse()` exposes the CSRF cookie so a frontend can read it and send the token header.
- `CookieService` creates a one-day HttpOnly `jwt` cookie using `COOKIE_SECURE` and `COOKIE_SAME_SITE`.
- `DashboardService` is admin-only and computes counts directly from repositories.
- `TempController` is a `CommandLineRunner` with commented development snippets; it does not expose HTTP endpoints.
