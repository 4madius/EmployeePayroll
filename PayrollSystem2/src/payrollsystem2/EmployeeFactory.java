/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

/**
 *
 * @author nabil
 */
public class EmployeeFactory {
    
    // use "double... args" to accept any number of salary parameters
    public static Employee createEmployee(String type, String id, String name, double... args) {
        
        // Normalize input (Handle "1" or "FullTime")
        if (type.equals("1") || type.equalsIgnoreCase("Full Time") || type.equalsIgnoreCase("FullTime")) {
            // args[0] = basic, args[1] = allowance
            return new FullTimeEmployee(id, name, args[0], args[1]);
            
        } else if (type.equals("2") || type.equalsIgnoreCase("Part Time") || type.equalsIgnoreCase("PartTime")) {
            // args[0] = hourlyRate, args[1] = hoursWorked (cast to int)
            return new PartTimeEmployee(id, name, args[0], (int) args[1]);
            
        } else if (type.equals("3") || type.equalsIgnoreCase("Contract")) {
            // args[0] = contractAmount
            return new ContractEmployee(id, name, args[0]);
            
        } else {
            return null; // Invalid Type
        }
    }
}