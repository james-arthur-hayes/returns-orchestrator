# Returns Orchestrator: Event-Driven Reactive Ecosystem

## 📖 Overview
A high-scale, non-blocking microservice ecosystem designed to orchestrate complex product return workflows using an **Event-Driven Architecture (EDA)**. Built with **Spring WebFlux** and **Apache Kafka**, this project demonstrates a modern approach to distributed systems, ensuring high concurrency, resiliency, and loose coupling between core orchestration and downstream logistics.


<img src="img.png" style="width: 35%;">

## 🏗️ Multi-Module Architecture
This project is architected as a **Multi-Module Gradle build**, enforcing strict domain boundaries and enabling code reuse through a shared contract layer:

* **`common-models`**: The "Single Source of Truth." A shared library containing Java 21 Records and DTOs, ensuring schema consistency and preventing "integration drift" across services.
* **`returns-orchestrator`**: The Core Producer. Manages the ingress API, business validation, and persistence, then publishes domain events to Kafka.
* **`carrier-service`**: A reactive consumer that independently manages 3PL logistics and shipping label generation.
* **`notification-service`**: A reactive consumer that handles customer-facing communications via SMTP/SendGrid.

---

## ✅ The Solution: Reactive Fan-Out
Traditional imperative microservices often struggle with thread-starvation and high latency when orchestrating multiple third-party APIs. This solution solves that through:

* **Reactive Streams**: Utilizing Project Reactor (`Mono`/`Flux`) to handle thousands of concurrent requests with minimal resource overhead.
* **Kafka Fan-Out Pattern**: By publishing a `ReturnInitiatedEvent`, the Orchestrator allows multiple services to react to the same data point simultaneously without being directly coupled to the source.

---

## 🚀 Key Architectural Patterns

### Intelligent Triage (Strategy Pattern)
Uses a `TriageFactory` to dynamically route requests to specific logic handlers (e.g., LTL Freight vs. Standard Parcel) at runtime based on product metadata.

### Distributed Tracing & Observability
Implements a custom **Trace ID propagation** strategy. The ID travels from the initial REST call, through the database, into Kafka Headers, and finally into the logs of every consumer service, enabling end-to-end visibility.

### Resilient Data Access
Uses **Spring Data R2DBC** for non-blocking communication with SQL Server, maintaining a fully reactive chain from the HTTP layer to the disk.

---

## 🛠 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (Records, Pattern Matching) |
| **Framework** | Spring Boot 3.2+ |
| **Messaging** | Apache Kafka (Event-Driven Fan-out) |
| **Reactive Library** | Project Reactor (`flatMap`, `zip`, `onErrorResume`) |
| **Persistence** | R2DBC / Microsoft SQL Server |
| **Build Tool** | Gradle (Multi-Module) |
| **API Layer** | Spring WebFlux (Netty-based) |

---

## ✅ Data Integrity & Validation
The system ensures high data quality at the ingress point using **Jakarta Bean Validation**:

1.  **Payload Constraints**: Strict validation on incoming `ReturnRequest` objects (e.g., Email format, SKU requirements, and non-empty IDs) to prevent malformed data from entering the reactive pipeline.
2.  **Schema Enforcement**: The `common-models` module acts as a strict contract, ensuring that the Orchestrator and all Consumer services are always in sync.

---

## 📖 Getting Started

### Prerequisites
* Java 21
* Docker & Docker Compose (for Kafka and SQL Server instances)

### Running the Ecosystem
1.  **Start Infrastructure**: `docker-compose up -d`
2.  **Build Project**: `./gradlew clean build`
3.  **Launch Services**:
    * `:returns-orchestrator` (Port 8080)
    * `:carrier-service` (Port 8081)
    * `:notification-service` (Port 8082)

### Sample API Request
**Endpoint:** `POST /api/v1/returns`

```json
{
  "sku": "HAYE-PROD-001",
  "quantity": 1,
  "pickupZip": "90210",
  "customerId": "CUST-JH-01",
  "customerEmail": "james@example.com",
  "reasonCode": "DEFECTIVE",
  "orderId": "ORD-2026-99"
}
```

---

## 🔮 Future Roadmap & Enhancements
* **Reactive Unit Testing**: Implementing a comprehensive test suite using `StepVerifier` to validate asynchronous event signals and backpressure handling.
* **Integration Testing**: Utilizing **Testcontainers** to run automated integration tests against live Kafka and SQL Server instances.
* **Fault Tolerance**: Implementing **Dead Letter Queues (DLQ)** for Kafka consumer retries.
* **State Machine**: Transitioning from discrete events to a full **Saga Pattern** for long-running return lifecycles.