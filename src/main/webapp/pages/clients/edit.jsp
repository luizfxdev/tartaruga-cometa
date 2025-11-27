<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>
<%@ page import="com.tartarugacometasystem.model.PersonType" %>

<t:header title="Editar Cliente">
    <%
        Client client = (Client) request.getAttribute("client");
        PersonType[] personTypes = (PersonType[]) request.getAttribute("personTypes");
    %>

    <div class="page-header">
        <h2>Editar Cliente</h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/clients/save" class="form" onsubmit="return validarFormularioCliente(this)">
        <input type="hidden" name="id" value="<%= client.getId() %>">

        <div class="form-group">
            <label for="personType">Tipo de Pessoa *</label>
            <select id="personType" name="personType" required onchange="updateDocumentLabel()">
                <%
                    for (PersonType pt : personTypes) {
                %>
                    <option value="<%= pt.getValue() %>" 
                        <%= client.getPersonType() == pt ? "selected" : "" %>>
                        <%= pt.getLabel() %>
                    </option>
                <%
                    }
                %>
            </select>
        </div>

        <div class="form-group">
            <label for="document" id="documentLabel">CPF/CNPJ *</label>
            <input type="text" id="document" name="document" 
                value="<%= client.getDocument() %>" 
                required placeholder="000.000.000-00">
        </div>

        <div class="form-group">
            <label for="name">Nome Completo/Razão Social *</label>
            <input type="text" id="name" name="name" 
                value="<%= client.getName() %>" 
                required placeholder="Nome completo">
        </div>

        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" 
                value="<%= client.getEmail() != null ? client.getEmail() : "" %>" 
                placeholder="email@example.com">
        </div>

        <div class="form-group">
            <label for="phone">Telefone</label>
            <input type="text" id="phone" name="phone" 
                value="<%= client.getPhone() != null ? client.getPhone() : "" %>" 
                placeholder="(11) 99999-9999">
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar Alterações</button>
            <a href="${pageContext.request.contextPath}/clients/view/<%= client.getId() %>" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>

    <script>
        function updateDocumentLabel() {
            const personType = document.getElementById('personType').value;
            const label = document.getElementById('documentLabel');
            if (personType === 'PF') {
                label.textContent = 'CPF *';
            } else if (personType === 'PJ') {
                label.textContent = 'CNPJ *';
            }
        }
    </script>
</t:header>

<t:footer />
