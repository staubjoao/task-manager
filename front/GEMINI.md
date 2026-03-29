# Task Manager Front-end

This is the front-end application for the Task Management system, built with Angular 18, focusing on a clean, minimalist UI and modern reactive patterns.

## Tech Stack
- **Framework:** Angular 18 (Standalone Components)
- **State Management:** Angular Signals (Modern reactivity)
- **Styling:** Vanilla CSS (Minimalist & Responsive)
- **Authentication:** JWT-based with `jwt-decode`
- **Communication:** HttpClient with Functional Interceptors

## Architecture
The project follows a modular and service-oriented architecture:
- **Components:** Standalone components for Login, Register, and Dashboard.
- **Services:** 
  - `AuthService`: Manages authentication state, JWT storage, and role-based signals (`isAdmin`, `userRole`).
  - `ProjectService`: Handles CRUD operations for projects.
  - `TaskService`: Manages task status updates and listing.
  - `UserService`: Retrieves system users for administrative tasks.
- **Guards:** `AuthGuard` protects private routes, ensuring only authenticated users access the dashboard.
- **Interceptors:** `AuthInterceptor` automatically attaches the Bearer token to all API requests.
- **Models:** Centralized TypeScript interfaces and enums for data consistency.

## Key Features

### 1. Authentication & Role-Based Access
- **Login/Register:** Secure entry points that store JWT in `localStorage`.
- **Signal-driven UI:** The interface reacts instantly to the user's role:
    - `ADMIN`: Full access to project management (create/edit) and user listing.
    - `MEMBER`: Access to task tracking and status updates.

### 2. Project Management (Admin Only)
- **Creation:** Form to create projects with name, description, and member assignment.
- **Edition:** Real-time editing of project details and member list.
- **Reactive Updates:** Uses Signals to update the UI without full page reloads.

### 3. Task Tracking
- **Unified View:** List of tasks assigned to the user or visible to the admin.
- **Status Flow:** Simple buttons to move tasks through the lifecycle (`TODO` -> `IN_PROGRESS` -> `DONE`).
- **Visual Feedback:** Colored badges indicating priority and status.

## Development & Execution
- **Install Dependencies:** `npm install`
- **Run Development Server:** `npm start` (available at `http://localhost:4200`)
- **Build for Production:** `npm run build`

## API Configuration
- **Base URL:** `http://localhost:8080`
- **CORS:** Configured in the back-end to allow requests from the front-end origin.

--- Context saved for future sessions ---
