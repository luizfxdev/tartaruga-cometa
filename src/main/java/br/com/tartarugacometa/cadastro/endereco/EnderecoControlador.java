package br.com.tartarugacometa.cadastro.endereco;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.enums.TipoEndereco;
import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.endereco.EnderecoBO;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.util.Mapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/endereco/*")
public class EnderecoControlador extends HttpServlet {

    private static final Logger LOG = Logger.getLogger(EnderecoControlador.class.getName());

    private EnderecoBO addressService;
    private ClienteBO clientService;

    @Override
    public void init() throws ServletException {
        try {
            addressService = new EnderecoBO();
            clientService = new ClienteBO();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Falha ao inicializar EnderecoControlador", e);
            throw new ServletException("Erro ao inicializar EnderecoControlador", e);
        }
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Falha de banco de dados em EnderecoControlador", e);
            throw new ServletException("Erro de banco de dados no EnderecoControlador", e);
        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Falha inesperada em EnderecoControlador", e);
            throw new ServletException("Erro inesperado no EnderecoControlador", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.equals("/save")) {
                saveAddress(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rota POST inválida.");
            }

        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Falha de banco de dados no doPost de EnderecoControlador", e);
            throw new ServletException("Erro ao processar operação no EnderecoControlador", e);

        } catch (IllegalArgumentException e) {
            request.getSession().setAttribute("error", e.getMessage());

            try {
                Endereco addressWithError = Mapper.mapToAddress(request);
                request.setAttribute("address", addressWithError);

                request.setAttribute("allClients", clientService.getAllClients());
                request.setAttribute("addressTypes", TipoEndereco.values());

                if (addressWithError.getClientId() != null) {
                    clientService.getClientById(addressWithError.getClientId())
                                 .ifPresent(client -> request.setAttribute("client", client));
                }

                request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp").forward(request, response);
            } catch (Exception ex) {
                LOG.log(Level.SEVERE, "Falha ao redirecionar após erro de validação", ex);
                throw new ServletException("Erro ao processar validação.", ex);
            }
        }
    }

    private void saveAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        Endereco address = Mapper.mapToAddress(request);

        if (address.getClientId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório.");
        }

        if (address.getId() != null) {
            addressService.updateAddress(address);
        } else {
            addressService.createAddress(address);
        }

        response.sendRedirect(request.getContextPath() + "/endereco/client/" + address.getClientId());
    }

    private void listAddresses(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Endereco> addresses = addressService.getAllAddresses();

        request.setAttribute("addresses", addresses);
        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/lista.jsp")
                .forward(request, response);
    }

    private void listAddressesByClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String clientIdStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);

        if (!clientIdStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do cliente inválido.");
            return;
        }

        int clientId = Integer.parseInt(clientIdStr);

        Optional<Cliente> clientOpt = clientService.getClientById(clientId);

        List<Endereco> addresses = addressService.getAddressesByClientId(clientId);

        request.setAttribute("addresses", addresses);
        request.setAttribute("clientId", clientId);

        if (clientOpt.isPresent()) {
            request.setAttribute("client", clientOpt.get());
        }

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/lista.jsp")
                .forward(request, response);
    }

    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        List<Cliente> allClients = clientService.getAllClients();

        request.setAttribute("allClients", allClients);
        request.setAttribute("addressTypes", TipoEndereco.values());

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
                .forward(request, response);
    }

    private void showNewFormForClient(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String clientIdStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);

        if (!clientIdStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do cliente inválido.");
            return;
        }

        int clientId = Integer.parseInt(clientIdStr);
        Optional<Cliente> clientOpt = clientService.getClientById(clientId);

        if (clientOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cliente não encontrado.");
            return;
        }

        request.setAttribute("client", clientOpt.get());
        request.setAttribute("clientId", clientId);
        request.setAttribute("addressTypes", TipoEndereco.values());
        request.setAttribute("allClients", clientService.getAllClients());

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
                .forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);

        if (!idStr.matches("\\d+")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID de endereço inválido.");
            return;
        }

        int id = Integer.parseInt(idStr);
        Optional<Endereco> addressOpt = addressService.getAddressById(id);

        if (addressOpt.isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Endereço não encontrado.");
            return;
        }

        Endereco address = addressOpt.get();
        Optional<Cliente> clientOpt = clientService.getClientById(address.getClientId());

        request.setAttribute("address", address);
        if (clientOpt.isPresent()) {
            request.setAttribute("client", clientOpt.get());
        }
        request.setAttribute("addressTypes", TipoEndereco.values());
        request.setAttribute("allClients", clientService.getAllClients());

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
                .forward(request, response);
    }

    private void deleteAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, ServletException, IOException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);

        if (!idStr.matches("\\d+")) {
            request.setAttribute("error", "ID inválido para exclusão.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int id = Integer.parseInt(idStr);

        Optional<Endereco> addressOpt = addressService.getAddressById(id);
        int clientId = addressOpt.isPresent() ? addressOpt.get().getClientId() : 0;

        addressService.deleteAddress(id);

        if (clientId > 0) {
            response.sendRedirect(request.getContextPath() + "/endereco/client/" + clientId);
        } else {
            response.sendRedirect(request.getContextPath() + "/endereco/");
        }
    }

    private void setMainAddress(HttpServletRequest request, HttpServletResponse response, String pathInfo)
            throws SQLException, IOException, ServletException {

        String idStr = pathInfo.substring(pathInfo.lastIndexOf('/') + 1);

        if (!idStr.matches("\\d+")) {
            request.setAttribute("error", "ID inválido para definir endereço principal.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int addressId = Integer.parseInt(idStr);

        Optional<Endereco> addressOpt = addressService.getAddressById(addressId);
        if (addressOpt.isEmpty()) {
            request.setAttribute("error", "Endereço não encontrado.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
            return;
        }

        int clientId = addressOpt.get().getClientId();

        addressService.setMainAddress(clientId, addressId);

        response.sendRedirect(request.getContextPath() + "/endereco/client/" + clientId);
    }
}
