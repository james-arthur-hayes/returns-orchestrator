# Returns Orchestrator: Reactive Microservice

## 📖 Overview
A high-performance, non-blocking microservice built with **Spring WebFlux** designed to orchestrate complex product return workflows. This project demonstrates the transition from traditional imperative programming to **Reactive Streams**, ensuring system resiliency and high concurrency under heavy load.

---

## ⚠️ The Problem
Traditional imperative microservices often struggle with **thread-starvation** and high latency when orchestrating multiple third-party logistics (3PL) APIs during a return lifecycle. Sequential processing of validation, carrier selection, and label generation creates bottlenecks that hinder scalability.

## ✅ The Solution
This orchestrator uses a **fully reactive approach** to handle thousands of concurrent return requests with minimal resource overhead. By leveraging non-blocking I/O, it ensures that "Decision Intelligence" is delivered in real-time, regardless of external API latencies.

---

## 🚀 Key Architectural Patterns

* **Reactive Orchestration:** Utilizes Project Reactor (`Mono`/`Flux`) to manage asynchronous workflows, from validation and triage to external API integration and database persistence.
* **Intelligent Triage (Strategy Pattern):** Implements a `TriageFactory` to dynamically route requests to specific logic handlers (e.g., LTL Freight vs. Standard Parcel). This decouples business rules from routing, allowing for rapid scaling of decision-making logic.
* **Resilient External Integration:** Leverages `WebClient` with built-in timeout and fallback mechanisms to interact with third-party carrier APIs, preventing cascading failures in the logistics chain.
* **Reactive Data Access:** Uses **Spring Data R2DBC** for non-blocking communication with SQL Server, maintaining a fully reactive chain from the HTTP layer to the disk.
* **Observability & Monitoring:** Integrated structured logging and health indicators (**Spring Boot Actuator**) to ensure system reliability and a "zero-blind-spot" on-call experience, facilitating methodical debugging in production.

---

## 🛠 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (Records, Pattern Matching) |
| **Framework** | Spring Boot 3.2+ |
| **Web Stack** | Spring WebFlux (Netty-based) |
| **Persistence** | R2DBC / Microsoft SQL Server |
| **Reactive Library** | Project Reactor (`flatMap`, `zip`, `onErrorResume`) |
| **Testing** | JUnit 5 & StepVerifier |
| **Documentation** | OpenAPI / Swagger |

---

## 🧪 Testing & Quality Assurance
This project maintains high reliability through a comprehensive test suite:

* **StepVerifier:** To validate asynchronous event signals and backpressure in reactive streams.
* **Mockito:** For isolated testing of orchestration logic by mocking external service dependencies.
* **Jakarta Validation:** Ensuring strict data integrity for all incoming API payloads.

---

## 📖 Getting Started

### Prerequisites
* Java 21
* SQL Server (or Docker for a containerized instance)