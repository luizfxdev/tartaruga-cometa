package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClientDAOTest {

    private ClientDAO clientDAO;
    private Client testClient;

    @Before
    public void setUp() {
        clientDAO = new ClientDAO();
        testClient = new Client();
        testClient.setPersonType(PersonType.FISICA);
        testClient.setDocument("12345678901");
        testClient.setName("João Silva");
        testClient.setEmail("joao@example.com");
        testClient.setPhone("11999999999");
    }

    @Test
    public void testCreateClient() {
        try {
            clientDAO.create(testClient);
            assertNotNull(testClient.getId());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetClientById() {
        try {
            clientDAO.create(testClient);
            Optional<Client> retrieved = clientDAO.getById(testClient.getId());
            assertTrue(retrieved.isPresent());
            assertEquals(testClient.getName(), retrieved.get().getName());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateClient() {
        try {
            clientDAO.create(testClient);
            testClient.setName("João Silva Atualizado");
            clientDAO.update(testClient);

            Optional<Client> updated = clientDAO.getById(testClient.getId());
            assertTrue(updated.isPresent());
            assertEquals("João Silva Atualizado", updated.get().getName());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteClient() {
        try {
            clientDAO.create(testClient);
            Integer id = testClient.getId();
            clientDAO.delete(id);

            Optional<Client> deleted = clientDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllClients() {
        try {
            List<Client> clients = clientDAO.getAll();
            assertNotNull(clients);
            assertTrue(clients.size() >= 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testSearchClientByName() {
        try {
            clientDAO.create(testClient);
            List<Client> results = clientDAO.searchByName("João");
            assertNotNull(results);
            assertTrue(results.size() > 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetClientByInvalidId() {
        try {
            Optional<Client> result = clientDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
