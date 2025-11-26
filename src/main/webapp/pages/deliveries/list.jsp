<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>
<%@ page import="com.tartarugacometasystem.model.DeliveryStatus" %>

<t:header title="Entregas">
    <div class="page-header">
        <h2>Entregas</h2>
        <a href="${pageContext.request.contextPath}/deliveries/new" class="btn btn-success">+ Nova Entrega</a>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/deliveries/">
            <select name="status" onchange="this.form.submit()">
                <option value="">Todos os Status</option>
                <%
                    DeliveryStatus[] statuses = (DeliveryStatus[]) request.getAttribute("statuses");
                    String selectedStatus = (String) request.getAttribute("selectedStatus");
                    for (DeliveryStatus status : statuses) {
                %>
                    <option value="<%= status.getValue() %>" 
                        <%= status.getValue().equals(selectedStatus) ? "selected" : "" %>>
                        <%= status.getLabel() %>
                    </option>
                <%
                    }
                %>
            </select>
        </form>
    </div>

    <%
        List<Delivery> deliveries = (List<Delivery>) request.getAttribute("deliveries");
        if (deliveries != null && !deliveries.isEmpty()) {
    %>
        <table class="table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Código de Rastreio</th>
                    <th>Remetente</th>
                    <th>Destinatário</th>
                    <th>Status</th>
                    <th>Valor Total</th>
                    <th>Data de Criação</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                    for (Delivery delivery : deliveries) {
                %>
                    <tr>
                        <td><%= delivery.getId() %></td>
                        <td><strong><%= delivery.getTrackingCode() %></strong></td>
                        <td><%= delivery.getShipperId() %></td>
                        <td><%= delivery.getRecipientId() %></td>
                        <td>
                            <span class="badge <%= getStatusBadgeClass(delivery.getStatus()) %>">
                                <%= delivery.getStatus().getLabel() %>
                            </span>
                        </td>
                        <td>R$ <%= String.format("%.2f", delivery.getTotalValue()) %></td>
                        <td><%= delivery.getCreatedAt() %></td>
                        <td>
                            <a href="${pageContext.request.contextPath}/deliveries/view/<%= delivery.getId() %>" class="btn btn-sm btn-info">Ver</a>
                            <a href="${pageContext.request.contextPath}/deliveries/edit/<%= delivery.getId() %>" class="btn btn-sm btn-warning">Editar</a>
                            <form method="POST" action="${pageContext.request.contextPath}/deliveries/delete/<%= delivery.getId() %>" style="display:inline;">
                                <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Tem certeza?')">Deletar</button>
                            </form>
                        </td>
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
            Nenhuma entrega encontrada. <a href="${pageContext.request.contextPath}/deliveries/new">Criar nova entrega</a>
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
