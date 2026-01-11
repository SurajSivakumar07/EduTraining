package com.employee.management.repository;

import com.employee.management.model.Employee;
import com.employee.management.util.LoggerUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * In-memory implementation of EmployeeRepository using ArrayList.
 */
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private static final Logger LOGGER = LoggerUtil.getLogger(InMemoryEmployeeRepository.class);

    private final List<Employee> employees;

    public InMemoryEmployeeRepository() {
        this.employees = new ArrayList<>();
        LOGGER.info("InMemoryEmployeeRepository initialized");
    }

    @Override
    public synchronized void save(Employee employee) {
        employees.add(employee);
        LOGGER.info("Employee saved: " + employee.getFullName());
    }

    @Override
    public synchronized Optional<Employee> findByFirstName(String firstName) {
        LOGGER.info("Searching for employee with firstName: " + firstName);
        return employees.stream()
                .filter(emp -> emp.firstName().equalsIgnoreCase(firstName))
                .findFirst();
    }

    @Override
    public synchronized List<Employee> findAllByFirstName(String firstName) {
        LOGGER.info("Searching for all employees with firstName: " + firstName);
        return employees.stream()
                .filter(emp -> emp.firstName().equalsIgnoreCase(firstName))
                .toList();
    }

    @Override
    public synchronized List<Employee> findAll() {
        LOGGER.info("Retrieving all employees. Total count: " + employees.size());
        // Return a copy to prevent external modification
        return new ArrayList<>(employees);
    }

    @Override
    public synchronized boolean updateEmailAndPhone(String firstName, String email, String phoneNumber) {
        LOGGER.info("Attempting to update email and phone for: " + firstName);

        for (int i = 0; i < employees.size(); i++) {
            var employee = employees.get(i);
            if (employee.firstName().equalsIgnoreCase(firstName)) {
                // Create a new Employee record with updated values
                var updatedEmployee = new Employee(
                        employee.firstName(),
                        employee.lastName(),
                        employee.department(),
                        employee.address(),
                        email,
                        phoneNumber,
                        employee.dateOfBirth(),
                        employee.weddingDate(),
                        employee.salary());
                employees.set(i, updatedEmployee);
                LOGGER.info("Employee updated successfully: " + firstName);
                return true;
            }
        }

        LOGGER.warning("Employee not found for update: " + firstName);
        return false;
    }

    @Override
    public synchronized boolean deleteByFirstName(String firstName) {
        LOGGER.info("Attempting to delete employee: " + firstName);

        var result = employees.removeIf(emp -> emp.firstName().equalsIgnoreCase(firstName));

        if (result) {
            LOGGER.info("Employee deleted successfully: " + firstName);
        } else {
            LOGGER.warning("Employee not found for deletion: " + firstName);
        }

        return result;
    }

    @Override
    public synchronized boolean existsByFirstName(String firstName) {
        return employees.stream()
                .anyMatch(emp -> emp.firstName().equalsIgnoreCase(firstName));
    }

    @Override
    public synchronized long count() {
        return employees.size();
    }
}
