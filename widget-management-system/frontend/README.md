# 🧩 Widget Management App

A React + TypeScript application for managing widgets — including creation, retrieval, update, deletion, and listing. Styled with Material UI and tested using Cypress.

---

## 🚀 Features

- ✅ Create widgets with name, description, and price
- ✅ List all widgets
- ✅ Retrieve a widget by name
- ✅ Update a widget’s description or price
- ✅ Delete a widget
- ✅ Validates widget data using React Hook Form + Yup
- ✅ Tested with Cypress

---

## 🛠 Tech Stack

- **React 18** + **TypeScript**
- **Material UI (MUI)** for styling
- **React Hook Form** + **Yup** for validation
- **Axios** for HTTP requests
- **Cypress** for end-to-end testing


---

## 📦 Installation

```bash
git clone https://github.com/Madhulika-kandhadi/CRUD-Operation-Java.git

cd widget-app
npm install

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

## Frontend Usage
1. Navigate to `http://localhost:3000`.
2. Use the **Widget Form** to:
    - **Create**: Enter name, description, price, and click "Create Widget".
    - **Edit**: Load an existing widget, update fields, and click "Update Widget".
3. Validation ensures:
    - Name: 3–100 characters.
    - Description: 5–1000 characters.
    - Price: 1–20,000 with exactly 2 decimal places.


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

Select widgetApp.cy.ts to run the full test suite.

## API Endpoints
The backend exposes a REST API at `/api/widgets`:

| Method | Endpoint | Description                     | Request Body                     | Response                     |
|--------|----------|---------------------------------|----------------------------------|------------------------------|
| `GET`  |          | Get all widgets                 | None                             | `Widget[]` (200 OK)          |
| `GET`  | `/{name}` | Get widget by name              | None                             | `Widget` (200 OK)            |
| `POST` |          | Create a widget                 | `{ name, description, price }`   | `Widget` (201 Created)       |
| `PATCH`| `/{name}` | Update widget’s description/price | `{ description?, price? }`     | `Widget` (200 OK)            |
| `DELETE` | `/{name}`| Delete widget by name           | None                             | None (204 No Content)        |
