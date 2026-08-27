package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.ClienteDAO;
import com.tartarugacometasystem.model.Cliente;
import br.com.tartarugacometa.enums.TipoPessoa;
import com.tartarugacometasystem.util.DateFormatter;
import com.tartarugacometasystem.util.Validator;

public class ClienteBO {
    private final ClienteDAO clientDAO;

    public ClienteBO() {
        this.clientDAO = new ClienteDAO();
    }

    public Cliente createClient(Cliente client) throws SQLException {
        validateClient(client);
        return clientDAO.save(client);
    }

    public Optional<Cliente> getClientById(Integer id) throws SQLException {
        Optional<Cliente> client = clientDAO.findById(id);
        client.ifPresent(this::enrichClient);
        return client;
    }

    public void updateClient(Cliente client) throws SQLException {
        if (client.getId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização.");
        }
        validateClient(client);
        Optional<Cliente> existingClient = clientDAO.findById(client.getId());
        if (existingClient.isEmpty()) {
            throw new IllegalArgumentException("Cliente com ID " + client.getId() + " não encontrado.");
        }
        clientDAO.update(client);
    }

    public void deleteClient(Integer id) throws SQLException {
        clientDAO.delete(id);
    }

    public List<Cliente> getAllClients() throws SQLException {
        List<Cliente> clients = clientDAO.getAll();
        clients.forEach(this::enrichClient);
        return clients;
    }

    public List<Cliente> search(String searchTerm) throws SQLException {
        List<Cliente> clients = clientDAO.search(searchTerm);
        clients.forEach(this::enrichClient);
        return clients;
    }

    private void validateClient(Cliente client) {
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
        if (client.getPersonType() == TipoPessoa.FISICA) {
            if (!Validator.isValidCPF(client.getDocument())) {
                throw new IllegalArgumentException("CPF inválido.");
            }
        } else if (client.getPersonType() == TipoPessoa.JURIDICA) {
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

    private void enrichClient(Cliente client) {
        if (client == null) return;

        if (client.getCreatedAt() != null) {
            client.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(client.getCreatedAt()));
        }
        if (client.getUpdatedAt() != null) {
            client.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(client.getUpdatedAt()));
        }
        if (client.getPersonType() != null) {
            client.setFormattedPersonType(client.getPersonType().getRotulo());
        }
    }
}