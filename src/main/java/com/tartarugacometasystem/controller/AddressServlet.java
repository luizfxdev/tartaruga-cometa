package com.tartarugacometasystem.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
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
    private ClientService clientService;

    @Override
    public void init() throws ServletException {
        try {
            addressService = new AddressService();
            clientService = new ClientService();
            System.out.println("✅ AddressServlet inicializado com sucesso");
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar AddressServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro ao inicializar AddressServlet", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        System.out.println("🔍 AddressServlet.doGet - pathInfo: " + pathInfo);

        try {
            if (pathInfo == null || pathInfo.equals("/")) {
                listAddresses(request, response);

            } else if (pathInfo.equals("/new")) {
                showNewForm(request, response);

            } else if (pathInfo.matches("/new/\\d+")) {
                showNewFormForClient(request, response, pathInfo);

            } else if (pathInfo.matches("/edit/\\d+")) {
                showEditForm(request, response, pathInfo);

            } else if (pathInfo.matches("/delete/\\d+")) {
                deleteAddress(request, response, pathInfo);

            } else if (pathInfo.matches("/set-principal/\\d+")) {
                setMainAddress(request, response, pathInfo);

            } else if (pathInfo.matches("/client/\\d+")) {
                listAddressesByClient(request, response, pathInfo);

            } else {
                System.err.println("❌ Path não encontrado: " + pathInfo);
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de SQL no AddressServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro de banco de dados no AddressServlet", e);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Argumento inválido: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado no AddressServlet: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro inesperado no AddressServlet", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        System.out.println("🔍 AddressServlet.doPost - pathInfo: " + pathInfo);

        try {
            if (pathInfo != null && pathInfo.equals("/save")) {
                saveAddress(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rota POST inválida.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de SQL no doPost: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro ao processar operação no AddressServlet", e);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Argumento inválido no doPost: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", e.getMessage());

            try {
                String id = request.getParameter("id");
                
                if (id != null && !id.isEmpty() && id.matches("\\d+")) {
                    // Está editando - redireciona para o formulário de edição
                    response.sendRedirect(request.getContextPath() + "/addresses/edit/" + id);
                } else {
                    // Está criando novo - redireciona para o formulário de novo
                    String clientId = request.getParameter("clientId");
                    if (clientId != null && !clientId.isEmpty()) {
                        response.sendRedirect(request.getContextPath() + "/addresses/new/" + clientId);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/addresses/new");
                    }
                }
            } catch (Exception ex) {
                System.err.println("❌ Erro ao redirecionar após erro de validação: " + ex.getMessage());
                ex.printStackTrace();
                throw new ServletException("Erro ao processar validação.", ex);
            }
        }
    }

    // SAVE (INSERT OR UPDATE)
    private void saveAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        String idStr = request.getParameter("id");
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));

        Address address = Mapper.mapToAddress(params);

        if (idStr != null && !idStr.isEmpty() && idStr.matches("\\d+")) {
            // UPDATE
            System.out.println("✏️ Atualizando endereço ID: " + idStr);
            addressService.updateAddress(address);
            System.out.println("✅ Endereço atualizado com sucesso");
        } else {
            // INSERT
            System.out.println("➕ Inserindo novo endereço");
            addressService.createAddress(address);
            System.out.println("✅ Endereço inserido com sucesso");
        }

        response.sendRedirect(request.getContextPath() + "/addresses/client/" + address.getClientId());
    }

    // LIST
    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        System.out.println("📋 Listando todos os endereços");
        List<Address> addresses = addressService.getAllAddresses();
        System.out.println("📋 Total de endereços encontrados: " + addresses.size());
        
        request.setAttribute("addresses", addresses);
        request.getRequestDispatcher("/pages/addresses/list.jsp")
                .forward(request, response);
    }

    private void listAddressesByClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String clientIdStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
        System.out.println("📋 Listando endereços do cliente: " + clientIdStr);

        if (!clientIdStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do cliente inválido.");
            return;
        }

        int clientId = Integer.parseInt(clientIdStr);
        
        // Busca o cliente para exibir o nome
        Optional<Client> clientOpt = clientService.getClientById(clientId);
        
        List<Address> addresses = addressService.getAddressesByClientId(clientId);
        System.out.println("📋 Total de endereços do cliente " + clientId + ": " + addresses.size());

        request.setAttribute("addresses", addresses);
        request.setAttribute("clientId", clientId);
        
        if (clientOpt.isPresent()) {
            request.setAttribute("client", clientOpt.get());
        }

        request.getRequestDispatcher("/pages/addresses/list.jsp")
                .forward(request, response);
    }

    // FORMS
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        System.out.println("📝 Exibindo formulário de novo endereço (sem cliente pré-selecionado)");
        
        try {
            List<Client> clients = clientService.getAllClients();
            System.out.println("📝 Total de clientes carregados: " + (clients != null ? clients.size() : 0));
            
            request.setAttribute("clients", clients);
            request.setAttribute("addressTypes", AddressType.values());

            request.getRequestDispatcher("/pages/addresses/new.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar formulário de novo endereço: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void showNewFormForClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String clientIdStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
        System.out.println("📝 Exibindo formulário de novo endereço para cliente: " + clientIdStr);

        if (!clientIdStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do cliente inválido.");
            return;
        }

        int clientId = Integer.parseInt(clientIdStr);
        Optional<Client> clientOpt = clientService.getClientById(clientId);

        if (clientOpt.isEmpty()) {
            System.err.println("❌ Cliente não encontrado: " + clientId);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cliente não encontrado.");
            return;
        }

        try {
            request.setAttribute("client", clientOpt.get());
            request.setAttribute("addressTypes", AddressType.values());

            request.getRequestDispatcher("/pages/addresses/new.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            System.err.println("❌ Erro ao carregar formulário para cliente: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
        System.out.println("✏️ Exibindo formulário de edição para endereço: " + idStr);

        if (!idStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de endereço inválido.");
            return;
        }

        int id = Integer.parseInt(idStr);
        Optional<Address> addressOpt = addressService.getAddressById(id);

        if (addressOpt.isEmpty()) {
            System.err.println("❌ Endereço não encontrado: " + id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endereço não encontrado.");
            return;
        }

        Address address = addressOpt.get();
        Optional<Client> clientOpt = clientService.getClientById(address.getClientId());

        request.setAttribute("address", address);
        if (clientOpt.isPresent()) {
            request.setAttribute("client", clientOpt.get());
        }
        request.setAttribute("addressTypes", AddressType.values());

        request.getRequestDispatcher("/pages/addresses/new.jsp")
                .forward(request, response);
    }

    // INSERT
    private void insertAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        System.out.println("➕ Inserindo novo endereço");
        
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));

        Address address = Mapper.mapToAddress(params);
        addressService.createAddress(address);
        
        System.out.println("✅ Endereço inserido com sucesso");

        response.sendRedirect(request.getContextPath() + "/addresses/client/" + address.getClientId());
    }

    // UPDATE
    private void updateAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {

        System.out.println("✏️ Atualizando endereço");
        
        HashMap<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v[0]));

        Address address = Mapper.mapToAddress(params);
        addressService.updateAddress(address);
        
        System.out.println("✅ Endereço atualizado com sucesso");

        response.sendRedirect(request.getContextPath() + "/addresses/client/" + address.getClientId());
    }

    // DELETE
    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
        System.out.println("🗑️ Deletando endereço: " + idStr);

        if (!idStr.matches("\\d+")) {
            request.setAttribute("error", "ID inválido para exclusão.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int id = Integer.parseInt(idStr);
        
        // Pega o clientId antes de deletar
        Optional<Address> addressOpt = addressService.getAddressById(id);
        int clientId = addressOpt.isPresent() ? addressOpt.get().getClientId() : 0;
        
        addressService.deleteAddress(id);
        
        System.out.println("✅ Endereço deletado com sucesso: " + id);

        if (clientId > 0) {
            response.sendRedirect(request.getContextPath() + "/addresses/client/" + clientId);
        } else {
            response.sendRedirect(request.getContextPath() + "/addresses/");
        }
    }

    // MAIN ADDRESS
    private void setMainAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException, ServletException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
        System.out.println("⭐ Definindo endereço principal: " + idStr);

        if (!idStr.matches("\\d+")) {
            request.setAttribute("error", "ID inválido para definir endereço principal.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int addressId = Integer.parseInt(idStr);

        Optional<Address> addressOpt = addressService.getAddressById(addressId);
        if (addressOpt.isEmpty()) {
            System.err.println("❌ Endereço não encontrado para definir como principal: " + addressId);
            request.setAttribute("error", "Endereço não encontrado.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int clientId = addressOpt.get().getClientId();

        addressService.setMainAddress(clientId, addressId);
        
        System.out.println("✅ Endereço " + addressId + " definido como principal para cliente " + clientId);

        response.sendRedirect(request.getContextPath() + "/addresses/client/" + clientId);
    }
}