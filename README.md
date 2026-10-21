# Foreign Language School Management System «BESTSchoolEVER»

A high-performance corporate ERP application built with a distributed client-server architecture. The system is designed to automate core workflows within a language school ecosystem, including interactive schedule synchronization, user enrollment, role-based access control, and full CRUD data management.

---

## Key Features

* **Multi-Threaded TCP/IP Networking:** Robust real-time communication between client and server modules using Java Sockets and structured `Request`/`Response` payload serialization.
* **Dynamic Schedule Management:** An interactive timeline grid auto-populating training events from the database based on assigned teachers, languages, complexity levels, and specific time slots.
* **Role-Based Access Control (RBAC):** Strict security routing for three distinct privilege tiers: Guest (read-only), Authenticated Student, and Administrator.
* **Data Integrity & Collision Prevention:** Severe server-side data validation preventing duplicate record errors (e.g., preventing a student from signing up for the exact same class twice) via `JavaFX Alert` dialog feedback loop.

---

## Tech Stack & Architectural Decisions

The architectural design choice satisfies modern industrial standards for enterprise desktop solutions:
* **UI Frontend:** JavaFX coupled with declarative FXML layout files and custom external **CSS** stylesheets for a modern, responsive user experience.
* **Database Backend:** **PostgreSQL** relational DBMS handling multi-table entity definitions, strict referential foreign key constraints, unique indexes, and cascading deletions.
* **Build System:** Apache Maven (configured with Maven Wrapper `mvnw` for standalone environment reproducibility).
* **Core Libraries:** **Lombok** (automated compilation-level DTO boilerplate generation) and **Spring Framework Core** components for service layer management.

---

## Module Demonstration (Video / GIF Placeholders)

### 1. Security Routing Interface (`EntranceWindow`, `entrance.fxml`)
The primary system gateway distributing access permissions to Guest, Student, or Admin consoles.

<img width="856" height="430" alt="document_5388745829630648400" src="https://github.com/user-attachments/assets/3ba81ae0-14a2-459f-b1b8-1e9542dd048d" />

### 2. Authentication & User Enrollment (`LoginWindow`, `RegistrationWindow`)
Handles secure alphanumeric user input gathering, packing network payloads, and persisting new unique entities into the database.

<img width="856" height="430" alt="document_5388745829630648402" src="https://github.com/user-attachments/assets/616f1c83-6c3b-4fcc-aa02-8afab1bee4ab" />

### 3. Interactive Schedule Matrix (`ScheduleWindow`)
The main dashboard generating a dynamic calendar timeline grid (Monday to Sunday, 08:00 - 16:00) populated by `ScheduleResponseDTO` packages.
<img width="856" height="430" alt="document_5388745829630648403" src="https://github.com/user-attachments/assets/3e2f34f2-b449-4677-b457-058348b6421a" />

### 4. Modal Verification & Error Handling (`AlertUtil`)
Intercepts faulty server packets (e.g., PostgreSQL unique registration constraint violations) and generates visual `JavaFX Alert` dialog responses stating *"You are already enrolled in this lesson"*.

<img width="856" height="430" alt="document_5388745829630648397" src="https://github.com/user-attachments/assets/0f51afd3-189d-4104-9e5c-bbab34336055" />

### 5. Administrative CRUD Workspace (`ManageLessonsController`)
A management UI panel allowing managers to call database rows and trigger Create, Read, Update, and Delete operations using the sub-form `change.fxml`.

<img width="856" height="430" alt="document_5388745829630648405" src="https://github.com/user-attachments/assets/1a7722ad-2351-4566-bbb2-9b09f0eb0072" />

---

## Core Algorithms & Business Logic

The core logic of the background thread execution and response parsing is formalised through the following backend block-flow charts:

### 1. User/Guest Session List Generation Algorithm
Traces data filtering sequence across connected system sockets, fetching respective entity database roles, and wrapping them into an array structured for the administrator dashboard view.

<img width="550" height="462" alt="image" src="https://github.com/user-attachments/assets/e6515275-916a-42a6-812a-1d20893e3c2b" />

### 2. Server Capacity and Compute Load Evaluation Algorithm
Reflects multithreaded socket server execution logic computing the active CPU load threshold based on current concurrent TCP connection pools vs thread pool maximum limits.

<img width="524" height="494" alt="image" src="https://github.com/user-attachments/assets/3ca73dee-e234-4f58-b28f-07cc18ef30d5" />

---

## System Engineering & UML blueprints

The logical, entity relational, and system runtime behaviors were fully documented in strict compliance with Object-Oriented Design (OOD) standards:

### 1. Use Case Diagram
Defines user interactions, business goal allocation, and structural system boundaries across Guest, Student, and Administrator sessions.

<img width="782" height="616" alt="image" src="https://github.com/user-attachments/assets/a1b41e9c-9e5f-466a-a034-f4f2fb967995" />

### 2. "Lesson" Entity State Machine Diagram
Describes the life cycle transitions of an event object instance—ranging from initialization ("Новый"), field entry validation, up to remote transactional storage, update loops, or final deletion.

<img width="840" height="583" alt="image" src="https://github.com/user-attachments/assets/0f3cb57a-062d-4164-b699-9d46e8c48d5a" />

### 3. System Sequence Diagram
Tracks synchronous data message transfers (`Request`/`Response`) looping across the Client UI view, client-side internal socket connection (`Client Socket`), backend master receiver (`Server Socket`), and Data Access Layer (`LessonRepository`).

<img width="853" height="459" alt="image" src="https://github.com/user-attachments/assets/b1c64202-94ab-4945-adb2-16d1d488a154" />

### 4. Structural Component Diagram
Illustrates architectural layer separation, dependency injection routing across controllers, DTO mappings on the client app node, and respective services, data repositories, and framework boundaries (`Spring`, `Log4j2`) on the remote server host.

<img width="747" height="781" alt="image" src="https://github.com/user-attachments/assets/936f08b9-fb94-4b8d-824f-325767adb27e" />

### 5. Hardware Deployment Diagram
Depicts the hardware execution layers of the distributed multi-module system, pinpointing standalone "Client" workstation environments and the isolated "Server" host connecting over network TCP/IP channels.

<img width="620" height="441" alt="image" src="https://github.com/user-attachments/assets/dcd72c58-9cda-40fa-8498-48df64fa5ec4" />

