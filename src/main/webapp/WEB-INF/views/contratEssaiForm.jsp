<%@ page import="java.util.List" %>
<%@ page import="com.example.gestion_rh.Model.Poste" %>
<%
    String candidateId = request.getParameter("candidateId");
    String annonceId = request.getParameter("annonceId");
    List<Poste> postes = (List<Poste>) request.getAttribute("postes");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire Contrat d'essai</title>
</head>
<body>
    <h1>Proposer un contrat d'essai</h1>

    <p>Candidat ID: <strong><%= candidateId != null ? candidateId : "-" %></strong></p>
    <p>Annonce ID: <strong><%= annonceId != null ? annonceId : "-" %></strong></p>

    <form id="contratForm" onsubmit="return submitForm(event);">
        <input type="hidden" id="candidateId" value="<%= candidateId %>"/>
        <label for="posteId">Poste :</label>
        <select id="posteId" required>
            <option value="">-- sélectionner un poste --</option>
            <% if (postes != null) {
                for (Poste p : postes) { %>
                    <option value="<%= p.getId() %>"><%= p.getNomPoste() %></option>
            <%  }
            } %>
        </select>
        <br/><br/>

        <label for="dateDebut">Date de début :</label>
        <input type="date" id="dateDebut" required />
        <br/><br/>

        <label for="duree">Durée (jours) :</label>
        <input type="number" id="duree" min="1" required />
        <br/><br/>

        <label for="salaire">Salaire :</label>
        <input type="number" step="0.01" id="salaire" required />
        <br/><br/>

        <label for="conditions">Conditions :</label><br/>
        <textarea id="conditions" rows="5" cols="50"></textarea>
        <br/><br/>

        <button type="submit">Envoyer la proposition</button>
        <a href="index.jsp">Annuler</a>
    </form>

    <script>
        async function submitForm(e) {
            e.preventDefault();
            const candidateId = document.getElementById('candidateId').value;
            const posteId = document.getElementById('posteId').value;
            const dateDebut = document.getElementById('dateDebut').value;
            const duree = parseInt(document.getElementById('duree').value, 10);
            const salaire = parseFloat(document.getElementById('salaire').value);
            const conditions = document.getElementById('conditions').value;

            if (!posteId || !dateDebut || !duree || isNaN(salaire)) {
                alert('Veuillez remplir tous les champs requis.');
                return false;
            }

            const payload = {
                candidat: { id: candidateId ? Number(candidateId) : null },
                poste: { id: Number(posteId) },
                dateDebut: dateDebut,
                duree: duree,
                salaire: salaire,
                conditions: conditions
            };

            try {
                const resp = await fetch('/api/contrats-essai', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(payload)
                });
                if (!resp.ok) {
                    const txt = await resp.text();
                    throw new Error('Erreur serveur: ' + txt);
                }
                alert('Proposition envoyée.');
                window.location.href = 'index.jsp';
            } catch (err) {
                alert('Erreur: ' + err.message);
            }

            return false;
        }
    </script>
</body>
</html>