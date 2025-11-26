<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.tartarugacometasystem.model.Address" %>
<%@ page import="com.tartarugacometasystem.model.AddressType" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>

<t:header title="Novo Endereço">
    <%
        Address address = (Address) request.getAttribute("address");
        AddressType[] addressTypes = (AddressType[]) request.getAttribute("addressTypes");
        Client client = (Client) request.getAttribute("client");
        String title = address != null && address.getId() != null ? "Editar Endereço" : "Novo Endereço";
    %>

    <div class="page-header">
        <h2><%= title %></h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/addresses/save" class="form">
        <% if (address != null && address.getId() != null) { %>
            <input type="hidden" name="id" value="<%= address.getId() %>">
            <input type="hidden" name="clientId" value="<%= address.getClientId() %>">
        <% } else if (client != null) { %>
            <input type="hidden" name="clientId" value="<%= client.getId() %>">
        <% } %>

        <div class="form-group">
            <label for="addressType">Tipo de Endereço *</label>
            <select id="addressType" name="addressType" required>
                <option value="">Selecione...</option>
                <%
                    for (AddressType at : addressTypes) {
                %>
                    <option value="<%= at.getValue() %>" 
                        <%= address != null && address.getAddressType() == at ? "selected" : "" %>>
                        <%= at.getLabel() %>
                    </option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label for="street">Logradouro *</label>
            <input type="text" id="street" name="street" 
                value="<%= address != null && address.getStreet() != null ? address.getStreet() : "" %>" 
                required placeholder="Rua, Avenida, etc">
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="number">Número *</label>
                <input type="text" id="number" name="number" 
                    value="<%= address != null && address.getNumber() != null ? address.getNumber() : "" %>" 
                    required placeholder="123">
            </div>

            <div class="form-group">
                <label for="complement">Complemento</label>
                <input type="text" id="complement" name="complement" 
                    value="<%= address != null && address.getComplement() != null ? address.getComplement() : "" %>" 
                    placeholder="Apto, Sala, etc">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="neighborhood">Bairro *</label>
                <input type="text" id="neighborhood" name="neighborhood" 
                    value="<%= address != null && address.getNeighborhood() != null ? address.getNeighborhood() : "" %>" 
                    required placeholder="Bairro">
            </div>

            <div class="form-group">
                <label for="city">Cidade *</label>
                <input type="text" id="city" name="city" 
                    value="<%= address != null && address.getCity() != null ? address.getCity() : "" %>" 
                    required placeholder="Cidade">
            </div>
        </div>

        <div class="form-row">
            <div class="form-group">
                <label for="state">Estado (UF) *</label>
                <input type="text" id="state" name="state" maxlength="2" 
                    value="<%= address != null && address.getState() != null ? address.getState() : "" %>" 
                    required placeholder="SP">
            </div>

            <div class="form-group">
                <label for="zipCode">CEP *</label>
                <input type="text" id="zipCode" name="zipCode" 
                    value="<%= address != null && address.getZipCode() != null ? address.getZipCode() : "" %>" 
                    required placeholder="12345-678">
            </div>
        </div>

        <div class="form-group">
            <label for="reference">Referência</label>
            <input type="text" id="reference" name="reference" 
                value="<%= address != null && address.getReference() != null ? address.getReference() : "" %>" 
                placeholder="Próximo a...">
        </div>

        <div class="form-group">
            <label for="isPrincipal">
                <input type="checkbox" id="isPrincipal" name="isPrincipal" value="true" 
                    <%= address != null && address.getIsPrincipal() ? "checked" : "" %>>
                Endereço Principal
            </label>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar</button>
            <a href="${pageContext.request.contextPath}/addresses/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
