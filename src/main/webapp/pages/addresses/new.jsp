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
            <input type="hidden" name="clientId" value="${address.clientId}">
        </c:if>
        <c:if test="${client != null}">
            <input type="hidden" name="clientId" value="${client.id}">
        </c:if>

        <div class="form-group">
            <label for="addressType">Tipo de Endereço *</label>
            <select id="addressType" name="addressType" required>
                <option value="">Selecione...</option>
                <option value="RESIDENCIAL" ${address != null && address.addressType.value == 'RESIDENCIAL' ? 'selected' : ''}>Residencial</option>
                <option value="COMERCIAL" ${address != null && address.addressType.value == 'COMERCIAL' ? 'selected' : ''}>Comercial</option>
                <option value="INDUSTRIAL" ${address != null && address.addressType.value == 'INDUSTRIAL' ? 'selected' : ''}>Industrial</option>
            </select>
        </div>

        <div class="form-group">
            <label for="street">Logradouro *</label>
            <input type="text" id="street" name="street" 
                   value="${address != null && address.street != null ? address.street : ''}" 
                   required placeholder="Rua, Avenida, etc">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="number">Número *</label>
                <input type="text" id="number" name="number" 
                       value="${address != null && address.number != null ? address.number : ''}" 
                       required placeholder="123">
            </div>

            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement" 
                       value="${address != null && address.complement != null ? address.complement : ''}" 
                       placeholder="Apto, Sala, etc">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="neighborhood">Bairro *</label>
                <input type="text" id="neighborhood" name="neighborhood" 
                       value="${address != null && address.neighborhood != null ? address.neighborhood : ''}" 
                       required placeholder="Bairro">
            </div>

            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city" 
                       value="${address != null && address.city != null ? address.city : ''}" 
                       required placeholder="Cidade">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="state">Estado (UF) *</label>
                <input type="text" id="state" name="state" maxlength="2"
                       value="${address != null && address.state != null ? address.state : ''}" 
                       required placeholder="SP">
            </div>

            <div class="form-group">
                <label for="zipCode">CEP *</label>
                <input type="text" id="zipCode" name="zipCode" 
                       value="${address != null && address.zipCode != null ? address.zipCode : ''}" 
                       required placeholder="12345-678">
            </div>
        </div>

        <div class="form-group">
            <label for="reference">Referência</label>
            <input type="text" id="reference" name="reference" 
                   value="${address != null && address.reference != null ? address.reference : ''}" 
                   placeholder="Próximo a...">
        </div>

        <div class="form-group">
            <label for="isPrincipal">
                <input type="checkbox" id="isPrincipal" name="isPrincipal" value="true" 
                       ${address != null && address.isPrincipal ? 'checked' : ''}>
                Endereço Principal
            </label>
        </div>

        <div class

