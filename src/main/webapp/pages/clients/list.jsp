<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>

<t:header title="Clientes">
    <div class="page-header">
        <h2>Clientes</h2>
        <a href="${pageContext.request.contextPath}/clients/new" class="btn btn-success">+ Novo Cliente</a>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/clients/search">
            <input type="text" name="q" placeholder="Buscar cliente por nome..." required>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>

    <%
        List<Client> clients = (List<Client>) request.getAttribute("clients");
        if (clients != null && !clients.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Tipo</th>
                    <th>Documento</th>
                    <th>Email</th>
                    <th>Telefone</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Client client : clients) {
                %>
                    <tr>
                        <td><%= client.getId() %></td>
                        <td><%= client.getName() %></td>
                        <td><%= client.getPersonType().getLabel() %></td>
                        <td><%= client.getDocument() %></td>
                        <td><%= client.getEmail() != null ? client.getEmail() : "-" %></td>
                        <td><%= client.getPhone() != null ? client.getPhone() : "-" %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/clients/view/<%= client.getId() %>" class="btn btn-sm btn-info">Ver</a>
                            <a href="${pageContext.request.contextPath}/clients/edit/<%= client.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                            <form method="POST" action="${pageContext.request.contextPath}/clients/delete/<%= client.getId() %>" style="display:inline;">
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
            Nenhum cliente encontrado. <a href="${pageContext.request.contextPath}/clients/new">Criar novo cliente</a>
        </div>
    <%
        }
    %>
</t:header>

<t:footer />
