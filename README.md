# Returns Orchestrator: Event-Driven Reactive Ecosystem

## 📋 Overview
A high-scale, non-blocking microservice ecosystem designed to orchestrate complex product return workflows using an **Event-Driven Architecture (EDA)**. Built with **Spring WebFlux** and **Apache Kafka**, this project demonstrates a production-grade approach to distributed systems, ensuring high concurrency, resiliency, and complete asynchronous decoupling between core orchestration gates and downstream consumer workers.

---

## 🏗️ Multi-Module Architecture
This project is architected as a **Multi-Module Gradle build**, enforcing strict domain boundaries and enabling absolute code reuse through a shared contract layer:

* **`common-models`**: The "Single Source of Truth." A shared domain library containing immutable Java 21 Records and DTOs, ensuring strict schema enforcement and preventing payload drift across processing boundaries.
* **`returns-orchestrator`**: The Core Ingress Node & Event Producer. Processes active web traffic, executes schema validation, performs synchronous real-time 3PL carrier tracking requests, registers persistent audit trails to database structures, and fires transaction boundary tokens downstream over Kafka.
* **`carrier-service`**: An asynchronous logistics consumer worker. Intercepts broadcast events to handle downstream long-running tasks, such as uploading shipping manifest duplicates to permanent enterprise AWS S3 cloud archiving buckets.
* **`notification-service`**: An asynchronous communications worker. Automatically monitors topic boundaries to handle non-blocking customer transaction notifications via transactional SMTP engines (SendGrid).

---

## ⚡ The Solution: Reactive Fan-Out
Traditional imperative microservices often struggle with thread-starvation, blocked event loops, and high tail-latencies when orchestrating multiple third-party systems. This ecosystem eliminates those architectural bottlenecks through:

* **Non-Blocking Execution Loops**: Utilizing Project Reactor (`Mono`/`Flux`) and Netty to optimize thread scheduling, allowing thousands of concurrent requests to scale efficiently with minimal hardware footprint.
* **Asynchronous Fire-and-Forget (Fan-Out)**: By dropping a `ReturnInitiatedEvent` into a shared broker cluster, the core orchestrator answers the client gateway immediately. It completely separates the lifecycle of web traffic from heavy background routines like cloud uploads and email generation.

---

## 🎨 Key Architectural Design Patterns

### Self-Healing Multi-Stage Infrastructure
To bypass the limitation where Microsoft SQL Server containers do not automatically initialize a database instance on boot, this project introduces a self-healing cluster script inside Docker Compose. The primary database runs a native health check tool. A sidecar worker container (`sql-server-init`) detects when the database engine is healthy, verifies whether `returns_db` is missing, provisions the catalog space automatically, and cleanly shuts itself down to preserve system host memory.

### Distributed Tracing & Observability Matrix
Implements a unified telemetry strategy using **Micrometer Tracing** and the **Brave/B3 Propagation Schema**.
* Outbound tracing parameters (`x-b3-traceid`) are automatically stamped onto the binary header arrays of Kafka network packets.
* Consumers ingest the `ConsumerRecord` metadata envelope directly, extracting and restoring the trace identifier inside Logback's thread execution scope.
* Advanced **Component Prefix Tagging** (`[ORCHESTRATOR-*]`, `[CARRIER-SERVICE]`, `[NOTIFICATION-SERVICE]`) ensures that when multiple isolated application consoles are centralized, transactions can be parsed sequentially by tracing a single alphanumeric string.

### Intelligent Triage (Strategy Pattern)
Uses a specialized `TriageFactory` component to evaluate incoming item properties dynamically at runtime. It routes items to optimal sorting channels (e.g., LTL Freight handling lines versus Standard Parcel sorting lines) without polluting the core controller implementation with conditional logic blocks.

### Resilient Reactive Data Access
Employs **Spring Data R2DBC** to handle non-blocking driver calls to Microsoft SQL Server. This eliminates legacy JDBC thread-locking friction, preserving a fully reactive chain from the netty socket layer all the way down to physical disk operations.

---

## 💻 Tech Stack

