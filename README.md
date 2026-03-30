# LNU Deadline Tracker API 🎓

A robust RESTful API built with Spring Boot to help students manage university assignments, coursework, and daily deadlines. 

This project implements a clean **3-layer architecture** (Controller - Service - Repository) and provides a solid backend foundation for task management.

## 🚀 Tech Stack
* **Language:** Java 17
* **Framework:** Spring Boot 3.x (Web, Data JPA)
* **Database:** PostgreSQL (Dockerized)
* **Tools:** Lombok, Gradle

## ✨ Features
* **CRUD Operations:** Create, Read, Update (status), and Delete tasks.
* **Smart Filtering:** Fetch all tasks or filter them dynamically by status (`TODO`, `IN_PROGRESS`, `DONE`).
* **Business Logic:** Prevents the creation of duplicate assignments based on the task title.
* **Global Exception Handling:** Clean, unified JSON error responses (e.g., `404 Not Found`, `400 Bad Request`) instead of raw stack traces using `@RestControllerAdvice`.

## 🛠️ Local Development Setup

### Prerequisites
* Java 17+ installed
* Docker installed and running

### 1. Start the Database
The project uses a PostgreSQL database hosted in a Docker container. Run the following command to spin it up:
```bash
docker run --name lnu-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=qwerty -e POSTGRES_DB=lnu_tracker -p 5432:5432 -d postgres:alpine

2. Run the ApplicationYou can run the application directly from your IDE (IntelliJ IDEA) or via the terminal using Gradle:Bash./gradlew bootRun
The server will start on http://localhost:8080.📡 API EndpointsMethodEndpointDescriptionPOST/api/v1/tasksCreate a new taskGET/api/v1/tasksRetrieve all tasksGET/api/v1/tasks?status=TODORetrieve tasks filtered by statusPATCH/api/v1/tasks/{id}/statusUpdate task status (requires ?status= param)DELETE/api/v1/tasks/{id}Delete a task by IDSample Payload (POST)JSON{
  "title": "Coursework",
  "description": "Data caching methods analysis",
  "subjectName": "Software Engineering",
  "dueDate": "2026-05-15T12:00:00",
  "status": "TODO"
}
🏗️ ArchitectureThe project follows standard enterprise Java practices:Controllers: Handle incoming HTTP requests and map them to standard responses.Services: Contain the core business logic and validations.Repositories: Interface with the PostgreSQL database using Spring Data JPA derived queries.
***

### Що робити далі:

1. Збережи цей файл.
2. Відкрий термінал в IDEA.
3. Виконай ці три команди, щоб відправити код на твій GitHub:
   ```bash
   git add .
   git commit -m "feat: initial release with CRUD, PostgreSQL and Global Exception Handling"
   git push
