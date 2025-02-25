package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.model.Avion;

public class AvionService {

    public static List<Avion> getAllAvions(Connection connection) {
        List<Avion> avions = new ArrayList<>();
        String query = "SELECT id_avion, compagnie, modele, date_fabrication, nombre_max_passager FROM avion";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            
            while (resultSet.next()) {
                Avion avion = new Avion();
                avion.setIdAvion(resultSet.getInt("id_avion"));
                avion.setCompagnie(resultSet.getString("compagnie"));
                avion.setModele(resultSet.getString("modele"));
                avion.setDateFabrication(resultSet.getDate("date_fabrication"));
                avion.setNombreMaxPassager(resultSet.getInt("nombre_max_passager"));
                avions.add(avion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return avions;
    }

    public static Avion getAvionById(Connection connection, int idAvion) {
        Avion avion = null;
        String query = "SELECT id_avion, compagnie, modele, date_fabrication, nombre_max_passager FROM avion WHERE id_avion = ?";
        
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idAvion);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    avion = new Avion();
                    avion.setIdAvion(resultSet.getInt("id_avion"));
                    avion.setCompagnie(resultSet.getString("compagnie"));
                    avion.setModele(resultSet.getString("modele"));
                    avion.setDateFabrication(resultSet.getDate("date_fabrication"));
                    avion.setNombreMaxPassager(resultSet.getInt("nombre_max_passager"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return avion;
    }
}