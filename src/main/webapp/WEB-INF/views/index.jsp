<%@ page import="java.util.List" %>
<%@ page import="com.example.gestion_rh.model.Annonce, com.example.gestion_rh.model.Candidat" %>
<%
    List<Annonce> annonces = (List<Annonce>) request.getAttribute("annonces");
    List<Candidat> candidats = (List<Candidat>) request.getAttribute("candidats");
    String selectedAnnonce = request.getParameter("annonceId");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des candidats</title>
</head>
<body>
    <h1>Liste des candidats filtrée par annonce</h1>

    <form method="get" action="index.jsp">
        <label for="annonceId">Filtrer par annonce :</label>
        <select name="annonceId" id="annonceId">
            <option value="">-- Toutes les annonces --</option>
            <% if (annonces != null) {
                for (Annonce a : annonces) { %>
                    <option value="<%= a.getId() %>" <%= (selectedAnnonce != null && selectedAnnonce.equals(String.valueOf(a.getId()))) ? "selected" : "" %>><%= a.getProfil() %></option>
            <%  }
            } %>
        </select>
        <button type="submit">Filtrer</button>
    </form>

    <hr/>

    <h2>Résultats</h2>
    <% if (candidats == null || candidats.isEmpty()) { %>
        <p>Aucun candidat disponible pour ce filtre.</p>
    <% } else { %>
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
            <% for (Candidat c : candidats) { %>
                <tr>
                    <td><%= c.getNom() %></td>
                    <td><%= c.getPrenom() %></td>
                    <td><%= c.getEmail() %></td>
                    <td><%= c.getAnnonce() != null ? c.getAnnonce().getProfil() : "" %></td>
                    <td>
                        <a href="contratEssaiForm.jsp?candidateId=<%= c.getId() %>&annonceId=<%= c.getAnnonce() != null ? c.getAnnonce().getId() : "" %>">
                            Proposer Contrat d'essai
                        </a>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
    <% } %>
</body>
</html>