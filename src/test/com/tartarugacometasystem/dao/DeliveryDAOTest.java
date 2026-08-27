package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Entrega;
import br.com.tartarugacometa.enums.StatusEntrega;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DeliveryDAOTest {

    private EntregaDAO deliveryDAO;
    private Entrega testDelivery;

    @Before
    public void setUp() {
        deliveryDAO = new EntregaDAO();
        testDelivery = new Entrega();
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
    public void testCreateDelivery() {
        try {
            deliveryDAO.create(testDelivery);
            assertNotNull(testDelivery.getId());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeliveryById() {
        try {
            deliveryDAO.create(testDelivery);
            Optional<Entrega> retrieved = deliveryDAO.getById(testDelivery.getId());
            assertTrue(retrieved.isPresent());
            assertEquals(testDelivery.getTrackingCode(), retrieved.get().getTrackingCode());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateDelivery() {
        try {
            deliveryDAO.create(testDelivery);
            testDelivery.setStatus(StatusEntrega.EM_TRANSITO);
            deliveryDAO.update(testDelivery);

            Optional<Entrega> updated = deliveryDAO.getById(testDelivery.getId());
            assertTrue(updated.isPresent());
            assertEquals(StatusEntrega.EM_TRANSITO, updated.get().getStatus());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteDelivery() {
        try {
            deliveryDAO.create(testDelivery);
            Integer id = testDelivery.getId();
            deliveryDAO.delete(id);

            Optional<Entrega> deleted = deliveryDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllDeliveries() {
        try {
            List<Entrega> deliveries = deliveryDAO.getAll();
            assertNotNull(deliveries);
            assertTrue(deliveries.size() >= 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeliveryByTrackingCode() {
        try {
            deliveryDAO.create(testDelivery);
            Optional<Entrega> result = deliveryDAO.getByTrackingCode(testDelivery.getTrackingCode());
            assertTrue(result.isPresent());
            assertEquals(testDelivery.getTrackingCode(), result.get().getTrackingCode());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeliveryByInvalidId() {
        try {
            Optional<Entrega> result = deliveryDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
