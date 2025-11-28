<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Detalhes Completos do Cliente">
    <div class="page-header">
        <h2>Detalhes Completos - ${client.name}</h2>
        <div>
            <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/clients/" class="btn btn-secondary">Voltar</a>
        </div>
    </div>

    <div class="details-card">
        <h3>Informações Pessoais</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span>${client.id}</span>
        </div>

        <div class="detail-row">
            <label>Tipo de Pessoa:</label>
            <span>
                <c:if test="${client.personType.value == 'FISICA'}">
                    <span class="badge badge-info">Pessoa Física</span>
                </c:if>
                <c:if test="${client.personType.value == 'JURIDICA'}">
                    <span class="badge badge-info">Pessoa Jurídica</span>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Documento:</label>
            <span><strong>${client.document}</strong></span>
        </div>

        <div class="detail-row">
            <label>Nome/Razão Social:</label>
            <span>${client.name}</span>
        </div>
    </div>

    <div class="details-card">
        <h3>Contato</h3>

        <div class="detail-row">
            <label>Email:</label>
            <span>${client.email != null ? client.email : '-'}</span>
        </div>

        <div class="detail-row">
            <label>Telefone:</label>
            <span>${client.phone != null ? client.phone : '-'}</span>
        </div>
    </div>

    <div class="details-card">
        <h3>Informações do Sistema</h3>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span>${client.formattedCreatedAt}</span>
        </div>

        <div class="detail-row">
            <label>Última Atualização:</label>
            <span>
                <c:if test="${client.formattedUpdatedAt != null}">
                    ${client.formattedUpdatedAt}
                </c:if>
                <c:if test="${client.formattedUpdatedAt == null}">
                    Nunca atualizado
                </c:if>
            </span>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/clients/" class="btn btn-secondary">Voltar à Lista</a>
    </div>
</t:header>

<t:footer />
