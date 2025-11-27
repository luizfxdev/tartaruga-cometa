<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Detalhes da Entrega">
    <div class="page-header">
        <h2>Detalhes da Entrega</h2>
        <div>
            <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
        </div>
    </div>

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
            </span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><fmt:formatDate value="${delivery.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
        </div>
    </div>

    <div class="details-card">
        <h3>Partes Envolvidas</h3>

        <div class="detail-row">
            <label>Remetente:</label>
            <span>${shipper != null ? shipper.name : 'N/A'}</span>
        </div>

        <div class="detail-row">
            <label>Destinatário:</label>
            <span>${recipient != null ? recipient.name : 'N/A'}</span>
        </div>
    </div>

    <div class="details-card">
        <h3>Endereços</h3>

        <div class="detail-row">
            <label>Origem:</label>
            <span>
                <c:if test="${originAddress != null}">
                    ${originAddress.street}, ${originAddress.number} - ${originAddress.city}, ${originAddress.state}
                </c:if>
                <c:if test="${originAddress == null}">
                    N/A
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Destino:</label>
            <span>
                <c:if test="${destinationAddress != null}">
                    ${destinationAddress.street}, ${destinationAddress.number} - ${destinationAddress.city}, ${destinationAddress.state}
                </c:if>
                <c:if test="${destinationAddress == null}">
                    N/A
                </c:if>
            </span>
        </div>
    </div>

    <div class="details-card">
        <h3>Valores</h3>

        <div class="detail-row">
            <label>Valor Total:</label>
            <span><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ "/></span>
        </div>

        <div class="detail-row">
            <label>Peso Total:</label>
            <span>${delivery.totalWeightKg} kg</span>
        </div>

        <div class="detail-row">
            <label>Volume Total:</label>
            <span>${delivery.totalVolumeM3} m³</span>
        </div>

        <div class="detail-row">
            <label>Valor do Frete:</label>
            <span><fmt:formatNumber value="${delivery.freightValue}" type="currency" currencySymbol="R$ "/></span>
        </div>
    </div>

    <c:if test="${not empty delivery.observations}">
        <div class="details-card">
            <h3>Observações</h3>
            <p>${delivery.observations}</p>
        </div>
    </c:if>

    <c:if test="${not empty history}">
        <div class="details-card">
            <h3>Histórico</h3>
            <div class="table-container">
                <table class="table">
                    <thead>
                        <tr>
                            <th>Data</th>
                            <th>Status Anterior</th>
                            <th>Novo Status</th>
                            <th>Usuário</th>
                            <th>Observações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="h" items="${history}">
                            <tr>
                                <td><fmt:formatDate value="${h.changeDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                <td>${h.previousStatus != null ? h.previousStatus.label : '-'}</td>
                                <td>${h.newStatus.label}</td>
                                <td>${h.user}</td>
                                <td>${h.observations != null ? h.observations : '-'}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/deliveries/edit/${delivery.id}" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
