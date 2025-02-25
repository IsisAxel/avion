package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.model.Ville;

public class VilleService {

    public static List<Ville> getAllVilles(Connection connection) {
        List<Ville> villes = new ArrayList<>();
        String query = "SELECT id_ville, nom, pays, imageUrl FROM ville";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Ville ville = new Ville();
                ville.setIdVille(resultSet.getInt("id_ville"));
                ville.setNom(resultSet.getString("nom"));
                ville.setPays(resultSet.getString("pays"));
                ville.setImageUrl(resultSet.getString("imageUrl"));
                villes.add(ville);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return villes;
    }

    public static Ville getVilleById(Connection connection, int idVille) {
        Ville ville = null;
        String query = "SELECT id_ville, nom, pays, imageUrl FROM ville WHERE id_ville = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idVille);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    ville = new Ville();
                    ville.setIdVille(resultSet.getInt("id_ville"));
                    ville.setNom(resultSet.getString("nom"));
                    ville.setPays(resultSet.getString("pays"));
                    ville.setImageUrl(resultSet.getString("imageUrl"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return ville;
    }
}