| Category | Technology |
| :--- | :--- |
| **Language** | Java 21 (Records, Patterns, Virtual Compatibility) |
| **Framework** | Spring Boot 3.2+ |
| **Messaging** | Apache Kafka (Distributed Broker / Fan-out Topology) |
| **Reactive Engine** | Project Reactor (`flatMap`, `deferContextual`, `doOnSuccess`) |
| **Persistence** | R2DBC Asynchronous Driver / Microsoft SQL Server 2022 |
| **Observability** | Micrometer Tracing / Zipkin Brave Framework |
| **Build Tool** | Multi-Module Gradle Platform |
| **API Container** | Spring WebFlux / Reactor Netty Event Loop |

---

## 🛡️ Data Integrity & Ingress Guardrails
The system stops bad data from entering the reactive chain at the gateway container edge using **Jakarta Bean Validation**:

1.  **Payload Boundary Constraints**: Strict validation on incoming fields (e.g., valid email formats, matching SKU character codes, and non-empty IDs) to keep garbage collection steps lean.
2.  **Contract Enforcement**: Because the schema definitions live natively inside the `common-models` JAR library, it is compile-time impossible for a producer to send a payload that a downstream consumer cannot parse.

---

## 🚀 Getting Started

### Prerequisites
* Java 21 SDK (Temurin / Oracle)
* Docker Desktop & Docker Compose Engine

### Running the Ecosystem
1. **Launch Self-Healing Cluster**:
   ```bash
   docker compose up -d
   ```
   *(This starts Zookeeper, Kafka, SQL Server, and provisions the `returns_db` instance automatically).*

2. **Re-Index Project Dependencies**:
   ```bash
   ./gradlew clean build -x test
   ```

3. **Launch the Core Microservices inside your IDE**:
   * `:returns-orchestrator` $\rightarrow$ Listening on Port `8080`
   * `:carrier-service` $\rightarrow$ Listening on Port `8081`
   * `:notification-service` $\rightarrow$ Listening on Port `8082`

---

## 🧪 Testing

```bash
curl -X POST http://localhost:8080/api/v1/returns \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "HAYE-PROD-001",
    "quantity": 1,
    "pickupZip": "90210",
    "customerId": "CUST-JH-01",
    "customerEmail": "james@example.com",
    "reasonCode": "DEFECTIVE",
    "orderId": "ORD-2026-99"
  }'
```

#### Expected Success Response Payload
```json
{
  "trackingId": "PRCL-12b44442",
  "status": "PARCEL_READY",
  "labelUrl": "[https://carrier.com/labels/PRCL-12b44442.pdf](https://carrier.com/labels/PRCL-12b44442.pdf)",
  "timestamp": 1779160855022
}
```

#### Centralized Trace Output Architecture
When checking your distributed application terminals side-by-side, notice how the unique telemetry `TraceID` smoothly propagates across system barriers and independent runtime threads:

```text
2026-05-18 22:05:01.102 [reactor-http-epoll-4] INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [ORCHESTRATOR-API] Ingesting incoming return request for Order: [ORD-2026-99]
2026-05-18 22:05:01.105 [reactor-http-epoll-4] INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [ORCHESTRATOR-CORE] Return routing strategy computed -> Assigned Tracking ID: [PRCL-12b44442]
2026-05-18 22:05:01.480 [reactor-tcp-epoll-6]  INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [ORCHESTRATOR-DATABASE] Manifest audit record permanently persisted to SQL Server...
2026-05-18 22:05:01.481 [reactor-tcp-epoll-6]  INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [ORCHESTRATOR-KAFKA] Broadcasting ReturnInitiatedEvent downstream...

... (Message fan-out travels asynchronously over the Kafka network boundary instantly) ...

2026-05-18 22:05:01.486 [KafkaContainer#0-C-1] INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [CARRIER-SERVICE] Asynchronous Kafka event received for processing.
2026-05-18 22:05:01.486 [KafkaContainer#0-C-1] INFO  [TraceID: 6a0bd716fb4278eace554a3d4e44b59d] [NOTIFICATION-SERVICE] Asynchronous Kafka message picked up successfully.
```