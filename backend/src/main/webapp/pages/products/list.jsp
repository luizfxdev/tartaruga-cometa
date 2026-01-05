<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.tartarugacometasystem.util.DateFormatter" %> <%-- Importa a classe DateFormatter --%>

<t:header title="Lista de Produtos">
    <div class="page-header">
        <h2>Produtos</h2>
        <div>
            <a href="${pageContext.request.contextPath}/products/new" class="btn btn-primary">Novo Produto</a>
        </div>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>
    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="search-bar">
        <form action="${pageContext.request.contextPath}/products/search" method="GET">
            <input type="text" name="query" placeholder="Buscar por nome, categoria..." value="${param.query}">
            <button type="submit" class="btn btn-info">Buscar</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty products}">
            <div class="table-responsive">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Categoria</th>
                            <th>Preço</th>
                            <th>Peso (kg)</th>
                            <th>Volume (m³)</th>
                            <th>Valor Declarado</th>
                            <th>Ativo</th>
                            <th>Estoque</th>
                            <th>Criado Em</th>
                            <th>Atualizado Em</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.name}</td>
                                <td>${product.category}</td>
                                <td><fmt:formatNumber value="${product.price}" type="currency" currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td><fmt:formatNumber value="${product.weightKg}" pattern="#,##0.00"/></td>
                                <td><fmt:formatNumber value="${product.volumeM3}" pattern="#,##0.00"/></td>
                                <td><fmt:formatNumber value="${product.declaredValue}" type="currency" currencySymbol="R$" minFractionDigits="2" maxFractionDigits="2"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${product.active}">
                                            <span class="badge bg-success">Sim</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-danger">Não</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${product.stockQuantity}</td>
                                <td>
                                    <%-- Formata LocalDateTime usando o DateFormatter --%>
                                    ${product.createdAt != null ? DateFormatter.formatLocalDateTime(product.createdAt) : '-'}
                                </td>
                                <td>
                                    <%-- Formata LocalDateTime usando o DateFormatter --%>
                                    ${product.updatedAt != null ? DateFormatter.formatLocalDateTime(product.updatedAt) : '-'}
                                </td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <a href="${pageContext.request.contextPath}/products/view/${product.id}" class="btn btn-info btn-sm">Ver</a>
                                        <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="btn btn-warning btn-sm">Editar</a>
                                        <form action="${pageContext.request.contextPath}/products/delete/${product.id}" method="POST" style="display:inline;">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja deletar este produto?')">Deletar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">Nenhum produto encontrado. <a href="${pageContext.request.contextPath}/products/new">Criar novo produto</a></div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />
