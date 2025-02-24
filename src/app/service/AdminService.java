package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import app.model.Admin;

public class AdminService {

    public static boolean isValidAdmin(Connection connection, Admin admin) {
        String query = "SELECT 1 FROM admin WHERE username = ? AND password = crypt(?, password)";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}