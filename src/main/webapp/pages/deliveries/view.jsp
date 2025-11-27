<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
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
        <h3>Entrega #${delivery.id}</h3>

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
            <label>Remetente:</label>
            <span>${delivery.shipperId}</span>
        </div>

        <div class="detail-row">
            <label>Destinatário:</label>
            <span>${delivery.recipientId}</span>
        </div>

        <div class="detail-row">
            <label>Endereço de Origem:</label>
            <span>${delivery.originAddressId}</span>
        </div>

        <div class="detail-row">
            <label>Endereço de Destino:</label>
            <span>${delivery.destinationAddressId}</span>
        </div>

        <div class="detail-row">
            <label>Valor Total:</label>
            <span><fmt:formatNumber value="${delivery.totalValue}" type="currency" currencySymbol="R$ "/></span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><fmt:formatDate value="${delivery.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
        </div>

        <div class="detail-row">
            <label>Última Atualização:</label>
            <span><fmt:formatDate value="${delivery.updatedAt}" pattern="dd/MM/yyyy HH:mm"/></span>
        </div>
    </div>
</t:header>

<t:footer />
