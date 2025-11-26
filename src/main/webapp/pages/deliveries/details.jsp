<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>
<%@ page import="com.tartarugacometasystem.model.DeliveryHistory" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>
<%@ page import="com.tartarugacometasystem.model.Address" %>

<t:header title="Detalhes da Entrega">
    <%
        Delivery delivery = (Delivery) request.getAttribute("delivery");
        Client shipper = (Client) request.getAttribute("shipper");
        Client recipient = (Client) request.getAttribute("recipient");
        Address originAddress = (Address) request.getAttribute("originAddress");
        Address destinationAddress = (Address) request.getAttribute("destinationAddress");
        List<DeliveryHistory> history = (List<DeliveryHistory>) request.getAttribute("history");
    %>

    <div class="page-header">
        <h2>Detalhes da Entrega</h2>
    </div>

    <div class="details-card">
        <h3>Informações Gerais</h3>

        <div class="detail-row">
            <label>ID:</label>
            <span><%= delivery.getId() %></span>
        </div>

        <div class="detail-row">
            <label>Código de Rastreio:</label>
            <span><strong><%= delivery.getTrackingCode() %></strong></span>
        </div>

        <div class="detail-row">
            <label>Status:</label>
            <span class="badge <%= getStatusBadgeClass(delivery.getStatus()) %>">
                <%= delivery.getStatus().getLabel() %>
            </span>
        </div>

        <div class="detail-row">
            <label>Data de Criação:</label>
            <span><%= delivery.getCreatedAt() %></span>
        </div>
    </div>

    <div class="details-card">
        <h3>Partes Envolvidas</h3>

        <div class="detail-row">
            <label>Remetente:</label>
            <span><%= shipper != null ? shipper.getName() : "N/A" %></span>
        </div>

        <div class="detail-row">
            <label>Destinatário:</label>
            <span><%= recipient != null ? recipient.getName() : "N/A" %></span>
        </div>
    </div>

    <div class="details-card">
        <h3>Endereços</h3>

        <div class="detail-row">
            <label>Origem:</label>
            <span>
                <% if (originAddress != null) { %>
                    <%= originAddress.getStreet() %>, <%= originAddress.getNumber() %> - 
                    <%= originAddress.getCity() %>, <%= originAddress.getState() %>
                <% } else { %>
                    N/A
                <% } %>
            </span>
        </div>

        <div class="detail-row">
            <label>Destino:</label>
            <span>
                <% if (destinationAddress != null) { %>
                    <%= destinationAddress.getStreet() %>, <%= destinationAddress.getNumber() %> - 
                    <%= destinationAddress.getCity() %>, <%= destinationAddress.getState() %>
                <% } else { %>
                    N/A
                <% } %>
            </span>
        </div>
    </div>

    <div class="details-card">
        <h3>Valores</h3>

        <div class="detail-row">
            <label>Valor Total:</label>
            <span>R$ <%= String.format("%.2f", delivery.getTotalValue()) %></span>
        </div>

        <div class="detail-row">
            <label>Peso Total:</label>
            <span><%= delivery.getTotalWeightKg() %> kg</span>
        </div>

        <div class="detail-row">
            <label>Volume Total:</label>
            <span><%= delivery.getTotalVolumeM3() %> m³</span>
        </div>

        <div class="detail-row">
            <label>Valor do Frete:</label>
            <span>R$ <%= String.format("%.2f", delivery.getFreightValue()) %></span>
        </div>
    </div>

    <% if (delivery.getObservations() != null && !delivery.getObservations().isEmpty()) { %>
        <div class="details-card">
            <h3>Observações</h3>
            <p><%= delivery.getObservations() %></p>
        </div>
    <% } %>

    <% if (history != null && !history.isEmpty()) { %>
        <div class="details-card">
            <h3>Histórico</h3>
            <table class="table">
                <thead>
                    <tr>
                        <th>Data</th>
                        <th>Status Anterior</th>
                        <th>Novo Status</th>
                        <th>Usuário</th>
                        <th>Observações</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (DeliveryHistory h : history) {
                    %>
                        <tr>
                            <td><%= h.getChangeDate() %></td>
                            <td><%= h.getPreviousStatus() != null ? h.getPreviousStatus().getLabel() : "-" %></td>
                            <td><%= h.getNewStatus().getLabel() %></td>
                            <td><%= h.getUser() %></td>
                            <td><%= h.getObservations() != null ? h.getObservations() : "-" %></td>
                        </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    <% } %>

    <div class="form-actions">
        <a href="${pageContext.request.contextPath}/deliveries/edit/<%= delivery.getId() %>" class="btn btn-warning">Editar</a>
        <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Voltar</a>
    </div>

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
