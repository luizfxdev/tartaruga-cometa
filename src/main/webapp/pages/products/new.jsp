<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="${product != null ? 'Editar Produto' : 'Novo Produto'}">
    <div class="page-header">
        <h2>${product != null ? 'Editar Produto' : 'Novo Produto'}</h2>
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
        </c:if>

        <div class="form-group">
            <label for="name">Nome do Produto *</label>
            <input type="text" id="name" name="name" 
                   value="${product != null && product.name != null ? product.name : ''}" 
                   required placeholder="Nome do produto">
        </div>

        <div class="form-group">
            <label for="description">Descrição</label>
            <textarea id="description" name="description" placeholder="Descrição do produto" rows="4">${product != null && product.description != null ? product.description : ''}</textarea>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="category">Categoria *</label>
                <input type="text" id="category" name="category" 
                       value="${product != null && product.category != null ? product.category : ''}" 
                       required placeholder="Categoria">
            </div>

            <div class="form-group">
                <label for="weightKg">Peso (kg) *</label>
                <input type="number" id="weightKg" name="weightKg" step="0.01"
                       value="${product != null && product.weightKg != null ? product.weightKg : ''}" 
                       required placeholder="0.00">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="volumeM3">Volume (m³) *</label>
                <input type="number" id="volumeM3" name="volumeM3" step="0.01"
                       value="${product != null && product.volumeM3 != null ? product.volumeM3 : ''}" 
                       required placeholder="0.00">
            </div>

            <div class="form-group">
                <label for="declaredValue">Valor Declarado (R$) *</label>
                <input type="number" id="declaredValue" name="declaredValue" step="0.01"
                       value="${product != null && product.declaredValue != null ? product.declaredValue : ''}" 
                       required placeholder="0.00">
            </div>
        </div>

        <div class="form-group">
            <label for="active">
                <input type="checkbox" id="active" name="active" value="true" 
                       ${product != null && product.active ? 'checked' : ''}>
                Ativo
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar</button>
            <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
