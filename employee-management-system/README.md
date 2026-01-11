# Employee Management System

A Maven-based Java SE console application for managing employee records in an in-memory data store.

## Features

- Add, update, delete, and query employee records
- Search employees by birthday or wedding anniversary
- Calculate average salary by department
- Find top earners
- Comprehensive exception handling
- Logging with java.util.logging
- Built using Java 17+ features (Records, Streams, Modules)

## Technologies Used

- Java 17
- Maven
- Java Collections Framework
- Java 8 Streams API
- Java Records
- Java Modules
- java.util.logging

## Project Structure

```
employee-management-system/
├── pom.xml
├── src/main/java/
│   ├── module-info.java
│   └── com/employee/management/
│       ├── Main.java
│       ├── model/Employee.java
│       ├── repository/
│       ├── service/
│       ├── exception/
│       └── util/
└── src/main/resources/
    └── logging.properties
```

## Building the Application

```bash
mvn clean package
```

This will create:
- `target/employee-management-system-1.0.0.jar`
- `target/employee-management-system-1.0.0-jar-with-dependencies.jar`

## Running the Application

```bash
java -jar target/employee-management-system-1.0.0-jar-with-dependencies.jar
```

## Available Operations

1. Add Employee
2. Get Employees by First Name
3. Get All Employees (First Name & Phone)
4. Update Email & Phone
5. Delete Employee
6. Find Employees with Birthday on Date
7. Find Employees with Anniversary on Date
8. Get Employee Count
9. Get Average Salary by Department
10. Get Top 3 Highest Paid Employees
11. Exit

## Author

TCS Training Project
