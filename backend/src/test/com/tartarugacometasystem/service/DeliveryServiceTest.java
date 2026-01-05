package com.tartarugacometasystem.service;

import com.tartarugacometasystem.dao.DeliveryDAO;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
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

@DisplayName("DeliveryService Tests")
class DeliveryServiceTest {

    private DeliveryService deliveryService;

    @Mock
    private DeliveryDAO deliveryDAO;

    private Delivery testDelivery;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deliveryService = new DeliveryService();

        testDelivery = new Delivery();
        testDelivery.setId(1);
        testDelivery.setTrackingCode("TC123456789");
        testDelivery.setShipperId(1);
        testDelivery.setRecipientId(2);
        testDelivery.setOriginAddressId(1);
        testDelivery.setDestinationAddressId(2);
        testDelivery.setStatus(DeliveryStatus.PENDENTE);
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
            Optional<Delivery> result = deliveryService.getDeliveryById(1);
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar lista de entregas")
    void testGetAllDeliveries() {
        assertDoesNotThrow(() -> {
            List<Delivery> deliveries = Arrays.asList(testDelivery);
            when(deliveryDAO.getAll()).thenReturn(deliveries);
            List<Delivery> result = deliveryService.getAllDeliveries();
            assertNotNull(result);
            assertEquals(1, result.size());
        });
    }

    @Test
    @DisplayName("Deve buscar entrega por código de rastreio")
    void testGetDeliveryByTrackingCode() {
        assertDoesNotThrow(() -> {
            when(deliveryDAO.getByTrackingCode("TC123456789")).thenReturn(Optional.of(testDelivery));
            Optional<Delivery> result = deliveryService.getDeliveryByTrackingCode("TC123456789");
            assertTrue(result.isPresent());
        });
    }

    @Test
    @DisplayName("Deve retornar entregas por status")
    void testGetDeliverieesNotThrow(() -> {
            List<Delivery> deliveries = Arrays.asList(testDelivery);
            when(deliveryDAO.getByStatus(DeliveryStatus.PENDENTE)).thenReturn(deliveries);
            List<Delivery> result = deliveryService.getDeliveriesByStatus(DeliveryStatus.PENDENTE);
            assertNotNull(result);
            assertTrue(result.size() > 0);
        });
    }

    @Test
    @DisplayName("Deve atualizar status da entrega")
    void testUpdateDeliveryStatus() {
        assertDoesNotThrow(() -> {
            deliveryService.updateDeliveryStatus(1, DeliveryStatus.EM_TRANSITO, "Em rota", "SYSTEM");
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
