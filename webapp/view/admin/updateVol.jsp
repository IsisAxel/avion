<%@page import="java.util.List" %>
<%@page import="app.model.Avion"%>
<%@page import="app.model.TypeSiege"%>
<%@page import="app.model.Ville"%>
<%@page import="app.model.Vol"%>
<%@page import="app.model.VolDetails"%>
<%@page import="app.model.RegleVol"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mettre à jour Vol</title>
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
        <h1>Mettre à jour Vol</h1>
        <form action="<%= request.getContextPath()%>/admin/vol/update" method="post">
            <input type="hidden" name="vol.idVol" value="<%= ((Vol) request.getAttribute("vol")).getIdVol() %>">
            <div class="form-group">
                <label for="avion">Avion</label>
                <select id="avion" name="vol.avion.idAvion">
                    <!-- Remplir avec les avions disponibles -->
                    <%
                        List<Avion> avions = (List<Avion>) request.getAttribute("avions");
                        Avion selectedAvion = ((Vol) request.getAttribute("vol")).getAvion();
                        for (Avion avion : avions) {
                    %>
                    <option value="<%= avion.getIdAvion() %>" <%= avion.getIdAvion() == selectedAvion.getIdAvion() ? "selected" : "" %>><%= avion.getCompagnie() %> - <%= avion.getModele() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="villeDepart">Ville Départ</label>
                <select id="villeDepart" name="vol.villeDepart.idVille">
                    <!-- Remplir avec les villes disponibles -->
                    <%
                        List<Ville> villes = (List<Ville>) request.getAttribute("villes");
                        Ville selectedVilleDepart = ((Vol) request.getAttribute("vol")).getVilleDepart();
                        for (Ville ville : villes) {
                    %>
                    <option value="<%= ville.getIdVille() %>" <%= ville.getIdVille() == selectedVilleDepart.getIdVille() ? "selected" : "" %>><%= ville.getNom() %></option>
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
                        Ville selectedVilleDestination = ((Vol) request.getAttribute("vol")).getVilleDestination();
                        for (Ville ville : villes) {
                    %>
                    <option value="<%= ville.getIdVille() %>" <%= ville.getIdVille() == selectedVilleDestination.getIdVille() ? "selected" : "" %>><%= ville.getNom() %></option>
                    <%
                        }
                    %>
                </select>
            </div>
            <div class="form-group">
                <label for="dateDepart">Date Départ</label>
                <input type="datetime-local" id="dateDepart" name="vol.dateDepart" value="<%= ((Vol) request.getAttribute("vol")).getDateDepart().toString().replace(" ", "T") %>" required>
            </div>
            <div class="form-group">
                <label for="dateArrive">Date Arrivée</label>
                <input type="datetime-local" id="dateArrive" name="vol.dateArrive" value="<%= ((Vol) request.getAttribute("vol")).getDateArrive().toString().replace(" ", "T") %>" required>
            </div>
            <div class="form-group">
                <label>Types de Sièges</label>
                <%
                    List<TypeSiege> typesSiege = (List<TypeSiege>) request.getAttribute("type_sieges");
                    List<VolDetails> volDetailsList = ((Vol) request.getAttribute("vol")).getVolDetails();
                    int i = 0;
                    for (TypeSiege typeSiege : typesSiege) {
                        VolDetails volDetails = volDetailsList.stream().filter(vd -> vd.getTypeSiege().getIdTypeSiege() == typeSiege.getIdTypeSiege()).findFirst().orElse(null);
                %>
                <div class="form-group">
                    <label for="typeSiege_<%= typeSiege.getIdTypeSiege() %>"><%= typeSiege.getType() %></label>
                    <input type="hidden" name="vol.volDetails[<%= i %>].typeSiege.idTypeSiege" value="<%= typeSiege.getIdTypeSiege() %>">
                    <label for="prix_<%= typeSiege.getIdTypeSiege() %>">Prix</label>
                    <input type="number" step="0.01" min="0.01" id="prix_<%= typeSiege.getIdTypeSiege() %>" name="vol.volDetails[<%= i %>].prix" value="<%= volDetails != null ? volDetails.getPrix() : "" %>" required>
                    <label for="placeDispo_<%= typeSiege.getIdTypeSiege() %>">Places Disponibles</label>
                    <input type="number" min="0" id="placeDispo_<%= typeSiege.getIdTypeSiege() %>" name="vol.volDetails[<%= i %>].placeDispo" value="<%= volDetails != null ? volDetails.getPlaceDispo() : "" %>" required>
                </div>
                <%
                    i++;
                    }
                %>
            </div>
            <div class="form-group">
                <label for="heureMaxReservation">Heure Max de Réservation</label>
                <input type="number" id="heureMaxReservation" name="vol.regleVol.heureMaxReservation" value="<%= ((Vol) request.getAttribute("vol")).getRegleVol().getHeureMaxReservation() %>" required>
            </div>
            <div class="form-group">
                <label for="heureMaxAnnulation">Heure Max d'Annulation</label>
                <input type="number" id="heureMaxAnnulation" name="vol.regleVol.heureMaxAnnulation" value="<%= ((Vol) request.getAttribute("vol")).getRegleVol().getHeureMaxAnnulation() %>" required>
            </div>
            <div style="text-align: center;">
                <button type="submit" class="button">Mettre à jour</button>
            </div>
        </form>
    </div>
</body>
</html>