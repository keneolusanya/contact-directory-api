# Contact Directory API

A RESTful API (Backend) for managing a personal contact directory, built with Spring Boot. Supports creating, viewing, searching, updating, and deleting contacts.

## Technology Stack

- Java 21
- Spring Boot 3.5.14 (Spring Web, Bean Validation)
- Maven
- springdoc-openapi (Swagger UI) 2.8.13

## How to Build

From the project root:

```bash
./mvnw clean package
```

## How to Run

```bash
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`.

## API Documentation (Swagger UI)

Once the app is running, interactive documentation is available at:

```
http://localhost:8080/swagger-ui.html
```

The raw OpenAPI spec is at `http://localhost:8080/v3/api-docs`.

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | `/contacts` | Create a new contact |
| GET | `/contacts` | List all contacts (optional `?group=` and `?search=`) |
| GET | `/contacts/{id}` | Get a contact by ID |
| PUT | `/contacts/{id}` | Update a contact |
| DELETE | `/contacts/{id}` | Delete a contact |

### Query parameters on `GET /contacts`

- `group` — filter by group (`FAMILY`, `FRIEND`, `WORK`, `OTHER`), case-insensitive
- `search` — case-insensitive partial match against first name, last name, or email

Both are optional and can be combined (a contact must match both to be returned).

## Validation Rules

- `firstName` and `lastName` must not be blank
- `email` must be a valid format and unique across all contacts
- `phoneNumber` must be digits only, 10–15 characters
- `group` must be one of `FAMILY`, `FRIEND`, `WORK`, `OTHER`

## Status Codes

- `201 Created` — contact created
- `200 OK` — successful read or update
- `400 Bad Request` — validation failure
- `404 Not Found` — contact ID does not exist
- `409 Conflict` — email already in use

## Design Decisions & Assumptions

- **In-memory storage.** Contacts are held in a `ConcurrentHashMap`; data does not persist across restarts. A thread-safe map and an `AtomicLong` id counter are used since Spring handles requests concurrently.
- **Server-assigned fields.** `id` and `createdAt` are assigned by the server at creation. Any `id` or `createdAt` sent by the client is ignored.
- **Email uniqueness is case-insensitive** (`Ada@x.com` and `ada@x.com` are treated as the same address).
- **Invalid `group` filter** (e.g. `?group=BANANA`) returns an empty list rather than an error.
- **Layered architecture** (controller → service → repository) to separate HTTP handling, business rules, and storage.
- **springdoc 2.8.13** is used for Boot 3 compatibility; it flags some transitive-dependency CVEs, which are acceptable for this local, non-production project.