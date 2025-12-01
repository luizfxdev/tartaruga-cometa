<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes da Entrega">
    <div class="page-header">
        <h2>Detalhes da Entrega - ${delivery.trackingCode}</h2>
        <div>
            <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar à Lista</a>
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

    <div class="details-card">
        <h3>Informações da Entrega</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span>${delivery.id}</span>
        </div>

        <div class="detail-row">
            <label>Código de Rastreio:</label>
            <span><strong>${delivery.trackingCode}</strong></span>
        </div>

        <div class="detail-row">
            <label>Remetente:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.sender != null}">
                        <a href="${pageContext.request.contextPath}/clients/view/${delivery.sender.id}">${delivery.sender.name}</a> (${delivery.sender.document})
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="detail-row">
            <label>Destinatário:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.recipient != null}">
                        <a href="${pageContext.request.contextPath}/clients/view/${delivery.recipient.id}">${delivery.recipient.name}</a> (${delivery.recipient.document})
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="detail-row">
            <label>Endereço de Origem:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.originAddress != null}">
                        <a href="${pageContext.request.contextPath}/addresses/view/${delivery.originAddress.id}?clientId=${delivery.sender.id}">${delivery.originAddress.street}, ${delivery.originAddress.number} - ${delivery.originAddress.city}, ${delivery.originAddress.state}</a>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="detail-row">
            <label>Endereço de Destino:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.destinationAddress != null}">
                        <a href="${pageContext.request.contextPath}/addresses/view/${delivery.destinationAddress.id}?clientId=${delivery.recipient.id}">${delivery.destinationAddress.street}, ${delivery.destinationAddress.number} - ${delivery.destinationAddress.city}, ${delivery.destinationAddress.state}</a>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>

        <div class="detail-row">
            <label>Valor Total:</label>
            <span><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>

        <div class="detail-row">
            <label>Valor do Frete:</label>
            <span><fmt:formatNumber value="${delivery.freightValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>

        <div class="detail-row">
            <label>Peso Total:</label>
            <span><fmt:formatNumber value="${delivery.totalWeightKg}" pattern="#,##0.00" /> kg</span>
        </div>

        <div class="detail-row">
            <label>Volume Total:</label>
            <span><fmt:formatNumber value="${delivery.totalVolumeM3}" pattern="#,##0.00" /> m³</span>
        </div>

        <div class="detail-row">
            <label>Status:</label>
            <span>
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
            </span>
        </div>

        <div class="detail-row">
            <label>Observações:</label>
            <span>${delivery.observations != null && !delivery.observations.isEmpty() ? delivery.observations : '-'}</span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span>${delivery.formattedCreationDate}</span>
        </div>

        <div class="detail-row">
            <label>Última Atualização:</label>
            <span>${delivery.formattedUpdatedDate != null ? delivery.formattedUpdatedDate : 'Nunca atualizado'}</span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar à Lista</a>
    </div>
</t:header>

<t:footer />
