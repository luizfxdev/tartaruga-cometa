package com.tartarugacometasystem.service;

import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ClientServiceTest {

    private ClientService clientService;
    private Client testClient;

    @Before
    public void setUp() {
        clientService = new ClientService();
        testClient = new Client();
        testClient.setPersonType(PersonType.PF);
        testClient.setDocument("12345678901");
        testClient.setName("João Silva");
        testClient.setEmail("joao@test.com");
        testClient.setPhone("(11) 99999-9999");
    }

    @Test
    public void testCreateClient() throws Exception {
        Client created = clientService.createClient(testClient);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testGetClientById() throws Exception {
        Client created = clientService.createClient(testClient);
        Optional<Client> found = clientService.getClientById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testClient.getName(), found.get().getName());
    }

    @Test
    public void testGetAllClients() throws Exception {
        clientService.createClient(testClient);
        List<Client> clients = clientService.getAllClients();

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void testUpdateClient() throws Exception {
        Client created = clientService.createClient(testClient);
        created.setName("João Silva Atualizado");

        clientService.updateClient(created);
        Optional<Client> updated = clientService.getClientById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals("João Silva Atualizado", updated.get().getName());
    }

    @Test
    public void testDeleteClient() throws Exception {
        Client created = clientService.createClient(testClient);
        Integer id = created.getId();

        clientService.deleteClient(id);
        Optional<Client> deleted = clientService.getClientById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testSearchClientByName() throws Exception {
        clientService.createClient(testClient);
        List<Client> clients = clientService.searchClientsByName("João");

        assertNotNull(clients);
        assertTrue(clients.size() > 0);
    }

    @Test
    public void testValidateCPF() throws Exception {
        testClient.setDocument("12345678901");
        Client created = clientService.createClient(testClient);
        assertNotNull(created.getId());
    }

    @Test
    public void testValidateCNPJ() throws Exception {
        testClient.setPersonType(PersonType.PJ);
        testClient.setDocument("12345678000190");
        testClient.setName("Empresa LTDA");
        Client created = clientService.createClient(testClient);
        assertNotNull(created.getId());
    }

    @Test
    public void testInvalidCPF() throws Exception {
        testClient.setDocument("00000000000");
        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(testClient));
    }

    @Test
    public void testInvalidEmail() throws Exception {
        testClient.setEmail("email-invalido");
        assertThrows(IllegalArgumentException.class, () -> clientService.createClient(testClient));
    }

    @Test
    public void testClientWithoutOptionalFields() throws Exception {
        testClient.setEmail(null);
        testClient.setPhone(null);
        Client created = clientService.createClient(testClient);

        assertNotNull(created.getId());
        assertNull(created.getEmail());
        assertNull(created.getPhone());
    }
}
