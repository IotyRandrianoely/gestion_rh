<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Options Correctes - QCM</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .options-list {
            list-style: none;
            padding: 0;
        }
        .option-item {
            background-color: #e8f5e9;
            padding: 15px;
            margin: 10px 0;
            border-radius: 5px;
            border-left: 4px solid #4caf50;
        }
        .question-card {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <div class="container py-5">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <nav aria-label="breadcrumb" class="mb-4">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="<c:url value='/qcm/questions'/>">Questions</a></li>
                        <li class="breadcrumb-item active">Options Correctes</li>
                    </ol>
                </nav>

                <h1 class="mb-4">Options Correctes</h1>
                
                <c:if test="${question != null}">
                    <div class="question-card">
                        <h5 class="card-title">Question :</h5>
                        <p class="card-text">${question.text}</p>
                    </div>
                </c:if>

                <div class="card">
                    <div class="card-body">
                        <h5 class="card-title mb-4">Réponses correctes :</h5>
                        <c:choose>
                            <c:when test="${not empty correctOptions}">
                                <ul class="options-list">
                                    <c:forEach items="${correctOptions}" var="option">
                                        <li class="option-item">
                                            ${option.text}
                                        </li>
                                    </c:forEach>
                                </ul>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">Aucune option correcte trouvée pour cette question.</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>

                <div class="mt-4">
                    <a href="<c:url value='/qcm/questions'/>" class="btn btn-primary">
                        ← Retour à la liste des questions
                    </a>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>