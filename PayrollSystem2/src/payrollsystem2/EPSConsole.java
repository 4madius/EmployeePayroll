/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;
import java.util.Scanner;

/**
 *
 * @author nabil
 */ 
public class EPSConsole {
    
    static Scanner scanner = new Scanner(System.in);
    static PayrollManager manager = new PayrollManager();

    public static void main(String[] args) {
        if (performLogin()) {
            showDashboard();
        } else {
            System.out.println("Access Denied. Exiting System.");
        }
    }

   // ==========================================
   // LOGIN SCREEN
   // ==========================================
   public static boolean performLogin() {
        UserManager userManager = new UserManager();

        System.out.println("========================================");
        System.out.println("    EMPLOYEE PAYROLL SYSTEM (EPS)       ");
        System.out.println("========================================");
        
        while (true) {
            System.out.print("\nEnter Staff ID: ");
            String id = scanner.nextLine();
            
            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            String realName = userManager.authenticate(id, password);

            if (realName != null) {
                System.out.println("\nLogin Successful! Welcome, " + realName.toUpperCase());
                return true;
            } else {
                System.out.println("\n>> Access Denied: Invalid ID or Password. Please try again.");
            }
        }
    }

    // ==========================================
    // MAIN DASHBOARD MENU
    // ==========================================
    public static void showDashboard() {
        int choice = 0;
        
        while (choice != 7) {
            System.out.println("\n--- MAIN DASHBOARD ---");
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Update Salary Info");
            System.out.println("4. Delete Employee");
            System.out.println("5. Generate Payslip Report");
            System.out.println("6. View Payment History"); 
            System.out.println("7. Exit");
            System.out.print("Select Option: ");
            
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                choice = 0; 
            }

            switch (choice) {
                case 1:
                    inputEmployeeData();
                    break;
                case 2:
                    manager.viewAllEmployees();
                    break;
                case 3:
                    updateEmployeeLogic();
                    break;
                case 4:
                    System.out.print("\nEnter Employee ID to Delete: ");
                    String delId = scanner.nextLine();
                    manager.deleteEmployee(delId);
                    break;
                case 5:
                    manager.generatePayslips();
                    break;
                case 6: 
                    // HISTORY SUB-MENU
                    System.out.println("\n--- View History Options ---");
                    System.out.println("1. View All / Filter by ID");
                    System.out.println("2. View by Month & Year");
                    System.out.print("Select: ");
                    
                    try {
                        int histChoice = Integer.parseInt(scanner.nextLine());
                        if (histChoice == 1) {
                            System.out.print("Enter Employee ID to Filter (or PRESS ENTER to view all): ");
                            String historyId = scanner.nextLine().trim();
                            manager.viewPayslipHistory(historyId);
                        } else if (histChoice == 2) {
                            System.out.print("Enter Month (1-12): ");
                            int month = Integer.parseInt(scanner.nextLine());
                            System.out.print("Enter Year (e.g., 2026): ");
                            int year = Integer.parseInt(scanner.nextLine());
                            manager.viewMonthlyHistory(month, year);
                        } else {
                            System.out.println("Invalid selection.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Invalid number entered.");
                    }
                    break;
                case 7:
                    System.out.println("Exiting System. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // ==========================================
    // OPTION 1: ADD NEW EMPLOYEE (NOW USES FACTORY!)
    // ==========================================
    public static void inputEmployeeData() {
        System.out.println("\n--- Register New Employee ---");
        try {
            String id;
            
            // Loop until unique ID
            while (true) {
                System.out.print("Enter Employee ID (e.g., E001): ");
                id = scanner.nextLine();
                
                if (manager.isIdExists(id)) {
                    System.out.println(">> Error: The ID is already used. Please enter a different ID.");
                } else if (id.trim().isEmpty()) {
                    System.out.println(">> Error: ID cannot be empty.");
                } else {
                    break; 
                }
            }
            
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            System.out.println("Select Type: 1.FullTime 2.PartTime 3.Contract");
            String typeInput = scanner.nextLine(); // Read as String for Factory

            Employee newEmp = null;

            // [DESIGN PATTERN IMPLEMENTATION]
            // Kat sini the Factory create the object
            if (typeInput.equals("1")) {
                System.out.print("Enter Basic Salary: ");
                double basic = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter Allowance: ");
                double allow = Double.parseDouble(scanner.nextLine());
                
                // Panggil Factory
                newEmp = EmployeeFactory.createEmployee("1", id, name, basic, allow);

            } else if (typeInput.equals("2")) {
                System.out.print("Enter Hourly Rate: ");
                double rate = Double.parseDouble(scanner.nextLine());
                System.out.print("Enter Hours Worked: ");
                double hours = Double.parseDouble(scanner.nextLine());
                
                // Factory Call
                newEmp = EmployeeFactory.createEmployee("2", id, name, rate, hours);

            } else if (typeInput.equals("3")) {
                System.out.print("Enter Contract Amount: ");
                double amount = Double.parseDouble(scanner.nextLine());
                
                // Factory Call
                newEmp = EmployeeFactory.createEmployee("3", id, name, amount);
            }

            // Save to Manager
            if (newEmp != null) {
                if(manager.addEmployee(newEmp)) {
                    // Success message is handled inside manager (or moved here later for GUI)
                }
            } else {
                System.out.println("Invalid Employee Type or Input Error.");
            }

        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format entered.");
        }
    }
    
    // ==========================================
    // OPTION 3: UPDATE EMPLOYEE (Logic)
    // ==========================================
    public static void updateEmployeeLogic() {
        System.out.println("\n--- Update Salary Information ---");
        System.out.print("Enter Employee ID to Update: ");
        String searchId = scanner.nextLine();
        
        Employee emp = manager.getEmployee(searchId);

        if (emp != null) {
            System.out.println("Found: " + emp.getName() + " [" + emp.getType() + "]");
            
            try {
                if (emp instanceof FullTimeEmployee) {
                    System.out.print("Enter New Basic Salary: ");
                    double newBasic = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter New Allowance: ");
                    double newAllow = Double.parseDouble(scanner.nextLine());
                    ((FullTimeEmployee) emp).setSalaryDetails(newBasic, newAllow);

                } else if (emp instanceof PartTimeEmployee) {
                    System.out.print("Enter New Hourly Rate: ");
                    double newRate = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter New Hours Worked: ");
                    int newHours = Integer.parseInt(scanner.nextLine());
                    ((PartTimeEmployee) emp).setRateAndHours(newRate, newHours);

                } else if (emp instanceof ContractEmployee) {
                    System.out.print("Enter New Contract Amount: ");
                    double newAmount = Double.parseDouble(scanner.nextLine());
                    ((ContractEmployee) emp).setContractAmount(newAmount);
                }

                manager.updateEmployee(emp);

            } catch (NumberFormatException e) {
                System.out.println(">> Error: Invalid number entered. Update cancelled.");
            }
        } else {
            System.out.println(">> Error: Employee ID not found.");
        }
    }
}