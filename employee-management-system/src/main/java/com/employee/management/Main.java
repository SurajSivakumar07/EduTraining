package com.employee.management;

import com.employee.management.exception.DuplicateEmployeeException;
import com.employee.management.exception.EmployeeNotFoundException;
import com.employee.management.exception.InvalidInputException;
import com.employee.management.model.Employee;
import com.employee.management.repository.EmployeeRepository;
import com.employee.management.repository.InMemoryEmployeeRepository;
import com.employee.management.service.EmployeeService;
import com.employee.management.service.EmployeeServiceImpl;
import com.employee.management.util.InputValidator;
import com.employee.management.util.LoggerUtil;

import java.time.LocalDate;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Main console application for Employee Management System.
 */
public class Main {

    private static final Logger LOGGER = LoggerUtil.getLogger(Main.class);
    private static final Scanner scanner = new Scanner(System.in);
    private static EmployeeService employeeService;

    public static void main(String[] args) {
        LOGGER.info("Starting Employee Management System");

        // Initialize repository and service
        EmployeeRepository repository = new InMemoryEmployeeRepository();
        employeeService = new EmployeeServiceImpl(repository);

        // Pre-populate with sample data
        initializeSampleData();

        // Display welcome message
        displayWelcome();

        // Main menu loop
        var running = true;
        while (running) {
            try {
                displayMenu();
                var choice = readInt("Enter your choice: ");

                running = handleMenuChoice(choice);

            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                LOGGER.severe("Unexpected error: " + e.getMessage());
            }
        }

        scanner.close();
        LOGGER.info("Employee Management System terminated");
        System.out.println("\nThank you for using Employee Management System!");
    }

    private static void displayWelcome() {
        System.out.println("""
                ╔════════════════════════════════════════════════════════╗
                ║     EMPLOYEE MANAGEMENT SYSTEM - TCS TRAINING          ║
                ║              In-Memory Data Store                      ║
                ╚════════════════════════════════════════════════════════╝
                """);
    }

    private static void displayMenu() {
        System.out.println("""

                ┌────────────────── MAIN MENU ──────────────────────┐
                │  1.  Add Employee                                 │
                │  2.  Get Employees by First Name                  │
                │  3.  Get All Employees (First Name & Phone)       │
                │  4.  Update Email & Phone                         │
                │  5.  Delete Employee                              │
                │  6.  Find Employees with Birthday on Date         │
                │  7.  Find Employees with Anniversary on Date      │
                │  8.  Get Employee Count                           │
                │  9.  Get Average Salary by Department             │
                │  10. Get Top 3 Highest Paid Employees             │
                │  11. Exit                                         │
                └───────────────────────────────────────────────────┘
                """);
    }

    private static boolean handleMenuChoice(int choice) {
        System.out.println();

        return switch (choice) {
            case 1 -> {
                addEmployee();
                yield true;
            }
            case 2 -> {
                getEmployeesByFirstName();
                yield true;
            }
            case 3 -> {
                getFirstNameAndPhone();
                yield true;
            }
            case 4 -> {
                updateEmailAndPhone();
                yield true;
            }
            case 5 -> {
                deleteEmployee();
                yield true;
            }
            case 6 -> {
                findEmployeesWithBirthday();
                yield true;
            }
            case 7 -> {
                findEmployeesWithAnniversary();
                yield true;
            }
            case 8 -> {
                getEmployeeCount();
                yield true;
            }
            case 9 -> {
                getAverageSalaryByDepartment();
                yield true;
            }
            case 10 -> {
                getTopThreeHighestPaid();
                yield true;
            }
            case 11 -> {
                System.out.println("Exiting...");
                yield false;
            }
            default -> {
                System.out.println("Invalid choice! Please enter a number between 1 and 11.");
                yield true;
            }
        };
    }

