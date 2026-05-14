# Domain Service Layer
This package contains the **Business Logic Orchestration** for the returns system. Services here act as the "engine room," coordinating between the API layer and the underlying strategy and data layers.

### Responsibilities:
- **Orchestration:** Managing the high-level workflow of a return request.
- **Business Validation:** Enforcing rules that aren't purely structural (e.g., SKU authorization, eligibility checks).
- **Strategy Delegation:** Utilizing the `TriageFactory` to determine the specific processing path for each return.
- **Abstraction:** Ensuring the API layer stays decoupled from the complex decision-making logic.

### Core Components:
- **ReturnService:** The primary entry point for business logic. It validates the incoming `ReturnRequest`, resolves the appropriate `TriageStrategy`, and initiates the reactive processing flow.

### Reactive Principles:
All services in this package are non-blocking and return **Project Reactor** types (`Mono` or `Flux`). This ensures the system remains scalable and performs well under high concurrency.