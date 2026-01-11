package com.employee.management.repository;

import com.employee.management.model.Employee;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Employee CRUD operations.
 */
public interface EmployeeRepository {

    /**
     * Saves a new employee to the repository
     */
    void save(Employee employee);

    /**
     * Finds the first employee with the given first name
     */
    Optional<Employee> findByFirstName(String firstName);

    /**
     * Finds all employees with the given first name
     */
    List<Employee> findAllByFirstName(String firstName);

    /**
     * Returns all employees in the repository
     */
    List<Employee> findAll();

    /**
     * Updates the email and phone number of an employee
     * 
     * @return true if update was successful, false if employee not found
     */
    boolean updateEmailAndPhone(String firstName, String email, String phoneNumber);

    /**
     * Deletes an employee by first name
     * 
     * @return true if deletion was successful, false if employee not found
     */
    boolean deleteByFirstName(String firstName);

    /**
     * Checks if an employee with the given first name exists
     */
    boolean existsByFirstName(String firstName);

    /**
     * Returns the count of all employees
     */
    long count();
}
