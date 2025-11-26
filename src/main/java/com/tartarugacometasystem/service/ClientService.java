package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.ClientDAO;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import com.tartarugacometasystem.util.Validator;

public class ClientService {
    private final ClientDAO clientDAO;

    public ClientService() {
        this.clientDAO = new ClientDAO();
    }

    public void createClient(Client client) throws SQLException, IllegalArgumentException {
        validateClient(client);

        Optional<Client> existing = clientDAO.findByDocument(client.getDocument());
        if (existing.isPresent()) {
            throw new IllegalArgumentException("Cliente com este documento já existe");
        }

        clientDAO.save(client);
    }

    public void updateClient(Client client) throws SQLException, IllegalArgumentException {
        validateClient(client);

        if (client.getId() == null) {
            throw new IllegalArgumentException("ID do cliente é obrigatório para atualização");
        }

        clientDAO.update(client);
    }

    public void deleteClient(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        clientDAO.delete(clientId);
    }

    public Optional<Client> getClientById(Integer clientId) throws SQLException {
        if (clientId == null || clientId <= 0) {
            throw new IllegalArgumentException("ID do cliente inválido");
        }

        return clientDAO.findById(clientId);
    }

    public Optional<Client> getClientByDocument(String document) throws SQLException {
        if (!Validator.isValidDocument(document)) {
            throw new IllegalArgumentException("Documento inválido");
        }

        return clientDAO.findByDocument(document);
    }

    public List<Client> getAllClients() throws SQLException {
        return clientDAO.findAll();
    }

    public List<Client> getClientsByPersonType(PersonType personType) throws SQLException {
        if (personType == null) {
            throw new IllegalArgumentException("Tipo de pessoa é obrigatório");
        }

        return clientDAO.findByPersonType(personType);
    }

    public List<Client> searchClientsByName(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de busca não pode estar vazio");
        }

        return clientDAO.findByName(name.trim());
    }

    private void validateClient(Client client) {
        if (client.getPersonType() == null) {
            throw new IllegalArgumentException("Tipo de pessoa é obrigatório");
        }

        if (client.getDocument() == null || client.getDocument().trim().isEmpty()) {
            throw new IllegalArgumentException("Documento é obrigatório");
        }

        if (!Validator.isValidDocument(client.getDocument())) {
            throw new IllegalArgumentException("Documento inválido");
        }

        if (client.getName() == null || client.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        if (client.getEmail() != null && !client.getEmail().trim().isEmpty()) {
            if (!Validator.isValidEmail(client.getEmail())) {
                throw new IllegalArgumentException("Email inválido");
            }
        }
    }
}
