<%@page import="java.util.List" %>
<%@page import="app.model.Reservation" %>
<%@page import="app.model.DetailReservation" %>
<%@page import="app.model.TypeSiege" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Annuler Reservation</title>
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
        <script>
        function updateCheckboxNames() {
            const checkboxes = document.querySelectorAll('input[type="checkbox"]');
            let index = 0;
            checkboxes.forEach(checkbox => {
                if (checkbox.checked) {
                    checkbox.name = "detailsToCancel["+ index +"]";
                    index++;
                } else {
                    checkbox.name = "detailsToCancel";
                }
            });
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Annuler Reservation</h1>
        <form action="<%= request.getContextPath()%>/client/reservation/cancel" method="post">
            <input type="hidden" name="idReservation" value="<%= ((Reservation) request.getAttribute("reservation")).getIdReservation() %>">
            <div class="form-group">
                <label>Details des Passagers</label>
                <table>
                    <thead>
                        <tr>
                            <th>Nom Complet</th>
                            <th>Type de Siege</th>
                            <th>Prix</th>
                            <th>Annuler</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                        int i = 0;
                            List<DetailReservation> detailReservations = ((Reservation) request.getAttribute("reservation")).getDetailReservations();
                            for (DetailReservation detail : detailReservations) {
                        %>
                        <tr>
                            <td><%= detail.getNomComplet() %></td>
                            <td><%= detail.getTypeSiege().getType() %></td>
                            <td><%= detail.getPrix() %></td>
                            <td>
                                <input type="checkbox" name="detailsToCancel" value="<%= detail.getIdDetail() %>" onclick="updateCheckboxNames()">
                            </td>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>
                </table>
            </div>
            <div style="text-align: center;">
                <button type="submit" class="button">Annuler Selection</button>
            </div>
        </form>
    </div>
</body>
</html>