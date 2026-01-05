<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<t:header title="Relatório de Status">
    <div class="page-header">
        <h2>📈 Relatório de Status de Entregas</h2>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            ${sessionScope.success}
        </div>
        <c:remove var="success" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.error}">
        <div class="alert alert-danger">
            ${sessionScope.error}
        </div>
        <c:remove var="error" scope="session"/>
    </c:if>

    <div class="dashboard">
        <div class="card">
            <div class="feature-icon">⏳</div>
            <h3>Pendentes</h3>
            <p style="font-size: 2rem; font-weight: bold; color: #ff9800; margin: 1rem 0;">
                ${statusCounts.PENDENTE != null ? statusCounts.PENDENTE : 0}
            </p>
            <p>Aguardando processamento</p>
        </div>

        <div class="card">
            <div class="feature-icon">🚚</div>
            <h3>Em Trânsito</h3>
            <p style="font-size: 2rem; font-weight: bold; color: #2196f3; margin: 1rem 0;">
                ${statusCounts.EM_TRANSITO != null ? statusCounts.EM_TRANSITO : 0}
            </p>
            <p>Em rota de entrega</p>
        </div>

        <div class="card">
            <div class="feature-icon">✅</div>
            <h3>Entregues</h3>
            <p style="font-size: 2rem; font-weight: bold; color: #4caf50; margin: 1rem 0;">
                ${statusCounts.ENTREGUE != null ? statusCounts.ENTREGUE : 0}
            </p>
            <p>Entregas concluídas</p>
        </div>

        <div class="card">
            <div class="feature-icon">❌</div>
            <h3>Não Realizadas</h3>
            <p style="font-size: 2rem; font-weight: bold; color: #9e9e9e; margin: 1rem 0;">
                ${statusCounts.NAO_REALIZADA != null ? statusCounts.NAO_REALIZADA : 0}
            </p>emas na entrega</p>
        </div>

        <div class="card">
            <div class="feature-icon">🚫</div>
            <h3>Canceladas</h3>
            <p style="font-size: 2rem; font-weight: bold; color: #f44336; margin: 1rem 0;">
                ${statusCounts.CANCELADA != null ? statusCounts.CANCELADA : 0}
            </p>
            <p>Entregas canceladas</p>
        </div>
    </div>

    <div class="details-card" style="margin-top: 2rem;">
        <h3>📊 Estatísticas Gerais</h3>

        <div class="detail-row">
            <label>Total de Entregas:</label>
            <span><strong>${totalDeliveries != null ? totalDeliveries : 0}</strong></span>
        </div>

        <div class="detail-row">
            <label>Taxa de Sucesso (Entregues):</label>
            <span>
                <c:if test="${totalDeliveries > 0}">
                    <strong><fmt:formatNumber value="${(statusCounts.ENTREGUE / totalDeliveries) * 100}" maxFractionDigits="2"/>%</strong>
                </c:if>
                <c:if test="${totalDeliveries == 0}">
                    <strong>0%</strong>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Taxa de Problemas (Não Realizadas):</label>
            <span>
                <c:if test="${totalDeliveries > 0}">
                    <strong><fmt:formatNumber value="${(statusCounts.NAO_REALIZADA / totalDeliveries) * 100}" maxFractionDigits="2"/>%</strong>
                </c:if>
                <c:if test="${totalDeliveries == 0}">
                    <strong>0%</strong>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Taxa de Cancelamento:</label>
            <span>
                <c:if test="${totalDeliveries > 0}">
                    <strong><fmt:formatNumber value="${(statusCounts.CANCELADA / totalDeliveries) * 100}" maxFractionDigits="2"/>%</strong>
                </c:if>
                <c:if test="${totalDeliveries == 0}">
                    <strong>0%</strong>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Valor Total Transportado:</label>
            <span>
                <c:if test="${totalValue != null}">
                    <strong><fmt:formatNumber value="${totalValue}" type="currency" currencySymbol="R$ "/></strong>
                </c:if>
                <c:if test="${totalValue == null}">
                    <strong>R$ 0,00</strong>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Peso Total Transportado:</label>
            <span>
                <c:if test="${totalWeight != null}">
                    <strong>${totalWeight} kg</strong>
                </c:if>
                <c:if test="${totalWeight == null}">
                    <strong>0 kg</strong>
                </c:if>
            </span>
        </div>

        <div class="detail-row">
            <label>Volume Total Transportado:</label>
            <span>
                <c:if test="${totalVolume != null}">
                    <strong>${totalVolume} m³</strong>
                </c:if>
                <c:if test="${totalVolume == null}">
                    <strong>0 m³</strong>
                </c:if>
            </span>
        </div>
    </div>

    <div class="details-card" style="margin-top: 2rem;">
        <h3>📋 Distribuição por Status</h3>

        <table class="table">
            <thead>
                <tr>
                    <th>Status</th>
                    <th>Quantidade</th>
                    <th>Percentual</th>
                    <th>Visualização</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>
                        <span class="badge badge-warning">Pendente</span>
                    </td>
                    <td><strong>${statusCounts.PENDENTE != null ? statusCounts.PENDENTE : 0}</strong></td>
                    <td>
                        <c:if test="${totalDeliveries > 0}">
                            <fmt:formatNumber value="${(statusCounts.PENDENTE / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                        </c:if>
                        <c:if test="${totalDeliveries == 0}">
                            0%
                        </c:if>
                    </td>
                    <td>
                        <div style="background-color: #ff9800; height: 20px; width: 
                            <c:if test="${totalDeliveries > 0}">
                                ${(statusCounts.PENDENTE / totalDeliveries) * 200}px
                            </c:if>
                            <c:if test="${totalDeliveries == 0}">
                                0px
                            </c:if>
                        ;" style="border-radius: 3px;"></div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span class="badge badge-info">Em Trânsito</span>
                    </td>
                    <td><strong>${statusCounts.EM_TRANSITO != null ? statusCounts.EM_TRANSITO : 0}</strong></td>
                    <td>
                        <c:if test="${totalDeliveries > 0}">
                            <fmt:formatNumber value="${(statusCounts.EM_TRANSITO / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                        </c:if>
                        <c:if test="${totalDeliveries == 0}">
                            0%
                        </c:if>
                    </td>
                    <td>
                        <div style="background-color: #2196f3; height: 20px; width: 
                            <c:if test="${totalDeliveries > 0}">
                                ${(statusCounts.EM_TRANSITO / totalDeliveries) * 200}px
                            </c:if>
                            <c:if test="${totalDeliveries == 0}">
                                0px
                            </c:if>
                        ;" style="border-radius: 3px;"></div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span class="badge badge-success">Entregue</span>
                    </td>
                    <td><strong>${statusCounts.ENTREGUE != null ? statusCounts.ENTREGUE : 0}</strong></td>
                    <td>
                        <c:if test="${totalDeliveries > 0}">
                            <fmt:formatNumber value="${(statusCounts.ENTREGUE / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                        </c:if>
                        <c:if test="${totalDeliveries == 0}">
                            0%
                        </c:if>
                    </td>
                    <td>
                        <div style="background-color: #4caf50; height: 20px; width: 
                            <c:if test="${totalDeliveries > 0}">
                                ${(statusCounts.ENTREGUE / totalDeliveries) * 200}px
                            </c:if>
                            <c:if test="${totalDeliveries == 0}">
                                0px
                            </c:if>
                        ;" style="border-radius: 3px;"></div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span class="badge badge-secondary">Não Realizada</span>
                    </td>
                    <td><strong>${statusCounts.NAO_REALIZADA != null ? statusCounts.NAO_REALIZADA : 0}</strong></td>
                    <td>
                        <c:if test="${totalDeliveries > 0}">
                            <fmt:formatNumber value="${(statusCounts.NAO_REALIZADA / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                        </c:if>
                        <c:if test="${totalDeliveries == 0}">
                            0%
                        </c:if>
                    </td>
                    <td>
                        <div style="background-color: #9e9e9e; height: 20px; width: 
                            <c:if test="${totalDeliveries > 0}">
                                ${(statusCounts.NAO_REALIZADA / totalDeliveries) * 200}px
                            </c:if>
                            <c:if test="${totalDeliveries == 0}">
                                0px
                            </c:if>
                        ;" style="border-radius: 3px;"></div>
                    </td>
                </tr>

                <tr>
                    <td>
                        <span class="badge badge-danger">Cancelada</span>
                    </td>
                    <td><strong>${statusCounts.CANCELADA != null ? statusCounts.CANCELADA : 0}</strong></td>
                    <td>
                        <c:if test="${totalDeliveries > 0}">
                            <fmt:formatNumber value="${(statusCounts.CANCELADA / totalDeliveries) * 100}" maxFractionDigits="2"/>%
                        </c:if>
                        <c:if test="${totalDeliveries == 0}">
                            0%
                        </c:if>
                    </td>
                    <td>
                        <div style="background-color: #f44336; height: 20px; width: 
                            <c:if test="${totalDeliveries > 0}">
                                ${(statusCounts.CANCELADA / totalDeliveries) * 200}px
                            </c:if>
                            <c:if test="${totalDeliveries == 0}">
                                0px
                            </c:if>
                        ;" style="border-radius: 3px;"></div>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>

    <div class="form-actions" style="margin-top: 2rem;">
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
    </div>
</t:header>

<t:footer />
