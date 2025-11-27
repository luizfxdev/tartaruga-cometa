<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Relatório de Entregas">
    <div class="page-header">
        <h2>📊 Relatório de Entregas</h2>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/reports/deliveries">
            <input type="date" name="startDate" value="${param.startDate}" placeholder="Data Inicial">
            <input type="date" name="endDate" value="${param.endDate}" placeholder="Data Final">
            <select name="status">
                <option value="">Todos os Status</option>
                <option value="PENDENTE" ${param.status == 'PENDENTE' ? 'selected' : ''}>Pendente</option>
                <option value="EM_TRANSITO" ${param.status == 'EM_TRANSITO' ? 'selected' : ''}>Em Trânsito</option>
                <option value="ENTREGUE" ${param.status == 'ENTREGUE' ? 'selected' : ''}>Entregue</option>
                <option value="NAO_REALIZADA" ${param.status == 'NAO_REALIZADA' ? 'selected' : ''}>Não Realizada</option>
                <option value="CANCELADA" ${param.status == 'CANCELADA' ? 'selected' : ''}>Cancelada</option>
            </select>
            <button type="submit" class="btn btn-primary">Filtrar</button>
            <a href="${pageContext.request.contextPath}/reports/deliveries" class="btn btn-secondary">Limpar</a>
        </form>
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

    <c:choose>
        <c:when test="${not empty deliveries}">
            <div class="details-card" style="margin-bottom: 2rem;">
                <h3>Resumo do Relatório</h3>
                <div class="detail-row">
                    <label>Total de Registros:</label>
                    <span><strong>${deliveries.size()}</strong></span>
                </div>
                <div class="detail-row">
                    <label>Período:</label>
                    <span>
                        <c:if test="${not empty param.startDate && not empty param.endDate}">
                            ${param.startDate} até ${param.endDate}
                        </c:if>
                        <c:if test="${empty param.startDate || empty param.endDate}">
                            Sem filtro de data
                        </c:if>
                    </span>
                </div>
            </div>

            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Código de Rastreio</th>
                            <th>Remetente</th>
                            <th>Destinatário</th>
                            <th>Status</th>
                            <th>Valor Total</th>
                            <th>Peso (kg)</th>
                            <th>Data de Criação</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="delivery" items="${deliveries}">
                            <tr>
                                <td>${delivery.id}</td>
                                <td><strong>${delivery.trackingCode}</strong></td>
                                <td>${delivery.shipperId}</td>
                                <td>${delivery.recipientId}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${delivery.status.value == 'PENDENTE'}">
                                            <span class="badge badge-warning">Pendente</span>
                                        </c:when>
                                        <c:when test="${delivery.status.value == 'EM_TRANSITO'}">
                                            <span class="badge badge-info">Em Trânsito</span>
                                        </c:when>
                                        <c:when test="${delivery.status.value == 'ENTREGUE'}">
                                            <span class="badge badge-success">Entregue</span>
                                        </c:when>
                                        <c:when test="${delivery.status.value == 'NAO_REALIZADA'}">
                                            <span class="badge badge-secondary">Não Realizada</span>
                                        </c:when>
                                        <c:when test="${delivery.status.value == 'CANCELADA'}">
                                            <span class="badge badge-danger">Cancelada</span>
                                        </c:when>
                                    </c:choose>
                                </td>
                                <td><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ "/></td>
                                <td>${delivery.totalWeightKg}</td>
                                <td><fmt:formatDate value="${delivery.createdAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/deliveries/view/${delivery.id}" class="btn btn-info btn-sm">Ver</a>
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
                Nenhuma entrega encontrada para os critérios informados. Tente ajustar os filtros.
            </div>
        </c:otherwise>
    </c:choose>

    <div class="form-actions" style="margin-top: 2rem;">
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
