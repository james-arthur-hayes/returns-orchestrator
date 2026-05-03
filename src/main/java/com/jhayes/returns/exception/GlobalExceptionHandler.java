package com.jhayes.returns.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Step 1: Add a Logger
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(WebExchangeBindException ex) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList(); // Modern Java 21 syntax

        log.warn("Validation failed for request: {}", errors);

        ErrorResponse response = new ErrorResponse(
                "Validation Failed",
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                errors
        );
        return ResponseEntity.badRequest().body(response);
    }

    // Step 2: Specific handler for "Not Found" to avoid 500 errors
    // We'll assume you throw a custom ReturnNotFoundException in your service
    @ExceptionHandler(ReturnNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ReturnNotFoundException ex) {
        log.info("Resource not found: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Not Found",
                HttpStatus.NOT_FOUND.value(),
                System.currentTimeMillis(),
                List.of(ex.getMessage())
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument encountered: {}", ex.getMessage());
        ErrorResponse response = new ErrorResponse(
                "Invalid Input",
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                List.of(ex.getMessage())
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(ServerWebInputException.class)
    public ResponseEntity<ErrorResponse> handleServerWebInputException(ServerWebInputException ex) {
        Throwable rootCause = ex.getRootCause();
        String message = (rootCause != null) ? rootCause.getMessage() : ex.getReason();

        log.error("Input exception: {}", message);

        ErrorResponse response = new ErrorResponse(
                "Invalid Request Format",
                HttpStatus.BAD_REQUEST.value(),
                System.currentTimeMillis(),
                List.of(message != null ? message : "Malformed JSON")
        );

        return ResponseEntity.badRequest().body(response);
    }

    // This is the LAST resort. We log it as an ERROR because 500s are "our fault."
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllUncaughtErrors(Exception ex) {
        log.error("UNEXPECTED SYSTEM ERROR: ", ex);
        ErrorResponse response = new ErrorResponse(
                "An unexpected error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                System.currentTimeMillis(),
                List.of()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}