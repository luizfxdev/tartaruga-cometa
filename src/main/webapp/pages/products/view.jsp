<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.tartarugacometasystem.model.Product" %>

<t:header title="Detalhes do Produto">
    <%
        Product product = (Product) request.getAttribute("product");
    %>

    <div class="page-header">
        <h2>Detalhes do Produto</h2>
    </div>

    <div class="details-card">
        <div class="detail-row">
            <label>ID:</label>
            <span><%= product.getId() %></span>
        </div>

        <div class="detail-row">
            <label>Nome:</label>
            <span><%= product.getName() %></span>
        </div>

        <div class="detail-row">
            <label>Descrição:</label>
            <span><%= product.getDescription() != null ? product.getDescription() : "-" %></span>
        </div>

        <div class="detail-row">
            <label>Categoria:</label>
            <span><%= product.getCategory() != null ? product.getCategory() : "-" %></span>
        </div>

        <div class="detail-row">
            <label>Peso:</label>
            <span><%= product.getWeightKg() %> kg</span>
        </div>

        <div class="detail-row">
            <label>Volume:</label>
            <span><%= product.getVolumeM3() %> m³</span>
        </div>

        <div class="detail-row">
            <label>Valor Declarado:</label>
            <span>R$ <%= String.format("%.2f", product.getDeclaredValue()) %></span>
        </div>

        <div class="detail-row">
            <label>Status:</label>
            <span class="badge <%= product.getActive() ? "badge-success" : "badge-danger" %>">
                <%= product.getActive() ? "Ativo" : "Inativo" %>
            </span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><%= product.getCreatedAt() %></span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/products/edit/<%= product.getId() %>" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
