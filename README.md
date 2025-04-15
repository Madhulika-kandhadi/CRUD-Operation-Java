

# Widget Management System

The Widget Management System is a full-stack application for creating, reading, updating, and deleting widgets. The backend is built with **Spring Boot** and **Spring Data JPA**, using an **H2 in-memory database** for persistence. The frontend is a **React** application with **TypeScript**, featuring a form for widget CRUD operations, validated with **Yup**, and connected to the backend via **Axios**.

## Table of Contents
- [Features](#features)
- [Tech Stack](#technologies)
- [Prerequisites](#prerequisites)
- [Setup](#setup)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
- [Frontend Usage](#frontend-usage)
- [Testing](#testing)

## Features
- Create a widget with name, description, and price.
- Retrieve all widgets or a single widget by name.
- Update a widget’s description and/or price.
- Delete a widget by name.
- Input validation on both backend (JSR-303) and frontend (Yup).
- Transactional database operations for data integrity.
- Logging for debugging and monitoring.
- RESTful API with proper HTTP status codes (201, 204, 404, etc.).
- Frontend form for easy widget management.
- Unit tests for backend and frontend, plus Cypress end-to-end tests.

## Technologies
### Backend
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Gradle**
- **SLF4J/Logback** (logging)
- **JUnit 5** (testing)

### Frontend
- **React 18**
- **TypeScript**
- **Axios** (API client)
- **React Hook Form** (form handling)
- **Yup** (validation)
- **Material-UI** (styling)
- **Jest** (unit tests)
- **Cypress** (end-to-end tests)

## Prerequisites
- **Java 21
- **Node.js 20** or higher
- **Gradle 8.8+**
- **Git**
- A modern browser (e.g., Chrome, Firefox) for the frontend

## Setup

### Clone the Repository
```bash
git clone https://github.com/Madhulika-kandhadi/CRUD-Operation-Java.git
cd widget-management-system
```

### Backend Setup
1. Navigate to the backend directory (if separated, e.g., `backend/`).
2. Install dependencies:
   ```bash
   ./gradlew clean build
   ```
3. Verify `application.yaml` (in `src/main/resources`):
   

### Frontend Setup
1. Navigate to the frontend directory (e.g., `frontend/`):
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
   or
   ```bash
   yarn install
   ```
3. Verify the API base URL in `src/lib/apiConnect.ts`:
   ```typescript
   const api = axios.create({
     baseURL: 'http://localhost:9000/api/widgets',
     headers: { 'Content-Type': 'application/json' },
   });
   ```

## Running the Application

### Backend
1. From the backend directory:
   ```bash
   ./gradlew bootrun
   ```
2. The backend runs on `http://localhost:9000`.
3. Access the H2 console (optional) at `http://localhost:9000/h2-console` with JDBC URL `jdbc:h2:mem:testdb`.

### Frontend
1. From the frontend directory:
   ```bash
   npm start
   ```
   or
   ```bash
   yarn start
   ```
2. The frontend runs on `http://localhost:3000`.
3. Open `http://localhost:3000` in your browser to use the widget form.

## API Endpoints
The backend exposes a REST API at `/api/widgets`:

| Method | Endpoint | Description                     | Request Body                     | Response                     |
|--------|----------|---------------------------------|----------------------------------|------------------------------|
| `GET`  |          | Get all widgets                 | None                             | `Widget[]` (200 OK)          |
| `GET`  | `/{name}` | Get widget by name              | None                             | `Widget` (200 OK)            |
| `POST` |          | Create a widget                 | `{ name, description, price }`   | `Widget` (201 Created)       |
| `PATCH`| `/{name}` | Update widget’s description/price | `{ description?, price? }`     | `Widget` (200 OK)            |
| `DELETE` | `/{name}`| Delete widget by name           | None                             | None (204 No Content)        |

### Example Requests
- **Create Widget**:
  ```bash
  curl -X POST http://localhost:9000/v1/widgets -H "Content-Type: application/json" -d '{"name":"testwidget","description":"Test widget","price":9.99}'
  ```
- **Get Widget**:
  ```bash
  curl http://localhost:9000/v1/widgets/testwidget
  ```
- **Delete Widget**:
  ```bash
  curl -X DELETE http://localhost:9000/v1/widgets/testwidget
  ```

### Error Responses
- **404 Not Found**: Widget not found by name.
- **409 Conflict**: Widget already exists (create).
- **400 Bad Request**: Invalid input (e.g., name < 3 characters).

## Frontend Usage
1. Navigate to `http://localhost:3000`.
2. Use the **Widget Form** to:
    - **Create**: Enter name, description, price, and click "Create Widget".
    - **Edit**: Load an existing widget, update fields, and click "Update Widget".
3. Validation ensures:
    - Name: 3–100 characters.
    - Description: 5–1000 characters.
    - Price: 1–20,000 with exactly 2 decimal places.

## Testing

### Backend Tests
1. Run unit tests with JUnit:
   ```bash
   ./gradlew test
   ```
2. Tests cover `WidgetController`, `WidgetServiceImpl`, and repository interactions.

### Frontend Tests
1. Run Jest unit tests:
   ```bash
   cd frontend
   npm test
   ```
2. Tests in `src/lib/apiConnect.test.ts` cover API client methods.
3. Run Cypress end-to-end tests:
   ```bash
   npm run cypress:open
   ```
4. Cypress tests verify form submission, deletion, and UI updates.



