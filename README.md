# Wandr

Wandr is a digital travel journal. It helps users organize trips and keep memories of places, meals, and activities, along with photos, tags, ratings, and expenses.

## Features

- Create and browse trips with dates, destination, status, budget, and cover image.
- Add memories to a trip, such as places, meals, activities, or accommodation.
- Attach photos, tags, and ratings to memories.
- Track trip expenses and their categories.
- Browse a map, dashboard, and statistics in the web interface. 

## Architecture

```text
Browser
   |
   | Vue 3 / Vite (port 5173)
   | REST API requests
   v
Spring Boot API (port 8080)
   |
   | Spring Data JPA
   v
PostgreSQL / PostGIS (port 5432)
```

The frontend uses Vue 3, JavaScript, Vue Router, and Vite. The backend is a Spring Boot REST API written in Java 21. PostgreSQL stores the application data, and PostGIS provides spatial database features.

## Project structure

```text
Wandr/
├── docker-compose.yml              # frontend, backend, and database orchestration
├── README.md
├── frontend/
│   ├── Dockerfile
│   ├── package.json
│   ├── public/                     # static files, e.g. local images
│   └── src/
│       ├── App.vue                 # root Vue component
│       ├── main.js                 # Vue entry point and plugins
│       ├── router/                 # pages and navigation
│       ├── views/                  # application screens
│       ├── components/             # reusable Vue components
│       ├── composable/             # shared Vue logic
│       ├── services/               # HTTP calls to the API
│       └── assets/                 # frontend-bundled assets
└── wandr-backend/
    ├── Dockerfile
    ├── build.gradle                # Gradle dependencies and configuration
    ├── gradlew                     # Gradle wrapper
    └── src/
        ├── main/java/wandrbackend/
        │   ├── config/             # Spring configuration
        │   ├── controller/         # REST endpoints
        │   ├── entity/             # JPA entities
        │   │   └── repository/     # database access for entities
        │   ├── exception/          # errors and error responses
        │   └── services/           # application logic
        │       └── implementation/
        ├── main/resources/
        │   ├── application.properties
        │   └── db/init.sql          # SQL schema and categories currently present
        └── test/java/               # backend tests
```

## Prerequisites

To run the full application with Docker:

- Docker Desktop with Docker Compose enabled.
- The provided `.env` file at the repository root.

To run a module directly on your machine:

- Frontend: Node.js and npm.
- Backend: Java 21; the Gradle wrapper is included.

## Environment configuration

Docker Compose reads the `.env` file from the repository root. It is included in this school project so that teammates and instructors can run and test the application with the same development configuration. No separate environment file needs to be created.

The `.env` file contains local database credentials, the backend database connection settings, the JWT secret, and allowed frontend origins. The credentials are for local coursework and testing only. Do not reuse them in a production environment.

`DB_HOST=db` is the PostgreSQL service name on the Docker network. The frontend uses `VITE_API_BASE_URL` for API requests and defaults to `http://localhost:8080` when it is not set.

## Run with Docker Compose

From the repository root:

```bash
docker compose up --build
```

To run the containers in the background:

```bash
docker compose up --build -d
```

Local addresses:

- Frontend: <http://localhost:5173>
- Backend: <http://localhost:8080>
- PostgreSQL: `localhost:5432`

Useful commands:

```bash
docker compose ps                         # check service status
docker compose logs -f frontend backend   # follow application logs
docker compose down                       # stop services
```

`docker compose down` keeps PostgreSQL data in the `postgres_data` volume. Add `-v` only when you intentionally want to permanently delete the local database data.

## Run modules separately

### Frontend

```bash
cd frontend
npm install
npm run start
```

Vite serves the application at <http://localhost:5173>. The backend must be reachable on port 8080. To create a production build:

```bash
npm run build
```

### Backend

Start PostgreSQL first and provide `DB_HOST`, `DB_NAME`, `DB_USER`, and `DB_PASSWORD`. From `wandr-backend`:

```bash
./gradlew bootRun
```

To build the backend:

```bash
./gradlew build
```

### SQL initialization: check before a clean setup

`application.properties` currently tells Spring to load `schema.sql` and `data.sql` from the root of `src/main/resources`, while the repository currently contains `src/main/resources/db/init.sql`. These paths must be aligned for a fresh database to initialize automatically. Check the SQL scripts before resetting an existing database: they create tables and may contain demo data.

## REST API

The API has no global path prefix. Main routes include:

| Resource | Routes |
| --- | --- |
| Users | `GET /users`, `GET /users/{userId}`, `POST /users`, `PUT /users/{userId}`, `DELETE /users/{userId}` |
| Trips | `GET /trips/user/{userId}`, `GET /trips/{tripId}`, `POST /trips/user/{userId}`, `PUT /trips/{tripId}`, `DELETE /trips/{tripId}` |
| Memories | `GET /trips/{tripId}/memories`, `GET /trips/{tripId}/memories/{memoryId}`, `POST /trips/{tripId}/memories`, `PUT /trips/{tripId}/memories/{memoryId}`, `DELETE /trips/{tripId}/memories/{memoryId}` |
| Photos | `GET/POST /memories/{memoryId}/photos`, `GET/PUT/DELETE /memories/{memoryId}/photos/{photoId}` |
| Expenses | `GET/POST /trips/{tripId}/expenses`, `GET/PUT/DELETE /trips/{tripId}/expenses/{expenseId}` |
| Ratings | `GET/POST /memories/{memoryId}/ratings`, `GET/PUT/DELETE /memories/{memoryId}/ratings/{ratingId}` |
| Tags | `GET/POST /memories/{memoryId}/tags`, `GET/PUT/DELETE /memories/{memoryId}/tags/{tagId}` |
| Expense categories | `GET /expense-categories`, `GET /expense-categories/{expenseCategoryId}`, `POST /expense-categories` |

For example, retrieve a user's trips with `GET http://localhost:8080/trips/user/1`. To create an expense, provide `expenseCategoryId` and optionally `memoryId` as query parameters:

```text
POST http://localhost:8080/trips/1/expenses?expenseCategoryId=3&memoryId=1
```

## Local images

Place images that should be served directly by the browser under `frontend/public/images/`. For example, `frontend/public/images/lisbon.jpg` is available in the application as `/images/lisbon.jpg`.

## Data model

The main entities are `User`, `Trip`, `Memory`, `Photo`, `Tag`, `Rating`, `Expense`, and `ExpenseCategory`. A user owns trips; each trip contains memories and expenses; a memory can have photos, tags, ratings, and expenses.

## Team

This project is the result of a collaborative effort by a team of 2 students from the same college. Working together, we implemented a complete development lifecycle, from initial planning and database schema design to final interfaces and testing. Made by CESA Matthew-Frédérick and NGUYEN Constant.
