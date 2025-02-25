package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.model.Vol;
import app.model.VolDetails;
import app.model.RegleVol;
import app.model.TypeSiege;

public class VolService {

    public static boolean insertVolWithDetails(Connection connection, Vol vol, List<VolDetails> volDetailsList) {
        String insertVolQuery = "INSERT INTO vol (id_avion, id_ville_depart, id_ville_destination, date_depart, date_arrive) VALUES (?, ?, ?, ?, ?)";
        String insertVolDetailsQuery = "INSERT INTO vol_details (id_vol, id_type_siege, prix, place_dispo) VALUES (?, ?, ?, ?)";
        String insertRegleVolQuery = "INSERT INTO regle_vol (id_vol, heure_max_reservation, heure_max_annulation) VALUES (?, ?, ?)";
        
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement volStatement = connection.prepareStatement(insertVolQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                volStatement.setInt(1, vol.getAvion().getIdAvion());
                volStatement.setInt(2, vol.getVilleDepart().getIdVille());
                volStatement.setInt(3, vol.getVilleDestination().getIdVille());
                volStatement.setTimestamp(4, Timestamp.valueOf(vol.getDateDepart()));
                volStatement.setTimestamp(5, Timestamp.valueOf(vol.getDateArrive()));
                volStatement.executeUpdate();
                
                try (ResultSet generatedKeys = volStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int volId = generatedKeys.getInt(1);
                        
                        // Insert into vol_details table for each VolDetails
                        try (PreparedStatement volDetailsStatement = connection.prepareStatement(insertVolDetailsQuery)) {
                            for (VolDetails volDetails : volDetailsList) {
                                volDetailsStatement.setInt(1, volId);
                                volDetailsStatement.setInt(2, volDetails.getTypeSiege().getIdTypeSiege());
                                volDetailsStatement.setDouble(3, volDetails.getPrix());
                                volDetailsStatement.setInt(4, volDetails.getPlaceDispo());
                                volDetailsStatement.addBatch();
                            }
                            volDetailsStatement.executeBatch();
                        }

                        // Insert into regle_vol table
                        try (PreparedStatement regleVolStatement = connection.prepareStatement(insertRegleVolQuery)) {
                            regleVolStatement.setInt(1, volId);
                            regleVolStatement.setInt(2, vol.getRegleVol().getHeureMaxReservation());
                            regleVolStatement.setInt(3, vol.getRegleVol().getHeureMaxAnnulation());
                            regleVolStatement.executeUpdate();
                        }
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    public static List<Vol> getAllVol(Connection connection) {
        String getAllVolQuery = "SELECT v.id_vol, v.id_avion, v.id_ville_depart, v.id_ville_destination, v.date_depart, v.date_arrive, "
                              + "vd.id_vol_prix, vd.id_type_siege, vd.prix, vd.place_dispo, "
                              + "rv.id_regle_vol, rv.heure_max_reservation, rv.heure_max_annulation "
                              + "FROM vol v "
                              + "LEFT JOIN vol_details vd ON v.id_vol = vd.id_vol "
                              + "LEFT JOIN regle_vol rv ON v.id_vol = rv.id_vol";
        
        Map<Integer, Vol> volMap = new HashMap<>();
        
        try (PreparedStatement statement = connection.prepareStatement(getAllVolQuery);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                int volId = resultSet.getInt("id_vol");
                Vol vol = volMap.get(volId);
                
                if (vol == null) {
                    vol = new Vol();
                    vol.setIdVol(volId);
                    vol.setAvion(AvionService.getAvionById(connection, resultSet.getInt("id_avion")));
                    vol.setVilleDepart(VilleService.getVilleById(connection, resultSet.getInt("id_ville_depart")));
                    vol.setVilleDestination(VilleService.getVilleById(connection, resultSet.getInt("id_ville_destination")));
                    vol.setDateDepart(resultSet.getTimestamp("date_depart").toLocalDateTime());
                    vol.setDateArrive(resultSet.getTimestamp("date_arrive").toLocalDateTime());
                    vol.setVolDetails(new ArrayList<>());

                    RegleVol regleVol = new RegleVol();
                    regleVol.setIdRegleVol(resultSet.getInt("id_regle_vol"));
                    regleVol.setHeureMaxReservation(resultSet.getInt("heure_max_reservation"));
                    regleVol.setHeureMaxAnnulation(resultSet.getInt("heure_max_annulation"));
                    vol.setRegleVol(regleVol);

                    volMap.put(volId, vol);
                }
                
                VolDetails volDetails = new VolDetails();
                volDetails.setIdVolDetails(resultSet.getInt("id_vol_prix"));
                volDetails.setTypeSiege(new TypeSiege(resultSet.getInt("id_type_siege")));
                volDetails.setPrix(resultSet.getDouble("prix"));
                volDetails.setPlaceDispo(resultSet.getInt("place_dispo"));
                
                vol.getVolDetails().add(volDetails);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new ArrayList<>(volMap.values());
    }

    public static Vol getVolById(Connection connection, int idVol) {
        String getVolByIdQuery = "SELECT v.id_vol, v.id_avion, v.id_ville_depart, v.id_ville_destination, v.date_depart, v.date_arrive, "
                               + "vd.id_vol_prix, vd.id_type_siege, vd.prix, vd.place_dispo, "
                               + "rv.id_regle_vol, rv.heure_max_reservation, rv.heure_max_annulation "
                               + "FROM vol v "
                               + "LEFT JOIN vol_details vd ON v.id_vol = vd.id_vol "
                               + "LEFT JOIN regle_vol rv ON v.id_vol = rv.id_vol "
                               + "WHERE v.id_vol = ?";
        
        Vol vol = null;
        
        try (PreparedStatement statement = connection.prepareStatement(getVolByIdQuery)) {
            statement.setInt(1, idVol);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    if (vol == null) {
                        vol = new Vol();
                        vol.setIdVol(resultSet.getInt("id_vol"));
                        vol.setAvion(AvionService.getAvionById(connection, resultSet.getInt("id_avion")));
                        vol.setVilleDepart(VilleService.getVilleById(connection, resultSet.getInt("id_ville_depart")));
                        vol.setVilleDestination(VilleService.getVilleById(connection, resultSet.getInt("id_ville_destination")));
                        vol.setDateDepart(resultSet.getTimestamp("date_depart").toLocalDateTime());
                        vol.setDateArrive(resultSet.getTimestamp("date_arrive").toLocalDateTime());
                        vol.setVolDetails(new ArrayList<>());

                        RegleVol regleVol = new RegleVol();
                        regleVol.setIdRegleVol(resultSet.getInt("id_regle_vol"));
                        regleVol.setHeureMaxReservation(resultSet.getInt("heure_max_reservation"));
                        regleVol.setHeureMaxAnnulation(resultSet.getInt("heure_max_annulation"));
                        vol.setRegleVol(regleVol);
                    }
                    
                    VolDetails volDetails = new VolDetails();
                    volDetails.setIdVolDetails(resultSet.getInt("id_vol_prix"));
                    volDetails.setTypeSiege(new TypeSiege(resultSet.getInt("id_type_siege")));
                    volDetails.setPrix(resultSet.getDouble("prix"));
                    volDetails.setPlaceDispo(resultSet.getInt("place_dispo"));
                    
                    vol.getVolDetails().add(volDetails);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return vol;
    }

    public static boolean updateVol(Connection connection, Vol vol) {
        String updateVolQuery = "UPDATE vol SET id_avion = ?, id_ville_depart = ?, id_ville_destination = ?, date_depart = ?, date_arrive = ? WHERE id_vol = ?";
        String deleteVolDetailsQuery = "DELETE FROM vol_details WHERE id_vol = ?";
        String insertVolDetailsQuery = "INSERT INTO vol_details (id_vol, id_type_siege, prix, place_dispo) VALUES (?, ?, ?, ?)";
        String updateRegleVolQuery = "UPDATE regle_vol SET heure_max_reservation = ?, heure_max_annulation = ? WHERE id_vol = ?";
        
        try {
            connection.setAutoCommit(false);
            
            try (PreparedStatement volStatement = connection.prepareStatement(updateVolQuery)) {
                volStatement.setInt(1, vol.getAvion().getIdAvion());
                volStatement.setInt(2, vol.getVilleDepart().getIdVille());
                volStatement.setInt(3, vol.getVilleDestination().getIdVille());
                volStatement.setTimestamp(4, Timestamp.valueOf(vol.getDateDepart()));
                volStatement.setTimestamp(5, Timestamp.valueOf(vol.getDateArrive()));
                volStatement.setInt(6, vol.getIdVol());
                volStatement.executeUpdate();
            }

            try (PreparedStatement deleteVolDetailsStatement = connection.prepareStatement(deleteVolDetailsQuery)) {
                deleteVolDetailsStatement.setInt(1, vol.getIdVol());
                deleteVolDetailsStatement.executeUpdate();
            }

            try (PreparedStatement volDetailsStatement = connection.prepareStatement(insertVolDetailsQuery)) {
                for (VolDetails volDetails : vol.getVolDetails()) {
                    volDetailsStatement.setInt(1, vol.getIdVol());
                    volDetailsStatement.setInt(2, volDetails.getTypeSiege().getIdTypeSiege());
                    volDetailsStatement.setDouble(3, volDetails.getPrix());
                    volDetailsStatement.setInt(4, volDetails.getPlaceDispo());
                    volDetailsStatement.addBatch();
                }
                volDetailsStatement.executeBatch();
            }

            try (PreparedStatement regleVolStatement = connection.prepareStatement(updateRegleVolQuery)) {
                regleVolStatement.setInt(1, vol.getRegleVol().getHeureMaxReservation());
                regleVolStatement.setInt(2, vol.getRegleVol().getHeureMaxAnnulation());
                regleVolStatement.setInt(3, vol.getIdVol());
                regleVolStatement.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }

    public static List<Vol> searchVols(Connection connection, Vol searchCriteria) {
        List<Vol> vols = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder("SELECT v.id_vol, v.id_avion, v.id_ville_depart, v.id_ville_destination, v.date_depart, v.date_arrive, "
                + "vd.id_vol_prix, vd.id_type_siege, vd.prix, vd.place_dispo, "
                + "rv.id_regle_vol, rv.heure_max_reservation, rv.heure_max_annulation "
                + "FROM vol v "
                + "LEFT JOIN vol_details vd ON v.id_vol = vd.id_vol "
                + "LEFT JOIN regle_vol rv ON v.id_vol = rv.id_vol "
                + "WHERE 1=1");

        List<Object> parameters = new ArrayList<>();

        if (searchCriteria.getAvion() != null && searchCriteria.getAvion().getIdAvion() != 0) {
            queryBuilder.append(" AND v.id_avion = ?");
            parameters.add(searchCriteria.getAvion().getIdAvion());
        }
        if (searchCriteria.getVilleDepart() != null && searchCriteria.getVilleDepart().getIdVille() != 0) {
            queryBuilder.append(" AND v.id_ville_depart = ?");
            parameters.add(searchCriteria.getVilleDepart().getIdVille());
        }
        if (searchCriteria.getVilleDestination() != null && searchCriteria.getVilleDestination().getIdVille() != 0) {
            queryBuilder.append(" AND v.id_ville_destination = ?");
            parameters.add(searchCriteria.getVilleDestination().getIdVille());
        }
        if (searchCriteria.getDateDepart() != null) {
            queryBuilder.append(" AND v.date_depart >= ?");
            parameters.add(Timestamp.valueOf(searchCriteria.getDateDepart()));
        }
        if (searchCriteria.getDateArrive() != null) {
            queryBuilder.append(" AND v.date_arrive <= ?");
            parameters.add(Timestamp.valueOf(searchCriteria.getDateArrive()));
        }

        try (PreparedStatement statement = connection.prepareStatement(queryBuilder.toString())) {
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                Map<Integer, Vol> volMap = new HashMap<>();

                while (resultSet.next()) {
                    int volId = resultSet.getInt("id_vol");
                    Vol vol = volMap.get(volId);

                    if (vol == null) {
                        vol = new Vol();
                        vol.setIdVol(volId);
                        vol.setAvion(AvionService.getAvionById(connection, resultSet.getInt("id_avion")));
                        vol.setVilleDepart(VilleService.getVilleById(connection, resultSet.getInt("id_ville_depart")));
                        vol.setVilleDestination(VilleService.getVilleById(connection, resultSet.getInt("id_ville_destination")));
                        vol.setDateDepart(resultSet.getTimestamp("date_depart").toLocalDateTime());
                        vol.setDateArrive(resultSet.getTimestamp("date_arrive").toLocalDateTime());
                        vol.setVolDetails(new ArrayList<>());

                        RegleVol regleVol = new RegleVol();
                        regleVol.setIdRegleVol(resultSet.getInt("id_regle_vol"));
                        regleVol.setHeureMaxReservation(resultSet.getInt("heure_max_reservation"));
                        regleVol.setHeureMaxAnnulation(resultSet.getInt("heure_max_annulation"));
                        vol.setRegleVol(regleVol);

                        volMap.put(volId, vol);
                    }

                    VolDetails volDetails = new VolDetails();
                    volDetails.setIdVolDetails(resultSet.getInt("id_vol_prix"));
                    volDetails.setTypeSiege(new TypeSiege(resultSet.getInt("id_type_siege")));
                    volDetails.setPrix(resultSet.getDouble("prix"));
                    volDetails.setPlaceDispo(resultSet.getInt("place_dispo"));

                    vol.getVolDetails().add(volDetails);
                }

                vols.addAll(volMap.values());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vols;
    }
}