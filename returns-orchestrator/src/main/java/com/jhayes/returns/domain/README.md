# Domain Layer (Core)
This is the "Heart" of the application. It contains the business logic, entities, and rules that are independent of any framework.

### Sub-packages:
- `model`: Core data structures (Records/Classes) like Shipment and Manifest.
- `disposition`: The logic governing the "fate" of a return (Restock, Liquidate, etc.) using Sealed Interfaces for type safety.