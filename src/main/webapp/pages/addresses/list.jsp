<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Endereços">
    <div class="page-header">
        <h2>Endereços</h2>
        <a href="${pageContext.request.contextPath}/addresses/new" class="btn btn-primary">+ Novo Endereço</a>
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

    <c:choose>
        <c:when test="${not empty addresses}">
            <div class="table-container">
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
                        <c:forEach var="address" items="${addresses}">
                            <tr>
                                <td>${address.id}</td>
                                <td>${address.addressType.label}</td>
                                <td>${address.street}</td>
                                <td>${address.number}</td>
                                <td>${address.neighborhood}</td>
                                <td>${address.city}</td>
                                <td>${address.state}</td>
                                <td>${address.zipCode}</td>
                                <td>
                                    <c:if test="${address.isPrincipal}">
                                        <span class="badge badge-success">Sim</span>
                                    </c:if>
                                    <c:if test="${not address.isPrincipal}">
                                        <span class="badge badge-secondary">Não</span>
                                    </c:if>
                                </td>
                                <td>
                                    <div class="actions">
                                        <a href="${pageContext.request.contextPath}/addresses/edit/${address.id}" class="btn btn-warning btn-sm">Editar</a>
                                        <c:if test="${not address.isPrincipal}">
                                            <form method="POST" action="${pageContext.request.contextPath}/addresses/set-principal/${address.id}" style="display:inline;">
                                                <button type="submit" class="btn btn-info btn-sm">Principal</button>
                                            </form>
                                        </c:if>
                                        <form method="POST" action="${pageContext.request.contextPath}/addresses/delete/${address.id}" style="display:inline;">
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
                Nenhum endereço encontrado. <a href="${pageContext.request.contextPath}/addresses/new">Criar novo endereço</a>
            </div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />
