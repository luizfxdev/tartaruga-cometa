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
        
        <c:choose>
            <c:when test="${client != null}">
                <input type="hidden" name="clientId" value="${client.id}">
                <div class="form-group">
                    <label>Cliente</label>
                    <input type="text" value="${client.name}" disabled class="form-control-plaintext">
                </div>
            </c:when>
            <c:when test="${address != null && address.clientId != null}">
                <input type="hidden" name="clientId" value="${address.clientId}">
            </c:when>
            <c:otherwise>
                <div class="form-group">
                    <label for="clientId">Cliente *</label>
                    <select id="clientId" name="clientId" required>
                        <option value="">Selecione um cliente...</option>
                        <c:forEach var="c" items="${clients}">
                            <option value="${c.id}">${c.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:otherwise>
        </c:choose>

        <div class="form-group">
            <label for="addressType">Tipo de Endereço *</label>
            <select id="addressType" name="addressType" required>
                <option value="">Selecione...</option>
                <option value="RESIDENCIAL" ${address != null && address.addressType.name() == 'RESIDENCIAL' ? 'selected' : ''}>Residencial</option>
                <option value="COMERCIAL" ${address != null && address.addressType.name() == 'COMERCIAL' ? 'selected' : ''}>Comercial</option>
                <option value="INDUSTRIAL" ${address != null && address.addressType.name() == 'INDUSTRIAL' ? 'selected' : ''}>Industrial</option>
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
                             placeholder="Apto 101, Bloco B">
            </div>
        </div>

        <div class="form-group">
            <label for="neighborhood">Bairro *</label>
            <input type="text" id="neighborhood" name="neighborhood" 
                         value="${address != null && address.neighborhood != null ? address.neighborhood : ''}" 
                         required placeholder="Centro">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city" 
                             value="${address != null && address.city != null ? address.city : ''}" 
                             required placeholder="São Paulo">
            </div>

            <div class="form-group">
                <label for="state">Estado *</label>
                <input type="text" id="state" name="state" 
                             value="${address != null && address.state != null ? address.state : ''}" 
                             required placeholder="SP" maxlength="2">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="zipCode">CEP *</label>
                <input type="text" id="zipCode" name="zipCode" 
                             value="${address != null && address.zipCode != null ? address.zipCode : ''}" 
                             required placeholder="00000-000" maxlength="9">
            </div>

            <div class="form-group">
                <label for="country">País *</label>
                <input type="text" id="country" name="country" 
                             value="${address != null && address.country != null ? address.country : 'Brasil'}" 
                             required placeholder="Brasil">
            </div>
        </div>

        <div class="form-group">
            <label for="reference">Ponto de Referência</label>
            <input type="text" id="reference" name="reference" 
                         value="${address != null && address.reference != null ? address.reference : ''}" 
                         placeholder="Próximo à padaria">
        </div>

        <div class="form-group checkbox-group">
            <input type="checkbox" id="isMain" name="isMain" value="true" 
                   ${address != null && address.isMain ? 'checked' : ''}>
            <label for="isMain">Endereço Principal</label>
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
</t:header>

<t:footer />