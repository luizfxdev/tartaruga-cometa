<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:header title="Erro">
    <div class="error-container">
        <h2>Oops! Algo deu errado</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <% if (request.getAttribute("javax.servlet.error.message") != null) { %>
            <div class="alert alert-danger">
                <%= request.getAttribute("javax.servlet.error.message") %>
            </div>
        <% } %>

        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar ao Home</a>
            <a href="javascript:history.back()" class="btn btn-secondary">Voltar</a>
        </div>
    </div>
</t:header>

<t:footer />
