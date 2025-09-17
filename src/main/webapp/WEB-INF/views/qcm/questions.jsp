<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Liste des Questions - QCM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .question-card {
            transition: transform 0.2s;
            margin-bottom: 20px;
        }
        .question-card:hover {
            transform: translateY(-2px);
        }
        .options-link {
            text-decoration: none;
        }
        .options-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-10">
                <h1 class="mb-4">Questions du QCM</h1>
                
                <c:choose>
                    <c:when test="${not empty questions}">
                        <div class="row">
                            <c:forEach items="${questions}" var="question">
                                <div class="col-md-6">
                                    <div class="card question-card">
                                        <div class="card-body">
                                            <h5 class="card-title">Question ${question.id}</h5>
                                            <p class="card-text">${question.text}</p>
                                            <a href="<c:url value='/qcm/question/${question.id}/correct-options'/>" 
                                               class="options-link text-primary">
                                                Voir les réponses correctes →
                                            </a>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            Aucune question n'est disponible pour le moment.
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>