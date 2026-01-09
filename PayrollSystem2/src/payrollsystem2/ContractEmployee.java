/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

/**
 *
 * @author nabil
 */
class ContractEmployee extends Employee {


    private double contractAmount;

    public ContractEmployee(String id, String name, double contractAmount) {
        super(id, name, "Contract");
        this.contractAmount = contractAmount;
    }

    public void setContractAmount(double contractAmount) {
        this.contractAmount = contractAmount;
    }

    @Override
    public double calculateGrossSalary() {
        return contractAmount;
    }
    
        public double getContractAmount() {
        return contractAmount;
    }
}
