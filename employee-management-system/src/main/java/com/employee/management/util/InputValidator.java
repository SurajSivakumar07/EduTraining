package com.employee.management.util;

import com.employee.management.exception.InvalidInputException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

/**
 * Utility class for input validation.
 */
public class InputValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Private constructor to prevent instantiation
     */
    private InputValidator() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Validates email format
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Validates phone number format (10 digits)
     */
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }

    /**
     * Validates and parses date string
     */
    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank()) {
            throw new InvalidInputException("Date cannot be null or empty");
        }
        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException(
                    "Invalid date format. Please use dd-MM-yyyy format", e);
        }
    }

    /**
     * Validates and parses optional date string (can be empty for wedding date)
     */
    public static LocalDate parseOptionalDate(String dateStr) {
        if (dateStr == null || dateStr.isBlank() || dateStr.equalsIgnoreCase("N/A")) {
            return null;
        }
        return parseDate(dateStr);
    }

    /**
     * Validates non-empty string
     */
    public static void validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new InvalidInputException(fieldName + " cannot be null or empty");
        }
    }

    /**
     * Validates positive number
     */
    public static void validatePositive(double value, String fieldName) {
        if (value < 0) {
            throw new InvalidInputException(fieldName + " cannot be negative");
        }
    }

    /**
     * Formats date for display
     */
    public static String formatDate(LocalDate date) {
        return date != null ? date.format(DATE_FORMATTER) : "N/A";
    }
}
