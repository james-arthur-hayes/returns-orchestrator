# Disposition Domain
This package defines the **Decision Logic** for returned items. It determines the final "fate" of a product once it enters the orchestration flow.

### Core Concept: Sealed Hierarchy
We use Java 21 **Sealed Interfaces** to represent a fixed set of outcomes. This ensures that our orchestration logic is exhaustive and type-safe.

### Available Dispositions:
- **Restock:** Item is in "Like New" condition and can return to active inventory.
- **Liquidate:** Item is functional but cannot be sold as new; routed to secondary markets.
- **ReturnToVendor (RTV):** Defective items sent back to the original manufacturer for credit.
- **Dispose:** Non-functional or hazardous items routed for recycling or waste.

### Responsibilities:
- Modeling the state transitions of a return.
- Defining the metadata required for each specific outcome (e.g., a `LiquidationPrice` for the Liquidate path).
- Ensuring the business rules for "where the item goes" are centralized and immutable.