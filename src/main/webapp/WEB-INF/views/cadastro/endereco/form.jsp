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

        <c:if test="${clientId != null}">
            <input type="hidden" name="clientId" value="${clientId}">
        </c:if>

        <div class="form-group">
            <label for="addressType">Tipo de Endereço *</label>
            <select id="addressType" name="addressType" required>
                <option value="">Selecione...</option>
                <option value="RESIDENTIAL" ${address != null && address.addressType.name() == 'RESIDENTIAL' ? 'selected' : ''}>Residencial</option>
                <option value="COMMERCIAL" ${address != null && address.addressType.name() == 'COMMERCIAL' ? 'selected' : ''}>Comercial</option>
                <option value="INDUSTRIAL" ${address != null && address.addressType.name() == 'INDUSTRIAL' ? 'selected' : ''}>Industrial</option>
                <option value="RURAL" ${address != null && address.addressType.name() == 'RURAL' ? 'selected' : ''}>Rural</option>
                <option value="OTHER" ${address != null && address.addressType.name() == 'OTHER' ? 'selected' : ''}>Outro</option>
            </select>
        </div>

        <div class="form-group">
            <label for="zipCode">CEP *</label>
            <input type="text" id="zipCode" name="zipCode"
                   value="${address != null ? address.zipCode : ''}"
                   placeholder="00000-000" required maxlength="9">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="street">Logradouro *</label>
                <input type="text" id="street" name="street"
                       value="${address != null ? address.street : ''}"
                       placeholder="Rua, Avenida, etc." required>
            </div>

            <div class="form-group">
                <label for="number">Número *</label>
                <input type="text" id="number" name="number"
                       value="${address != null ? address.number : ''}"
                       placeholder="123" required>
            </div>
        </div>

        <div class="form-group">
            <label for="complement">Complemento</label>
            <input type="text" id="complement" name="complement"
                   value="${address != null && address.complement != null ? address.complement : ''}"
                   placeholder="Apto, Bloco, etc.">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="neighborhood">Bairro *</label>
                <input type="text" id="neighborhood" name="neighborhood"
                       value="${address != null ? address.neighborhood : ''}"
                       placeholder="Nome do bairro" required>
            </div>

            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city"
                       value="${address != null ? address.city : ''}"
                       placeholder="Nome da cidade" required>
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="state">Estado *</label>
                <select id="state" name="state" required>
                    <option value="">Selecione...</option>
                    <option value="AC" ${address != null && address.state == 'AC' ? 'selected' : ''}>Acre</option>
                    <option value="AL" ${address != null && address.state == 'AL' ? 'selected' : ''}>Alagoas</option>
                    <option value="AP" ${address != null && address.state == 'AP' ? 'selected' : ''}>Amapá</option>
                    <option value="AM" ${address != null && address.state == 'AM' ? 'selected' : ''}>Amazonas</option>
                    <option value="BA" ${address != null && address.state == 'BA' ? 'selected' : ''}>Bahia</option>
                    <option value="CE" ${address != null && address.state == 'CE' ? 'selected' : ''}>Ceará</option>
                    <option value="DF" ${address != null && address.state == 'DF' ? 'selected' : ''}>Distrito Federal</option>
                    <option value="ES" ${address != null && address.state == 'ES' ? 'selected' : ''}>Espírito Santo</option>
                    <option value="GO" ${address != null && address.state == 'GO' ? 'selected' : ''}>Goiás</option>
                    <option value="MA" ${address != null && address.state == 'MA' ? 'selected' : ''}>Maranhão</option>
                    <option value="MT" ${address != null && address.state == 'MT' ? 'selected' : ''}>Mato Grosso</option>
                    <option value="MS" ${address != null && address.state == 'MS' ? 'selected' : ''}>Mato Grosso do Sul</option>
                    <option value="MG" ${address != null && address.state == 'MG' ? 'selected' : ''}>Minas Gerais</option>
                    <option value="PA" ${address != null && address.state == 'PA' ? 'selected' : ''}>Pará</option>
                    <option value="PB" ${address != null && address.state == 'PB' ? 'selected' : ''}>Paraíba</option>
                    <option value="PR" ${address != null && address.state == 'PR' ? 'selected' : ''}>Paraná</option>
                    <option value="PE" ${address != null && address.state == 'PE' ? 'selected' : ''}>Pernambuco</option>
                    <option value="PI" ${address != null && address.state == 'PI' ? 'selected' : ''}>Piauí</option>
                    <option value="RJ" ${address != null && address.state == 'RJ' ? 'selected' : ''}>Rio de Janeiro</option>
                    <option value="RN" ${address != null && address.state == 'RN' ? 'selected' : ''}>Rio Grande do Norte</option>
                    <option value="RS" ${address != null && address.state == 'RS' ? 'selected' : ''}>Rio Grande do Sul</option>
                    <option value="RO" ${address != null && address.state == 'RO' ? 'selected' : ''}>Rondônia</option>
                    <option value="RR" ${address != null && address.state == 'RR' ? 'selected' : ''}>Roraima</option>
                    <option value="SC" ${address != null && address.state == 'SC' ? 'selected' : ''}>Santa Catarina</option>
                    <option value="SP" ${address != null && address.state == 'SP' ? 'selected' : ''}>São Paulo</option>
                    <option value="SE" ${address != null && address.state == 'SE' ? 'selected' : ''}>Sergipe</option>
                    <option value="TO" ${address != null && address.state == 'TO' ? 'selected' : ''}>Tocantins</option>
                </select>
            </div>

            <div class="form-group">
                <label for="country">País *</label>
                <input type="text" id="country" name="country"
                       value="${address != null ? address.country : 'Brasil'}"
                       placeholder="Brasil" required>
            </div>
        </div>

        <div class="form-group">
            <label>
                <input type="checkbox" id="isMain" name="isMain" value="true"
                       ${address != null && address.isMain ? 'checked' : ''}>
                Endereço Principal
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="custom-btn btn-success">Salvar</button>
            <c:choose>
                <c:when test="${clientId != null}">
                    <a href="${pageContext.request.contextPath}/clients/view/${clientId}" class="custom-btn btn-secondary">Cancelar</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/addresses/" class="custom-btn btn-secondary">Cancelar</a>
                </c:otherwise>
            </c:choose>
        </div>
    </form>
</t:header>

<t:footer />