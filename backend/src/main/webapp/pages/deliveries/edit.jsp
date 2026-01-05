<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="${delivery != null && delivery.id != null ? 'Editar Entrega' : 'Nova Entrega'}">
    <div class="page-header">
        <h2>${delivery != null && delivery.id != null ? 'Editar Entrega' : 'Nova Entrega'}</h2>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/deliveries/save" class="form">
        <c:if test="${delivery != null && delivery.id != null}">
            <input type="hidden" name="id" value="${delivery.id}">
        </c:if>

        <div class="form-group">
            <label for="trackingCode">Código de Rastreio *</label>
            <input type="text" id="trackingCode" name="trackingCode" value="${delivery.trackingCode}" required>
        </div>

        <div class="form-group">
            <label for="senderId">Remetente *</label>
            <select id="senderId" name="senderId" required>
                <option value="">Selecione...</option>
                <c:forEach var="client" items="${clients}">
                    <option value="${client.id}" ${delivery != null && delivery.senderId == client.id ? 'selected' : ''}>
                        ${client.name} (${client.document})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="recipientId">Destinatário *</label>
            <select id="recipientId" name="recipientId" required>
                <option value="">Selecione...</option>
                <c:forEach var="client" items="${clients}">
                    <option value="${client.id}" ${delivery != null && delivery.recipientId == client.id ? 'selected' : ''}>
                        ${client.name} (${client.document})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="originAddressId">Endereço de Origem *</label>
            <select id="originAddressId" name="originAddressId" required>
                <option value="">Selecione...</option>
                <c:forEach var="address" items="${addresses}">
                    <option value="${address.id}" ${delivery != null && delivery.originAddressId == address.id ? 'selected' : ''}>
                        ${address.street}, ${address.number} - ${address.city}, ${address.state} (${address.addressType.label})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="destinationAddressId">Endereço de Destino *</label>
            <select id="destinationAddressId" name="destinationAddressId" required>
                <option value="">Selecione...</option>
                <c:forEach var="address" items="${addresses}">
                    <option value="${address.id}" ${delivery != null && delivery.destinationAddressId == address.id ? 'selected' : ''}>
                        ${address.street}, ${address.number} - ${address.city}, ${address.state} (${address.addressType.label})
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="deliveryDate">Data Prevista de Entrega</label>
            <input type="datetime-local" id="deliveryDate" name="deliveryDate"
                   value="<fmt:formatDate value="${delivery.deliveryDate}" pattern="yyyy-MM-dd'T'HH:mm"/>">
        </div>

        <div class="form-group">
            <label for="status">Status *</label>
            <select id="status" name="status" required>
                <option value="">Selecione...</option>
                <c:forEach var="deliveryStatus" items="${deliveryStatuses}">
                    <option value="${deliveryStatus.name()}" ${delivery != null && delivery.status.name() == deliveryStatus.name() ? 'selected' : ''}>
                        ${deliveryStatus.label}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="totalValue">Valor Total (R$) *</label>
            <input type="number" step="0.01" id="totalValue" name="totalValue" value="${delivery.totalValue}" required>
        </div>

        <div class="form-group">
            <label for="totalWeightKg">Peso Total (kg) *</label>
            <input type="number" step="0.01" id="totalWeightKg" name="totalWeightKg" value="${delivery.totalWeightKg}" required>
        </div>

        <div class="form-group">
            <label for="totalVolumeM3">Volume Total (m³) *</label>
            <input type="number" step="0.01" id="totalVolumeM3" name="totalVolumeM3" value="${delivery.totalVolumeM3}" required>
        </div>

        <div class="form-group">
            <label for="freightValue">Valor do Frete (R$) *</label>
            <input type="number" step="0.01" id="freightValue" name="freightValue" value="${delivery.freightValue}" required>
        </div>

        <div class="form-group">
            <label for="observations">Observações</label>
            <textarea id="observations" name="observations" rows="3">${delivery.observations}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
