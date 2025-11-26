<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Address" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>

<t:header title="Endereços">
    <%
        Client client = (Client) request.getAttribute("client");
        List<Address> addresses = (List<Address>) request.getAttribute("addresses");
    %>

    <div class="page-header">
        <h2>
            Endereços
            <% if (client != null) { %>
                de <%= client.getName() %>
            <% } %>
        </h2>
        <% if (client != null) { %>
            <a href="${pageContext.request.contextPath}/addresses/new?clientId=<%= client.getId() %>" class="btn btn-success">+ Novo Endereço</a>
        <% } %>
    </div>

    <%
        if (addresses != null && !addresses.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tipo</th>
                    <th>Logradouro</th>
                    <th>Número</th>
                    <th>Bairro</th>
                    <th>Cidade</th>
                    <th>Estado</th>
                    <th>CEP</th>
                    <th>Principal</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Address address : addresses) {
                %>
                    <tr>
                        <td><%= address.getId() %></td>
                        <td><%= address.getAddressType().getLabel() %></td>
                        <td><%= address.getStreet() %></td>
                        <td><%= address.getNumber() %></td>
                        <td><%= address.getNeighborhood() %></td>
                        <td><%= address.getCity() %></td>
                        <td><%= address.getState() %></td>
                        <td><%= address.getZipCode() %></td>
                        <td>
                            <% if (address.getIsPrincipal()) { %>
                                <span class="badge badge-success">Sim</span>
                            <% } else { %>
                                <span class="badge badge-secondary">Não</span>
                            <% } %>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/addresses/edit/<%= address.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                            <% if (!address.getIsPrincipal()) { %>
                                <form method="POST" action="${pageContext.request.contextPath}/addresses/set-principal/<%= address.getId() %>" style="display:inline;">
                                    <button type="submit" class="btn btn-sm btn-info">Principal</button>
                                </form>
                            <% } %>
                            <form method="POST" action="${pageContext.request.contextPath}/addresses/delete/<%= address.getId() %>" style="display:inline;">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Tem certeza?')">Deletar</button>
                            </form>
                        </td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        } else {
    %>
        <div class="alert alert-info">
            Nenhum endereço encontrado.
            <% if (client != null) { %>
                <a href="${pageContext.request.contextPath}/addresses/new?clientId=<%= client.getId() %>">Criar novo endereço</a>
            <% } %>
        </div>
    <%
        }
    %>
</t:header>

<t:footer />
