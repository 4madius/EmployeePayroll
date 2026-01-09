/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

/**
 *
 * @author nabil
 */
class PartTimeEmployee extends Employee {


    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String id, String name, double hourlyRate, int hoursWorked) {
        super(id, name, "Part Time");
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    public void setRateAndHours(double hourlyRate, int hoursWorked) {
        this.hourlyRate = hourlyRate;
        this.hoursWorked = hoursWorked;
    }

    @Override
    public double calculateGrossSalary() {
        return hourlyRate * hoursWorked;
    }
    
        public double getHourlyRate() {
        return hourlyRate;
    }

    public int getHoursWorked() {
        return hoursWorked;
    }
}
