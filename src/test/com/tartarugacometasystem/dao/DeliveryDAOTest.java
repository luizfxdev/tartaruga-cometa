package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DeliveryDAOTest {

    private DeliveryDAO deliveryDAO;
    private Delivery testDelivery;

    @Before
    public void setUp() {
        deliveryDAO = new DeliveryDAO();
        testDelivery = new Delivery();
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
            Optional<Delivery> retrieved = deliveryDAO.getById(testDelivery.getId());
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
            testDelivery.setStatus(DeliveryStatus.EM_TRANSITO);
            deliveryDAO.update(testDelivery);

            Optional<Delivery> updated = deliveryDAO.getById(testDelivery.getId());
            assertTrue(updated.isPresent());
            assertEquals(DeliveryStatus.EM_TRANSITO, updated.get().getStatus());
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

            Optional<Delivery> deleted = deliveryDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllDeliveries() {
        try {
            List<Delivery> deliveries = deliveryDAO.getAll();
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
            Optional<Delivery> result = deliveryDAO.getByTrackingCode(testDelivery.getTrackingCode());
            assertTrue(result.isPresent());
            assertEquals(testDelivery.getTrackingCode(), result.get().getTrackingCode());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetDeliveryByInvalidId() {
        try {
            Optional<Delivery> result = deliveryDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
