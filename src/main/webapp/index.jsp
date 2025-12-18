<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Bem-vindo">
    <style>
        .stats-section {
            margin-top: 3rem;
            padding: 1.5rem 2rem;
            border-radius: 12px;
            background: linear-gradient(90deg, #3bbf9f 0%, #58c9a9 50%, #73d3b3 100%);
            display: flex;
            justify-content: space-around;
            align-items: center;
            color: #fff;
            text-align: center;
        }

        .stat-item {
            display: flex;
            flex-direction: column;
            gap: 0.25rem;
        }

        .stat-number {
            font-size: 2rem;
            font-weight: 700;
        }

        .stat-label {
            font-size: 0.95rem;
            opacity: 0.9;
        }
    </style>

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
    <div class="stats-section" id="stats-section">
        <div class="stat-item">
            <span class="stat-number">∞</span>
            <span class="stat-label">Entregas Gerenciadas</span>
        </div>
        <div class="stat-item">
            <span class="stat-number counter" data-target="100" data-suffix="%">0%</span>
            <span class="stat-label">Controle Total</span>
        </div>
        <div class="stat-item">
            <span class="stat-number counter" data-target="24" data-suffix="/7">0/7</span>
            <span class="stat-label">Disponibilidade</span>
        </div>
    </div>

    <script>
        // Função de animação individual
        function animateCounter(counter, duration) {
            const target = parseInt(counter.getAttribute("data-target"), 10);
            const suffix = counter.getAttribute("data-suffix") || "";
            const start = 0;
            const startTime = performance.now();

            function update(now) {
                const progress = Math.min((now - startTime) / duration, 1);
                const value = Math.floor(start + (target - start) * progress);
                counter.textContent = value + suffix;

                if (progress < 1) {
                    requestAnimationFrame(update);
                }
            }

            requestAnimationFrame(update);
        }

        document.addEventListener("DOMContentLoaded", function () {
            const statsSection = document.getElementById("stats-section");
            const counters = document.querySelectorAll(".counter");
            let alreadyAnimated = false;

            // IntersectionObserver para disparar ao entrar no viewport [web:30][web:50][web:62]
            const observerOptions = {
                root: null,          // viewport
                threshold: 0.4       // 40% visível já dispara
            };

            const observer = new IntersectionObserver((entries, obs) => {
                entries.forEach(entry => {
                    if (entry.isIntersecting && !alreadyAnimated) {
                        alreadyAnimated = true;

                        counters.forEach(counter => {
                            animateCounter(counter, 1500);
                        });

                        // depois de animar uma vez, não observar mais
                        obs.unobserve(entry.target);
                    }
                });
            }, observerOptions);

            if (statsSection) {
                observer.observe(statsSection);
            }
        });
    </script>

</t:header>

<t:footer />
