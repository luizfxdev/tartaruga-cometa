<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Lista de Entregas">
    <t:breadcrumb />

    <div class="page-header">
        <h2>Entregas</h2>
        <a href="${pageContext.request.contextPath}/deliveries/new" class="custom-btn btn-primary">+ Nova Entrega</a>
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
        <form action="${pageContext.request.contextPath}/deliveries/search" method="GET">
            <input type="text" name="query" placeholder="Buscar por código, status..." value="${param.query}">
            <button type="submit" class="custom-btn btn-info">Buscar</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty deliveries}">
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Código de Rastreio</th>
                            <th>Remetente</th>
                            <th>Destinatário</th>
                            <th>Status</th>
                            <th>Data de Criação</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="delivery" items="${deliveries}">
                            <tr>
                                <td>${delivery.id}</td>
                                <td><strong>${delivery.trackingCode}</strong></td>
                                <td>${delivery.sender != null ? delivery.sender.name : 'N/A'}</td>
                                <td>${delivery.recipient != null ? delivery.recipient.name : 'N/A'}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${delivery.status.name() == 'PENDING'}">
                                            <span class="badge badge-warning">${delivery.status.label}</span>
                                        </c:when>
                                        <c:when test="${delivery.status.name() == 'IN_TRANSIT'}">
                                            <span class="badge badge-info">${delivery.status.label}</span>
                                        </c:when>
                                        <c:when test="${delivery.status.name() == 'DELIVERED'}">
                                            <span class="badge badge-success">${delivery.status.label}</span>
                                        </c:when>
                                        <c:when test="${delivery.status.name() == 'NOT_PERFORMED'}">
                                            <span class="badge badge-secondary">${delivery.status.label}</span>
                                        </c:when>
                                        <c:when test="${delivery.status.name() == 'CANCELED'}">
                                            <span class="badge badge-danger">${delivery.status.label}</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-light">${delivery.status.label}</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${delivery.formattedCreationDate}</td>
                                <td>
                                    <div class="btn-group">
                                        <a href="${pageContext.request.contextPath}/deliveries/view/${delivery.id}" class="custom-btn btn-info btn-sm">Ver</a>
                                        <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="custom-btn btn-warning btn-sm">Editar</a>
                                        <form method="POST" action="${pageContext.request.contextPath}/deliveries/delete/${delivery.id}" style="display:inline;">
                                            <button type="submit" class="custom-btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja deletar esta entrega?')">Deletar</button>
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
                Nenhuma entrega encontrada. <a href="${pageContext.request.contextPath}/deliveries/new" style="color: var(--primary); font-weight: 600;">Criar nova entrega</a>
            </div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />