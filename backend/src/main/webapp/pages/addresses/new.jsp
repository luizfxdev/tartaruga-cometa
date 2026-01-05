<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="${address != null && address.id != null ? 'Editar Endereço' : 'Novo Endereço'}">
    <div class="page-header">
        <h2>${address != null && address.id != null ? 'Editar Endereço' : 'Novo Endereço'}</h2>
    </div>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <form method="POST" action="${pageContext.request.contextPath}/addresses/save" class="form">
        <c:if test="${address != null && address.id != null}">
            <input type="hidden" name="id" value="${address.id}">
        </c:if>

        <%-- Lógica para o campo Cliente --%>
        <c:choose>
            <c:when test="${client != null}">
                <%-- Cliente já definido (vindo de /addresses/new/{clientId} ou edição) --%>
                <input type="hidden" name="clientId" value="${client.id}">
                <div class="form-group">
                    <label for="clientNameDisplay">Cliente</label>
                    <input type="text" id="clientNameDisplay" value="${client.name} (${client.document})" disabled class="form-control-plaintext">
                </div>
            </c:when>
            <c:otherwise>
                <%-- Nenhuma cliente pré-selecionado, exibe o dropdown --%>
                <div class="form-group">
                    <label for="clientSelectForAddress">Cliente *</label>
                    <select id="clientSelectForAddress" name="clientId" required>
                        <option value="">Selecione o cliente</option>
                        <c:forEach var="c" items="${allClients}"> <%-- AQUI: Usando allClients --%>
                            <option value="${c.id}" ${address != null && address.clientId == c.id ? 'selected' : ''}>
                                ${c.name} (${c.document})
                            </option>
                        </c:forEach>
                    </select>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="form-group">
            <label for="addressType">Tipo de Endereço *</label>
            <select id="addressType" name="addressType" required>
                <option value="">Selecione o Tipo</option>
                <c:forEach var="type" items="${addressTypes}">
                    <option value="${type}" ${address != null && address.addressType == type ? 'selected' : ''}>
                        ${type}
                    </option>
                </c:forEach>
            </select>
        </div>

        <div class="form-group">
            <label for="street">Rua *</label>
            <input type="text" id="street" name="street"
                   value="${address != null ? address.street : ''}" required>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="number">Número *</label>
                <input type="text" id="number" name="number"
                       value="${address != null ? address.number : ''}" required>
            </div>
            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement"
                       value="${address != null ? address.complement : ''}">
            </div>
        </div>

        <div class="form-group">
            <label for="neighborhood">Bairro *</label>
            <input type="text" id="neighborhood" name="neighborhood"
                   value="${address != null ? address.neighborhood : ''}" required>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city"
                       value="${address != null ? address.city : ''}" required>
            </div>
            <div class="form-group">
                <label for="state">Estado *</label>
                <input type="text" id="state" name="state"
                       value="${address != null ? address.state : ''}" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="zipCode">CEP *</label>
                <input type="text" id="zipCode" name="zipCode"
                       value="${address != null ? address.zipCode : ''}" required>
            </div>
            <div class="form-group">
                <label for="country">País *</label>
                <input type="text" id="country" name="country"
                       value="${address != null ? address.country : ''}" required>
            </div>
        </div>

        <div class="form-group">
            <label for="reference">Ponto de Referência</label>
            <input type="text" id="reference" name="reference"
                   value="${address != null ? address.reference : ''}">
        </div>

        <div class="form-group form-check">
            <input type="checkbox" id="isMain" name="isMain" value="true"
                   ${address != null && address.isMain ? 'checked' : ''}>
            <label class="form-check-label" for="isMain">
                Endereço Principal
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Salvar</button>
            <c:choose>
                <c:when test="${client != null}">
                    <a href="${pageContext.request.contextPath}/addresses/client/${client.id}" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:when test="${address != null && address.clientId != null}">
                    <a href="${pageContext.request.contextPath}/addresses/client/${address.clientId}" class="btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/addresses/" class="btn btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>

    <%-- Script para redirecionar ao selecionar cliente no dropdown --%>
    <script>
        document.addEventListener('DOMContentLoaded', function() {
            const clientSelect = document.getElementById('clientSelectForAddress');
            if (clientSelect) {
                clientSelect.addEventListener('change', function() {
                    const selectedClientId = this.value;
                    if (selectedClientId) {
                        // Redireciona para a URL /addresses/new/{clientId}
                        window.location.href = '${pageContext.request.contextPath}/addresses/new/' + selectedClientId;
                    }
                });
            }
        });
    </script>
</t:header>

<t:footer />
