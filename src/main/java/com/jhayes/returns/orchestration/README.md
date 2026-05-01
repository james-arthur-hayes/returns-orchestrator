# Orchestration Layer
The "Traffic Controller" of the system.

### Responsibilities:
- Coordinating the flow between the **Strategy** engine, **External Clients**, and the **Repository**.
- Managing reactive streams (`Flux` and `Mono`) to ensure non-blocking execution across the entire lifecycle of a return.