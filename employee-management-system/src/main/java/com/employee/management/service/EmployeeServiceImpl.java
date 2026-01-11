package com.employee.management.service;

import com.employee.management.exception.DuplicateEmployeeException;
import com.employee.management.exception.EmployeeNotFoundException;
import com.employee.management.exception.InvalidInputException;
import com.employee.management.model.Employee;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.util.InputValidator;
import com.employee.management.util.LoggerUtil;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Implementation of EmployeeService using Java 8 Streams.
 */
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOGGER = LoggerUtil.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository repository;

    public EmployeeServiceImpl(EmployeeRepository repository) {
        this.repository = repository;
        LOGGER.info("EmployeeServiceImpl initialized");
    }

    @Override
    public void addEmployee(Employee employee) {
        LOGGER.info("Adding employee: " + employee.getFullName());

        // Validate email
        if (!InputValidator.isValidEmail(employee.email())) {
            var errorMsg = "Invalid email format: " + employee.email();
            LOGGER.warning(errorMsg);
            throw new InvalidInputException(errorMsg);
        }

        // Validate phone
        if (!InputValidator.isValidPhone(employee.phoneNumber())) {
            var errorMsg = "Invalid phone number format: " + employee.phoneNumber();
            LOGGER.warning(errorMsg);
            throw new InvalidInputException(errorMsg);
        }

        // Check for duplicate
        if (repository.existsByFirstName(employee.firstName())) {
            var errorMsg = "Employee with firstName '" + employee.firstName() + "' already exists";
            LOGGER.warning(errorMsg);
            throw new DuplicateEmployeeException(errorMsg);
        }

        repository.save(employee);
        LOGGER.info("Employee added successfully: " + employee.getFullName());
    }

    @Override
    public List<Employee> getEmployeesByFirstName(String firstName) {
        LOGGER.info("Getting employees by firstName: " + firstName);

        var employees = repository.findAllByFirstName(firstName);

        if (employees.isEmpty()) {
            var errorMsg = "No employees found with firstName: " + firstName;
            LOGGER.warning(errorMsg);
            throw new EmployeeNotFoundException(errorMsg);
        }

        LOGGER.info("Found " + employees.size() + " employee(s) with firstName: " + firstName);
        return employees;
    }

    @Override
    public List<Map<String, String>> getFirstNameAndPhone() {
        LOGGER.info("Getting firstName and phone for all employees");

        return repository.findAll().stream()
                .map(emp -> Map.of(
                        "firstName", emp.firstName(),
                        "phoneNumber", emp.phoneNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public void updateEmailAndPhone(String firstName, String email, String phoneNumber) {
        LOGGER.info("Updating email and phone for: " + firstName);

        // Validate email
        if (!InputValidator.isValidEmail(email)) {
            var errorMsg = "Invalid email format: " + email;
            LOGGER.warning(errorMsg);
            throw new InvalidInputException(errorMsg);
        }

        // Validate phone
        if (!InputValidator.isValidPhone(phoneNumber)) {
            var errorMsg = "Invalid phone number format: " + phoneNumber;
            LOGGER.warning(errorMsg);
            throw new InvalidInputException(errorMsg);
        }

        var updated = repository.updateEmailAndPhone(firstName, email, phoneNumber);

        if (!updated) {
            var errorMsg = "Employee not found with firstName: " + firstName;
            LOGGER.warning(errorMsg);
            throw new EmployeeNotFoundException(errorMsg);
        }

        LOGGER.info("Employee updated successfully: " + firstName);
    }

    @Override
    public void deleteByFirstName(String firstName) {
        LOGGER.info("Deleting employee by firstName: " + firstName);

        var deleted = repository.deleteByFirstName(firstName);

        if (!deleted) {
            var errorMsg = "Employee not found with firstName: " + firstName;
            LOGGER.warning(errorMsg);
            throw new EmployeeNotFoundException(errorMsg);
        }

        LOGGER.info("Employee deleted successfully: " + firstName);
    }

    @Override
    public List<Map<String, String>> getEmployeesWithBirthdayOn(LocalDate date) {
        LOGGER.info("Finding employees with birthday on: " + date);

        return repository.findAll().stream()
                .filter(emp -> emp.hasBirthdayOn(date))
                .map(emp -> Map.of(
                        "firstName", emp.firstName(),
                        "email", emp.email()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, String>> getEmployeesWithAnniversaryOn(LocalDate date) {
        LOGGER.info("Finding employees with anniversary on: " + date);

        return repository.findAll().stream()
                .filter(emp -> emp.hasAnniversaryOn(date))
                .map(emp -> Map.of(
                        "firstName", emp.firstName(),
                        "phoneNumber", emp.phoneNumber()))
                .collect(Collectors.toList());
    }

    @Override
    public long getEmployeeCount() {
        var count = repository.count();
        LOGGER.info("Total employee count: " + count);
        return count;
    }

    @Override
    public Map<String, Double> getAverageSalaryByDepartment() {
        LOGGER.info("Calculating average salary by department");

        return repository.findAll().stream()
                .collect(Collectors.groupingBy(
                        Employee::department,
                        Collectors.averagingDouble(Employee::salary)));
    }

    @Override
    public List<Employee> getTopThreeHighestPaid() {
        LOGGER.info("Finding top 3 highest paid employees");

        return repository.findAll().stream()
                .sorted(Comparator.comparingDouble(Employee::salary).reversed())
                .limit(3)
                .collect(Collectors.toList());
    }
}
