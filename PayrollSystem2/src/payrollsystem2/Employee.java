/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

/**
 *
 * @author nabil
 */
import java.util.Scanner;

// ==========================================
// PART 3: MANAGE EMPLOYEE (Encapsulation & Inheritance)
// Responsibility: Muqri
// ==========================================

// [OOP Concept: Abstraction]
abstract class Employee {
    // [CHANGE: ID is now String]
    private String id;
    private String name;
    protected String type; 

    public Employee(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    // Getters and Setters
    public String getId() { return id; } 
    public String getName() { return name; }
    public String getType() { return type; }
    
    // [OOP Concept: Polymorphism]
    public abstract double calculateGrossSalary();
    
    public double calculateTax() {
        return calculateGrossSalary() * 0.05; 
    }

    public double calculateNetSalary() {
        return calculateGrossSalary() - calculateTax();
    }
}
