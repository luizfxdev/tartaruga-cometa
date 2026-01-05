<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ attribute name="title" required="true" type="java.lang.String"%>

<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${title} - Tartaruga Cometa</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <nav class="navbar">
        <div class="container">
            <div class="navbar-brand">
                <a href="${pageContext.request.contextPath}/">
                    <h1>🐢 Tartaruga Cometa</h1>
                </a>
            </div>
            <ul class="navbar-menu">
                <li><a href="${pageContext.request.contextPath}/clients/">Clientes</a></li>
                <li><a href="${pageContext.request.contextPath}/products/">Produtos</a></li>
                <li><a href="${pageContext.request.contextPath}/addresses/">Endereços</a></li>
                <li><a href="${pageContext.request.contextPath}/deliveries/">Entregas</a></li>
            </ul>
        </div>
    </nav>

    <div class="container">
        <% if (request.getSession().getAttribute("success") != null) { %>
            <div class="alert alert-success">
                <%= request.getSession().getAttribute("success") %>
                <% request.getSession().removeAttribute("success"); %>
            </div>
        <% } %>

        <% if (request.getSession().getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getSession().getAttribute("error") %>
                <% request.getSession().removeAttribute("error"); %>
            </div>
        <% } %>

        <!-- ESTA LINHA ESTAVA FALTANDO! -->
        <jsp:doBody/>
    </div>
