package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.ClientDAO;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("ClientService Tests")
class ClientServiceTest {

    private ClientService clientService;

    @Mock
    private ClientDAO clientDAO;

    private Client testClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService();

        testClient = new Client();
        testClient.setId(1);
        testClient.setPersonType(PersonType.FISICA);
        testClient.setDocument("12345678901");
        testClient.setName("João Silva");
        testClient.setEmail("joao@example.com");
        testClient.setPhone("11999999999");
    }

    @Test
    @DisplayName("Deve criar cliente com validação de documento")
    void testCreateClientWithValidation() {
        assertDoesNotThrow(() -> {
            clientService.createClient(testClient);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção para documento inválido")
    void testCreateClientWithInvalidDocument() {
        testClient.setDocument("123");
        assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(testClient);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção para nome vazio")
    void testCreateClientWithEmptyName() {
        testClient.setName("");
        assertThrows(IllegalArgumentException.class, () -> {
            clientService.createClient(testClient);
        });
    }

    @Test
    @DisplayName("Deve recuperar cliente por ID")
    void testGetClientById() {
        assertDoesNotThrow(() -> {
            when(clientDAO.getById(1)).thenReturn(Optional.of(testClient));
            Optional<Client> result = clientService.getClientById(1);
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar lista de clientes")
    void testGetAllClients() {
        assertDoesNotThrow(() -> {
            List<Client> clients = Arrays.asList(testClient);
            when(clientDAO.getAll()).thenReturn(clients);
            List<Client> result = clientService.getAllClients();
            assertNotNull(result);
            assertEquals(1, result.size());
        });
    }

    @Test
    @DisplayName("Deve buscar clientes por nome")
    void testSearchClientsByName() {
        assertDoesNotThrow(() -> {
            List<Client> clients = Arrays.asList(testClient);
            when(clientDAO.searchByName("João")).thenReturn(clients);
            List<Client> result = clientService.searchClientsByName("João");
            assertNotNull(result);
            assertTrue(result.size() > 0);
        });
    }

    @Test
    @DisplayName("Deve atualizar cliente")
    void testUpdateClient() {
        assertDoesNotThrow(() -> {
            testClient.setName("João Silva Atualizado");
            clientService.updateClient(testClient);
        });
    }

    @Test
    @DisplayName("Deve deletar cliente")
    void testDeleteClient() {
        assertDoesNotThrow(() -> {
            clientService.deleteClient(1);
        });
    }
}
