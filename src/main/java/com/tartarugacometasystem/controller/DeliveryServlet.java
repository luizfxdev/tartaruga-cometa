package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.service.DeliveryService;
import com.tartarugacometasystem.util.Mapper; // Certifique-se de que esta classe e seus métodos estão corretos

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
        // Instanciação dos serviços
        this.deliveryService = new DeliveryService();
        this.clientService = new ClientService();
        this.addressService = new AddressService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        // Garante que pathInfo nunca seja null para evitar NullPointerExceptions
        String action = (pathInfo == null || pathInfo.equals("/")) ? "/" : pathInfo;

        try {
            if (action.equals("/")) {
                listDeliveries(request, response);
            } else if (action.equals("/new")) {
                showNewForm(request, response);
            } else if (action.startsWith("/edit/")) {
                showEditForm(request, response, action);
            } else if (action.startsWith("/view/")) {
                viewDelivery(request, response, action);
            } else if (action.startsWith("/search")) {
                searchDeliveries(request, response);
            } else if (action.startsWith("/markDelivered/")) {
                // Para GET, apenas redireciona para a view após a ação, ou exibe um formulário de confirmação
                // Se a intenção é que a ação de marcar como entregue seja idempotente e segura,
                // um GET pode ser aceitável, mas POST é geralmente preferível para mudanças de estado.
                // Por enquanto, mantemos a lógica existente.
                markAsDelivered(request, response, action);
            } else if (action.startsWith("/markNotDelivered/")) {
                // Similar ao markDelivered, POST é preferível.
                markAsNotDelivered(request, response, action);
            } else if (action.startsWith("/delete/")) {
                // Ações de exclusão via GET são desaconselhadas por questões de segurança e idempotência.
                // Idealmente, isso deveria ser um POST. Por enquanto, mantemos a lógica existente.
                deleteDelivery(request, response, action);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            // Loga o erro para depuração
            System.err.println("Erro de SQL no doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erro ao processar requisição de banco de dados: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de argumento inválido no doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("Erro inesperado no doGet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erro inesperado: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        // Garante que pathInfo nunca seja null para evitar NullPointerExceptions
        String action = (pathInfo == null || pathInfo.equals("/")) ? "/" : pathInfo;

        try {
            if (action.equals("/") || action.equals("/save")) {
                saveDelivery(request, response);
            } else if (action.startsWith("/delete/")) {
                deleteDelivery(request, response, action);
            } else if (action.startsWith("/markDelivered/")) {
                markAsDelivered(request, response, action);
            } else if (action.startsWith("/markNotDelivered/")) {
                markAsNotDelivered(request, response, action);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            System.err.println("Erro de SQL no doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erro ao processar requisição de banco de dados: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de argumento inválido no doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            // Se houver um erro de validação ao salvar, redireciona para o formulário com os dados preenchidos
            // e a mensagem de erro.
            try {
                // Tenta mapear novamente para preencher o formulário com os dados submetidos
                request.setAttribute("delivery", Mapper.mapToDelivery(request));
                request.setAttribute("allClients", clientService.getAllClients());
                request.setAttribute("allAddresses", addressService.getAllAddresses());
                request.setAttribute("deliveryStatuses", DeliveryStatus.values());
                request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
            } catch (SQLException ex) {
                System.err.println("Erro ao preparar formulário após erro de validação: " + ex.getMessage());
                ex.printStackTrace();
                request.setAttribute("error", "Erro ao preparar formulário após validação: " + ex.getMessage());
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.err.println("Erro inesperado no doPost: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Erro inesperado: " + e.getMessage());
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
        request.setAttribute("allClients", clientService.getAllClients());
        request.setAttribute("allAddresses", addressService.getAllAddresses());
        request.setAttribute("deliveryStatuses", DeliveryStatus.values());
        request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        if (id == null) {
            request.setAttribute("error", "ID da entrega inválido para edição.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        Optional<Delivery> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            request.setAttribute("delivery", delivery.get());
            request.setAttribute("allClients", clientService.getAllClients());
            request.setAttribute("allAddresses", addressService.getAllAddresses());
            request.setAttribute("deliveryStatuses", DeliveryStatus.values());
            request.getRequestDispatcher("/pages/deliveries/new.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Entrega não encontrada para edição com o ID: " + id);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void viewDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer deliveryId = extractId(pathInfo);
        if (deliveryId == null) {
            request.setAttribute("error", "ID da entrega inválido para visualização.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        Optional<Delivery> deliveryOptional = deliveryService.getDeliveryById(deliveryId);

        if (deliveryOptional.isPresent()) {
            Delivery delivery = deliveryOptional.get();
            request.setAttribute("delivery", delivery);
            request.getRequestDispatcher("/pages/deliveries/view.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Entrega não encontrada com o ID: " + deliveryId);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void saveDelivery(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        // A lógica de tratamento de IllegalArgumentException já está no doPost,
        // então podemos remover o try-catch daqui para evitar duplicação e simplificar.
        // Se o Mapper.mapToDelivery ou os services lançarem IllegalArgumentException,
        // ela será capturada no doPost.
        Delivery delivery = Mapper.mapToDelivery(request);

        if (delivery.getId() == null) {
            deliveryService.createDelivery(delivery);
            request.getSession().setAttribute("message", "Entrega criada com sucesso!");
        } else {
            deliveryService.updateDelivery(delivery);
            request.getSession().setAttribute("message", "Entrega atualizada com sucesso!");
        }
        response.sendRedirect(request.getContextPath() + "/deliveries/");
    }

    private void deleteDelivery(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        if (id != null) {
            deliveryService.deleteDelivery(id);
            request.getSession().setAttribute("message", "Entrega deletada com sucesso!");
        } else {
            request.getSession().setAttribute("error", "ID da entrega inválido para exclusão.");
        }
        response.sendRedirect(request.getContextPath() + "/deliveries/");
    }

    private void markAsDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException, ServletException {
        Integer id = extractId(pathInfo);
        if (id == null) {
            request.setAttribute("error", "ID da entrega inválido para marcar como entregue.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        try {
            // O campo 'reasonNotDelivered' não é aplicável aqui, então passamos null.
            // O campo 'updatedBy' é "Sistema" conforme seu código.
            deliveryService.updateDeliveryStatus(id, DeliveryStatus.DELIVERED, null, "Sistema");
            request.getSession().setAttribute("message", "Entrega marcada como entregue com sucesso!");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            viewDelivery(request, response, pathInfo); // Permite reexibir a página de visualização com o erro
        }
    }

    private void markAsNotDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException, ServletException {
        Integer id = extractId(pathInfo);
        // Pega o motivo da requisição. Se for um POST, pode vir de um formulário.
        // Se for um GET (o que não é ideal para esta ação), pode vir como parâmetro de query.
        String reason = request.getParameter("reasonNotDelivered");
        if (id == null) {
            request.setAttribute("error", "ID da entrega inválido para marcar como não entregue.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        try {
            deliveryService.updateDeliveryStatus(id, DeliveryStatus.NOT_PERFORMED, reason, "Sistema");
            request.getSession().setAttribute("message", "Entrega marcada como não entregue com sucesso!");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            viewDelivery(request, response, pathInfo); // Permite reexibir a página de visualização com o erro
        }
    }

    private void searchDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("query");
        List<Delivery> deliveries = deliveryService.search(searchTerm);
        request.setAttribute("deliveries", deliveries);
        request.setAttribute("searchTerm", searchTerm); // Mantém o termo de busca no campo de busca
        request.getRequestDispatcher("/pages/deliveries/list.jsp").forward(request, response);
    }

    private Integer extractId(String pathInfo) {
        try {
            // pathInfo pode ser algo como "/edit/123" ou "/delete/456"
            // Queremos pegar o "123" ou "456"
            String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
            return Integer.parseInt(idStr);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("Erro ao extrair ID da URL: " + pathInfo + " - " + e.getMessage());
            return null;
        }
    }
}
