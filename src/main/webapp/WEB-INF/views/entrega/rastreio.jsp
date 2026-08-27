<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Rastreamento de Entrega">
    <div class="page-header">
        <h2>Rastreamento de Entrega</h2>
    </div>

    <c:if test="${not empty delivery}">
        <div class="tracking-container">
            <div class="tracking-header">
                <h3>Código: ${delivery.trackingCode}</h3>
            </div>

            <div class="tracking-info">
                <div class="info-box">
                    <h4>Status Atual</h4>
                    <p>
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
                    </p>
                </div>

                <div class="info-box">
                    <h4>Valor</h4>
                    <p><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ "/></p>
                </div>

                <div class="info-box">
                    <h4>Data de Criação</h4>
                    <p><fmt:formatDate value="${delivery.createdAt}" pattern="dd/MM/yyyy HH:mm"/></p>
                </div>
            </div>

            <div class="timeline">
                <h4>Histórico de Atualizações</h4>
                <c:if test="${not empty deliveryHistory}">
                    <c:forEach var="history" items="${deliveryHistory}">
                        <div class="timeline-item">
                            <div class="timeline-date">
                                <fmt:formatDate value="${history.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                            </div>
                            <div class="timeline-content">
                                <h5>${history.status.label}</h5>
                                <p>${history.observations}</p>
                                <small>Por: ${history.user}</small>
                            </div>
                        </div>
                    </c:forEach>
                </c:if>
                <c:if test="${empty deliveryHistory}">
                    <p>Nenhum histórico disponível.</p>
                </c:if>
            </div>
        </div>
    </c:if>

    <c:if test="${empty delivery}">
        <div class="alert alert-danger">
            Entrega não encontrada.
        </div>
    </c:if>
</t:header>

<t:footer />
