<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- Lógica para detectar se é a página inicial --%>
<c:set var="currentURI" value="${pageContext.request.requestURI}" />
<c:set var="isHome" value="${fn:endsWith(currentURI, '/index.jsp') or fn:endsWith(currentURI, '/') or currentURI == pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Tartaruga Cometa</title>

    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700;800;900&display=swap">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    
    <%-- CSS Global do Sistema --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/main.css">

    <%-- Carrega o CSS específico da Home apenas se for a Home --%>
    <c:if test="${isHome}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pages/home.css">
    </c:if>
</head>
<body>
    <nav class="navbar">
        <div class="navbar-container">
            <a href="${pageContext.request.contextPath}/" class="navbar-brand">
                <img
                    id="navbar-logo-img"
                    src="${pageContext.request.contextPath}/assets/logolight.png"
                    alt="Tartaruga Cometa Logo"
                    class="navbar-logo-img"
                >
                <span id="navbar-title" class="navbar-title">Tartaruga Cometa</span>
            </a>
            <div class="navbar-actions">
            </div>
        </div>
    </nav>

    <%-- 
        CORREÇÃO AQUI: 
        Se for Home, usamos 'home-wrapper-full' para ocupar a tela toda.
        Se for outra página, usamos 'container' para manter o layout encaixotado do sistema.
    --%>
    <div class="${isHome ? 'home-wrapper-full' : 'container'}">
        <jsp:doBody/>
    </div>

    <script src="${pageContext.request.contextPath}/js/masks.js"></script>
    <script src="${pageContext.request.contextPath}/js/validation.js"></script>
</body>
</html>