<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Bem-vindo">
    <!-- Hero principal -->
    <div class="welcome-section hero-transport">
        <div class="hero-content">
            <p class="hero-badge">Soluções em Logística</p>

            <h1 class="hero-title">
                Movendo cargas com a calma de quem sabe o caminho.
            </h1>

            <p class="hero-subtitle">
                Posso parecer lenta, mas entrego com precisão em cada rota.
                Planeje, acompanhe e finalize cada entrega com controle total.
            </p>

            <div class="hero-actions">
                <a href="${pageContext.request.contextPath}/deliveries/"
                   class="btn btn-primary">
                    Começar agora
                </a>
                <a href="${pageContext.request.contextPath}/clients/"
                   class="btn btn-outline">
                    Ver clientes
                </a>
            </div>

            <div class="hero-contact">
                <span>Telefone: (11) 4000-1234</span>
                <span>E-mail: contato@tartarugacometa.com</span>
            </div>
        </div>
    </div>

    <!-- Recursos principais -->
    <div class="features-section">
        <h2>Recursos Principais</h2>

        <div class="features-grid">
            <div class="feature-card">
                <h3>👥 Gestão de Clientes</h3>
                <p>Cadastre e gerencie seus clientes com facilidade, mantendo todas as informações organizadas.</p>
                <a href="${pageContext.request.contextPath}/clients/" class="feature-link">Acessar →</a>
            </div>

            <div class="feature-card">
                <h3>📦 Controle de Produtos</h3>
                <p>Registre produtos com peso, volume e valor declarado para cálculo preciso de fretes.</p>
                <a href="${pageContext.request.contextPath}/products/" class="feature-link">Acessar →</a>
            </div>

            <div class="feature-card">
                <h3>📍 Endereços</h3>
                <p>Gerencie endereços de origem e destino com validação de CEP e dados completos.</p>
                <a href="${pageContext.request.contextPath}/addresses/" class="feature-link">Acessar →</a>
            </div>

            <div class="feature-card">
                <h3>🚚 Rastreamento</h3>
                <p>Acompanhe suas entregas em tempo real com código de rastreamento único.</p>
                <a href="${pageContext.request.contextPath}/deliveries/" class="feature-link">Acessar →</a>
            </div>
        </div>
    </div>

    <!-- Indicadores -->
    <div class="stats-section">
        <div class="stat-item">
            <span class="stat-number">∞</span>
            <span class="stat-label">Entregas Gerenciadas</span>
        </div>
        <div class="stat-item">
            <span class="stat-number">100%</span>
            <span class="stat-label">Controle Total</span>
        </div>
        <div class="stat-item">
            <span class="stat-number">24/7</span>
            <span class="stat-label">Disponibilidade</span>
        </div>
    </div>

</t:header>

<t:footer />
