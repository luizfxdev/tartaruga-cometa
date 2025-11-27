<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Product" %>

<t:header title="Produtos">
    <div class="page-header">
        <h2>Produtos</h2>
        <a href="${pageContext.request.contextPath}/products/new" class="btn btn-success">+ Novo Produto</a>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/products/search">
            <input type="text" name="q" placeholder="Buscar produto por nome..." required>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>

    <%
        List<Product> products = (List<Product>) request.getAttribute("products");
        if (products != null && !products.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Categoria</th>
                    <th>Peso (kg)</th>
                    <th>Volume (m³)</th>
                    <th>Valor Declarado</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Product product : products) {
                %>
                    <tr>
                        <td><%= product.getId() %></td>
                        <td><%= product.getName() %></td>
                        <td><%= product.getCategory() != null ? product.getCategory() : "-" %></td>
                        <td><%= product.getWeightKg() %></td>
                        <td><%= product.getVolumeM3() %></td>
                        <td>R$ <%= String.format("%.2f", product.getDeclaredValue()) %></td>
                        <td>
                            <span class="badge <%= product.getActive() ? "badge-success" : "badge-danger" %>">
                                <%= product.getActive() ? "Ativo" : "Inativo" %>
                            </span>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/products/view/<%= product.getId() %>" class="btn btn-sm btn-info">Ver</a>
                            <a href="${pageContext.request.contextPath}/products/edit/<%= product.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                            <form method="POST" action="${pageContext.request.contextPath}/products/delete/<%= product.getId() %>" style="display:inline;">
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
            Nenhum produto encontrado. <a href="${pageContext.request.contextPath}/products/new">Criar novo produto</a>
        </div>
    <%
        }
    %>
</t:header>

<t:footer />
