<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Erro">
    <div class="error-container">
        <h2>Oops! Algo deu errado</h2>
        <c:if test="${request.getAttribute('error') != null}">
            <div class="alert alert-danger">
                <c:out value="${request.getAttribute('error')}" />
            </div>
        </c:if>
        <c:if test="${request.getAttribute('jakarta.servlet.error.message') != null}">
            <div class="alert alert-danger">
                <c:out value="${request.getAttribute('jakarta.servlet.error.message')}" />
            </div>
        </c:if>
        <div class="error-actions">
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">Voltar ao Home</a>
            <a href="javascript:history.back()" class="btn btn-secondary">Voltar</a>
        </div>
    </div>
</t:header>
<t:footer />