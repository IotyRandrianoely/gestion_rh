<%@ page import="java.util.List" %>
<%@ page import="com.example.gestion_rh.Model.Candidat" %>
<%
    List<Candidat> candidats = (List<Candidat>) request.getAttribute("candidats");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des candidats</title>
</head>
<body>
    <h1>Liste des candidats</h1>
    <%
        if (candidats == null || candidats.isEmpty()) {
    %>
        <p>Aucun candidat trouvé.</p>
    <%
        } else {
    %>
    <table border="1" cellpadding="6" cellspacing="0">
        <thead>
            <tr>
                <th>Nom</th>
                <th>Prénom</th>
                <th>Email</th>
                <th>Annonce</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
        <%
            for (Candidat c : candidats) {
        %>
            <tr>
                <td><%= c.getNom() %></td>
                <td><%= c.getPrenom() %></td>
                <td><%= c.getEmail() %></td>
                <td><%= c.getAnnonce() != null ? c.getAnnonce().getProfil() : "" %></td>
                <td>
                    <a href="detail/<%= c.getId() %>">Voir détail</a>
                </td>
            </tr>
        <%
            }
        %>
        </tbody>
    </table>
    <%
        }
    %>
</body>
</html>