<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.Map" %>

<t:header title="Relatório de Status">
    <div class="page-header">
        <h2>Relatório de Status de Entregas</h2>
    </div>

    <%
        Map<String, Object> statistics = (Map<String, Object>) request.getAttribute("statistics");
    %>

    <% if (statistics != null && !statistics.isEmpty()) { %>
        <div class="dashboard">
            <div class="card">
                <h3>Pendentes</h3>
                <p class="stat-number" style="color: #ffb74d;">
                    <%= statistics.get("PENDENTE_count") != null ? statistics.get("PENDENTE_count") : 0 %>
                </p>
                <p>Valor: R$ <%= String.format("%.2f", 
                    statistics.get("PENDENTE_value") != null ? (Double) statistics.get("PENDENTE_value") : 0.0) %></p>
            </div>

            <div class="card">
                <h3>Em Trânsito</h3>
                <p class="stat-number" style="color: #42a5f5;">
                    <%= statistics.get("EM_

