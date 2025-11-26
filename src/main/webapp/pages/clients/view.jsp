<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>

<t:header title="Detalhes do Cliente">
    <%
        Client client = (Client) request.getAttribute("client");
    %>

    <div class="page-header">
        <h2>Detalhes do Cliente</h2>
    </div>

    <div class="details-card">
        <div class="detail-row">
            <label>ID:</label>
            <span><%= client.getId() %></span>
        </div>

        <div class="detail-row">
            <label>Tipo de Pessoa:</label>
            <span><%= client.getPersonType().getLabel() %></span>
        </div>

        <div class="detail-row">
            <label>Documento:</label>
            <span><%= client.getDocument() %></span>
        </div>

        <div class="detail-row">
            <label>Nome:</label>
            <span><%= client.getName() %></span>
        </div>

        <div class="detail-row">
            <label>Email:</label>
            <span><%= client.getEmail() != null ? client.getEmail() : "-" %></span>
        </div>

        <div class="detail-row">
            <label>Telefone:</label>
            <span><%= client.getPhone() != null ? client.getPhone() : "-" %></span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><%= client.getCreatedAt() %></span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/clients/edit/<%= client.getId() %>" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/clients/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
