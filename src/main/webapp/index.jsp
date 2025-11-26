<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:header title="Home">
    <div class="home-container">
        <div class="hero">
            <h2>Bem-vindo ao Sistema Tartaruga Cometa</h2>
            <p>Sistema de Gerenciamento de Entregas para Transportadoras</p>
        </div>

        <div class="dashboard">
            <div class="card">
                <h3>📦 Clientes</h3>
                <p>Gerencie seus clientes e suas informações</p>
                <a href="${pageContext.request.contextPath}/clients/" class="btn btn-primary">Acessar</a>
            </div>

            <div class="card">
                <h3>🛍️ Produtos</h3>
                <p>Cadastre e controle seus produtos</p>
                <a href="${pageContext.request.contextPath}/products/" class="btn btn-primary">Acessar</a>
            </div>

            <div class="card">
                <h3>📍 Endereços</h3>
                <p>Gerencie endereços de origem e destino</p>
                <a href="${pageContext.request.contextPath}/addresses/" class="btn btn-primary">Acessar</a>
            </div>

            <div class="card">
                <h3>🚚 Entregas</h3>
                <p>Controle todas as suas entregas</p>
                <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-primary">Acessar</a>
            </div>
        </div>
    </div>
</t:header>

<t:footer />
