# Strategy Engine
Implements the **Strategy Design Pattern** to determine triage logic.

### Responsibilities:
- Deciding the processing path (LTL vs. Parcel) based on product metadata and weight.
- `impl/`: Contains specific implementations like `LtlStrategy` and `ParcelStrategy`.