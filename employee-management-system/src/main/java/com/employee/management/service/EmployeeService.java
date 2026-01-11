package com.employee.management.service;

import com.employee.management.model.Employee;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for employee business operations.
 */
public interface EmployeeService {

    /**
     * Adds a new employee
     */
    void addEmployee(Employee employee);

    /**
     * Gets the list of employees by their firstName
     */
    List<Employee> getEmployeesByFirstName(String firstName);

    /**
     * Gets the list of employees with firstName and phoneNumber
     */
    List<Map<String, String>> getFirstNameAndPhone();

    /**
     * Updates the email and phoneNumber of a particular employee
     */
    void updateEmailAndPhone(String firstName, String email, String phoneNumber);

    /**
     * Deletes details of a particular employee by firstName
     */
    void deleteByFirstName(String firstName);

    /**
     * Gets a list of employees with their firstName and emailAddress
     * whose birthday falls on the given date
     */
    List<Map<String, String>> getEmployeesWithBirthdayOn(LocalDate date);

    /**
     * Gets the list of employees with their firstName and phoneNumber
     * whose wedding anniversary falls on the given date
     */
    List<Map<String, String>> getEmployeesWithAnniversaryOn(LocalDate date);

    /**
     * Gets the count of employees
     */
    long getEmployeeCount();

    /**
     * Groups employees by their department and within each department,
     * calculates the average salary
     */
    Map<String, Double> getAverageSalaryByDepartment();

    /**
     * Gets the top three highest paid employees
     */
    List<Employee> getTopThreeHighestPaid();
}
