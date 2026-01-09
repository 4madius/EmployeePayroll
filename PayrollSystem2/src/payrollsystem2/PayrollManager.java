/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

import java.sql.*;

/**
 *
 * @author nabil
 */
public class PayrollManager {

    // ==========================================
    // 1. HELPER: CHECK IF ID EXISTS
    // ==========================================
    public boolean isIdExists(String id) {
        return getEmployee(id) != null;
    }

    // ==========================================
    // 2. DATABASE HELPER: GET SINGLE EMPLOYEE (NOW USES FACTORY!)
    // ==========================================
    public Employee getEmployee(String id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        Employee emp = null;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String type = rs.getString("type");

                // [DESIGN PATTERN IMPLEMENTATION]
                // Use Factory to reconstruct object from Database data
                if (type.equalsIgnoreCase("Full Time")) {
                    emp = EmployeeFactory.createEmployee(type, id, name, rs.getDouble("basic_salary"), rs.getDouble("allowance"));
                    
                } else if (type.equalsIgnoreCase("Part Time")) {
                    // Note: hours_worked is int, but Factory accepts double args, so it works fine
                    emp = EmployeeFactory.createEmployee(type, id, name, rs.getDouble("hourly_rate"), rs.getDouble("hours_worked"));
                    
                } else if (type.equalsIgnoreCase("Contract")) {
                    emp = EmployeeFactory.createEmployee(type, id, name, rs.getDouble("contract_amount"));
                }
            }

        } catch (SQLException e) {
            System.out.println(">> Error Finding Employee: " + e.getMessage());
        }
        return emp;
    }

    // ==========================================
    // 3. DATABASE OPERATION: CREATE (INSERT)
    // ==========================================
    public boolean addEmployee(Employee emp) {
        String sql = "INSERT INTO employees (id, name, type, basic_salary, allowance, hourly_rate, hours_worked, contract_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, emp.getId());
            pst.setString(2, emp.getName());
            pst.setString(3, emp.getType());

            double basic = 0, allow = 0, rate = 0, amount = 0;
            int hours = 0;

            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                basic = ft.getBasicSalary();
                allow = ft.getAllowance();
            } else if (emp instanceof PartTimeEmployee) {
                PartTimeEmployee pt = (PartTimeEmployee) emp;
                rate = pt.getHourlyRate();
                hours = pt.getHoursWorked();
            } else if (emp instanceof ContractEmployee) {
                ContractEmployee ct = (ContractEmployee) emp;
                amount = ct.getContractAmount();
            }

            pst.setDouble(4, basic);
            pst.setDouble(5, allow);
            pst.setDouble(6, rate);
            pst.setInt(7, hours);
            pst.setDouble(8, amount);

            pst.executeUpdate();
            System.out.println(">> Success: Employee Added to Database.");
            return true;

        } catch (SQLException e) {
            System.out.println(">> Error Adding Employee: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 4. DATABASE OPERATION: READ (VIEW ALL)
    // ==========================================
    public void viewAllEmployees() {
        String sql = "SELECT * FROM employees";
        System.out.println("\n--- Employee List (From Database) ---");
        System.out.printf("%-10s %-25s %-15s %-15s\n", "ID", "NAME", "TYPE", "GROSS SALARY");
        System.out.println("---------------------------------------------------------------------");

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                String name = rs.getString("name");
                String type = rs.getString("type");
                double gross = 0;

                if (type.equalsIgnoreCase("Full Time")) {
                    gross = rs.getDouble("basic_salary") + rs.getDouble("allowance");
                } else if (type.equalsIgnoreCase("Part Time")) {
                    gross = rs.getDouble("hourly_rate") * rs.getInt("hours_worked");
                } else if (type.equalsIgnoreCase("Contract")) {
                    gross = rs.getDouble("contract_amount");
                }

                System.out.printf("%-10s %-25s %-15s RM %.2f\n", id, name, type, gross);
            }

        } catch (SQLException e) {
            System.out.println(">> Error Loading Data: " + e.getMessage());
        }
    }

    // ==========================================
    // 5. DATABASE OPERATION: UPDATE
    // ==========================================
    public void updateEmployee(Employee emp) {
        String sql = "UPDATE employees SET basic_salary=?, allowance=?, hourly_rate=?, hours_worked=?, contract_amount=? WHERE id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            double basic = 0, allow = 0, rate = 0, amount = 0;
            int hours = 0;

            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                basic = ft.getBasicSalary();
                allow = ft.getAllowance();
            } else if (emp instanceof PartTimeEmployee) {
                PartTimeEmployee pt = (PartTimeEmployee) emp;
                rate = pt.getHourlyRate();
                hours = pt.getHoursWorked();
            } else if (emp instanceof ContractEmployee) {
                ContractEmployee ct = (ContractEmployee) emp;
                amount = ct.getContractAmount();
            }

            pst.setDouble(1, basic);
            pst.setDouble(2, allow);
            pst.setDouble(3, rate);
            pst.setInt(4, hours);
            pst.setDouble(5, amount);
            pst.setString(6, emp.getId());

            int rows = pst.executeUpdate();
            if (rows > 0) System.out.println(">> Success: Database updated.");
            else System.out.println(">> Error: Update failed.");

        } catch (SQLException e) {
            System.out.println(">> Database Update Error: " + e.getMessage());
        }
    }

    // ==========================================
    // 6. DATABASE OPERATION: DELETE
    // ==========================================
    public boolean deleteEmployee(String id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, id);
            int rows = pst.executeUpdate();
            
            if (rows > 0) {
                System.out.println(">> Success: Employee " + id + " deleted.");
                return true;
            } else {
                System.out.println(">> Error: ID not found.");
                return false;
            }
        } catch (SQLException e) {
            System.out.println(">> Database Delete Error: " + e.getMessage());
            return false;
        }
    }

    // ==========================================
    // 7. PAYSLIP REPORT (Snapshot Strategy)
    // ==========================================
    public void generatePayslips() {
        String sql = "SELECT * FROM employees";
        System.out.println("\n==========================================");
        System.out.println("          GENERATING PAYSLIPS...          ");
        System.out.println("==========================================");

        try (Connection con = DBConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String id = rs.getString("id");
                
                if (hasPaidThisMonth(id)) {
                    System.out.println("Skipping " + rs.getString("name") + ": Already paid this month.");
                    continue; 
                }

                Employee emp = getEmployee(id); // Factory is used inside here now!
                if (emp != null) {
                    System.out.println("Processing: " + emp.getName());
                    System.out.printf("   Gross: RM %.2f | Tax: RM %.2f | Net: RM %.2f\n", 
                            emp.calculateGrossSalary(), emp.calculateTax(), emp.calculateNetSalary());
                    
                    savePayslipToHistory(emp); 
                }
            }
            System.out.println(">> Payslip generation complete.");

        } catch (SQLException e) {
            System.out.println(">> Error: " + e.getMessage());
        }
    }

    private boolean hasPaidThisMonth(String empId) {
        String sql = "SELECT * FROM payslips WHERE employee_id = ? AND MONTH(payment_date) = MONTH(CURRENT_DATE()) AND YEAR(payment_date) = YEAR(CURRENT_DATE())";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, empId);
            ResultSet rs = pst.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            return false;
        }
    }

    private void savePayslipToHistory(Employee emp) {
        String sql = "INSERT INTO payslips (employee_id, gross_salary, tax_amount, net_salary, hours_worked, hourly_rate, allowance, contract_amount) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setString(1, emp.getId());
            pst.setDouble(2, emp.calculateGrossSalary());
            pst.setDouble(3, emp.calculateTax());
            pst.setDouble(4, emp.calculateNetSalary());
            
            double rate = 0, allow = 0, amount = 0;
            int hours = 0;

            if (emp instanceof FullTimeEmployee) {
                FullTimeEmployee ft = (FullTimeEmployee) emp;
                allow = ft.getAllowance();
            } else if (emp instanceof PartTimeEmployee) {
                PartTimeEmployee pt = (PartTimeEmployee) emp;
                rate = pt.getHourlyRate();
                hours = pt.getHoursWorked();
            } else if (emp instanceof ContractEmployee) {
                ContractEmployee ct = (ContractEmployee) emp;
                amount = ct.getContractAmount();
            }

            pst.setInt(5, hours);
            pst.setDouble(6, rate);
            pst.setDouble(7, allow);
            pst.setDouble(8, amount);

            pst.executeUpdate();
            
        } catch (SQLException e) {
            System.out.println("   [History Save Failed]: " + e.getMessage());
        }
    }

    // ==========================================
    // 8. VIEW PAYSLIP HISTORY (With Details)
    // ==========================================
    public void viewPayslipHistory(String targetId) {
        String sql = "SELECT p.*, e.name FROM payslips p JOIN employees e ON p.employee_id = e.id ";
        
        if (targetId != null && !targetId.isEmpty()) {
            sql += "WHERE p.employee_id = ? ";
        }
        
        sql += "ORDER BY p.payment_date DESC";

        System.out.println("\n--- Payment History Log ---");
        System.out.printf("%-17s %-8s %-15s %-20s %-10s\n", "DATE", "ID", "NAME", "DETAILS", "NET PAID");
        System.out.println("---------------------------------------------------------------------------");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            if (targetId != null && !targetId.isEmpty()) {
                pst.setString(1, targetId);
            }

            ResultSet rs = pst.executeQuery();
            boolean found = false;
            double totalPaid = 0;
            int recordCount = 0;

            while (rs.next()) {
                found = true;
                Timestamp date = rs.getTimestamp("payment_date");
                String id = rs.getString("employee_id");
                String name = rs.getString("name");
                double net = rs.getDouble("net_salary");
                
                String details = "-";
                if (rs.getInt("hours_worked") > 0) {
                    details = rs.getInt("hours_worked") + " hrs @ " + rs.getInt("hourly_rate");
                } else if (rs.getDouble("allowance") > 0) {
                    details = "Allow: " + rs.getInt("allowance");
                } else if (rs.getDouble("contract_amount") > 0) {
                    details = "Contract";
                }

                totalPaid += net;
                recordCount++;

                String shortDate = date.toString().substring(0, 16);
                System.out.printf("%-17s %-8s %-15s %-20s RM %.2f\n", shortDate, id, name, details, net);
            }

            if (!found) {
                System.out.println(">> No payment records found.");
            } else {
                System.out.println("---------------------------------------------------------------------------");
                System.out.printf("%63s RM %.2f\n", "TOTAL PAYOUT:", totalPaid);
                System.out.println("===========================================================================");
                System.out.println("   Records Found: " + recordCount);
            }

        } catch (SQLException e) {
            System.out.println(">> Error Loading History: " + e.getMessage());
        }
    }
    
    // ==========================================
    // 9. VIEW HISTORY BY MONTH (With Details)
    // ==========================================
    public void viewMonthlyHistory(int month, int year) {
        String sql = "SELECT p.*, e.name FROM payslips p JOIN employees e ON p.employee_id = e.id " +
                     "WHERE MONTH(p.payment_date) = ? AND YEAR(p.payment_date) = ? " +
                     "ORDER BY p.payment_date DESC";

        System.out.println("\n--- Monthly Payroll Report (" + month + "/" + year + ") ---");
        System.out.printf("%-17s %-8s %-15s %-20s %-10s\n", "DATE", "ID", "NAME", "DETAILS", "NET PAID");
        System.out.println("---------------------------------------------------------------------------");

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, month);
            pst.setInt(2, year);

            ResultSet rs = pst.executeQuery();
            boolean found = false;
            double totalPaid = 0;

            while (rs.next()) {
                found = true;
                Timestamp date = rs.getTimestamp("payment_date");
                String id = rs.getString("employee_id");
                String name = rs.getString("name");
                double net = rs.getDouble("net_salary");

                String details = "-";
                if (rs.getInt("hours_worked") > 0) {
                    details = rs.getInt("hours_worked") + " hrs @ " + rs.getInt("hourly_rate");
                } else if (rs.getDouble("allowance") > 0) {
                    details = "Allow: " + rs.getInt("allowance");
                } else if (rs.getDouble("contract_amount") > 0) {
                    details = "Contract";
                }

                totalPaid += net;
                String shortDate = date.toString().substring(0, 16);
                System.out.printf("%-17s %-8s %-15s %-20s RM %.2f\n", shortDate, id, name, details, net);
            }

            if (!found) {
                System.out.println(">> No records found for this month.");
            } else {
                System.out.println("---------------------------------------------------------------------------");
                System.out.printf("%63s RM %.2f\n", "TOTAL MONTHLY PAYOUT:", totalPaid);
                System.out.println("===========================================================================");
            }

        } catch (SQLException e) {
            System.out.println(">> Error Loading Monthly History: " + e.getMessage());
        }
    }
}