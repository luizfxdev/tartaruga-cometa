<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Produtos">
    <div class="page-header">
        <h2>Produtos</h2>
        <a href="${pageContext.request.contextPath}/products/new" class="btn btn-primary">+ Novo Produto</a>
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

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/products/search">
            <input type="text" name="q" placeholder="Buscar produto por nome..." required>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty products}">
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Nome</th>
                            <th>Categoria</th>
                            <th>Peso (kg)</th>
                            <th>Volume (m³)</th>
                            <th>Valor Declarado</th>
                            <th>Status</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td>${product.id}</td>
                                <td>${product.name}</td>
                                <td>${product.category != null ? product.category : '-'}</td>
                                <td>${product.weightKg}</td>
                                <td>${product.volumeM3}</td>
                                <td><fmt:formatNumber value="${product.declaredValue}" type="currency" currencySymbol="R$ "/></td>
                                <td>
                                    <c:if test="${product.active}">
                                        <span class="badge badge-success">Ativo</span>
                                    </c:if>
                                    <c:if test="${not product.active}">
                                        <span class="badge badge-danger">Inativo</span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/products/view/${product.id}" class="btn btn-info btn-sm">Ver</a>
                                        <a href="${pageContext.request.contextPath}/products/edit/${product.id}" class="btn btn-warning btn-sm">Editar</a>
                                        <form method="POST" action="${pageContext.request.contextPath}/products/delete/${product.id}" style="display:inline;">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza?')">Deletar</button>
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
            <div class="alert alert-info">
                Nenhum produto encontrado. <a href="${pageContext.request.contextPath}/products/new">Criar novo produto</a>
            </div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />
