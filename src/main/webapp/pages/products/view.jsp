<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.tartarugacometasystem.util.DateFormatter" %>

<t:header title="Detalhes do Produto">
    <div class="page-header">
        <h2>Detalhes do Produto</h2>
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="custom-btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/products/" class="custom-btn btn-secondary">Voltar</a>
        </div>
    </div>

    <div class="details-container">
        <div class="details-card">
            <h3>Informações Básicas</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>ID</label>
                    <span>${product.id}</span>
                </div>

                <div class="detail-item">
                    <label>Nome</label>
                    <span><strong>${product.name}</strong></span>
                </div>

                <div class="detail-item">
                    <label>Categoria</label>
                    <span>${product.category != null && !product.category.isEmpty() ? product.category : '-'}</span>
                </div>

                <div class="detail-item full-width">
                    <label>Descrição</label>
                    <span>${product.description != null && !product.description.isEmpty() ? product.description : '-'}</span>
                </div>
            </div>
        </div>

        <div class="details-card">
            <h3>Especificações Físicas</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>Preço</label>
                    <span><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>

                <div class="detail-item">
                    <label>Peso</label>
                    <span><fmt:formatNumber value="${product.weightKg}" pattern="#,##0.00" /> kg</span>
                </div>

                <div class="detail-item">
                    <label>Volume</label>
                    <span><fmt:formatNumber value="${product.volumeM3}" pattern="#,##0.00" /> m³</span>
                </div>

                <div class="detail-item">
                    <label>Valor Declarado</label>
                    <span><fmt:formatNumber value="${product.declaredValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>
            </div>
        </div>

        <div class="details-card">
            <h3>Estoque e Status</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>Quantidade em Estoque</label>
                    <span>${product.stockQuantity != null ? product.stockQuantity : '-'}</span>
                </div>

                <div class="detail-item">
                    <label>Situação</label>
                    <span>
                        <c:choose>
                            <c:when test="${product.active}">
                                <span class="badge badge-success">Ativo</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-danger">Inativo</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="detail-item">
                    <label>Data de Criação</label>
                    <span>${product.createdAt != null ? DateFormatter.formatLocalDateTime(product.createdAt) : '-'}</span>
                </div>

                <div class="detail-item">
                    <label>Última Atualização</label>
                    <span>${product.updatedAt != null ? DateFormatter.formatLocalDateTime(product.updatedAt) : 'Nunca atualizado'}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="custom-btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/products/" class="custom-btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />