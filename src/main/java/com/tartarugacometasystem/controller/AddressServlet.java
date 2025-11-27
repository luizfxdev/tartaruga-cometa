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

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.service.AddressService;
import com.tartarugacometasystem.service.ClientService;
import com.tartarugacometasystem.util.Mapper;

@WebServlet("/addresses/*")
public class AddressServlet extends HttpServlet {
    private AddressService addressService;
    private ClientService clientService;

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
            } else if (pathInfo.startsWith("/client/")) {
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
            if (pathInfo.equals("/save")) {
                saveAddress(request, response);
            } else if (pathInfo.startsWith("/delete/")) {
                deleteAddress(request, response, pathInfo);
            } else if (pathInfo.startsWith("/set-principal/")) {
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
        String clientId = request.getParameter("clientId");

        if (clientId != null && !clientId.isEmpty()) {
            Integer id = Integer.parseInt(clientId);
            List<Address> addresses = addressService.getAddressesByClientId(id);
            Optional<Client> client = clientService.getClientById(id);

            request.setAttribute("addresses", addresses);
            request.setAttribute("client", client.orElse(null));
        }

        request.getRequestDispatcher("/pages/addresses/list.jsp").forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, ServletException, IOException {
        String clientId = request.getParameter("clientId");

        if (clientId != null && !clientId.isEmpty()) {
            Integer id = Integer.parseInt(clientId);
            Optional<Client> clientOptional = clientService.getClientById(id);
            Client client = clientOptional.orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

            request.setAttribute("client", client);
            request.setAttribute("addressTypes", AddressType.values());
        }

        request.getRequestDispatcher("/pages/addresses/new.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        Integer id = extractId(pathInfo);
        Optional<Address> addressOptional = addressService.getAddressById(id);
        Address address = addressOptional.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

        Optional<Client> client = clientService.getClientById(address.getClientId());

        request.setAttribute("address", address);
        request.setAttribute("client", client.orElse(null));
        request.setAttribute("addressTypes", AddressType.values());
        request.getRequestDispatcher("/pages/addresses/new.jsp").forward(request, response);
    }

    private void listAddressesByClient(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, ServletException, IOException {
        Integer clientId = extractId(pathInfo);
        List<Address> addresses = addressService.getAddressesByClientId(clientId);
        Optional<Client> client = clientService.getClientById(clientId);

        request.setAttribute("addresses", addresses);
        request.setAttribute("client", client.orElse(null));
        request.getRequestDispatcher("/pages/addresses/list.jsp").forward(request, response);
    }

    private void saveAddress(HttpServletRequest request, HttpServletResponse response) 
            throws SQLException, IOException {
        try {
            HashMap<String, String> params = new HashMap<>();
            params.put("id", request.getParameter("id"));
            params.put("clientId", request.getParameter("clientId"));
            params.put("addressType", request.getParameter("addressType"));
            params.put("street", request.getParameter("street"));
            params.put("number", request.getParameter("number"));
            params.put("complement", request.getParameter("complement"));
            params.put("neighborhood", request.getParameter("neighborhood"));
            params.put("city", request.getParameter("city"));
            params.put("state", request.getParameter("state"));
            params.put("zipCode", request.getParameter("zipCode"));
            params.put("reference", request.getParameter("reference"));
            params.put("isPrincipal", request.getParameter("isPrincipal"));

            Address address = Mapper.mapToAddress(params);

            if (address.getId() != null && address.getId() > 0) {
                addressService.updateAddress(address);
                request.getSession().setAttribute("success", "Endereço atualizado com sucesso");
            } else {
                addressService.createAddress(address);
                request.getSession().setAttribute("success", "Endereço criado com sucesso");
            }

            response.sendRedirect(request.getContextPath() + "/addresses/client/" + address.getClientId());
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/addresses/new?clientId=" + request.getParameter("clientId"));
        }
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer id = extractId(pathInfo);
            Optional<Address> addressOptional = addressService.getAddressById(id);
            Address address = addressOptional.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

            Integer clientId = address.getClientId();
            addressService.deleteAddress(id);
            request.getSession().setAttribute("success", "Endereço deletado com sucesso");
            response.sendRedirect(request.getContextPath() + "/addresses/client/" + clientId);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/addresses/");
        }
    }

    private void setPrincipalAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo) 
            throws SQLException, IOException {
        try {
            Integer addressId = extractId(pathInfo);
            Optional<Address> addressOptional = addressService.getAddressById(addressId);
            Address address = addressOptional.orElseThrow(() -> new IllegalArgumentException("Endereço não encontrado"));

            Integer clientId = address.getClientId();
            addressService.setPrincipalAddress(clientId, addressId);
            request.getSession().setAttribute("success", "Endereço principal atualizado com sucesso");
            response.sendRedirect(request.getContextPath() + "/addresses/client/" + clientId);
        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());
            response.sendRedirect(request.getContextPath() + "/addresses/");
        }
    }

    private Integer extractId(String pathInfo) {
        String[] parts = pathInfo.split("/");
        return Integer.parseInt(parts[parts.length - 1]);
    }
}
