<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Clientes">
    <div class="page-header">
        <h2>Clientes</h2>
        <a href="${pageContext.request.contextPath}/clients/new" class="btn btn-primary">+ Novo Cliente</a>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/clients/search">
            <input type="text" name="q" placeholder="Buscar cliente por nome..." required>
            <button type="submit" class="btn btn-primary">Buscar</button>
        </form>
    </div>

    <c:choose>
        <c:when test="${not empty clients}">
            <div class="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Tipo</th>
                            <th>Documento</th>
                            <th>Nome</th>
                            <th>Email</th>
                            <th>Telefone</th>
                            <th>Cadastro</th>
                            <th>Última Atualização</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="client" items="${clients}">
                            <tr>
                                <td>${client.id}</td>
                                <td>${client.personType.value}</td>
                                <td>${client.document}</td>
                                <td>${client.name}</td>
                                <td>${client.email}</td>
                                <td>${client.phone}</td>
                                <td>${client.formattedCreatedAt}</td>
                                <td>${client.formattedUpdatedAt != null ? client.formattedUpdatedAt : '-'}</td>
                                <td>
                                    <div class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="btn btn-warning btn-sm">Editar</a>
                                        <form method="POST" action="${pageContext.request.contextPath}/clients/delete/${client.id}" style="display:inline;">
                                            <button type="submit" class="btn btn-danger btn-sm" onclick="return confirm('Tem certeza?')">Deletar</button>
                                        </form>
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:when>
        <c:otherwise>
            <div class="alert alert-info">
                Nenhum cliente encontrado. <a href="${pageContext.request.contextPath}/clients/new">Criar novo cliente</a>
            </div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />
