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
        testClient.setPersonType(PersonType.PF);
        testClient.setDocument("12345678901");
        testClient.setName("João Silva");
        testClient.setEmail("joao@test.com");
        testClient.setPhone("(11) 99999-9999");
    }

    @Test
    public void testCreateClient() throws Exception {
        Client created = clientDAO.create(testClient);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testFindClientById() throws Exception {
        Client created = clientDAO.create(testClient);
        Optional<Client> found = clientDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testClient.getName(), found.get().getName());
        assertEquals(testClient.getDocument(), found.get().getDocument());
    }

    @Test
    public void testFindAllClients() throws Exception {
        clientDAO.create(testClient);
        List<Client> clients = clientDAO.findAll();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client created = clientDAO.create(testClient);
        created.setName("João Silva Atualizado");
        created.setEmail("joao.novo@test.com");

        clientDAO.update(created);
        Optional<Client> updated = clientDAO.findById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals("João Silva Atualizado", updated.get().getName());
        assertEquals("joao.novo@test.com", updated.get().getEmail());
    }

    @Test
    public void testDeleteClient() throws Exception {
        Client created = clientDAO.create(testClient);
        Integer id = created.getId();

        clientDAO.delete(id);
        Optional<Client> deleted = clientDAO.findById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testFindClientByDocument() throws Exception {
        clientDAO.create(testClient);
        Optional<Client> found = clientDAO.findByDocument(testClient.getDocument());

        assertTrue(found.isPresent());
        assertEquals(testClient.getDocument(), found.get().getDocument());
    }

    @Test
    public void testFindClientsByType() throws Exception {
        clientDAO.create(testClient);
        List<Client> clients = clientDAO.findByPersonType(PersonType.PF);

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void testSearchClientByName() throws Exception {
        clientDAO.create(testClient);
        List<Client> clients = clientDAO.searchByName("João");

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void testCreateClientWithoutEmail() throws Exception {
        testClient.setEmail(null);
        Client created = clientDAO.create(testClient);

        assertNotNull(created.getId());
        assertNull(created.getEmail());
    }

    @Test
    public void testDuplicateDocument() throws Exception {
        clientDAO.create(testClient);

        Client duplicate = new Client();
        duplicate.setPersonType(PersonType.PF);
        duplicate.setDocument("12345678901");
        duplicate.setName("Outro Nome");

        assertThrows(Exception.class, () -> clientDAO.create(duplicate));
    }
}
