# Task Manager Project

This is a back-end Task Management system built with Spring Boot, focused on multi-project collaboration with robust security and business rules.

## Tech Stack
- **Framework:** Spring Boot 4.0.4
- **Language:** Java 21
- **Security:** Spring Security with JWT (Stateless)
- **Database:** H2 (File-based)
- **Persistence:** Spring Data JPA / Hibernate 7
- **Utilities:** Lombok, Maven, JJWT

## Architecture
The project follows a layered architecture to maintain a clean separation of concerns:
- **Controllers:** Handle REST API endpoints.
- **Gateways:** Act as an orchestration layer between Controllers and Services, utilizing **Translators** to convert between Request/Response DTOs and Domain Entities.
- **Services:** Contain the core business logic and rule validation.
- **Domain:** Contains JPA Entities and Enums representing the business model.
- **Repository:** Interfaces for database access using Spring Data JPA.
- **Config:** Security and Application configurations (separated to prevent circular dependencies).

## Key Features & Rules

### 1. Authentication & Authorization
- **JWT-based:** Authentication is performed via `/public/auth/login`, returning a Bearer token.
- **Profiles (Roles):**
    - `ADMIN`: Full access to manage all projects, members, and close any tasks.
    - `MEMBER`: Can manage tasks within projects they belong to.
- **Password Security:** All passwords are encrypted using BCrypt before storage.
- **Public Endpoints:** `/public/**` (Registration, Login, Swagger UI).
- **Protected Endpoints:** `/api/**` (requires valid JWT).

### 2. Project Management
- **Ownership:** Every project has an `Owner` (the user who created it).
- **Membership:** Projects can have multiple `Members`.
- **Visibility:** Users can only see and interact with projects where they are either the Owner or a Member. ADMINs can see all projects.
- **Permissions:** Only `ADMIN` users are authorized to Create or Update projects.

### 3. Task Management
- **Attributes:** Title, Description, Status, Priority, Due Date, Assignee, and automatic Timestamps (`createdAt`, `updatedAt`).
- **Status Flow:** `TODO` -> `IN_PROGRESS` -> `DONE`.
    - *Transition Rule:* A task cannot be moved back from `DONE` to `TODO`.
- **Priority:** `LOW`, `MEDIUM`, `HIGH`, `CRITICAL`.
- **Assignment:** The `Assignee` of a task must be a member of the project.
- **Critical Task Rule:** A task with `CRITICAL` priority can only be moved to `DONE` by an `ADMIN` or the project's `Owner`.

## API Endpoints Summary
- `POST /public/register`: Register a new user.
- `POST /public/auth/login`: Authenticate and get JWT.
- `GET /api/project`: List available projects (filtered by permission).
- `POST /api/project`: Create a new project (ADMIN only).
- `GET /api/task`: List tasks (filtered by project membership).
- `PATCH /api/task/{id}/status`: Update only the status of a task.

## Development & Execution
- **Compile:** `./mvnw clean compile`
- **Run:** `./mvnw spring-boot:run`
- **Database Console:** Available at `/h2-console` (Credentials in `application.yaml`).
- **Documentation:** Swagger UI available at `/swagger-ui.html`.
