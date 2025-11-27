<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tartarugacometasystem.model.Delivery" %>
<%@ page import="com.tartarugacometasystem.model.Client" %>
<%@ page import="com.tartarugacometasystem.model.Address" %>
<%@ page import="com.tartarugacometasystem.model.DeliveryStatus" %>

<t:header title="Editar Entrega">
    <%
        Delivery delivery = (Delivery) request.getAttribute("delivery");
        List<Client> clients = (List<Client>) request.getAttribute("clients");
        DeliveryStatus[] statuses = (DeliveryStatus[]) request.getAttribute("statuses");
        Client shipper = (Client) request.getAttribute("shipper");
        Client recipient = (Client) request.getAttribute("recipient");
        Address originAddress = (Address) request.getAttribute("originAddress");
        Address destinationAddress = (Address) request.getAttribute("destinationAddress");
    %>

    <div class="page-header">
        <h2>Editar Entrega</h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/deliveries/save" class="form" onsubmit="return validarFormularioEntrega(this)">
        <input type="hidden" name="id" value="<%= delivery.getId() %>">

        <fieldset>
            <legend>Informações Básicas</legend>

            <div class="form-group">
                <label>Código de Rastreio</label>
                <input type="text" value="<%= delivery.getTrackingCode() %>" readonly>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label for="shipperId">Remetente *</label>
                    <select id="shipperId" name="shipperId" required>
                        <%
                            for (Client client : clients) {
                        %>
                            <option value="<%= client.getId() %>" 
                                <%= delivery.getShipperId().equals(client.getId()) ? "selected" : "" %>>
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
                        <%
                            for (Client client : clients) {
                        %>
                            <option value="<%= client.getId() %>" 
                                <%= delivery.getRecipientId().equals(client.getId()) ? "selected" : "" %>>
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
                        value="<%= delivery.getOriginAddressId() %>" required>
                </div>

                <div class="form-group">
                    <label for="destinationAddressId">Endereço de Destino *</label>
                    <input type="number" id="destinationAddressId" name="destinationAddressId" 
                        value="<%= delivery.getDestinationAddressId() %>" required>
                </div>
            </div>
        </fieldset>

        <fieldset>
            <legend>Valores</legend>

            <div class="form-row">
                <div class="form-group">
                    <label for="freightValue">Valor do Frete (R$) *</label>
                    <input type="number" id="freightValue" name="freightValue" 
                        value="<%= delivery.getFreightValue() %>" required step="0.01">
                </div>

                <div class="form-group">
                    <label for="status">Status *</label>
                    <select id="status" name="status" required>
                        <%
                            for (DeliveryStatus status : statuses) {
                        %>
                            <option value="<%= status.getValue() %>" 
                                <%= delivery.getStatus() == status ? "selected" : "" %>>
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
                <textarea id="observations" name="observations" rows="4"><%=
                    delivery.getObservations() != null ? delivery.getObservations() : ""
                %></textarea>
            </div>
        </fieldset>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">Salvar Alterações</button>
            <a href="${pageContext.request.contextPath}/deliveries/view/<%= delivery.getId() %>" class="btn btn-secondary">Cancelar</a>
        </div>
    </form>
</t:header>

<t:footer />
