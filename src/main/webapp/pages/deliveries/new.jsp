<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="${delivery != null ? 'Editar Entrega' : 'Nova Entrega'}">
    <div class="page-header">
        <h2>${delivery != null ? 'Editar Entrega' : 'Nova Entrega'}</h2>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/deliveries/save" class="form">
        <input type="hidden" name="id" value="${delivery != null ? delivery.id : ''}">

        <div class="form-group">
            <label for="trackingCode">Código de Rastreio *</label>
            <input type="text" id="trackingCode" name="trackingCode"
                   value="${delivery != null ? delivery.trackingCode : ''}" required>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="shipperId">Remetente (Nome) *</label>
                <select id="shipperId" name="shipperId" required>
                    <option value="">Selecione o Remetente</option>
                    <c:forEach var="client" items="${allClients}">
                        <option value="${client.id}" ${delivery != null && delivery.shipperId == client.id ? 'selected' : ''}>
                            ${client.name} (${client.document})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="recipientId">Destinatário (Nome) *</label>
                <select id="recipientId" name="recipientId" required>
                    <option value="">Selecione o Destinatário</option>
                    <c:forEach var="client" items="${allClients}">
                        <option value="${client.id}" ${delivery != null && delivery.recipientId == client.id ? 'selected' : ''}>
                            ${client.name} (${client.document})
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="originAddressId">Endereço de Origem *</label>
                <select id="originAddressId" name="originAddressId" required>
                    <option value="">Selecione o Endereço de Origem</option>
                    <c:forEach var="address" items="${allAddresses}">
                        <option value="${address.id}" ${delivery != null && delivery.originAddressId == address.id ? 'selected' : ''}>
                            ${address.street}, ${address.number} - ${address.city}/${address.state} (Cliente ID: ${address.clientId})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="destinationAddressId">Endereço de Destino *</label>
                <select id="destinationAddressId" name="destinationAddressId" required>
                    <option value="">Selecione o Endereço de Destino</option>
                    <c:forEach var="address" items="${allAddresses}">
                        <option value="${address.id}" ${delivery != null && delivery.destinationAddressId == address.id ? 'selected' : ''}>
                            ${address.street}, ${address.number} - ${address.city}/${address.state} (Cliente ID: ${address.clientId})
                        </option>
                    </c:forEach>
                </select>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="totalValue">Valor Total (R$) *</label>
                <input type="number" id="totalValue" name="totalValue" step="0.01"
                       value="${delivery != null ? delivery.totalValue : ''}" required>
            </div>

            <div class="form-group">
                <label for="freightValue">Valor do Frete (R$) *</label>
                <input type="number" id="freightValue" name="freightValue" step="0.01"
                       value="${delivery != null ? delivery.freightValue : ''}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="totalWeightKg">Peso Total (kg) *</label>
                <input type="number" id="totalWeightKg" name="totalWeightKg" step="0.01"
                       value="${delivery != null ? delivery.totalWeightKg : ''}" required>
            </div>

            <div class="form-group">
                <label for="totalVolumeM3">Volume Total (m³) *</label>
                <input type="number" id="totalVolumeM3" name="totalVolumeM3" step="0.01"
                       value="${delivery != null ? delivery.totalVolumeM3 : ''}" required>
            </div>
        </div>

        <div class="form-group">
            <label for="status">Status *</label>
            <select id="status" name="status" required>
                <option value="">Selecione...</option>
                <c:forEach var="deliveryStatus" items="${deliveryStatuses}">
                    <option value="${deliveryStatus.value}" ${delivery != null && delivery.status.value == deliveryStatus.value ? 'selected' : ''}>
                        ${deliveryStatus.label}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="observations">Observações</label>
            <textarea id="observations" name="observations" rows="4">${delivery != null && delivery.observations != null ? delivery.observations : ''}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar</button>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
