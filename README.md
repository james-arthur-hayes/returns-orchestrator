# Returns Orchestrator: Reactive Microservice

### **Overview**
A high-performance, non-blocking microservice built with **Spring WebFlux** designed to orchestrate complex product return workflows. This project demonstrates the transition from traditional imperative programming to **Reactive Streams**, ensuring system resiliency and high concurrency under heavy load.

---

### **🚀 Key Architectural Patterns**

* **Reactive Orchestration:** Utilizes **Project Reactor** (`Mono`/`Flux`) to manage asynchronous workflows, from validation and triage to external API integration and database persistence.
* **Strategy Design Pattern:** Implements a `TriageFactory` to dynamically route return requests to specific logic handlers (e.g., LTL Freight vs. Standard Parcel) based on business rules.
* **Resilient External Integration:** Leverages **WebClient** with built-in timeout and fallback mechanisms to interact with third-party carrier APIs, preventing cascading failures.
* **Reactive Data Access:** Uses **Spring Data R2DBC** for non-blocking communication with SQL Server, maintaining a fully reactive chain from the HTTP layer to the disk.

---

### **🛠 Tech Stack**
* **Java 21** (Utilizing modern Record types and Pattern Matching)
* **Spring Boot 3.2+**
* **Spring WebFlux** (Netty-based reactive web server)
* **R2DBC / Microsoft SQL Server**
* **Project Reactor** (Advanced operators: `flatMap`, `zip`, `onErrorResume`)
* **JUnit 5 & StepVerifier** (Reactive Unit/Integration Testing)
* **OpenAPI/Swagger** (Interactive API Documentation)

---

### **🧪 Testing & Quality Assurance**
This project maintains high reliability through a comprehensive test suite that utilizes:
* **StepVerifier:** To validate asynchronous event signals and backpressure in reactive streams.
* **Mockito:** For isolated testing of orchestration logic by mocking external service dependencies.
* **Jakarta Validation:** Ensuring strict data integrity for all incoming API payloads.

---

### **📖 Getting Started**

#### **Prerequisites**
* Java 21
* SQL Server (or Docker for a containerized instance)

#### **Running the Application**
```bash
./gradlew bootRun