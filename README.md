# 📅 Task Tracker App

A full-stack Task Tracking application designed to help users organize their daily tasks, set priorities, and track their progress efficiently. Built with **Spring Boot**, **React**, and **PostgreSQL**, this project follows a clean architecture and includes a RESTful API backend and a modern, responsive frontend.

---

## 📊 Features

### 📂 Task Lists
- Create multiple task lists (e.g., Work, Personal, Projects)
- Edit or delete task lists
- View overall progress of each task list

### 📅 Tasks
- Add tasks with:
  - Title
  - Optional description
  - Due date
  - Priority (HIGH, MEDIUM, LOW)
- Mark tasks as complete / reopen
- Edit and delete tasks

### ✅ Progress Tracking
- View progress per task list (completion percentage)
- Visual progress bar

### ⭐ Bonus Ideas (planned/future):
- Reminders and calendar sync
- Search and filter by priority/due date
- Pomodoro timer integration
- Gamification system

---

## 🚀 Tech Stack

| Layer | Tech |
|------|------|
| Frontend | React + TypeScript + NextUI |
| Backend  | Spring Boot (v3.3.5), Java 21 |
| Database | PostgreSQL via Docker Compose |
| Build Tools | Maven (with wrapper), Vite (frontend) |

---

## ⚙️ Setup Instructions

### ☕ Backend (Spring Boot)
1. Ensure Docker is running
2. Run PostgreSQL with Docker Compose:
   ```bash
   docker-compose up
   ```
3. Create `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   spring.datasource.username=postgres
   spring.datasource.password=changemeinprod!
   spring.jpa.hibernate.ddl-auto=update
   ```
4. Build and run the backend:
   ```bash
   ./mvnw clean install
   ./mvnw spring-boot:run
   ```

> ⚠️ **Important:** Do NOT commit `application.properties` to version control. It is already ignored via `.gitignore`.

### 🔧 Frontend (React)
```bash
cd tasks-fe
npm install
npm run dev
```
Visit [http://localhost:5173](http://localhost:5173) to view the app.

---

## 🌐 API Endpoints Summary

### Task Lists
- `GET    /task-lists` - List all
- `POST   /task-lists` - Create
- `GET    /task-lists/{id}` - Get by ID
- `PUT    /task-lists/{id}` - Update
- `DELETE /task-lists/{id}` - Delete

### Tasks (within a task list)
- `GET    /task-lists/{listId}/tasks` - List tasks
- `POST   /task-lists/{listId}/tasks` - Create task
- `GET    /task-lists/{listId}/tasks/{taskId}` - Get task
- `PUT    /task-lists/{listId}/tasks/{taskId}` - Update
- `DELETE /task-lists/{listId}/tasks/{taskId}` - Delete

---

## 🖊️ What I Learned from This Project

- Creating immutable DTO objects using Java 21’s `record` keyword, making data transfer safer and cleaner.
- Understanding the difference between Entity and DTO, and their responsibilities in a layered architecture.
- Implementing manual mapping between domain and DTO using `Mapper` interfaces to decouple the service layer logic.
- Learning the purpose and usage context of key Spring Boot annotations such as `@Component`, `@Service`, `@RestController`, `@ControllerAdvice`.
- Using a global exception handler (`GlobalExceptionHandler`) with `@ControllerAdvice` and `@ExceptionHandler` to centralize error handling.
- Running a PostgreSQL instance via Docker Compose, and understanding how `docker-compose.yml`, `ports`, and `environment` sections work together.
-  Validation annotations like `@Valid` in controller parameters to trigger automatic field validation based on constraints defined in DTOs.(Not used in this project, but understood conceptually)
- Leveraging Lombok annotations such as `@Getter`, `@Setter`, `@AllArgsConstructor`, `@NoArgsConstructor`, and `@Builder` to reduce boilerplate code in entities and services.((Not used in this project, but understood conceptually))
- Applying `@Transactional` in service methods to ensure atomicity and rollback behavior during data modification operations


---
