<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="${client != null && client.id != null ? 'Editar Cliente' : 'Novo Cliente'}">
    <div class="page-header">
        <h2>${client != null && client.id != null ? 'Editar Cliente' : 'Novo Cliente'}</h2>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/clients/save" class="form">
        <c:if test="${client != null && client.id != null}">
            <input type="hidden" name="id" value="${client.id}">
        </c:if>

        <div class="form-group">
            <label for="personType">Tipo de Pessoa *</label>
            <select id="personType" name="personType" required>
                <option value="">Selecione...</option>
                <option value="INDIVIDUAL" ${client != null && client.personType.name() == 'INDIVIDUAL' ? 'selected' : ''}>Pessoa Física</option>
                <option value="LEGAL_ENTITY" ${client != null && client.personType.name() == 'LEGAL_ENTITY' ? 'selected' : ''}>Pessoa Jurídica</option>
            </select>
        </div>

        <div class="form-group">
            <label for="document">Documento (CPF/CNPJ) *</label>
            <input type="text" id="document" name="document"
                   value="${client != null ? client.document : ''}"
                   placeholder="000.000.000-00" required>
        </div>

        <div class="form-group">
            <label for="name">Nome/Razão Social *</label>
            <input type="text" id="name" name="name"
                   value="${client != null ? client.name : ''}"
                   placeholder="Nome completo ou razão social" required>
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email"
                   value="${client != null ? client.email : ''}"
                   placeholder="email@example.com">
        </div>

        <div class="form-group">
            <label for="phone">Telefone</label>
            <input type="tel" id="phone" name="phone"
                   value="${client != null ? client.phone : ''}"
                   placeholder="(11) 99999-9999">
        </div>

        <div class="form-actions">
            <button type="submit" class="custom-btn btn-success">Salvar</button>
            <a href="${pageContext.request.contextPath}/clients/" class="custom-btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />