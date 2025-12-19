<%@ tag description="Navigation Menu" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="breadcrumb-nav">
    <a href="${pageContext.request.contextPath}/" class="breadcrumb-link">Início</a>
    <span class="breadcrumb-separator">›</span>
    <a href="${pageContext.request.contextPath}/clients/" class="breadcrumb-link">Clientes</a>
    <span class="breadcrumb-separator">›</span>
    <a href="${pageContext.request.contextPath}/addresses/" class="breadcrumb-link">Endereços</a>
    <span class="breadcrumb-separator">›</span>
    <a href="${pageContext.request.contextPath}/products/" class="breadcrumb-link">Produtos</a>
    <span class="breadcrumb-separator">›</span>
    <a href="${pageContext.request.contextPath}/deliveries/" class="breadcrumb-link">Entregas</a>
</nav>

<style>
.breadcrumb-nav {
    background: #f8f9fa;
    padding: 12px 20px;
    border-radius: 8px;
    margin-bottom: 24px;
    display: flex;
    align-items: center;
    flex-wrap: wrap;
    gap: 8px;
}

.breadcrumb-link {
    color: #2c5f2d;
    text-decoration: none;
    font-weight: 500;
    padding: 4px 8px;
    border-radius: 4px;
    transition: all 0.2s;
}

.breadcrumb-link:hover {
    background: #e9ecef;
    color: #1a3a1b;
}

.breadcrumb-separator {
    color: #6c757d;
    user-select: none;
}
</style>