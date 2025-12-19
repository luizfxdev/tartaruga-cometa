<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<t:header title="Bem-vindo">
    <main class="site-wrapper">
        <div class="pt-table desktop-768">
            <div class="pt-tablecell page-home relative" style="background-image: url('${pageContext.request.contextPath}/assets/background.jpg'); background-position: center; background-size: cover;">
                <div class="overlay"></div>
                <div class="container">
                    <div class="row">
                        <div class="col-xs-12 col-md-offset-1 col-md-10 col-lg-offset-2 col-lg-8">
                            <div class="page-title home text-center">
                                <span class="heading-page">Tartaruga Cometa</span>
                                <p class="mt20">Posso parecer lenta, mas entrego com precisão em cada rota.<br>Planeje, acompanhe e finalize cada entrega com controle total.</p>
                            </div>
                            <div class="hexagon-menu clear">
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="sobre"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-universal-access"></i></span>
                                            <span class="title">Sobre</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="rastrear"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-bullseye"></i></span>
                                            <span class="title">Rastrear</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="servicos"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-braille"></i></span>
                                            <span class="title">Serviços</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="${pageContext.request.contextPath}/login.jsp" class="hex-content"> <%-- Este link mantém o comportamento original para login --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-id-badge"></i></span>
                                            <span class="title">Colaborador</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="unidades"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-life-ring"></i></span>
                                            <span class="title">Unidades</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="cotacao"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-clipboard"></i></span>
                                            <span class="title">Cotação</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                                <div class="hexagon-item">
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <div class="hex-item"><div></div><div></div><div></div></div>
                                    <a href="#" class="hex-content" data-menu-item="contato"> <%-- Adicionado data-menu-item --%>
                                        <span class="hex-content-inner">
                                            <span class="icon"><i class="fa fa-map-signs"></i></span>
                                            <span class="title">Contato</span>
                                        </span>
                                        <svg viewBox="0 0 173.20508075688772 200" height="200" width="174" version="1.1" xmlns="http://www.w3.org/2000/svg"><path d="M86.60254037844386 0L173.20508075688772 50L173.20508075688772 150L86.60254037844386 200L0 150L0 50Z" fill="#1e2530"></path></svg>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </main>
</t:header>
<t:footer />

<%-- Bloco de script para os alertas --%>
<script>
    document.addEventListener('DOMContentLoaded', function() {
        // Seleciona todos os links com a classe 'hex-content' e o atributo 'data-menu-item'
        const menuItems = document.querySelectorAll('.hex-content[data-menu-item]');

        menuItems.forEach(item => {
            item.addEventListener('click', function(event) {
                // Previne o comportamento padrão do link (navegar para #)
                event.preventDefault();

                // Obtém o nome do item do menu para usar no alerta
                const menuItemName = this.querySelector('.title').textContent;

                // Exibe o alerta
                alert(`O menu "${menuItemName}" está em construção!`);
            });
        });
    });
</script>
