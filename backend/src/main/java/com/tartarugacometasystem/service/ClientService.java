package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.ClientDAO;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.util.DateFormatter;
import com.tartarugacometasystem.util.Validator;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    /**
     * Cria um novo cliente.
     *
     * @param client O objeto Client a ser criado.
     * @return O objeto Client criado com o ID.
     * @throws SQLException            Se ocorrer um erro de SQL.
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
        client.ifPresent(this::enrichClient);
        return client;
    }

    /**
     * Atualiza um cliente existente.
     *
     * @param client O objeto Client a ser atualizado.
     * @throws SQLException            Se ocorrer um erro de SQL.
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
     * Busca clientes por termo de busca (nome, documento, email), enriquecendo-os com dados formatados.
     *
     * @param searchTerm O termo de busca.
     * @return Uma lista de clientes que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Client> search(String searchTerm) throws SQLException { // Renomeado de searchClientsByName
        List<Client> clients = clientDAO.search(searchTerm); // Chama o novo método search do DAO
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
            throw new IllegalArgumentException("Nome do cliente é obrigatório.");
        }
        if (client.getDocument() == null || client.getDocument().trim().isEmpty()) {
            throw new IllegalArgumentException("Documento do cliente é obrigatório.");
        }

        if (client.getPersonType() == null) {
            throw new IllegalArgumentException("Tipo de pessoa é obrigatório.");
        }

        // Validação de documento baseada no tipo de pessoa
        if (client.getPersonType() == PersonType.INDIVIDUAL) {
            if (!Validator.isValidCPF(client.getDocument())) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        } else if (client.getPersonType() == PersonType.LEGAL_ENTITY) {
            if (!Validator.isValidCNPJ(client.getDocument())) {
                throw new IllegalArgumentException("CNPJ inválido.");
            }
        }

        if (client.getEmail() == null || client.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email do cliente é obrigatório.");
        }
        if (!Validator.isValidEmail(client.getEmail())) { // Usando Validator
            throw new IllegalArgumentException("Email inválido.");
        }

        if (client.getPhone() == null || client.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Telefone do cliente é obrigatório.");
        }
        if (!Validator.isValidPhone(client.getPhone())) { // Usando Validator
            throw new IllegalArgumentException("Telefone inválido.");
        }
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
        // NOVO: Define o tipo de pessoa formatado
        if (client.getPersonType() != null) {
            client.setFormattedPersonType(client.getPersonType().getLabel());
        }
    }
}
