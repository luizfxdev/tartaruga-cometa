<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="${product != null && product.id != null ? 'Editar Produto' : 'Novo Produto'}">
    <div class="page-header">
        <h2>${product != null && product.id != null ? 'Editar Produto' : 'Novo Produto'}</h2>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/products/save" class="form">
        <c:if test="${product != null && product.id != null}">
            <input type="hidden" name="id" value="${product.id}">
            <input type="hidden" name="createdAt" value="${product.creationDate}"> <%-- Passa a data original para o mapper --%>
            <input type="hidden" name="updatedAt" value="${product.updatedDate}"> <%-- Passa a data original para o mapper --%>
        </c:if>

        <div class="form-group">
            <label for="name">Nome *</label>
            <input type="text" id="name" name="name" value="${product.name}" required>
        </div>

        <div class="form-group">
            <label for="category">Categoria</label>
            <input type="text" id="category" name="category" value="${product.category}">
        </div>

        <div class="form-group">
            <label for="description">Descrição</label>
            <textarea id="description" name="description">${product.description}</textarea>
        </div>

        <div class="form-group">
            <label for="weightKg">Peso (kg) *</label>
            <input type="number" id="weightKg" name="weightKg" value="${product.weightKg}" step="0.01" min="0" required>
        </div>

        <div class="form-group">
            <label for="volumeM3">Volume (m³) *</label>
            <input type="number" id="volumeM3" name="volumeM3" value="${product.volumeM3}" step="0.001" min="0" required>
        </div>

        <div class="form-group">
            <label for="declaredValue">Valor Declarado *</label>
            <input type="number" id="declaredValue" name="declaredValue" value="${product.declaredValue}" step="0.01" min="0" required>
        </div>

        <div class="form-group">
            <label for="active">Ativo</label>
            <input type="checkbox" id="active" name="active" value="true" ${product.active ? 'checked' : ''}>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
