<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.HashMap" %>
<%@page import="app.model.Reservation" %>
<%@page import="app.model.DetailReservation" %>
<%@page import="app.model.TypeSiege" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Reservations</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #007BFF;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
        }
        h1 {
            text-align: center;
            margin-bottom: 20px;
        }
        .button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            font-weight: bold;
            text-align: center;
            text-decoration: none;
            color: #fff;
            background-color: #007BFF;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }
        .button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Liste des Reservations</h1>
        <table>
            <thead>
                <tr>
                    <th>ID Reservation</th>
                    <th>ID Vol</th>
                    <th>Nombre de Personnes</th>
                    <th>Date de Reservation</th>
                    <th>Montant Total</th>
                    <th>Details</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Reservation> reservations = (List<Reservation>) request.getAttribute("reservations");
                    if (reservations != null) {
                        for (Reservation reservation : reservations) {
                %>
                <tr>
                    <td><%= reservation.getIdReservation() %></td>
                    <td><%= reservation.getIdVol() %></td>
                    <td><%= reservation.getNombrePersonnes() %></td>
                    <td><%= reservation.getDateReservation() %></td>
                    <td><%= reservation.getMontantTotal() %></td>
                    <td>
                        <table>
                            <thead>
                                <tr>
                                    <th>Type de Siege</th>
                                    <th>Nombre de Reservations</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    Map<String, Integer> seatCounts = new HashMap<>();
                                    List<DetailReservation> detailReservations = reservation.getDetailReservations();
                                    for (DetailReservation detail : detailReservations) {
                                        String typeSiege = detail.getTypeSiege().getType();
                                        seatCounts.put(typeSiege, seatCounts.getOrDefault(typeSiege, 0) + 1);
                                    }
                                    for (Map.Entry<String, Integer> entry : seatCounts.entrySet()) {
                                %>
                                <tr>
                                    <td><%= entry.getKey() %></td>
                                    <td><%= entry.getValue() %></td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </td>
                    <td>
                        <a href="<%= request.getContextPath()%>/client/reservation/prepareCancel?idReservation=<%= reservation.getIdReservation() %>" class="button">Annuler</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</body>
</html>