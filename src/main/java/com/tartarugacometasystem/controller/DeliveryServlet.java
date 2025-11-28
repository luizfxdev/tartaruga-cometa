package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.service.DeliveryService;
import com.tartarugacometasystem.util.Mapper;

import jakarta.servlet.ServletException; // Importar Mapper
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
            } else if (pathInfo.startsWith("/markAsDelivered/")) {
                markAsDelivered(request, response, pathInfo);
            } else if (pathInfo.startsWith("/markAsNotDelivered/")) {
                markAsNotDelivered(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    /**
     * Lista todas as entregas.
     */
    private void listDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Delivery> deliveries = deliveryService.getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    /**
     * Exibe o formulário para criar uma nova entrega.
     */
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        List<Address> addresses = addressService.getAllAddresses();
        request.setAttribute("clients", clients);
        request.setAttribute("addresses", addresses);
        request.setAttribute("deliveryStatuses", DeliveryStatus.values()); // Para popular o dropdown de status
        request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
    }

    /**
     * Exibe o formulário para editar uma entrega existente.
     */
    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            List<Client> clients = clientService.getAllClients();
            List<Address> addresses = addressService.getAllAddresses();
            request.setAttribute("delivery", delivery.get());
            request.setAttribute("clients", clients);
            request.setAttribute("addresses", addresses);
            request.setAttribute("deliveryStatuses", DeliveryStatus.values());
            request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Exibe os detalhes de uma entrega.
     */
    private void viewDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            request.setAttribute("delivery", delivery.get());
            request.getRequestDispatcher("/pages/deliveries/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /**
     * Salva uma nova entrega ou atualiza uma existente.
     */
    private void saveDelivery(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        Delivery delivery = Mapper.mapToDelivery(params); // Usar Mapper

        try {
            if (delivery.getId() == null) {
                deliveryService.createDelivery(delivery);
                request.getSession().setAttribute("success", "Entrega criada com sucesso!");
            } else {
                deliveryService.updateDelivery(delivery);
                request.getSession().setAttribute("success", "Entrega atualizada com sucesso!");
            }
            response.sendRedirect(request.getContextPath() + "/deliveries/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            // Recarrega dados para o formulário em caso de erro
            List<Client> clients = clientService.getAllClients();
            List<Address> addresses = addressService.getAllAddresses();
            request.setAttribute("delivery", delivery); // Mantém os dados preenchidos
            request.setAttribute("clients", clients);
            request.setAttribute("addresses", addresses);
            request.setAttribute("deliveryStatuses", DeliveryStatus.values());
            request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
        }
    }

    /**
     * Deleta uma entrega.
     */
    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        deliveryService.deleteDelivery(id);
        request.getSession().setAttribute("success", "Entrega deletada com sucesso!");
        response.sendRedirect(request.getContextPath() + "/deliveries/");
    }

    /**
     * Marca uma entrega como entregue.
     */
    private void markAsDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        String user = request.getRemoteUser(); // Obtém o usuário logado (se houver)
        if (user == null || user.trim().isEmpty()) {
            user = "SYSTEM"; // Fallback para usuário do sistema
        }

        try {
            deliveryService.markAsDelivered(id, user);
            request.getSession().setAttribute("success", "Entrega marcada como entregue com sucesso!");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    /**
     * Marca uma entrega como não realizada.
     */
    private void markAsNotDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        String reason = request.getParameter("reasonNotDelivered"); // Motivo vem do formulário
        String user = request.getRemoteUser();
        if (user == null || user.trim().isEmpty()) {
            user = "SYSTEM";
        }

        try {
            if (reason == null || reason.trim().isEmpty()) {
                throw new IllegalArgumentException("Motivo da não realização é obrigatório.");
            }
            deliveryService.markAsNotDelivered(id, reason, user);
            request.getSession().setAttribute("success", "Entrega marcada como não realizada com sucesso");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + extractId(pathInfo));
        }
    }

    /**
     * Busca entregas por query.
     */
    private void searchDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String query = request.getParameter("query");
        List<Delivery> deliveries = deliveryService.searchDeliveries(query);
        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    /**
     * Método auxiliar para construir um objeto Delivery a partir dos parâmetros da requisição (usando Mapper).
     */
    private Delivery buildDeliveryFromRequest(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return Mapper.mapToDelivery(params);
    }

    /**
     * Extrai o ID da URL
     */
    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    /**
     * Extrai o código de rastreio da URL
     */
    private String extractTrackingCode(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return parts[parts.length - 1];
    }
}
