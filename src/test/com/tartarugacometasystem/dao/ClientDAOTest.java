package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Cliente;
import br.com.tartarugacometa.enums.TipoPessoa;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClientDAOTest {

    private ClienteDAO clientDAO;
    private Cliente testClient;

    @Before
    public void setUp() {
        clientDAO = new ClienteDAO();
        testClient = new Cliente();
        testClient.setPersonType(TipoPessoa.FISICA);
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
            Optional<Cliente> retrieved = clientDAO.getById(testClient.getId());
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

            Optional<Cliente> updated = clientDAO.getById(testClient.getId());
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

            Optional<Cliente> deleted = clientDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllClients() {
        try {
            List<Cliente> clients = clientDAO.getAll();
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
            List<Cliente> results = clientDAO.searchByName("João");
            assertNotNull(results);
            assertTrue(results.size() > 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetClientByInvalidId() {
        try {
            Optional<Cliente> result = clientDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
