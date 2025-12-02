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
                        <option value="${client.id}" ${delivery != null && delivery.senderId == client.id ? 'selected' : ''}>
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
                        <option value="${address.id}" data-client-id="${address.clientId}" ${delivery != null && delivery.originAddressId == address.id ? 'selected' : ''}>
                            ${address.street}, ${address.number} - ${address.city}/${address.state}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label for="destinationAddressId">Endereço de Destino *</label>
                <select id="destinationAddressId" name="destinationAddressId" required>
                    <option value="">Selecione o Endereço de Destino</option>
                    <c:forEach var="address" items="${allAddresses}">
                        <option value="${address.id}" data-client-id="${address.clientId}" ${delivery != null && delivery.destinationAddressId == address.id ? 'selected' : ''}>
                            ${address.street}, ${address.number} - ${address.city}/${address.state}
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

    <%-- Script para filtrar endereços dinamicamente com base no remetente/destinatário --%>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const shipperSelect = document.getElementById('shipperId');
            const recipientSelect = document.getElementById('recipientId');
            const originAddressSelect = document.getElementById('originAddressId');
            const destinationAddressSelect = document.getElementById('destinationAddressId');

            // Armazena todas as opções de endereço para filtragem (usando data-client-id)
            // É importante clonar as opções *antes* de manipulá-las para preservar o estado original,
            // especialmente se a página for carregada com um delivery existente.
            const allOriginAddressOptions = Array.from(originAddressSelect.options).filter(opt => opt.value !== '');
            const allDestinationAddressOptions = Array.from(destinationAddressSelect.options).filter(opt => opt.value !== '');

            function filterAddresses(selectElement, allOptions, clientId, selectedAddressId) {
                const placeholder = selectElement.id === 'originAddressId' ? 'Endereço de Origem' : 'Endereço de Destino';
                selectElement.innerHTML = '<option value="">Selecione o ' + placeholder + '</option>';

                let hasOptions = false;
                if (clientId) {
                    allOptions.forEach(option => {
                        const optionClientId = option.getAttribute('data-client-id');
                        if (optionClientId && parseInt(optionClientId) === parseInt(clientId)) {
                            const clonedOption = option.cloneNode(true);
                            // Preserva a seleção se for um formulário de edição
                            if (selectedAddressId && parseInt(clonedOption.value) === parseInt(selectedAddressId)) {
                                clonedOption.selected = true;
                            }
                            selectElement.appendChild(clonedOption);
                            hasOptions = true;
                        }
                    });

                    if (!hasOptions) {
                        const noOptionElement = document.createElement('option');
                        noOptionElement.value = '';
                        noOptionElement.textContent = 'Nenhum endereço encontrado para este cliente';
                        noOptionElement.disabled = true;
                        selectElement.appendChild(noOptionElement);
                    }
                }
            }

            // Event listener para o Remetente
            if (shipperSelect) {
                shipperSelect.addEventListener('change', function() {
                    const selectedShipperId = this.value;
                    // Ao mudar o remetente, limpa a seleção anterior do endereço de origem
                    filterAddresses(originAddressSelect, allOriginAddressOptions, selectedShipperId, null);
                });
            }

            // Event listener para o Destinatário
            if (recipientSelect) {
                recipientSelect.addEventListener('change', function() {
                    const selectedRecipientId = this.value;
                    // Ao mudar o destinatário, limpa a seleção anterior do endereço de destino
                    filterAddresses(destinationAddressSelect, allDestinationAddressOptions, selectedRecipientId, null);
                });
            }

            // Chama a filtragem inicial ao carregar a página, se houver valores pré-selecionados
            // Isso é crucial para o modo de edição
            if (shipperSelect && shipperSelect.value) {
                // Passa o ID do endereço de origem já selecionado (se houver) para que ele seja mantido
                filterAddresses(originAddressSelect, allOriginAddressOptions, shipperSelect.value, "${delivery.originAddressId}");
            }
            if (recipientSelect && recipientSelect.value) {
                // Passa o ID do endereço de destino já selecionado (se houver) para que ele seja mantido
                filterAddresses(destinationAddressSelect, allDestinationAddressOptions, recipientSelect.value, "${delivery.destinationAddressId}");
            }
        });
    </script>
</t:header>

<t:footer />
