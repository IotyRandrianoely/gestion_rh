<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Questions d'Entretien - QCM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .question-card {
            background-color: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            margin-bottom: 20px;
            padding: 20px;
        }
        .question-number {
            color: #6c757d;
            font-size: 0.9rem;
            margin-bottom: 10px;
        }
        .question-text {
            font-size: 1.1rem;
            margin-bottom: 15px;
        }
        .answer-section {
            background-color: #f8f9fa;
            border-radius: 5px;
            padding: 15px;
            margin-top: 10px;
        }
        .options-link {
            color: #0d6efd;
            text-decoration: none;
        }
        .options-link:hover {
            text-decoration: underline;
        }
        .header-section {
            background-color: #f8f9fa;
            padding: 20px 0;
            margin-bottom: 30px;
        }
    </style>
</head>
<body>
    <div class="header-section">
        <div class="container">
            <h1 class="mb-3">Questions d'Entretien</h1>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb">
                    <li class="breadcrumb-item"><a href="<c:url value='/qcm/questions'/>">Questions</a></li>
                    <li class="breadcrumb-item active">Entretien Filière ${filiereId}</li>
                </ol>
            </nav>
        </div>
    </div>

    <div class="container">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <c:choose>
                    <c:when test="${not empty questions}">
                        <c:forEach items="${questions}" var="question" varStatus="status">
                            <div class="question-card">
                                <div class="question-number">Question ${status.count}/7</div>
                                <div class="question-text">${question.text}</div>
                                <div class="d-flex justify-content-between align-items-center">
                                    <a href="<c:url value='/qcm/question/${question.id}/correct-options'/>" 
                                       class="options-link">
                                        Voir les réponses correctes →
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <div class="alert alert-info">
                            Aucune question n'est disponible pour cette filière.
                        </div>
                    </c:otherwise>
                </c:choose>

                <div class="mt-4">
                    <a href="<c:url value='/qcm/questions'/>" class="btn btn-primary">
                        ← Retour à la liste complète
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>