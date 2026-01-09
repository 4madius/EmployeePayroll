/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Update Port if needed (e.g., 3306 or 3307)
    private static final String URL = "jdbc:mysql://localhost:3307/payroll_db";
    private static final String USER = "root";
    private static final String PASSWORD = ""; 

    // [FIX] REMOVED the "static Connection connection" variable.
    // Now we create a NEW connection every time this is called.
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println(">> Database Connection Failed: " + e.getMessage());
        }
        return con;
    }
}