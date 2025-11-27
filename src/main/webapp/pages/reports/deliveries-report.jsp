<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>

<t:header title="Relatório de Entregas">
    <div class="page-header">
        <h2>Relatório de Entregas</h2>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/reports/deliveries">
            <input type="date" name="startDate" placeholder="Data Inicial">
            <input type="date" name="endDate" placeholder="Data Final">
            <select name="status">
                <option value="">Todos os Status</option>
                <option value="PENDENTE">Pendente</option>
                <option value="EM_TRANSITO">Em Trânsito</option>
                <option value="ENTREGUE">Entregue</option>
                <option value="CANCELADA">Cancelada</option>
                <option value="NAO_REALIZADA">Não Realizada</option>
            </select>
            <button type="submit" class="btn btn-primary">Filtrar</button>
        </form>
    </div>

    <%
        List<Delivery> deliveries = (List<Delivery>) request.getAttribute("deliveries");
        if (deliveries != null && !deliveries.isEmpty()) {
    %>
        <div class="details-card">
            <h3>Resumo</h3>
            <div class="detail-row">
                <label>Total de Entregas:</label>
                <span><%= deliveries.size() %></span>
            </div>
            <div class="detail-row">
                <label>Valor Total:</label>
                <span>R$ <%= String.format("%.2f", 
                    deliveries.stream().mapToDouble(d -> d.getTotalValue()).sum()) %></span>
            </div>
            <div class="detail-row">
                <label>Peso Total:</label>
                <span><%= String.format("%.2f", 
                    deliveries.stream().mapToDouble(d -> d.getTotalWeightKg() != null ? d.getTotalWeightKg() : 0).sum()) %> kg</span>
            </div>
        </div>

        <table class="table">
            <thead>
                <tr>
                    <th>Código</th>
                    <th>Remetente</th>
                    <th>Destinatário</th>
                    <th>Status</th>
                    <th>Valor Total</th>
                    <th>Peso</th>
                    <th>Data Criação</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Delivery delivery : deliveries) {
                %>
                    <tr>
                        <td><strong><%= delivery.getTrackingCode() %></strong></td>
                        <td><%= delivery.getShipperId() %></td>
                        <td><%= delivery.getRecipientId() %></td>
                        <td>
                            <span class="badge <%= getStatusBadgeClass(delivery.getStatus()) %>">
                                <%= delivery.getStatus().getLabel() %>
                            </span>
                        </td>
                        <td>R$ <%= String.format("%.2f", delivery.getTotalValue()) %></td>
                        <td><%= delivery.getTotalWeightKg() %> kg</td>
                        <td><%= delivery.getCreatedAt() %></td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    <%
        } else {
    %>
        <div class="alert alert-info">
            Nenhuma entrega encontrada com os filtros selecionados.
        </div>
    <%
        }
    %>

    <%!
        private String getStatusBadgeClass(com.tartarugacometasystem.model.DeliveryStatus status) {
            switch(status) {
                case PENDENTE:
                    return "badge-warning";
                case EM_TRANSITO:
                    return "badge-info";
                case ENTREGUE:
                    return "badge-success";
                case NAO_REALIZADA:
                    return "badge-secondary";
                case CANCELADA:
                    return "badge-danger";
                default:
                    return "badge-secondary";
            }
        }
    %>
</t:header>

<t:footer />
