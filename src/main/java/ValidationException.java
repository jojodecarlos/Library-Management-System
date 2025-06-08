// File: src/main/java/com/library/lms/exception/ValidationException.java
package com.library.lms.exception;

/**
 * Thrown when a validation rule is violated (e.g., invalid ID, blank fields, out-of-range fine).
 */
public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
