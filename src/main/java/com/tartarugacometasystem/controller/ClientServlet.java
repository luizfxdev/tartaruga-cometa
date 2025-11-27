package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.util.Mapper;

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
            if (pathInfo.equals("/save")) {
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
        request.setAttribute("personTypes", PersonType.values());
        request.getRequestDispatcher("/pages/clients/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Client> clientOptional = clientService.getClientById(id);
        Client client = clientOptional.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        request.setAttribute("client", client);
        request.setAttribute("personTypes", PersonType.values());
        request.getRequestDispatcher("/pages/clients/new.jsp").forward(request, response);
    }

    private void viewClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Client> clientOptional = clientService.getClientById(id);
        Client client = clientOptional.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        request.setAttribute("client", client);
        request.getRequestDispatcher("/pages/clients/view.jsp").forward(request, response);
    }

    private void searchClients(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String searchTerm = request.getParameter("q");
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/clients/");
            return;
        }
        List<Client> clients = clientService.searchClientsByName(searchTerm);
        request.setAttribute("clients", clients);
        request.setAttribute("searchTerm", searchTerm);
        request.getRequestDispatcher("/pages/clients/list.jsp").forward(request, response);
    }

    private void saveClient(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", request.getParameter("id"));
            params.put("personType", request.getParameter("personType"));
            params.put("document", request.getParameter("document"));
            params.put("name", request.getParameter("name"));
            params.put("email", request.getParameter("email"));
            params.put("phone", request.getParameter("phone"));
            Client client = Mapper.mapToClient(params);
            if (client.getId() != null && client.getId() > 0) {
                clientService.updateClient(client);
                request.getSession().setAttribute("success", "Cliente atualizado com sucesso");
            } else {
                clientService.createClient(client);
                request.getSession().setAttribute("success", "Cliente criado com sucesso");
            }
            response.sendRedirect(request.getContextPath() + "/clients/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/clients/new");
        }
    }

    private void deleteClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            clientService.deleteClient(id);
            request.getSession().setAttribute("success", "Cliente deletado com sucesso");
            response.sendRedirect(request.getContextPath() + "/clients/");
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/clients/");
        }
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
