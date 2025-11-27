<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<t:header title="Relatório de Status">
    <div class="page-header">
        <h2>Relatório de Status de Entregas</h2>
    </div>

    <div class="dashboard">
        <div class="card">
            <h3>📊 Resumo de Status</h3>
            <p>Visualize a distribuição de
        </div>

        <div class="card">
            <h3>⏳ Pendentes</h3>
            <p class="status-count">${statusCounts.PENDENTE != null ? statusCounts.PENDENTE : 0}</p>
            <span class="badge badge-warning">Aguardando processamento</span>
        </div>

        <div class="card">
            <h3>🚚 Em Trânsito</h3>
            <p class="status-count">${statusCounts.EM_TRANSITO != null ? statusCounts.EM_TRANSITO : 0}</p>
            <span class="badge badge-info">Em rota</span>
        </div>

        <div class="card">
            <h3>✅ Entregues</h3>
            <p class="status-count">${statusCounts.ENTREGUE != null ? statusCounts.ENTREGUE : 0}</p>
            <span class="badge badge-success">Concluídas</span>
        </div>

        <div class="card">
            <h3>❌ Não Realizadas</h3>
            <p class="status-count">${statusCounts.NAO_REALIZADA != null ? statusCounts.NAO_REALIZADA : 0}</p>
            <span class="badge badge-secondary">Problemas na entrega</span>
        </div>

        <div class="card">
            <h3>🚫 Canceladas</h3>
            <p class="status-count">${statusCounts.CANCELADA != null ? statusCounts.CANCELADA : 0}</p>
            <span class="badge badge-danger">Canceladas</span>
        </div>
    </div>

    <div class="detailsd" style="margin-top: 2rem;">
        <h3>Estatísticas Gerais</h3>

        <div class="detail-row">
            <label>Total de Entregas:</label>
            <span><strong>${totalDeliveries != null ? totalDeliveries : 0}</strong></span>
        </div>

        <div class="detail-row">
            <label>Taxa de Sucesso:</label>
            <span>
                <c:if test="${totalDeliveries > 0}">
                    <fmt:formatNumber value="${(statusCounts.ENTREGUE / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                </c:if>
                <c:if test="${totalDeliveries == 0}">
                    0%
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Valor Total Transportado:</label>
            <span>
                <c:if test="${totalValue != null}">
                    <fmt:formatNumber value="${totalValue}" type="currency" currencySymbol="R$ "/>
                </c:if>
                <c:if test="${totalValue == null}">
                    R$ 0,00
                </c:if>
            </span>
        </div>
    </div>

    <div class="form-actions" style="margin-top: 2rem;">
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
