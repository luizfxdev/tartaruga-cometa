package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.model.Product;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.service.DeliveryService;
import com.tartarugacometasystem.service.ProductService;
import com.tartarugacometasystem.util.Mapper;

@WebServlet("/deliveries/*")
public class DeliveryServlet extends HttpServlet {
    private DeliveryService deliveryService;
    private ClientService clientService;
    private AddressService addressService;
    private ProductService productService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.deliveryService = new DeliveryService();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
        this.productService = new ProductService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listDeliveries(request, response);
            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                showEditForm(request, response, pathInfo);
            } else if (pathInfo.startsWith("/view/")) {
                viewDelivery(request, response, pathInfo);
            } else if (pathInfo.startsWith("/track/")) {
                trackDelivery(request, response, pathInfo);
            } else if (pathInfo.startsWith("/search")) {
                searchDeliveries(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo.equals("/save")) {
                saveDelivery(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteDelivery(request, response, pathInfo);
            } else if (pathInfo.startsWith("/update-status/")) {
                updateDeliveryStatus(request, response, pathInfo);
            } else if (pathInfo.startsWith("/cancel/")) {
                cancelDelivery(request, response, pathInfo);
            } else if (pathInfo.startsWith("/mark-delivered/")) {
                markAsDelivered(request, response, pathInfo);
            } else if (pathInfo.startsWith("/mark-not-delivered/")) {
                markAsNotDelivered(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listDeliveries(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String statusParam = request.getParameter("status");
        List<Delivery> deliveries;

        if (statusParam != null && !statusParam.isEmpty()) {
            deliveries = deliveryService.getDeliveriesByStatus(DeliveryStatus.fromValue(statusParam));
        } else {
            deliveries = deliveryService.getAllDeliveries();
        }

        request.setAttribute("deliveries", deliveries);
        request.setAttribute("statuses", DeliveryStatus.values());
        request.setAttribute("selectedStatus", statusParam);
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        List<Product> products = productService.getAllActiveProducts();

        request.setAttribute("clients", clients);
        request.setAttribute("products", products);
        request.setAttribute("statuses", DeliveryStatus.values());
        request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Delivery> deliveryOptional = deliveryService.getDeliveryById(id);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        Optional<Client> shipper = clientService.getClientById(delivery.getShipperId());
        Optional<Client> recipient = clientService.getClientById(delivery.getRecipientId());
        Optional<Address> originAddress = addressService.getAddressById(delivery.getOriginAddressId());
        Optional<Address> destinationAddress = addressService.getAddressById(delivery.getDestinationAddressId());
        List<Client> clients = clientService.getAllClients();
        List<Product> products = productService.getAllActiveProducts();

        request.setAttribute("delivery", delivery);
        request.setAttribute("shipper", shipper.orElse(null));
        request.setAttribute("recipient", recipient.orElse(null));
        request.setAttribute("originAddress", originAddress.orElse(null));
        request.setAttribute("destinationAddress", destinationAddress.orElse(null));
        request.setAttribute("clients", clients);
        request.setAttribute("products", products);
        request.setAttribute("statuses", DeliveryStatus.values());
        request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
    }

    private void viewDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Delivery> deliveryOptional = deliveryService.getDeliveryById(id);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        Optional<Client> shipper = clientService.getClientById(delivery.getShipperId());
        Optional<Client> recipient = clientService.getClientById(delivery.getRecipientId());
        Optional<Address> originAddress = addressService.getAddressById(delivery.getOriginAddressId());
        Optional<Address> destinationAddress = addressService.getAddressById(delivery.getDestinationAddressId());
        List<DeliveryHistory> history = deliveryService.getDeliveryHistory(id);

        request.setAttribute("delivery", delivery);
        request.setAttribute("shipper", shipper.orElse(null));
        request.setAttribute("recipient", recipient.orElse(null));
        request.setAttribute("originAddress", originAddress.orElse(null));
        request.setAttribute("destinationAddress", destinationAddress.orElse(null));
        request.setAttribute("history", history);
        request.getRequestDispatcher("/pages/deliveries/details.jsp").forward(request, response);
    }

    private void trackDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        String trackingCode = extractTrackingCode(pathInfo);
        Optional<Delivery> deliveryOptional = deliveryService.getDeliveryByTrackingCode(trackingCode);
        Delivery delivery = deliveryOptional.orElseThrow(() -> new IllegalArgumentException("Entrega não encontrada"));

        Optional<Client> shipper = clientService.getClientById(delivery.getShipperId());
        Optional<Client> recipient = clientService.getClientById(delivery.getRecipientId());
        List<DeliveryHistory> history = deliveryService.getDeliveryHistory(delivery.getId());

        request.setAttribute("delivery", delivery);
        request.setAttribute("shipper", shipper.orElse(null));
        request.setAttribute("recipient", recipient.orElse(null));
        request.setAttribute("history", history);
        request.getRequestDispatcher("/pages/deliveries/track.jsp").forward(request, response);
    }

    private void searchDeliveries(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("q");

        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/deliveries/");
            return;
        }

        Optional<Delivery> deliveryOptional = deliveryService.getDeliveryByTrackingCode(searchTerm.trim());

        if (deliveryOptional.isPresent()) {
            Delivery delivery = deliveryOptional.get();
            Optional<Client> shipper = clientService.getClientById(delivery.getShipperId());
            Optional<Client> recipient = clientService.getClientById(delivery.getRecipientId());
            List<DeliveryHistory> history = deliveryService.getDeliveryHistory(delivery.getId());

            request.setAttribute("delivery", delivery);
            request.setAttribute("shipper", shipper.orElse(null));
            request.setAttribute("recipient", recipient.orElse(null));
            request.setAttribute("history", history);
            request.setAttribute("searchTerm", searchTerm);
            request.getRequestDispatcher("/pages/deliveries/track.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Entrega não encontrada com o código: " + searchTerm);
            request.getRequestDispatcher("/pages/deliveries/track.jsp").forward(request, response);
        }
    }

    private void saveDelivery(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", request.getParameter("id"));
            params.put("trackingCode", request.getParameter("trackingCode"));
            params.put("shipperId", request.getParameter("shipperId"));
            params.put("recipientId", request.getParameter("recipientId"));
            params.put("originAddressId", request.getParameter("originAddressId"));
            params.put("destinationAddressId", request.getParameter("destinationAddressId"));
            params.put("status", request.getParameter("status"));
            params.put("freightValue", request.getParameter("freightValue"));
            params.put("observations", request.getParameter("observations"));

            Delivery delivery = Mapper.mapToDelivery(params);

            if (delivery.getId() != null && delivery.getId() > 0) {
                deliveryService.updateDelivery(delivery);
                request.getSession().setAttribute("success", "Entrega atualizada com sucesso");
            } else {
                deliveryService.createDelivery(delivery);
                request.getSession().setAttribute("success", "Entrega criada com sucesso");
            }

            response.sendRedirect(request.getContextPath() + "/deliveries/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/new");
        }
    }

    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            deliveryService.deleteDelivery(id);
            request.getSession().setAttribute("success", "Entrega deletada com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/");
        }
    }

    private void updateDeliveryStatus(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            DeliveryStatus newStatus = DeliveryStatus.fromValue(request.getParameter("status"));
            String observations = request.getParameter("observations");
            String user = request.getParameter("user");

            if (user == null || user.trim().isEmpty()) {
                user = "SYSTEM";
            }

            deliveryService.updateDeliveryStatus(id, newStatus, observations, user);
            request.getSession().setAttribute("success", "Status da entrega atualizado com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    private void cancelDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            String reason = request.getParameter("reason");
            String user = request.getParameter("user");

            if (user == null || user.trim().isEmpty()) {
                user = "SYSTEM";
            }

            deliveryService.cancelDelivery(id, reason, user);
            request.getSession().setAttribute("success", "Entrega cancelada com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    private void markAsDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            String user = request.getParameter("user");

            if (user == null || user.trim().isEmpty()) {
                user = "SYSTEM";
            }

            deliveryService.markAsDelivered(id, user);
            request.getSession().setAttribute("success", "Entrega marcada como entregue com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    private void markAsNotDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            String reason = request.getParameter("reason");
            String user = request.getParameter("user");

            if (user == null || user.trim().isEmpty()) {
                user = "SYSTEM";
            }

            deliveryService.markAsNotDelivered(id, reason, user);
            request.getSession().setAttribute("success", "Entrega marcada como não realizada com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private String extractTrackingCode(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return parts[parts.length - 1];
    }
}
