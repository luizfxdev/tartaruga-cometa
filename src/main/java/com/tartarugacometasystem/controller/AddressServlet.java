package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional; // Importar Mapper

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/addresses/*")
public class AddressServlet extends HttpServlet {
    private AddressService addressService;
    private ClientService clientService; // Para buscar clientes para o formulário

    @Override
    public void init() throws ServletException {
        super.init();
        this.addressService = new AddressService();
        this.clientService = new ClientService();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listAddresses(request, response);
            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);
            } else if (pathInfo.startsWith("/edit/")) {
                showEditForm(request, response, pathInfo);
            } else if (pathInfo.startsWith("/view/")) {
                viewAddress(request, response, pathInfo);
            } else if (pathInfo.startsWith("/byClient/")) { // Novo endpoint para listar endereços de um cliente
                listAddressesByClient(request, response, pathInfo);
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
                saveAddress(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteAddress(request, response, pathInfo);
            } else if (pathInfo.startsWith("/setPrincipal/")) { // Novo endpoint para definir endereço principal
                setPrincipalAddress(request, response, pathInfo);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Erro ao processar requisição: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Address> addresses = addressService.getAllAddresses();
        request.setAttribute("addresses", addresses);
        request.getRequestDispatcher("/pages/addresses/list.jsp").forward(request, response);
    }

    private void listAddressesByClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer clientId = extractId(pathInfo);
        List<Address> addresses = addressService.getAddressesByClientId(clientId);
        Optional<Client> client = clientService.getClientById(clientId);

        if (client.isPresent()) {
            request.setAttribute("client", client.get());
            request.setAttribute("addresses", addresses);
            request.getRequestDispatcher("/pages/addresses/listByClient.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cliente não encontrado.");
        }
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Client> clients = clientService.getAllClients(); // Para popular o dropdown de clientes
        request.setAttribute("clients", clients);
        request.getRequestDispatcher("/pages/addresses/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Address> address = addressService.getAddressById(id);

        if (address.isPresent()) {
            List<Client> clients = clientService.getAllClients(); // Para popular o dropdown de clientes
            request.setAttribute("address", address.get());
            request.setAttribute("clients", clients);
            request.getRequestDispatcher("/pages/addresses/new.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void viewAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Address> address = addressService.getAddressById(id);

        if (address.isPresent()) {
            request.setAttribute("address", address.get());
            request.getRequestDispatcher("/pages/addresses/view.jsp").forward(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void saveAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        Address address = Mapper.mapToAddress(params); // Usar Mapper

        try {
            if (address.getId() == null) {
                addressService.createAddress(address);
                request.getSession().setAttribute("success", "Endereço criado com sucesso!");
            } else {
                addressService.updateAddress(address);
                request.getSession().setAttribute("success", "Endereço atualizado com sucesso!");
            }
            response.sendRedirect(request.getContextPath() + "/addresses/byClient/" + address.getClientId());
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            List<Client> clients = clientService.getAllClients(); // Recarrega clientes para o formulário
            request.setAttribute("address", address); // Mantém os dados preenchidos
            request.setAttribute("clients", clients);
            request.getRequestDispatcher("/pages/addresses/new.jsp").forward(request, response);
        }
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Address> addressToDelete = addressService.getAddressById(id);
        if (addressToDelete.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endereço não encontrado para exclusão.");
            return;
        }
        Integer clientId = addressToDelete.get().getClientId();
        addressService.deleteAddress(id);
        request.getSession().setAttribute("success", "Endereço deletado com sucesso!");
        response.sendRedirect(request.getContextPath() + "/addresses/byClient/" + clientId);
    }

    private void setPrincipalAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException {
        Integer addressId = extractId(pathInfo);
        Integer clientId = Integer.parseInt(request.getParameter("clientId")); // Precisa vir do formulário ou URL

        try {
            addressService.setPrincipalAddress(clientId, addressId);
            request.getSession().setAttribute("success", "Endereço principal definido com sucesso!");
            response.sendRedirect(request.getContextPath() + "/addresses/byClient/" + clientId);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/addresses/byClient/" + clientId);
        }
    }

    // Método auxiliar para construir um objeto Address a partir dos parâmetros da requisição (usando Mapper)
    private Address buildAddressFromRequest(HttpServletRequest request) {
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, value) -> params.put(key, value[0]));
        return Mapper.mapToAddress(params);
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
