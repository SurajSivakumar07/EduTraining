package com.employee.management.exception;

/**
 * Exception thrown when attempting to add a duplicate employee.
 */
public class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException(String message) {
        super(message);
    }

    public DuplicateEmployeeException(String message, Throwable cause) {
        super(message, cause);
    }
}
