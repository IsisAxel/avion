<%@page import="java.util.List" %>
<%@page import="app.model.Avion"%>
<%@page import="app.model.TypeSiege"%>
<%@page import="app.model.Ville"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Ajouter Vol</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            margin-bottom: 20px;
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
        <h1>Ajouter Vol</h1>
        <form action="<%= request.getContextPath()%>/admin/vol/insert" method="post">
            <div class="form-group">
                <label for="avion">Avion</label>
                <select id="avion" name="vol.avion.idAvion">
                    <!-- Remplir avec les avions disponibles -->
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
                    <!-- Remplir avec les villes disponibles -->
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
                    <!-- Remplir avec les villes disponibles -->
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
                <input type="datetime-local" id="dateDepart" name="vol.dateDepart" required>
            </div>
            <div class="form-group">
                <label for="dateArrive">Date Arrivee</label>
                <input type="datetime-local" id="dateArrive" name="vol.dateArrive" required>
            </div>
            <div class="form-group">
                <label>Types de Sieges</label>
                <%
                    List<TypeSiege> typesSiege = (List<TypeSiege>) request.getAttribute("type_sieges");
                    int i = 0;
                    for (TypeSiege typeSiege : typesSiege) {
                %>
                <div class="form-group">
                    <label for="typeSiege_<%= typeSiege.getIdTypeSiege() %>"><%= typeSiege.getType() %></label>
                    <input type="hidden" name="volDetails[<%= i %>].typeSiege.idTypeSiege" value="<%= typeSiege.getIdTypeSiege() %>">
                    <label for="prix">Prix</label>
                    <input type="number" step="0.01" min=0.01 id="prix" name="volDetails[<%= i %>].prix" required>
                    <label for="placeDispo">Places Disponibles</label>
                    <input type="number" id="placeDispo" min=0 name="volDetails[<%= i %>].placeDispo" required>
                </div>
                <%
                    i++;
                    }
                %>
            </div>
            <div class="form-group">
                <label for="heureMaxReservation">Heure Max de Reservation</label>
                <input type="number" id="heureMaxReservation" name="vol.regleVol.heureMaxReservation" required>
            </div>
            <div class="form-group">
                <label for="heureMaxAnnulation">Heure Max d'Annulation</label>
                <input type="number" id="heureMaxAnnulation" name="vol.regleVol.heureMaxAnnulation" required>
            </div>
            <div style="text-align: center;">
                <button type="submit" class="button">Ajouter</button>
            </div>
        </form>
    </div>
</body>
</html>