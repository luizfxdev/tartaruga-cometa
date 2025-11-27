<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>

<t:header title="Home">
    <main class="container">
        <div class="home-container">
            <div class="hero">
                <h2>🐢 Bem-vindo ao Sistema Tartaruga Cometa</h2>
                <p class="hero-subtitle">Sistema de Gerenciamento de Entregas para Transportadoras</p>
                <p class="hero-description">
                    Gerencie clientes, produtos, endereços e entregas de forma eficiente e organizada. 
                    Use o menu acima para navegar pelas funcionalidades do sistema.
                </p>
            </div>

            <div class="features">
                <div class="feature-card">
                    <div class="feature-icon">👥</div>
                    <h3>Clientes</h3>
                    <p>Cadastre e gerencie todos os clientes da transportadora.</p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">📦</div>
                    <h3>Produtos</h3>
                    <p>Controle de produtos transportados e itens associados às entregas.</p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">📍</div>
                    <h3>Endereços</h3>
                    <p>Organize endereços de retirada e entrega de forma centralizada.</p>
                </div>

                <div class="feature-card">
                    <div class="feature-icon">🚚</div>
                    <h3>Entregas</h3>
                    <p>Acompanhe o status de todas as entregas realizadas.</p>
                </div>
            </div>
        </div>
    </main>
</t:header>

<t:footer />
