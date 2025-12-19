<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes da Entrega">
    <div class="page-header">
        <h2>Detalhes da Entrega</h2>
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="custom-btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/deliveries/" class="custom-btn btn-secondary">Voltar</a>
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

    <div class="details-container">
        <div class="details-card">
            <h3>Informações da Entrega</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>ID</label>
                    <span>${delivery.id}</span>
                </div>

                <div class="detail-item">
                    <label>Código de Rastreio</label>
                    <span><strong>${delivery.trackingCode}</strong></span>
                </div>

                <div class="detail-item">
                    <label>Remetente</label>
                    <span>
                        <c:choose>
                            <c:when test="${delivery.sender != null}">
                                <a href="${pageContext.request.contextPath}/clients/view/${delivery.sender.id}">${delivery.sender.name}</a>
                                <br><small>${delivery.sender.document}</small>
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="detail-item">
                    <label>Destinatário</label>
                    <span>
                        <c:choose>
                            <c:when test="${delivery.recipient != null}">
                                <a href="${pageContext.request.contextPath}/clients/view/${delivery.recipient.id}">${delivery.recipient.name}</a>
                                <br><small>${delivery.recipient.document}</small>
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="detail-item full-width">
                    <label>Endereço de Origem</label>
                    <span>
                        <c:choose>
                            <c:when test="${delivery.originAddress != null}">
                                ${delivery.originAddress.street}, ${delivery.originAddress.number}
                                <br><small>${delivery.originAddress.city}, ${delivery.originAddress.state}</small>
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="detail-item full-width">
                    <label>Endereço de Destino</label>
                    <span>
                        <c:choose>
                            <c:when test="${delivery.destinationAddress != null}">
                                ${delivery.destinationAddress.street}, ${delivery.destinationAddress.number}
                                <br><small>${delivery.destinationAddress.city}, ${delivery.destinationAddress.state}</small>
                            </c:when>
                            <c:otherwise>N/A</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>

        <div class="details-card">
            <h3>Valores e Medidas</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>Valor Total</label>
                    <span><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>

                <div class="detail-item">
                    <label>Valor do Frete</label>
                    <span><fmt:formatNumber value="${delivery.freightValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
                </div>

                <div class="detail-item">
                    <label>Peso Total</label>
                    <span><fmt:formatNumber value="${delivery.totalWeightKg}" pattern="#,##0.00" /> kg</span>
                </div>

                <div class="detail-item">
                    <label>Volume Total</label>
                    <span><fmt:formatNumber value="${delivery.totalVolumeM3}" pattern="#,##0.00" /> m³</span>
                </div>
            </div>
        </div>

        <div class="details-card">
            <h3>Status e Observações</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>Status</label>
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

                <div class="detail-item">
                    <label>Data de Criação</label>
                    <span>${delivery.formattedCreationDate}</span>
                </div>

                <div class="detail-item">
                    <label>Última Atualização</label>
                    <span>${delivery.formattedUpdatedAt != null ? delivery.formattedUpdatedAt : 'Nunca atualizado'}</span>
                </div>

                <div class="detail-item full-width">
                    <label>Observações</label>
                    <span>${delivery.observations != null && !delivery.observations.isEmpty() ? delivery.observations : '-'}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="custom-btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/deliveries/" class="custom-btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />