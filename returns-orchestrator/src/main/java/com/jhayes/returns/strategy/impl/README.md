# Strategy Implementations
This package contains the concrete logic for the various return pathways. Each class here implements the `TriageStrategy` interface.

### The "How it Works"
The system uses the **Strategy Design Pattern**. At runtime, the `ReturnOrchestrator` looks at the metadata of a return (weight, dimensions, location) and picks the correct implementation from this folder.

### Current Implementations:
- **ParcelStrategy:** - Handles standard consumer returns (shoes, electronics, etc.).
    - Optimized for carriers like FedEx, UPS, or USPS.
    - Logic focuses on printable labels and drop-off locations.

- **LtlStrategy (Less-Than-Truckload):** - Handles heavy, bulky, or palletized items (furniture, appliances).
    - Logic focuses on scheduling freight pick-ups and specialized bill-of-lading (BOL) generation.

### Adding New Strategies:
To add a new processing path (e.g., `InternationalStrategy` or `HazardousMaterialStrategy`):
1. Create a new class in this directory.
2. Implement `TriageStrategy`.
3. Annotate with `@Component("SpecificName")`.