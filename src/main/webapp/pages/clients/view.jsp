<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes do Cliente">
    <div class="page-header">
        <h2>Detalhes do Cliente</h2>
        <div>
            <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/clients/" class="btn btn-secondary">Voltar</a>
        </div>
    </div>

    <div class="details-card">
        <h3>${client.name}</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span>${client.id}</span>
        </div>

        <div class="detail-row">
            <label>Tipo de Pessoa:</label>
            <span>${client.personType.label}</span>
        </div>

        <div class="detail-row">
            <label>Documento:</label>
            <span>${client.document}</span>
        </div>

        <div class="detail-row">
            <label>Nome:</label>
            <span>${client.name}</span>
        </div>

        <div class="detail-row">
            <label>Email:</label>
            <span>${client.email != null ? client.email : '-'}</span>
        </div>

        <div class="detail-row">
            <label>Telefone:</label>
            <span>${client.phone != null ? client.phone : '-'}</span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><fmt:formatDate value="${client.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/clients/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
