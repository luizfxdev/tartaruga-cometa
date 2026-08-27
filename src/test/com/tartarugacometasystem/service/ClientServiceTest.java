package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.ClienteDAO;
import com.tartarugacometasystem.model.Cliente;
import br.com.tartarugacometa.enums.TipoPessoa;
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

@DisplayName("ClienteBO Tests")
class ClientServiceTest {

    private ClienteBO clientService;

    @Mock
    private ClienteDAO clientDAO;

    private Cliente testClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClienteBO();

        testClient = new Cliente();
        testClient.setId(1);
        testClient.setPersonType(TipoPessoa.FISICA);
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
            Optional<Cliente> result = clientService.getClientById(1);
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar lista de clientes")
    void testGetAllClients() {
        assertDoesNotThrow(() -> {
            List<Cliente> clients = Arrays.asList(testClient);
            when(clientDAO.getAll()).thenReturn(clients);
            List<Cliente> result = clientService.getAllClients();
            assertNotNull(result);
            assertEquals(1, result.size());
        });
    }

    @Test
    @DisplayName("Deve buscar clientes por nome")
    void testSearchClientsByName() {
        assertDoesNotThrow(() -> {
            List<Cliente> clients = Arrays.asList(testClient);
            when(clientDAO.searchByName("João")).thenReturn(clients);
            List<Cliente> result = clientService.searchClientsByName("João");
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
