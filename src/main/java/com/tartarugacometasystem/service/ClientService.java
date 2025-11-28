package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.ClientDAO;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType; // Importar PersonType
import com.tartarugacometasystem.util.DateFormatter;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ClientService {
    private ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    /**
     * Cria um novo cliente.
     *
     * @param client O objeto Client a ser criado.
     * @return O objeto Client criado com o ID.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o cliente for inválido.
     */
    public Client createClient(Client client) throws SQLException {
        validateClient(client);
        return clientDAO.save(client);
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id O ID do cliente.
     * @return Um Optional contendo o Client se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Client> getClientById(Integer id) throws SQLException {
        Optional<Client> client = clientDAO.findById(id);
        client.ifPresent(this::enrichClient); // Enriquecer se presente
        return client;
    }

    /**
     * Atualiza um cliente existente.
     *
     * @param client O objeto Client a ser atualizado.
     * @throws SQLException           Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o cliente for inválido ou não existir.
     */
    public void updateClient(Client client) throws SQLException {
        if (client.getId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização.");
        }
        validateClient(client);
        Optional<Client> existingClient = clientDAO.findById(client.getId());
        if (existingClient.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + client.getId() + " não encontrado.");
        }
        clientDAO.update(client);
    }

    /**
     * Deleta um cliente pelo ID.
     *
     * @param id O ID do cliente a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteClient(Integer id) throws SQLException {
        clientDAO.delete(id);
    }

    /**
     * Busca todos os clientes, enriquecendo-os com dados formatados.
     *
     * @return Uma lista de todos os clientes.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = clientDAO.getAll();
        clients.forEach(this::enrichClient);
        return clients;
    }

    /**
     * Busca clientes por nome (ou parte do nome), enriquecendo-os com dados formatados.
     *
     * @param searchTerm O termo de busca.
     * @return Uma lista de clientes que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Client> searchClientsByName(String searchTerm) throws SQLException {
        List<Client> clients = clientDAO.searchByName(searchTerm);
        clients.forEach(this::enrichClient);
        return clients;
    }

    /**
     * Valida os campos de um cliente.
     *
     * @param client O objeto Client a ser validado.
     * @throws IllegalArgumentException Se algum campo for inválido.
     */
    private void validateClient(Client client) {
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório.");
        }
        if (client.getDocument() == null || client.getDocument().trim().isEmpty()) {
            throw new IllegalArgumentException("Documento (CPF/CNPJ) é obrigatório.");
        }
        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório.");
        }
        if (!isValidEmail(client.getEmail())) {
            throw new IllegalArgumentException("Formato de email inválido.");
        }
        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone é obrigatório.");
        }
        if (client.getPersonType() == null) {
            throw new IllegalArgumentException("Tipo de pessoa é obrigatório.");
        }
        // Adicionar validação de CPF/CNPJ se necessário
    }

    /**
     * Valida o formato de um email.
     *
     * @param email O email a ser validado.
     * @return true se o email for válido, false caso contrário.
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Enriquecer um objeto Client com dados formatados.
     *
     * @param client O objeto Client a ser enriquecido.
     */
    private void enrichClient(Client client) {
        if (client == null) return;

        if (client.getCreatedAt() != null) {
            client.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(client.getCreatedAt()));
        }
        if (client.getUpdatedAt() != null) {
            client.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(client.getUpdatedAt()));
        }
        // Você pode adicionar mais enriquecimentos aqui, como formatar o documento, etc.
    }
}
