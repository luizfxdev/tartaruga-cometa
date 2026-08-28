package br.com.tartarugacometa.entrega;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.tartarugacometa.entrega.Entrega;
import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.cadastro.endereco.EnderecoBO;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.entrega.EntregaBO;
import br.com.tartarugacometa.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/entrega/*")
public class EntregaControlador extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(EntregaControlador.class.getName());
    private static final String MENSAGEM_ERRO_GENERICA = "Não foi possível processar a requisição.";

    private EntregaBO deliveryService;
    private ClienteBO clientService;
    private EnderecoBO addressService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.deliveryService = new EntregaBO();
        this.clientService = new ClienteBO();
        this.addressService = new EnderecoBO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
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
                markAsDelivered(request, response, action);
            } else if (action.startsWith("/markNotDelivered/")) {
                markAsNotDelivered(request, response, action);
            } else if (action.startsWith("/delete/")) {
                deleteDelivery(request, response, action);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Falha de banco de dados no doGet de EntregaControlador", e);
            request.setAttribute("error", MENSAGEM_ERRO_GENERICA);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Falha inesperada no doGet de EntregaControlador", e);
            request.setAttribute("error", MENSAGEM_ERRO_GENERICA);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
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
            LOG.log(Level.SEVERE, "Falha de banco de dados no doPost de EntregaControlador", e);
            request.setAttribute("error", MENSAGEM_ERRO_GENERICA);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            try {
                request.setAttribute("delivery", Mapper.mapToDelivery(request));
                request.setAttribute("allClients", clientService.getAllClients());
                request.setAttribute("allAddresses", addressService.getAllAddresses());
                request.setAttribute("deliveryStatuses", StatusEntrega.values());
                request.getRequestDispatcher("/WEB-INF/views/entrega/form.jsp").forward(request, response);
            } catch (SQLException ex) {
                LOG.log(Level.SEVERE, "Falha ao preparar formulário após erro de validação", ex);
                request.setAttribute("error", MENSAGEM_ERRO_GENERICA);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Falha inesperada no doPost de EntregaControlador", e);
            request.setAttribute("error", MENSAGEM_ERRO_GENERICA);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Entrega> deliveries = deliveryService.getAllDeliveries();
        request.setAttribute("deliveries", deliveries);
        request.getRequestDispatcher("/WEB-INF/views/entrega/lista.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        request.setAttribute("allClients", clientService.getAllClients());
        request.setAttribute("allAddresses", addressService.getAllAddresses());
        request.setAttribute("deliveryStatuses", StatusEntrega.values());
        request.getRequestDispatcher("/WEB-INF/views/entrega/form.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        if (id == null) {
            request.setAttribute("error", "ID da entrega inválido para edição.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        Optional<Entrega> delivery = deliveryService.getDeliveryById(id);

        if (delivery.isPresent()) {
            request.setAttribute("delivery", delivery.get());
            request.setAttribute("allClients", clientService.getAllClients());
            request.setAttribute("allAddresses", addressService.getAllAddresses());
            request.setAttribute("deliveryStatuses", StatusEntrega.values());
            request.getRequestDispatcher("/WEB-INF/views/entrega/form.jsp").forward(request, response);
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

        Optional<Entrega> deliveryOptional = deliveryService.getDeliveryById(deliveryId);

        if (deliveryOptional.isPresent()) {
            Entrega delivery = deliveryOptional.get();
            request.setAttribute("delivery", delivery);
            request.getRequestDispatcher("/WEB-INF/views/entrega/detalhe.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Entrega não encontrada com o ID: " + deliveryId);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void saveDelivery(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        Entrega delivery = Mapper.mapToDelivery(request);

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
            deliveryService.updateDeliveryStatus(id, StatusEntrega.ENTREGUE, null, "Sistema");
            request.getSession().setAttribute("message", "Entrega marcada como entregue com sucesso!");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            viewDelivery(request, response, pathInfo);
        }
    }

    private void markAsNotDelivered(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException, ServletException {
        Integer id = extractId(pathInfo);
        String reason = request.getParameter("reasonNotDelivered");
        if (id == null) {
            request.setAttribute("error", "ID da entrega inválido para marcar como não entregue.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }
        try {
            deliveryService.updateDeliveryStatus(id, StatusEntrega.NAO_REALIZADA, reason, "Sistema");
            request.getSession().setAttribute("message", "Entrega marcada como não entregue com sucesso!");
            response.sendRedirect(request.getContextPath() + "/deliveries/view/" + id);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            viewDelivery(request, response, pathInfo);
        }
    }

    private void searchDeliveries(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("query");
        List<Entrega> deliveries = deliveryService.search(searchTerm);
        request.setAttribute("deliveries", deliveries);
        request.setAttribute("searchTerm", searchTerm); // Mantém o termo de busca no campo de busca
        request.getRequestDispatcher("/WEB-INF/views/entrega/lista.jsp").forward(request, response);
    }

    private Integer extractId(String pathInfo) {
        try {
            String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
            return Integer.parseInt(idStr);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            LOG.log(Level.WARNING, "Falha ao extrair ID da URL: " + pathInfo, e);
            return null;
        }
    }
}
