/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

/**
 *
 * @author nabil
 */
// [OOP Concept: Inheritance]
class FullTimeEmployee extends Employee {

    private double basicSalary;
    private double allowance;

    public FullTimeEmployee(String id, String name, double basicSalary, double allowance) {
        super(id, name, "Full Time");
        this.basicSalary = basicSalary;
        this.allowance = allowance;
    }

    public void setSalaryDetails(double basicSalary, double allowance) {
        this.basicSalary = basicSalary;
        this.allowance = allowance;
    }

    @Override
    public double calculateGrossSalary() {
        return basicSalary + allowance;
    }
    
    
    public double getBasicSalary() {
        return basicSalary;
    }

    public double getAllowance() {
        return allowance;
    }
}