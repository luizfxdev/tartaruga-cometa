<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:header title="Erro">
    <div class="error-container">
        <h2>❌ Erro ao processar requisição</h2>
        <p>${requestScope.error != null ? requestScope.error : 'Ocorreu um erro inesperado. Por favor, tente novamente.'}</p>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar à Home</a>
            <a href="javascript:history.back()" class="btn btn-secondary">Voltar</a>
        </div>
    </div>
</t:header>

<t:footer />
