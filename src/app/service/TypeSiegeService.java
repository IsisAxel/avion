package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.model.TypeSiege;

public class TypeSiegeService {

    public static List<TypeSiege> getAllTypeSiege(Connection connection) {
        List<TypeSiege> typeSieges = new ArrayList<>();
        String query = "SELECT id_type_siege, type FROM type_siege";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                TypeSiege typeSiege = new TypeSiege();
                typeSiege.setIdTypeSiege(resultSet.getInt("id_type_siege"));
                typeSiege.setType(resultSet.getString("type"));
                typeSieges.add(typeSiege);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return typeSieges;
    }
}