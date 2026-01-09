/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package payrollsystem2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author nabil
 */
public class UserManager {
    
    // ==========================================
    // LOGIN AUTHENTICATION
    // ==========================================
    public String authenticate(String staffId, String password) {
        
        // Match the database columns: staff_id, password
        String sql = "SELECT * FROM users WHERE staff_id = ? AND password = ?";
        
        try (Connection con = DBConnection.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            // Force Uppercase to match DB 
            pst.setString(1, staffId.toUpperCase()); 
            pst.setString(2, password);
            
            ResultSet rs = pst.executeQuery();
            
            if (rs.next()) {
                return rs.getString("name"); // Return real name to Main
            }
            
        } catch (SQLException e) {
            System.out.println(">> Database Error (Login): " + e.getMessage());
        }
        
        return null; // Login Failed
    }
    
}