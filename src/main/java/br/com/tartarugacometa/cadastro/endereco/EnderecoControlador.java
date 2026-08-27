package br.com.tartarugacometa.cadastro.endereco;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.enums.TipoEndereco;
import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.endereco.EnderecoBO;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.util.Mapper;

import jakarta.servlet.ServletException; // Importe o Mapper do pacote correto
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/endereco/*")
public class EnderecoControlador extends HttpServlet {

    private EnderecoBO addressService;
    private ClienteBO clientService;

    @Override
    public void init() throws ServletException {
        try {
            addressService = new EnderecoBO();
            clientService = new ClienteBO();
            System.out.println("✅ EnderecoControlador inicializado com sucesso");
        } catch (Exception e) {
            System.err.println("❌ Erro ao inicializar EnderecoControlador: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro ao inicializar EnderecoControlador", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        System.out.println("🔍 EnderecoControlador.doGet - pathInfo: " + pathInfo);

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
            System.err.println("❌ Erro de SQL no EnderecoControlador: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro de banco de dados no EnderecoControlador", e);
        } catch (IllegalArgumentException e) {
            System.err.println("❌ Argumento inválido: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            System.err.println("❌ Erro inesperado no EnderecoControlador: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro inesperado no EnderecoControlador", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        System.out.println("🔍 EnderecoControlador.doPost - pathInfo: " + pathInfo);

        try {
            if (pathInfo != null && pathInfo.equals("/save")) {
                saveAddress(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Rota POST inválida.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erro de SQL no doPost: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException("Erro ao processar operação no EnderecoControlador", e);

        } catch (IllegalArgumentException e) {
            System.err.println("❌ Argumento inválido no doPost: " + e.getMessage());
            e.printStackTrace();
            request.getSession().setAttribute("error", e.getMessage());

            // Ao redirecionar após erro de validação, precisamos repassar os dados
            // para que o formulário possa ser preenchido novamente e o dropdown de clientes funcione.
            try {
                // Mapeia o endereço do request para preencher os campos do formulário
                Endereco addressWithError = Mapper.mapToAddress(request); // Usa o Mapper corrigido
                request.setAttribute("address", addressWithError);

                // Carrega todos os clientes para o dropdown
                request.setAttribute("allClients", clientService.getAllClients());
                // Carrega os tipos de endereço
                request.setAttribute("addressTypes", TipoEndereco.values());

                // Se o erro foi em um formulário de edição, tenta manter o cliente associado
                if (addressWithError.getClientId() != null) {
                    clientService.getClientById(addressWithError.getClientId())
                                 .ifPresent(client -> request.setAttribute("client", client));
                }

                request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp").forward(request, response);
            } catch (Exception ex) {
                System.err.println("❌ Erro ao redirecionar após erro de validação: " + ex.getMessage());
                ex.printStackTrace();
                throw new ServletException("Erro ao processar validação.", ex);
            }
        }
    }

    // SAVE (INSERT OR UPDATE)
    private void saveAddress(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        // Não é mais necessário criar o HashMap aqui, o Mapper.mapToAddress(request) já faz isso
        Endereco address = Mapper.mapToAddress(request); // Usa o Mapper corrigido

        // Validação do clientId no servidor ANTES de tentar salvar
        if (address.getClientId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório.");
        }

        if (address.getId() != null) { // Verifica se o ID do endereço existe para UPDATE
            // UPDATE
            System.out.println("✏️ Atualizando endereço ID: " + address.getId());
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
        List<Endereco> addresses = addressService.getAllAddresses();
        System.out.println("📋 Total de endereços encontrados: " + addresses.size());

        request.setAttribute("addresses", addresses);
        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/lista.jsp")
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
        Optional<Cliente> clientOpt = clientService.getClientById(clientId);

        List<Endereco> addresses = addressService.getAddressesByClientId(clientId);
        System.out.println("📋 Total de endereços do cliente " + clientId + ": " + addresses.size());

        request.setAttribute("addresses", addresses);
        request.setAttribute("clientId", clientId);

        if (clientOpt.isPresent()) {
            request.setAttribute("client", clientOpt.get());
        }

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/lista.jsp")
                .forward(request, response);
    }

    // FORMS
    private void showNewForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        System.out.println("📝 Exibindo formulário de novo endereço (sem cliente pré-selecionado)");

        try {
            List<Cliente> allClients = clientService.getAllClients();
            System.out.println("📝 Total de clientes carregados: " + (allClients != null ? allClients.size() : 0));

            request.setAttribute("allClients", allClients);
            request.setAttribute("addressTypes", TipoEndereco.values());

            request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
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
        Optional<Cliente> clientOpt = clientService.getClientById(clientId);

        if (clientOpt.isEmpty()) {
            System.err.println("❌ Cliente não encontrado: " + clientId);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Cliente não encontrado.");
            return;
        }

        try {
            request.setAttribute("client", clientOpt.get());
            request.setAttribute("clientId", clientId);
            request.setAttribute("addressTypes", TipoEndereco.values());
            request.setAttribute("allClients", clientService.getAllClients()); // Garante que allClients esteja disponível para o dropdown caso o usuário queira mudar

            request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
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
        Optional<Endereco> addressOpt = addressService.getAddressById(id);

        if (addressOpt.isEmpty()) {
            System.err.println("❌ Endereço não encontrado: " + id);
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
        request.setAttribute("allClients", clientService.getAllClients()); // Garante que allClients esteja disponível para o dropdown

        request.getRequestDispatcher("/WEB-INF/views/cadastro/endereco/form.jsp")
                .forward(request, response);
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
        Optional<Endereco> addressOpt = addressService.getAddressById(id);
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

        Optional<Endereco> addressOpt = addressService.getAddressById(addressId);
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
