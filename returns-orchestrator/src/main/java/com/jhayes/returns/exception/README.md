# Exception Handling
Centralized error management for the application.

### Contents:
- `GlobalExceptionHandler`: A `@RestControllerAdvice` that catches domain exceptions and converts them into standard, user-friendly JSON error responses (e.g., 400 Bad Request, 404 Not Found).