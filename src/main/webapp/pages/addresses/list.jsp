<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${client != null}">
        <c:set var="pageTitle" value="Endereços do Cliente ${client.name}" />
    </c:when>
    <c:otherwise>
        <c:set var="pageTitle" value="Endereços" />
    </c:otherwise>
</c:choose>

<t:header title="${pageTitle}">
    <t:breadcrumb />

    <div class="page-header">
        <h2>${pageTitle}</h2>
        <div class="btn-group">
            <c:choose>
                <c:when test="${client != null}">
                    <a href="${pageContext.request.contextPath}/addresses/new/${client.id}" class="custom-btn btn-primary">+ Novo Endereço</a>
                    <a href="${pageContext.request.contextPath}/clients/view/${client.id}" class="custom-btn btn-secondary">Voltar ao Cliente</a>
                </c:when>
                <c:when test="${clientId != null}">
                    <a href="${pageContext.request.contextPath}/addresses/new/${clientId}" class="custom-btn btn-primary">+ Novo Endereço</a>
                    <a href="${pageContext.request.contextPath}/clients/view/${clientId}" class="custom-btn btn-secondary">Voltar ao Cliente</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/addresses/new" class="custom-btn btn-primary">+ Novo Endereço</a>
                </c:otherwise>
            </c:choose>
        </div>
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
                            <th>Cidade/Estado</th>
                            <th>CEP</th>
                            <th>Principal</th>
                            <th>Ações</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="address" items="${addresses}">
                            <tr>
                                <td>${address.id}</td>
                                <td>${address.addressType}</td>
                                <td>${address.street}</td>
                                <td>${address.number}</td>
                                <td>${address.neighborhood}</td>
                                <td>${address.city}/${address.state}</td>
                                <td>${address.zipCode}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${address.isMain}">
                                            <span class="badge badge-success">Sim</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-secondary">Não</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                    <div class="btn-group">
                                        <a href="${pageContext.request.contextPath}/addresses/edit/${address.id}" class="custom-btn btn-warning btn-sm">Editar</a>
                                        <c:if test="${not address.isMain}">
                                            <a href="${pageContext.request.contextPath}/addresses/set-principal/${address.id}" 
                                               class="custom-btn btn-info btn-sm"
                                               onclick="return confirm('Definir como endereço principal?')">
                                                Principal
                                            </a>
                                        </c:if>
                                        <form method="POST" action="${pageContext.request.contextPath}/addresses/delete/${address.id}" style="display:inline;">
                                            <button type="submit" class="custom-btn btn-danger btn-sm" onclick="return confirm('Tem certeza que deseja deletar este endereço?')">Deletar</button>
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
                Nenhum endereço encontrado.
                <c:choose>
                    <c:when test="${client != null}">
                        <a href="${pageContext.request.contextPath}/addresses/new/${client.id}" style="color: var(--primary); font-weight: 600;">Criar novo endereço</a>
                    </c:when>
                    <c:when test="${clientId != null}">
                        <a href="${pageContext.request.contextPath}/addresses/new/${clientId}" style="color: var(--primary); font-weight: 600;">Criar novo endereço</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/addresses/new" style="color: var(--primary); font-weight: 600;">Criar novo endereço</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:otherwise>
    </c:choose>
</t:header>

<t:footer />