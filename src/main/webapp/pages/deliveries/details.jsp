<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes da Entrega">
    <div class="page-header">
        <h2>Detalhes da Entrega - ${delivery.trackingCode}</h2>
        <div>
            <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
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
        <h3>Informações Gerais</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span>${delivery.id}</span>
        </div>

        <div class="detail-row">
            <label>Código de Rastreio:</label>
            <span><strong>${delivery.trackingCode}</strong></span>
        </div>

        <div class="detail-row">
            <label>Status:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.status.name() == 'PENDING'}">
                        <span class="badge badge-warning">Pendente</span>
                    </c:when>
                    <c:when test="${delivery.status.name() == 'IN_TRANSIT'}">
                        <span class="badge badge-info">Em Trânsito</span>
                    </c:when>
                    <c:when test="${delivery.status.name() == 'DELIVERED'}">
                        <span class="badge badge-success">Entregue</span>
                    </c:when>
                    <c:when test="${delivery.status.name() == 'NOT_PERFORMED'}">
                        <span class="badge badge-secondary">Não Realizada</span>
                    </c:when>
                    <c:when test="${delivery.status.name() == 'CANCELED'}">
                        <span class="badge badge-danger">Cancelada</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge badge-light">${delivery.status.label}</span>
                    </c:otherwise>
                </c:choose>
            </span>
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

    <div class="details-card">
        <h3>Partes Envolvidas</h3>

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
    </div>

    <div class="details-card">
        <h3>Endereços</h3>

        <div class="detail-row">
            <label>Origem:</label>
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
            <label>Destino:</label>
            <span>
                <c:choose>
                    <c:when test="${delivery.destinationAddress != null}">
                        <a href="${pageContext.request.contextPath}/addresses/view/${delivery.destinationAddress.id}?clientId=${delivery.recipient.id}">${delivery.destinationAddress.street}, ${delivery.destinationAddress.number} - ${delivery.destinationAddress.city}, ${delivery.destinationAddress.state}</a>
                    </c:when>
                    <c:otherwise>N/A</c:otherwise>
                </c:choose>
            </span>
        </div>
    </div>

    <div class="details-card">
        <h3>Valores</h3>

        <div class="detail-row">
            <label>Valor Total:</label>
            <span><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
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
            <label>Valor do Frete:</label>
            <span><fmt:formatNumber value="${delivery.freightValue}" type="currency" currencySymbol="R$ " minFractionDigits="2" maxFractionDigits="2"/></span>
        </div>
    </div>

    <c:if test="${not empty delivery.observations}">
        <div class="details-card">
            <h3>Observações</h3>
            <p>${delivery.observations}</p>
        </div>
    </c:if>

    <div class="details-card">
        <h3>Ações de Status</h3>
        <div class="form-actions">
            <c:if test="${delivery.status.name() == 'PENDING' || delivery.status.name() == 'IN_TRANSIT'}">
                <form method="POST" action="${pageContext.request.contextPath}/deliveries/markDelivered/${delivery.id}" style="display:inline;">
                    <button type="submit" class="btn btn-success" onclick="return confirm('Confirmar entrega?')">Marcar como Entregue</button>
                </form>
                <button type="button" class="btn btn-secondary" data-toggle="modal" data-target="#notDeliveredModal">Marcar como Não Realizada</button>
            </c:if>
            <c:if test="${delivery.status.name() == 'NOT_PERFORMED' || delivery.status.name() == 'DELIVERED' || delivery.status.name() == 'CANCELED'}">
                <span class="text-muted">Nenhuma ação de status disponível para este estado.</span>
            </c:if>
        </div>
    </div>

    <%-- Modal para Marcar como Não Realizada --%>
    <div class="modal fade" id="notDeliveredModal" tabindex="-1" role="dialog" aria-labelledby="notDeliveredModalLabel" aria-hidden="true">
        <div class="modal-dialog" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="notDeliveredModalLabel">Marcar Entrega como Não Realizada</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <form method="POST" action="${pageContext.request.contextPath}/deliveries/markNotDelivered/${delivery.id}">
                    <div class="modal-body">
                        <div class="form-group">
                            <label for="reasonNotDelivered">Motivo da Não Realização:</label>
                            <textarea class="form-control" id="reasonNotDelivered" name="reasonNotDelivered" rows="3" required></textarea>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancelar</button>
                        <button type="submit" class="btn btn-danger">Confirmar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <c:if test="${not empty delivery.history}">
        <div class="details-card">
            <h3>Histórico</h3>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Data da Mudança</th>
                            <th>Status Anterior</th>
                            <th>Novo Status</th>
                            <th>Motivo</th>
                            <th>Usuário</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="historyEntry" items="${delivery.history}">
                            <tr>
                                <td>${historyEntry.formattedChangeDate}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${historyEntry.previousStatus != null}">
                                            <span class="badge badge-secondary">${historyEntry.formattedPreviousStatus}</span>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <c:choose>
                                        <c:when test="${historyEntry.newStatus != null}">
                                            <span class="badge badge-info">${historyEntry.formattedNewStatus}</span>
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${historyEntry.reason != null && !historyEntry.reason.isEmpty() ? historyEntry.reason : '-'}</td>
                                <td>${historyEntry.changedBy != null && !historyEntry.changedBy.isEmpty() ? historyEntry.changedBy : '-'}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <c:if test="${delivery == null}">
        <div class="alert alert-danger">
            Entrega não encontrada.
        </div>
    </c:if>
</t:header>

<t:footer />