    private static void addEmployee() {
        System.out.println("═══ Add New Employee ═══\n");

        try {
            var firstName = readString("First Name: ");
            var lastName = readString("Last Name: ");
            var department = readString("Department: ");
            var address = readString("Address: ");
            var email = readString("Email: ");
            var phoneNumber = readString("Phone Number (10 digits): ");
            var dobStr = readString("Date of Birth (dd-MM-yyyy): ");
            var weddingStr = readString("Wedding Date (dd-MM-yyyy) [Press Enter if N/A]: ");
            var salary = readDouble("Salary: ");

            var dob = InputValidator.parseDate(dobStr);
            var weddingDate = InputValidator.parseOptionalDate(weddingStr);

            var employee = new Employee(
                    firstName, lastName, department, address,
                    email, phoneNumber, dob, weddingDate, salary);

            employeeService.addEmployee(employee);
            System.out.println("\n✓ Employee added successfully!");

        } catch (DuplicateEmployeeException | InvalidInputException e) {
            System.err.println("✗ Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ Failed to add employee: " + e.getMessage());
        }
    }

    private static void getEmployeesByFirstName() {
        System.out.println("═══ Get Employees by First Name ═══\n");

        try {
            var firstName = readString("Enter First Name: ");
            var employees = employeeService.getEmployeesByFirstName(firstName);

            System.out.println("\nFound " + employees.size() + " employee(s):\n");
            employees.forEach(emp -> System.out.println(emp.toFormattedString()));

        } catch (EmployeeNotFoundException e) {
            System.err.println("✗ " + e.getMessage());
        }
    }

    private static void getFirstNameAndPhone() {
        System.out.println("═══ All Employees (First Name & Phone) ═══\n");

        var list = employeeService.getFirstNameAndPhone();

        if (list.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            System.out.printf("%-20s %-15s%n", "First Name", "Phone Number");
            System.out.println("─".repeat(35));
            list.forEach(emp -> System.out.printf("%-20s %-15s%n",
                    emp.get("firstName"), emp.get("phoneNumber")));
            System.out.println("\nTotal: " + list.size() + " employee(s)");
        }
    }

    private static void updateEmailAndPhone() {
        System.out.println("═══ Update Email & Phone ═══\n");

        try {
            var firstName = readString("Enter First Name: ");
            var email = readString("New Email: ");
            var phoneNumber = readString("New Phone Number (10 digits): ");

            employeeService.updateEmailAndPhone(firstName, email, phoneNumber);
            System.out.println("\n✓ Employee updated successfully!");

        } catch (EmployeeNotFoundException | InvalidInputException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }

    private static void deleteEmployee() {
        System.out.println("═══ Delete Employee ═══\n");

        try {
            var firstName = readString("Enter First Name to Delete: ");
            var confirm = readString("Are you sure you want to delete '" + firstName + "'? (yes/no): ");

            if (confirm.equalsIgnoreCase("yes")) {
                employeeService.deleteByFirstName(firstName);
                System.out.println("\n✓ Employee deleted successfully!");
            } else {
                System.out.println("\n✗ Deletion cancelled.");
            }

        } catch (EmployeeNotFoundException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }

    private static void findEmployeesWithBirthday() {
        System.out.println("═══ Find Employees with Birthday ═══\n");

        try {
            var dateStr = readString("Enter Date (dd-MM-yyyy): ");
            var date = InputValidator.parseDate(dateStr);

            var list = employeeService.getEmployeesWithBirthdayOn(date);

            if (list.isEmpty()) {
                System.out.println("\nNo employees found with birthday on " + dateStr);
            } else {
                System.out.println("\nEmployees with birthday on " + dateStr + ":\n");
                System.out.printf("%-20s %-30s%n", "First Name", "Email");
                System.out.println("─".repeat(50));
                list.forEach(emp -> System.out.printf("%-20s %-30s%n",
                        emp.get("firstName"), emp.get("email")));
                System.out.println("\nTotal: " + list.size() + " employee(s)");
            }

        } catch (InvalidInputException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }

    private static void findEmployeesWithAnniversary() {
        System.out.println("═══ Find Employees with Anniversary ═══\n");

        try {
            var dateStr = readString("Enter Date (dd-MM-yyyy): ");
            var date = InputValidator.parseDate(dateStr);

            var list = employeeService.getEmployeesWithAnniversaryOn(date);

            if (list.isEmpty()) {
                System.out.println("\nNo employees found with anniversary on " + dateStr);
            } else {
                System.out.println("\nEmployees with anniversary on " + dateStr + ":\n");
                System.out.printf("%-20s %-15s%n", "First Name", "Phone Number");
                System.out.println("─".repeat(35));
                list.forEach(emp -> System.out.printf("%-20s %-15s%n",
                        emp.get("firstName"), emp.get("phoneNumber")));
                System.out.println("\nTotal: " + list.size() + " employee(s)");
            }

        } catch (InvalidInputException e) {
            System.err.println("✗ Error: " + e.getMessage());
        }
    }

    private static void getEmployeeCount() {
        System.out.println("═══ Employee Count ═══\n");

        var count = employeeService.getEmployeeCount();
        System.out.println("Total number of employees: " + count);
    }

    private static void getAverageSalaryByDepartment() {
        System.out.println("═══ Average Salary by Department ═══\n");

        var avgSalaries = employeeService.getAverageSalaryByDepartment();

        if (avgSalaries.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            System.out.printf("%-20s %-20s%n", "Department", "Average Salary");
            System.out.println("─".repeat(40));
            avgSalaries.forEach((dept, avg) -> System.out.printf("%-20s ₹%-19.2f%n", dept, avg));
        }
    }

    private static void getTopThreeHighestPaid() {
        System.out.println("═══ Top 3 Highest Paid Employees ═══\n");

        var topEmployees = employeeService.getTopThreeHighestPaid();

        if (topEmployees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            System.out.printf("%-5s %-25s %-20s %-15s%n",
                    "Rank", "Name", "Department", "Salary");
            System.out.println("─".repeat(65));

            for (int i = 0; i < topEmployees.size(); i++) {
                var emp = topEmployees.get(i);
                System.out.printf("%-5d %-25s %-20s ₹%-14.2f%n",
                        (i + 1), emp.getFullName(), emp.department(), emp.salary());
            }
        }
    }

    private static void initializeSampleData() {
        LOGGER.info("Initializing sample data");

        try {
            // Sample employees with upcoming birthdays and anniversaries for testing
            var today = LocalDate.now();

            employeeService.addEmployee(new Employee(
                    "Rajesh", "Kumar", "IT", "123 MG Road, Bangalore",
                    "rajesh.kumar@company.com", "9876543210",
                    today, // Birthday today for testing
                    LocalDate.of(2015, 6, 15), 75000.0));

            employeeService.addEmployee(new Employee(
                    "Priya", "Sharma", "HR", "456 Park Street, Mumbai",
                    "priya.sharma@company.com", "9876543211",
                    LocalDate.of(1992, 3, 20),
                    today, // Anniversary today for testing
                    65000.0));

            employeeService.addEmployee(new Employee(
                    "Amit", "Patel", "Finance", "789 Anna Salai, Chennai",
                    "amit.patel@company.com", "9876543212",
                    LocalDate.of(1988, 7, 10),
                    LocalDate.of(2012, 12, 5), 85000.0));

            employeeService.addEmployee(new Employee(
                    "Sneha", "Reddy", "IT", "321 Brigade Road, Bangalore",
                    "sneha.reddy@company.com", "9876543213",
                    LocalDate.of(1995, 11, 25),
                    null, 72000.0));

            employeeService.addEmployee(new Employee(
                    "Vikram", "Singh", "Operations", "654 Connaught Place, Delhi",
                    "vikram.singh@company.com", "9876543214",
                    LocalDate.of(1985, 2, 14),
                    LocalDate.of(2010, 8, 20), 95000.0));

            employeeService.addEmployee(new Employee(
                    "Ananya", "Iyer", "IT", "987 Banjara Hills, Hyderabad",
                    "ananya.iyer@company.com", "9876543215",
                    LocalDate.of(1993, 9, 5),
                    LocalDate.of(2018, 4, 22), 78000.0));

            employeeService.addEmployee(new Employee(
                    "Arjun", "Mehta", "Finance", "147 Residency Road, Pune",
                    "arjun.mehta@company.com", "9876543216",
                    LocalDate.of(1987, 12, 30),
                    LocalDate.of(2013, 11, 10), 88000.0));

            LOGGER.info("Sample data initialized successfully");
            System.out.println("✓ Sample data loaded (7 employees)\n");

        } catch (Exception e) {
            LOGGER.warning("Failed to initialize some sample data: " + e.getMessage());
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                var input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                var input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }
}
