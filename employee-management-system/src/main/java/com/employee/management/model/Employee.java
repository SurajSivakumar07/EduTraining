package com.employee.management.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Immutable Employee record representing employee data.
 * Uses Java Record feature for concise data modeling.
 */
public record Employee(
        String firstName,
        String lastName,
        String department,
        String address,
        String email,
        String phoneNumber,
        LocalDate dateOfBirth,
        LocalDate weddingDate,
        double salary) {
    /**
     * Compact constructor for validation
     */
    public Employee {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (department == null || department.isBlank()) {
            throw new IllegalArgumentException("Department cannot be null or empty");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number cannot be null or empty");
        }
        if (dateOfBirth == null) {
            throw new IllegalArgumentException("Date of birth cannot be null");
        }
        if (salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
    }

    /**
     * Returns formatted employee details for display
     */
    public String toFormattedString() {
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        var dobStr = dateOfBirth.format(formatter);
        var weddingStr = weddingDate != null ? weddingDate.format(formatter) : "N/A";

        return String.format("""
                ----------------------------------------
                Name: %s %s
                Department: %s
                Address: %s
                Email: %s
                Phone: %s
                Date of Birth: %s
                Wedding Date: %s
                Salary: ₹%.2f
                ----------------------------------------
                """, firstName, lastName, department, address, email,
                phoneNumber, dobStr, weddingStr, salary);
    }

    /**
     * Returns full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Checks if birthday matches the given date (ignoring year)
     */
    public boolean hasBirthdayOn(LocalDate date) {
        return dateOfBirth.getMonth() == date.getMonth()
                && dateOfBirth.getDayOfMonth() == date.getDayOfMonth();
    }

    /**
     * Checks if wedding anniversary matches the given date (ignoring year)
     */
    public boolean hasAnniversaryOn(LocalDate date) {
        if (weddingDate == null) {
            return false;
        }
        return weddingDate.getMonth() == date.getMonth()
                && weddingDate.getDayOfMonth() == date.getDayOfMonth();
    }
}
