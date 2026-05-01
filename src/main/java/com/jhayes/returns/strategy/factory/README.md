# Strategy Factory
This package contains the logic for **Strategy Resolution**.

### Responsibilities:
- **TriageFactory / StrategyResolver:** Acts as the single point of entry for selecting the correct processing path.
- **Selection Logic:** Evaluates incoming `ReturnRequest` metadata (e.g., weight, dimensions, SKU type) to determine which implementation from the `impl` package should be used.

### Why this exists:
It keeps the `orchestration` layer clean. The Orchestrator simply asks the Factory for "the right tool for this job" without needing to know the specific business rules behind the choice.