<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>
<%@ page import="com.tartarugacometasystem.model.Product" %>
<%@ page import="com.tartarugacometasystem.model.DeliveryStatus" %>

<t:header title="Nova Entrega">
    <%
        Delivery delivery = (Delivery) request.getAttribute("delivery");
        List<Client> clients = (List<Client>) request.getAttribute("clients");
        List<Product> products = (List<Product>) request.getAttribute("products");
        DeliveryStatus[] statuses = (DeliveryStatus[]) request.getAttribute("statuses");
        String title = delivery != null && delivery.getId() != null ? "Editar Entrega" : "Nova Entrega";
    %>

    <div class="page-header">
        <h2><%= title %></h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/deliveries/save" class="form">
        <% if (delivery != null && delivery.getId() != null) { %>
            <input type="hidden" name="id" value="<%= delivery.getId() %>">
        <% } %>

        <fieldset>
            <legend>Informações Básicas</legend>

            <div class="form-group">
                <label for="trackingCode">Código de Rastreio</label>
                <input type="text" id="trackingCode" name="trackingCode" 
                    value="<%= delivery != null && delivery.getTrackingCode() != null ? delivery.getTrackingCode() : "" %>" 
                    placeholder="Deixe em branco para gerar automaticamente" readonly>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="shipperId">Remetente *</label>
                    <select id="shipperId" name="shipperId" required>
                        <option value="">Selecione...</option>
                        <%
                            for (Client client : clients) {
                        %>
                            <option value="<%= client.getId() %>" 
                                <%= delivery != null && delivery.getShipperId() != null && delivery.getShipperId().equals(client.getId()) ? "selected" : "" %>>
                                <%= client.getName() %>
                            </option>
                        <%
                            }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="recipientId">Destinatário *</label>
                    <select id="recipientId" name="recipientId" required>
                        <option value="">Selecione...</option>
                        <%
                            for (Client client : clients) {
                        %>
                            <option value="<%= client.getId() %>" 
                                <%= delivery != null && delivery.getRecipientId() != null && delivery.getRecipientId().equals(client.getId()) ? "selected" : "" %>>
                                <%= client.getName() %>
                            </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="originAddressId">Endereço de Origem *</label>
                    <input type="number" id="originAddressId" name="originAddressId" 
                        value="<%= delivery != null && delivery.getOriginAddressId() != null ? delivery.getOriginAddressId() : "" %>" 
                        required placeholder="ID do endereço">
                </div>

                <div class="form-group">
                    <label for="destinationAddressId">Endereço de Destino *</label>
                    <input type="number" id="destinationAddressId" name="destinationAddressId" 
                        value="<%= delivery != null && delivery.getDestinationAddressId() != null ? delivery.getDestinationAddressId() : "" %>" 
                        required placeholder="ID do endereço">
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>Valores</legend>

            <div class="form-row">
                <div class="form-group">
                    <label for="freightValue">Valor do Frete (R$) *</label>
                    <input type="number" id="freightValue" name="freightValue" 
                        value="<%= delivery != null && delivery.getFreightValue() != null ? delivery.getFreightValue() : "" %>" 
                        required step="0.01" placeholder="0.00">
                </div>

                <div class="form-group">
                    <label for="status">Status *</label>
                    <select id="status" name="status" required>
                        <option value="">Selecione...</option>
                        <%
                            for (DeliveryStatus status : statuses) {
                        %>
                            <option value="<%= status.getValue() %>" 
                                <%= delivery != null && delivery.getStatus() == status ? "selected" : "" %>>
                                <%= status.getLabel() %>
                            </option>
                        <%
                            }
                        %>
                    </select>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>Observações</legend>

            <div class="form-group">
                <label for="observations">Observações</label>
                <textarea id="observations" name="observations" placeholder="Observações adicionais" rows="4"><%=
                    delivery != null && delivery.getObservations() != null ? delivery.getObservations() : ""
                %></textarea>
            </div>
        </fieldset>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar</button>
            <a href="${pageContext.request.contextPath}/deliveries/" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
