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

    public Client createClient(Client client) throws SQLException {
        validateClient(client);
        return clientDAO.save(client);
    }

    public Optional<Client> getClientById(Integer id) throws SQLException {
        Optional<Client> client = clientDAO.findById(id);
        client.ifPresent(this::enrichClient);
        return client;
    }

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

    public void deleteClient(Integer id) throws SQLException {
        clientDAO.delete(id);
    }

    public List<Client> getAllClients() throws SQLException {
        List<Client> clients = clientDAO.getAll();
        clients.forEach(this::enrichClient);
        return clients;
    }

    public List<Client> search(String searchTerm) throws SQLException {
        List<Client> clients = clientDAO.search(searchTerm);
        clients.forEach(this::enrichClient);
        return clients;
    }

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

        // Email é OPCIONAL - só valida se preenchido
        if (client.getEmail() != null && !client.getEmail().trim().isEmpty()) {
            if (!Validator.isValidEmail(client.getEmail())) {
                throw new IllegalArgumentException("Email inválido.");
            }
        }

        // Telefone é OPCIONAL - só valida se preenchido
        if (client.getPhone() != null && !client.getPhone().trim().isEmpty()) {
            if (!Validator.isValidPhone(client.getPhone())) {
                throw new IllegalArgumentException("Telefone inválido.");
            }
        }
    }

    private void enrichClient(Client client) {
        if (client == null) return;

        if (client.getCreatedAt() != null) {
            client.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(client.getCreatedAt()));
        }
        if (client.getUpdatedAt() != null) {
            client.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(client.getUpdatedAt()));
        }
        if (client.getPersonType() != null) {
            client.setFormattedPersonType(client.getPersonType().getLabel());
        }
    }
}