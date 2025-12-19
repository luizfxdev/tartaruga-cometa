<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Detalhes do Cliente">
    <div class="page-header">
        <h2>Detalhes do Cliente</h2>
        <div class="btn-group">
            <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="custom-btn btn-warning">Editar</a>
            <a href="${pageContext.request.contextPath}/clients/" class="custom-btn btn-secondary">Voltar</a>
        </div>
    </div>

    <div class="details-container">
        <div class="details-card">
            <h3>${client.name}</h3>
            
            <div class="details-grid">
                <div class="detail-item">
                    <label>ID</label>
                    <span>${client.id}</span>
                </div>

                <div class="detail-item">
                    <label>Tipo de Pessoa</label>
                    <span class="badge badge-info">${client.formattedPersonType}</span>
                </div>

                <div class="detail-item">
                    <label>Documento</label>
                    <span>${client.document}</span>
                </div>

                <div class="detail-item">
                    <label>Email</label>
                    <span>${client.email != null ? client.email : '-'}</span>
                </div>

                <div class="detail-item">
                    <label>Telefone</label>
                    <span>${client.phone != null ? client.phone : '-'}</span>
                </div>

                <div class="detail-item">
                    <label>Data de Criação</label>
                    <span>${client.formattedCreatedAt}</span>
                </div>

                <div class="detail-item">
                    <label>Última Atualização</label>
                    <span>${client.formattedUpdatedAt != null ? client.formattedUpdatedAt : 'Nunca atualizado'}</span>
                </div>
            </div>
        </div>
    </div>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/clients/edit/${client.id}" class="custom-btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/clients/" class="custom-btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />