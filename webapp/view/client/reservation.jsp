<%@page import="java.util.List" %>
<%@page import="app.model.Vol" %>
<%@page import="app.model.VolDetails" %>
<%@page import="app.model.TypeSiege" %>
<%@page import="app.model.Ville" %>
<%@page import="app.model.Avion" %>
<%@page import="app.model.Reservation" %>
<%@page import="app.model.DetailReservation" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reserver un Vol</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 20px;
        }
        .container {
            max-width: 800px;
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
        <h1>Reserver un Vol</h1>
        <form action="<%= request.getContextPath()%>/client/vol/reserve" method="post">
            <input type="hidden" name="reservation.idVol" value="<%= ((Vol) request.getAttribute("vol")).getIdVol() %>">
            <%-- <div class="form-group">
                <label for="nombrePersonnes">Nombre de Personnes</label>
                <input type="number" id="nombrePersonnes" name="reservation.nombrePersonnes" required>
            </div> --%>
            <div class="form-group">
                <label for="dateReservation">Date de Reservation</label>
                <input type="datetime-local" id="dateReservation" name="reservation.dateReservation" required>
            </div>
            <div class="form-group">
                <label>Details des Passagers</label>
                <div id="passengerDetails">
                    <div class="form-group">
                        <label for="nomComplet_0">Nom Complet</label>
                        <input type="text" id="nomComplet_0" name="detailReservations[0].nomComplet" required>
                        <label for="typeSiege_0">Type de Siege</label>
                        <select id="typeSiege_0" name="detailReservations[0].typeSiege.idTypeSiege" required>
                            <%
                                List<TypeSiege> typesSiege = (List<TypeSiege>) request.getAttribute("type_sieges");
                                for (TypeSiege typeSiege : typesSiege) {
                            %>
                            <option value="<%= typeSiege.getIdTypeSiege() %>"><%= typeSiege.getType() + " - " + ((Vol) request.getAttribute("vol")).getVolDetailsByIdTypeSiege(typeSiege.getIdTypeSiege()).getPrix() %></option>
                            <%
                                }
                            %>
                        </select>
                    </div>
                </div>
                <button type="button" class="button" onclick="addPassenger()">Ajouter un Passager</button>
            </div>
            <div style="text-align: center;">
                <button type="submit" class="button">Reserver</button>
            </div>
        </form>
    </div>
    <script>
        let passengerCount = 1;

        function addPassenger() {
            const passengerDetails = document.getElementById('passengerDetails');
            const newPassenger = document.createElement('div');
            newPassenger.classList.add('form-group');
            newPassenger.innerHTML = `
                <label for="nomComplet_${passengerCount}">Nom Complet</label>
                <input type="text" id="nomComplet_${passengerCount}" name="detailReservations[${passengerCount}].nomComplet" required>
                <label for="typeSiege_${passengerCount}">Type de Siege</label>
                <select id="typeSiege_${passengerCount}" name="detailReservations[${passengerCount}].typeSiege.idTypeSiege" required>
                    <% for (TypeSiege typeSiege : typesSiege) { %>
                    <option value="<%= typeSiege.getIdTypeSiege() %>"><%= typeSiege.getType() %></option>
                    <% } %>
                </select>
                <label for="prix_${passengerCount}">Prix</label>
                <input type="number" step="0.01" id="prix_${passengerCount}" name="detailReservations[${passengerCount}].prix" required>
            `;
            passengerDetails.appendChild(newPassenger);
            passengerCount++;
        }
    </script>
</body>
</html>