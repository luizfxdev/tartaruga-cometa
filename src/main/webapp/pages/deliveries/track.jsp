<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>
<%@ page import="com.tartarugacometasystem.model.DeliveryHistory" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>

<t:header title="Rastreamento de Entrega">
    <div class="page-header">
        <h2>Rastreamento de Entrega</h2>
    </div>

    <div class="search-box">
        <form method="GET" action="${pageContext.request.contextPath}/deliveries/search">
            <input type="text" name="q" placeholder="Digite o código de rastreamento..." required>
            <button type="submit" class="btn btn-primary">Rastrear</button>
        </form>
    </div>

    <%
        Delivery delivery = (Delivery) request.getAttribute("delivery");
        if (delivery != null) {
            Client shipper = (Client) request.getAttribute("shipper");
            Client recipient = (Client) request.getAttribute("recipient");
            List<DeliveryHistory> history = (List<DeliveryHistory>) request.getAttribute("history");
    %>
        <div class="tracking-container">
            <div class="tracking-header">
                <h3>Código: <%= delivery.getTrackingCode() %></h3>
                <span class="badge <%= getStatusBadgeClass(delivery.getStatus()) %>">
                    <%= delivery.getStatus().getLabel() %>
                </span>
            </div>

            <div class="tracking-info">
                <div class="info-box">
                    <h4>Remetente</h4>
                    <p><%= shipper != null ? shipper.getName() : "N/A" %></p>
                </div>

                <div class="info-box">
                    <h4>Destinatário</h4>
                    <p><%= recipient != null ? recipient.getName() : "N/A" %></p>
                </div>

                <div class="info-box">
                    <h4>Valor</h4>
                    <p>R$ <%= String.format("%.2f", delivery.getTotalValue()) %></p>
                </div>
            </div>

            <% if (history != null && !history.isEmpty()) { %>
                <div class="timeline">
                    <h4>Histórico de Rastreamento</h4>
                    <%
                        for (DeliveryHistory h : history) {
                    %>
                        <div class="timeline-item">
                            <div class="timeline-date">
                                <%= h.getChangeDate() %>
                            </div>
                            <div class="timeline-content">
                                <h5><%= h.getNewStatus().getLabel() %></h5>
                                <p><%= h.getObservations() != null ? h.getObservations() : "" %></p>
                                <small>Por: <%= h.getUser() %></small>
                            </div>
                        </div>
                    <%
                        }
                    %>
                </div>
            <% } %>
        </div>
    <%
        } else {
    %>
        <div class="alert alert-info">
            Digite o código de rastreamento acima para acompanhar sua entrega.
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
