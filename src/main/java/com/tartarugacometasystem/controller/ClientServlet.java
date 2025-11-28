package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List; // Importar Mapper
import java.util.Optional;

import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/clients/*")
public class ClientServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        super.init();
        this.clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listClients(request, response);
            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                showEditForm(request, response, pathInfo);
            } else if (pathInfo.startsWith("/view/")) {
                viewClient(request, response, pathInfo);
            } else if (pathInfo.startsWith("/search")) {
                searchClients(request, response);
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
                saveClient(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteClient(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients();
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/pages/clients/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("personTypes", PersonType.values()); // Envia os tipos de pessoa para o JSP
        request.getRequestDispatcher("/pages/clients/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Client> client = clientService.getClientById(id);

        if (client.isPresent()) {
            request.setAttribute("client", client.get());
            request.setAttribute("personTypes", PersonType.values()); // Envia os tipos de pessoa para o JSP
            request.getRequestDispatcher("/pages/clients/new.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void viewClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Client> client = clientService.getClientById(id);

        if (client.isPresent()) {
            request.setAttribute("client", client.get());
            request.getRequestDispatcher("/pages/clients/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void saveClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        Client client = Mapper.mapToClient(params); // Usar Mapper

        try {
            if (client.getId() == null) {
                clientService.createClient(client);
                request.getSession().setAttribute("success", "Cliente criado com sucesso!");
            } else {
                clientService.updateClient(client);
                request.getSession().setAttribute("success", "Cliente atualizado com sucesso!");
            }
            response.sendRedirect(request.getContextPath() + "/clients/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            request.setAttribute("client", client); // Mantém os dados preenchidos
            request.setAttribute("personTypes", PersonType.values()); // Recarrega os tipos de pessoa
            request.getRequestDispatcher("/pages/clients/new.jsp").forward(request, response);
        }
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        clientService.deleteClient(id);
        request.getSession().setAttribute("success", "Cliente deletado com sucesso!");
        response.sendRedirect(request.getContextPath() + "/clients/");
    }

    private void searchClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("query");
        List<Client> clients = clientService.searchClientsByName(searchTerm);
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/pages/clients/list.jsp").forward(request, response);
    }

    // Método auxiliar para construir um objeto Client a partir dos parâmetros da requisição (usando Mapper)
    private Client buildClientFromRequest(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return Mapper.mapToClient(params);
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
