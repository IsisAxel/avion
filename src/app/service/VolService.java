package app.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.model.Vol;
import app.model.VolDetails;
import app.model.DetailReservation;
import app.model.RegleVol;
import app.model.Reservation;
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

    public static boolean reserveVol(Connection connection, Reservation reservation, List<DetailReservation> detailReservations) {
        String checkAvailabilityQuery = "SELECT place_dispo FROM vol_details WHERE id_vol = ? AND id_type_siege = ?";
        String getVolQuery = "SELECT vol.id_vol , regle_vol.id_vol , date_depart, heure_max_reservation FROM vol JOIN regle_vol ON vol.id_vol=regle_vol.id_vol WHERE vol.id_vol = ?";
        String insertReservationQuery = "INSERT INTO reservation (id_vol , nombre_personnes, date_reservation, montant_total) VALUES (? , ?, ?, ?)";
        String insertDetailReservationQuery = "INSERT INTO detail_reservation (id_reservation, nom_complet, id_type_siege, prix) VALUES (?, ?, ?, ?)";

        try {
            connection.setAutoCommit(false);

            // Get vol details
            Timestamp dateDepart = null;
            int heureMaxReservation = 0;
            try (PreparedStatement volStmt = connection.prepareStatement(getVolQuery)) {
                volStmt.setInt(1, reservation.getIdVol());
                try (ResultSet rs = volStmt.executeQuery()) {
                    if (rs.next()) {
                        dateDepart = rs.getTimestamp("date_depart");
                        heureMaxReservation = rs.getInt("heure_max_reservation");
                    } else {
                        connection.rollback();
                        return false; // No such vol record found
                    }
                }
            }

            // Check reservation date
            Timestamp maxReservationDate = new Timestamp(dateDepart.getTime() - heureMaxReservation * 3600 * 1000);
            if (Timestamp.valueOf(reservation.getDateReservation()).after(maxReservationDate)) {
                connection.rollback();
                return false; // Reservation date is too late
            }

            // Check availability
            Map<Integer, Integer> seatCounts = new HashMap<>();
            for (DetailReservation detail : detailReservations) {
                int typeSiegeId = detail.getTypeSiege().getIdTypeSiege();
                seatCounts.put(typeSiegeId, seatCounts.getOrDefault(typeSiegeId, 0) + 1);
            }

            for (Map.Entry<Integer, Integer> entry : seatCounts.entrySet()) {
                int typeSiegeId = entry.getKey();
                int requiredSeats = entry.getValue();

                try (PreparedStatement checkStmt = connection.prepareStatement(checkAvailabilityQuery)) {
                    checkStmt.setInt(1, reservation.getIdVol());
                    checkStmt.setInt(2, typeSiegeId);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) {
                            int availableSeats = rs.getInt("place_dispo");
                            if (availableSeats < requiredSeats) {
                                connection.rollback();
                                return false; // Not enough seats available for this type of seat
                            }
                        } else {
                            connection.rollback();
                            return false; // No such vol_details record found
                        }
                    }
                }
            }

            // Insert reservation
            try (PreparedStatement reservationStmt = connection.prepareStatement(insertReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                reservationStmt.setInt(1, reservation.getIdVol());
                reservationStmt.setInt(2, reservation.getNombrePersonnes());
                reservationStmt.setTimestamp(3, Timestamp.valueOf(reservation.getDateReservation()));
                reservationStmt.setDouble(4, reservation.getMontantTotal());
                reservationStmt.executeUpdate();

                try (ResultSet generatedKeys = reservationStmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int reservationId = generatedKeys.getInt(1);
                        reservation.setIdReservation(reservationId);

                        // Insert detail reservations
                        try (PreparedStatement detailStmt = connection.prepareStatement(insertDetailReservationQuery)) {
                            for (DetailReservation detail : detailReservations) {
                                detailStmt.setInt(1, reservationId);
                                detailStmt.setString(2, detail.getNomComplet());
                                detailStmt.setInt(3, detail.getTypeSiege().getIdTypeSiege());
                                detailStmt.setDouble(4, detail.getPrix());
                                detailStmt.addBatch();
                            }
                            detailStmt.executeBatch();
                        }
                    } else {
                        connection.rollback();
                        return false; // Failed to insert reservation
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

    public static List<Reservation> getAllReservation(Connection connection) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.id_reservation, r.id_vol, r.nombre_personnes, r.date_reservation, r.montant_total, "
                     + "d.id_detail, d.nom_complet, d.id_type_siege, d.prix "
                     + "FROM reservation r "
                     + "LEFT JOIN detail_reservation d ON r.id_reservation = d.id_reservation";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int reservationId = resultSet.getInt("id_reservation");
                Reservation reservation = findReservationById(reservations, reservationId);

                if (reservation == null) {
                    reservation = new Reservation();
                    reservation.setIdReservation(reservationId);
                    reservation.setIdVol(resultSet.getInt("id_vol"));
                    reservation.setNombrePersonnes(resultSet.getInt("nombre_personnes"));
                    reservation.setDateReservation(resultSet.getTimestamp("date_reservation").toLocalDateTime());
                    reservation.setMontantTotal(resultSet.getDouble("montant_total"));
                    reservation.setDetailReservations(new ArrayList<>());
                    reservations.add(reservation);
                }

                DetailReservation detail = new DetailReservation();
                detail.setIdDetail(resultSet.getInt("id_detail"));
                detail.setNomComplet(resultSet.getString("nom_complet"));
                detail.setTypeSiege(TypeSiegeService.getTypeSiegeById(connection, resultSet.getInt("id_type_siege")));
                detail.setPrix(resultSet.getDouble("prix"));
                detail.setReservation(reservation);

                reservation.getDetailReservations().add(detail);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    private static Reservation findReservationById(List<Reservation> reservations, int idReservation) {
        for (Reservation reservation : reservations) {
            if (reservation.getIdReservation() == idReservation) {
                return reservation;
            }
        }
        return null;
    }

    public static Reservation getReservationById(Connection connection, int idReservation) {
        String query = "SELECT r.id_reservation, r.id_vol, r.nombre_personnes, r.date_reservation, r.montant_total, "
                     + "d.id_detail, d.nom_complet, d.id_type_siege, d.prix "
                     + "FROM reservation r "
                     + "LEFT JOIN detail_reservation d ON r.id_reservation = d.id_reservation "
                     + "WHERE r.id_reservation = ?";
        Reservation reservation = null;

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, idReservation);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    if (reservation == null) {
                        reservation = new Reservation();
                        reservation.setIdReservation(resultSet.getInt("id_reservation"));
                        reservation.setIdVol(resultSet.getInt("id_vol"));
                        reservation.setNombrePersonnes(resultSet.getInt("nombre_personnes"));
                        reservation.setDateReservation(resultSet.getTimestamp("date_reservation").toLocalDateTime());
                        reservation.setMontantTotal(resultSet.getDouble("montant_total"));
                        reservation.setDetailReservations(new ArrayList<>());
                    }

                    DetailReservation detail = new DetailReservation();
                    detail.setIdDetail(resultSet.getInt("id_detail"));
                    detail.setNomComplet(resultSet.getString("nom_complet"));
                    detail.setTypeSiege(TypeSiegeService.getTypeSiegeById(connection, resultSet.getInt("id_type_siege")));
                    detail.setPrix(resultSet.getDouble("prix"));
                    detail.setReservation(reservation);

                    reservation.getDetailReservations().add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservation;
    }

    public static void cancelReservationDetails(Connection connection, int idReservation, List<Integer> detailsToCancel) throws SQLException {
        String deleteDetailQuery = "DELETE FROM detail_reservation WHERE id_detail = ?";
        String checkRemainingDetailsQuery = "SELECT COUNT(*) FROM detail_reservation WHERE id_reservation = ?";
        String deleteReservationQuery = "DELETE FROM reservation WHERE id_reservation = ?";

        try {
            connection.setAutoCommit(false);

            // Delete the specified detail reservations
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteDetailQuery)) {
                for (int idDetail : detailsToCancel) {
                    preparedStatement.setInt(1, idDetail);
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }

            // Check if there are any remaining detail reservations for the given reservation
            boolean hasRemainingDetails = false;
            try (PreparedStatement checkStmt = connection.prepareStatement(checkRemainingDetailsQuery)) {
                checkStmt.setInt(1, idReservation);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next()) {
                        hasRemainingDetails = rs.getInt(1) > 0;
                    }
                }
            }

            // If no remaining detail reservations, delete the reservation
            if (!hasRemainingDetails) {
                try (PreparedStatement deleteStmt = connection.prepareStatement(deleteReservationQuery)) {
                    deleteStmt.setInt(1, idReservation);
                    deleteStmt.executeUpdate();
                }
            }

            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException autoCommitEx) {
                autoCommitEx.printStackTrace();
            }
        }
    }
}