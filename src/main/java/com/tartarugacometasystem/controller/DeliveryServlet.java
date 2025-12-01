package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.service.DeliveryService;
import com.tartarugacometasystem.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deliveries/*")
public class DeliveryServlet extends HttpServlet {

    private DeliveryService deliveryService;
    private ClientService clientService;
    private AddressService addressService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.deliveryService = new DeliveryService();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
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
            } else if (pathInfo.startsWith("/search")) {
                searchDeliveries(request, response);
            } else if (pathInfo.startsWith("/markDelivered/")) {
                markAsDelivered(request, response, pathInfo);
            } else if (pathInfo.startsWith("/markNotDelivered/")) {
                markAsNotDelivered(request, response, pathInfo);
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
            if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/save")) {
                saveDelivery(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteDelivery(request, response, pathInfo);
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

        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        request.setAttribute("clients", clientService.getAllClients());
        request.setAttribute("addresses", addressService.getAllAddresses());
        request.setAttribute("deliveryStatuses", DeliveryStatus.values());

        request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        Integer id = extractId(pathInfo);
        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da entrega inválido.");
            return;
        }

        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            request.setAttribute("delivery", delivery.get());
            request.setAttribute("clients", clientService.getAllClients());
            request.setAttribute("addresses", addressService.getAllAddresses());
            request.setAttribute("deliveryStatuses", DeliveryStatus.values());

            request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Entrega não encontrada.");
        }
    }

    private void viewDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        Integer id = extractId(pathInfo);

        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido.");
            return;
        }

        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            request.setAttribute("delivery", delivery.get());
            request.getRequestDispatcher("/pages/deliveries/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Entrega não encontrada.");
        }
    }

    private void saveDelivery(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));

        Delivery delivery = Mapper.mapToDelivery(params);

        try {
            if (delivery.getId() == null) {
                deliveryService.createDelivery(delivery); // CORRIGIDO ❗
                request.getSession().setAttribute("success", "Entrega criada com sucesso!");
            } else {
                deliveryService.updateDelivery(delivery); // CORRIGIDO ❗
                request.getSession().setAttribute("success", "Entrega atualizada com sucesso!");
            }

            response.sendRedirect(request.getContextPath() + "/deliveries/");

        } catch (IllegalArgumentException e) {

            request.setAttribute("error", e.getMessage());
            request.setAttribute("delivery", delivery);
            request.setAttribute("clients", clientService.getAllClients());
            request.setAttribute("addresses", addressService.getAllAddresses());
            request.setAttribute("deliveryStatuses", DeliveryStatus.values());

            request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
        }
    }

    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {

        Integer id = extractId(pathInfo);

        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        deliveryService.deleteDelivery(id);
        request.getSession().setAttribute("success", "Entrega deletada com sucesso!");
        response.sendRedirect(request.getContextPath() + "/deliveries/");
    }

    private void markAsDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {

        Integer id = extractId(pathInfo);

        if (id == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        deliveryService.updateDeliveryStatus(id, DeliveryStatus.DELIVERED, null, "SYSTEM");

        request.getSession().setAttribute("success", "Entrega marcada como entregue!");
        response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
    }

    private void markAsNotDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {

        Integer id = extractId(pathInfo);
        String reason = request.getParameter("reasonNotDelivered");

        if (id == null || reason == null || reason.isBlank()) {
            request.getSession().setAttribute("error", "Motivo obrigatório.");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
            return;
        }

        deliveryService.updateDeliveryStatus(id, DeliveryStatus.NOT_PERFORMED, reason, "SYSTEM");

        request.getSession().setAttribute("success", "Entrega marcada como não realizada!");
        response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
    }

    private void searchDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        String query = request.getParameter("query");
        List<Delivery> deliveries = deliveryService.search(query);

        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    private Integer extractId(String pathInfo) {
        try {
            return Integer.valueOf(pathInfo.substring(pathInfo.lastIndexOf('/') + 1));
        } catch (Exception e) {
            return null;
        }
    }
}
