package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.EntregaDAO;
import com.tartarugacometasystem.model.Entrega;
import br.com.tartarugacometa.enums.StatusEntrega;
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

@DisplayName("EntregaBO Tests")
class DeliveryServiceTest {

    private EntregaBO deliveryService;

    @Mock
    private EntregaDAO deliveryDAO;

    private Entrega testDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deliveryService = new EntregaBO();

        testDelivery = new Entrega();
        testDelivery.setId(1);
        testDelivery.setTrackingCode("TC123456789");
        testDelivery.setShipperId(1);
        testDelivery.setRecipientId(2);
        testDelivery.setOriginAddressId(1);
        testDelivery.setDestinationAddressId(2);
        testDelivery.setStatus(StatusEntrega.PENDENTE);
        testDelivery.setTotalValue(100.00);
        testDelivery.setFreightValue(25.00);
        testDelivery.setTotalWeightKg(5.5);
        testDelivery.setTotalVolumeM3(0.5);
    }

    @Test
    @DisplayName("Deve criar entrega com validação")
    void testCreateDelivery() {
        assertDoesNotThrow(() -> {
            deliveryService.createDelivery(testDelivery);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção para código de rastreio vazio")
    void testCreateDeliveryWithEmptyTrackingCode() {
        testDelivery.setTrackingCode("");
        assertThrows(IllegalArgumentException.class, () -> {
            deliveryService.createDelivery(testDelivery);
        });
    }

    @Test
    @DisplayName("Deve recuperar entrega por ID")
    void testGetDeliveryById() {
        assertDoesNotThrow(() -> {
            when(deliveryDAO.getById(1)).thenReturn(Optional.of(testDelivery));
            Optional<Entrega> result = deliveryService.getDeliveryById(1);
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar lista de entregas")
    void testGetAllDeliveries() {
        assertDoesNotThrow(() -> {
            List<Entrega> deliveries = Arrays.asList(testDelivery);
            when(deliveryDAO.getAll()).thenReturn(deliveries);
            List<Entrega> result = deliveryService.getAllDeliveries();
            assertNotNull(result);
            assertEquals(1, result.size());
        });
    }

    @Test
    @DisplayName("Deve buscar entrega por código de rastreio")
    void testGetDeliveryByTrackingCode() {
        assertDoesNotThrow(() -> {
            when(deliveryDAO.getByTrackingCode("TC123456789")).thenReturn(Optional.of(testDelivery));
            Optional<Entrega> result = deliveryService.getDeliveryByTrackingCode("TC123456789");
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar entregas por status")
    void testGetDeliverieesNotThrow(() -> {
            List<Entrega> deliveries = Arrays.asList(testDelivery);
            when(deliveryDAO.getByStatus(StatusEntrega.PENDENTE)).thenReturn(deliveries);
            List<Entrega> result = deliveryService.getDeliveriesByStatus(StatusEntrega.PENDENTE);
            assertNotNull(result);
            assertTrue(result.size() > 0);
        });
    }

    @Test
    @DisplayName("Deve atualizar status da entrega")
    void testUpdateDeliveryStatus() {
        assertDoesNotThrow(() -> {
            deliveryService.updateDeliveryStatus(1, StatusEntrega.EM_TRANSITO, "Em rota", "SYSTEM");
        });
    }

    @Test
    @DisplayName("Deve cancelar entrega")
    void testCancelDelivery() {
        assertDoesNotThrow(() -> {
            deliveryService.cancelDelivery(1, "Cancelado pelo cliente", "SYSTEM");
        });
    }

    @Test
    @DisplayName("Deve marcar entrega como entregue")
    void testMarkAsDelivered() {
        assertDoesNotThrow(() -> {
            deliveryService.markAsDelivered(1, "SYSTEM");
        });
    }

    @Test
    @DisplayName("Deve deletar entrega")
    void testDeleteDelivery() {
        assertDoesNotThrow(() -> {
            deliveryService.deleteDelivery(1);
        });
    }
}
