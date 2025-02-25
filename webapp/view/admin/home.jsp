<%@page import="java.util.List" %>
<%@page import="app.model.Vol"%>
<%@page import="app.model.VolDetails"%>
<%@page import="app.model.TypeSiege"%>
<%@page import="app.model.Ville"%>
<%@page import="app.model.Avion"%>
<%
    String oldDate = "2000-01-01T00:00";
    String futureDate = "2050-01-01T00:00";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Liste des Vols</title>
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
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input,
        .form-group select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Liste des Vols</h1>

        <form action="<%= request.getContextPath()%>/admin/vol/search" method="post">
            <div class="form-group">
                <label for="avion">Avion</label>
                <select id="avion" name="vol.avion.idAvion">
                    <option value="0">Tous</option>
                    <%
                        List<Avion> avions = (List<Avion>) request.getAttribute("avions");
                        for (Avion avion : avions) {
                    %>
                    <option value="<%= avion.getIdAvion() %>"><%= avion.getCompagnie() %> - <%= avion.getModele() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="villeDepart">Ville Depart</label>
                <select id="villeDepart" name="vol.villeDepart.idVille">
                    <option value="0">Toutes</option>
                    <%
                        List<Ville> villes = (List<Ville>) request.getAttribute("villes");
                        for (Ville ville : villes) {
                    %>
                    <option value="<%= ville.getIdVille() %>"><%= ville.getNom() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="villeDestination">Ville Destination</label>
                <select id="villeDestination" name="vol.villeDestination.idVille">
                    <option value="0">Toutes</option>
                    <%
                        for (Ville ville : villes) {
                    %>
                    <option value="<%= ville.getIdVille() %>"><%= ville.getNom() %></option>
                    <%
                        }
                    %>
                </select>
            </div>

            <div class="form-group">
                <label for="dateDepart">Date Depart</label>
                <input type="datetime-local" id="dateDepart" name="vol.dateDepart" value="<%= oldDate %>">
            </div>
            <div class="form-group">
                <label for="dateArrive">Date Arrivee</label>
                <input type="datetime-local" id="dateArrive" name="vol.dateArrive" value="<%= futureDate %>">
            </div>

            <div style="text-align: center;">
                <button type="submit" class="button">Rechercher</button>
            </div>
        </form>


        <table>
            <thead>
                <tr>
                    <th>Vol</th>
                    <th>Avion</th>
                    <th>Ville Depart</th>
                    <th>Ville Destination</th>
                    <th>Date Depart</th>
                    <th>Date Arrivee</th>
                    <th>Heure de Reservation max</th>
                    <th>Heure Annulation Max</th>
                    <th>Details</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Vol> vols = (List<Vol>) request.getAttribute("vols");
                    if (vols != null) {
                        for (Vol vol : vols) {
                %>
                <tr>
                    <td><%= vol.getIdVol() %></td>
                    <td><%= vol.getAvion().getCompagnie() + "-" + vol.getAvion().getModele() %></td>
                    <td><%= vol.getVilleDepart().getNom() %></td>
                    <td><%= vol.getVilleDestination().getNom() %></td>
                    <td><%= vol.getDateDepart() %></td>
                    <td><%= vol.getDateArrive() %></td>
                    <td><%= vol.getRegleVol().getHeureMaxReservation() %></td>
                    <td><%= vol.getRegleVol().getHeureMaxAnnulation() %></td>
                    <td>
                        <table>
                            <thead>
                                <tr>
                                    <th>ID Type Siege</th>
                                    <th>Prix</th>
                                    <th>Places Disponibles</th>
                                </tr>
                            </thead>
                            <tbody>
                                <%
                                    List<VolDetails> volDetailsList = vol.getVolDetails();
                                    for (VolDetails details : volDetailsList) {
                                %>
                                <tr>
                                    <td><%= details.getTypeSiege().getIdTypeSiege() %></td>
                                    <td><%= details.getPrix() %></td>
                                    <td><%= details.getPlaceDispo() %></td>
                                </tr>
                                <%
                                    }
                                %>
                            </tbody>
                        </table>
                    </td>
                    <td>
                        <a href="<%= request.getContextPath()%>/admin/vol/prepareUpdate?idVol=<%= vol.getIdVol() %>" class="button">Modifier</a>
                        <a href="<%= request.getContextPath()%>/admin/vol/delete?idVol=<%= vol.getIdVol() %>" class="button" style="background-color: #dc3545;">Supprimer</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
        <div style="text-align: center;">
            <a href="<%= request.getContextPath()%>/admin/vol/prepareInsert" class="button">Ajouter Vol</a>
        </div>
    </div>
</body>
</html>