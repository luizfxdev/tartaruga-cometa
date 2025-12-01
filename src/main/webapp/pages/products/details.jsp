<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes Completos do Produto">
    <div class="page-header">
        <h2>Detalhes Completos - ${product.name}</h2>
        <div>
            <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Voltar</a>
        </div>
    </div>

    <div class="details-card">
        <h3>Informações Básicas</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span>${product.id}</span>
        </div>

        <div class="detail-row">
            <label>Nome:</label>
            <span><strong>${product.name}</strong></span>
        </div>

        <div class="detail-row">
            <label>Categoria:</label>
            <span>${product.category != null && !product.category.isEmpty() ? product.category : '-'}</span>
        </div>

        <div class="detail-row">
            <label>Descrição:</label>
            <span>${product.description != null && !product.description.isEmpty() ? product.description : '-'}</span>
        </div>
    </div>

    <div class="details-card">
        <h3>Especificações Físicas</h3>

        <div class="detail-row">
            <label>Peso (kg):</label>
            <span><fmt:formatNumber value="${product.weightKg}" pattern="#,##0.00" /> kg</span>
        </div>

        <div class="detail-row">
            <label>Volume (m³):</label>
            <span><fmt:formatNumber value="${product.volumeM3}" pattern="#,##0.00" /> m³</span>
        </div>

        <div class="detail-row">
            <label>Valor Declarado:</label>
            <span><fmt:formatNumber value="${product.declaredValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>
    </div>

    <div class="details-card">
        <h3>Status</h3>

        <div class="detail-row">
            <label>Situação:</label>
            <span>
                <c:if test="${product.active}">
                    <span class="badge badge-success">Ativo</span>
                </c:if>
                <c:if test="${not product.active}">
                    <span class="badge badge-danger">Inativo</span>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span>${product.formattedCreationDate}</span>
        </div>

        <div class="detail-row">
            <label>Última Atualização:</label>
            <span>${product.formattedUpdatedDate != null ? product.formattedUpdatedDate : 'Nunca atualizado'}</span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/products/" class="btn btn-secondary">Voltar à Lista</a>
    </div>
</t:header>

<t:footer />
