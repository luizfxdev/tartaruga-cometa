package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.*;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DeliveryDAOTest {

    private DeliveryDAO deliveryDAO;
    private ClientDAO clientDAO;
    private AddressDAO addressDAO;
    private Delivery testDelivery;
    private Integer shipperId;
    private Integer recipientId;
    private Integer originAddressId;
    private Integer destinationAddressId;

    @Before
    public void setUp() throws Exception {
        deliveryDAO = new DeliveryDAO();
        clientDAO = new ClientDAO();
        addressDAO = new AddressDAO();

        Client shipper = new Client();
        shipper.setPersonType(PersonType.PJ);
        shipper.setDocument("12345678000190");
        shipper.setName("TechStore LTDA");
        Client createdShipper = clientDAO.create(shipper);
        shipperId = createdShipper.getId();

        Client recipient = new Client();
        recipient.setPersonType(PersonType.PF);
        recipient.setDocument("11122233344");
        recipient.setName("Pedro Lima");
        Client createdRecipient = clientDAO.create(recipient);
        recipientId = createdRecipient.getId();

        Address originAddr = new Address();
        originAddr.setClientId(shipperId);
        originAddr.setAddressType(AddressType.ORIGEM);
        originAddr.setStreet("Rua A");
        originAddr.setNumber("100");
        originAddr.setNeighborhood("Centro");
        originAddr.setCity("São Paulo");
        originAddr.setState("SP");
        originAddr.setZipCode("01310-100");
        Address createdOrigin = addressDAO.create(originAddr);
        originAddressId = createdOrigin.getId();

        Address destAddr = new Address();
        destAddr.setClientId(recipientId);
        destAddr.setAddressType(AddressType.DESTINO);
        destAddr.setStreet("Rua B");
        destAddr.setNumber("200");
        destAddr.setNeighborhood("Bairro");
        destAddr.setCity("Rio de Janeiro");
        destAddr.setState("RJ");
        destAddr.setZipCode("20000-000");
        Address createdDest = addressDAO.create(destAddr);
        destinationAddressId = createdDest.getId();

        testDelivery = new Delivery();
        testDelivery.setTrackingCode("TC2024TEST001");
        testDelivery.setShipperId(shipperId);
        testDelivery.setRecipientId(recipientId);
        testDelivery.setOriginAddressId(originAddressId);
        testDelivery.setDestinationAddressId(destinationAddressId);
        testDelivery.setStatus(DeliveryStatus.PENDENTE);
        testDelivery.setTotalValue(1500.00);
        testDelivery.setFreightValue(50.00);
        testDelivery.setTotalWeightKg(2.5);
        testDelivery.setTotalVolumeM3(0.015);
    }

    @Test
    public void testCreateDelivery() throws Exception {
        Delivery created = deliveryDAO.create(testDelivery);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testFindDeliveryById() throws Exception {
        Delivery created = deliveryDAO.create(testDelivery);
        Optional<Delivery> found = deliveryDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testDelivery.getTrackingCode(), found.get().getTrackingCode());
        assertEquals(DeliveryStatus.PENDENTE, found.get().getStatus());
    }

    @Test
    public void testFindAllDeliveries() throws Exception {
        deliveryDAO.create(testDelivery);
        List<Delivery> deliveries = deliveryDAO.findAll();

        assertNotNull(deliveries);
        assertTrue(deliveries.size() > 0);
    }

    @Test
    public void testUpdateDeliveryStatus() throws Exception {
        Delivery created = deliveryDAO.create(testDelivery);
        created.setStatus(DeliveryStatus.EM_TRANSITO);

        deliveryDAO.update(created);
        Optional<Delivery> updated = deliveryDAO.findById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals(DeliveryStatus.EM_TRANSITO, updated.get().getStatus());
    }

    @Test
    public void testDeleteDelivery() throws Exception {
        Delivery created = deliveryDAO.create(testDelivery);
        Integer id = created.getId();

        deliveryDAO.delete(id);
        Optional<Delivery> deleted = deliveryDAO.findById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testFindDeliveryByTrackingCode() throws Exception {
        deliveryDAO.create(testDelivery);
        Optional<Delivery> found = deliveryDAO.findByTrackingCode("TC2024TEST001");

        assertTrue(found.isPresent());
        assertEquals(testDelivery.getTrackingCode(), found.get().getTrackingCode());
    }

    @Test
    public void testFindDeliveriesByStatus() throws Exception {
        deliveryDAO.create(testDelivery);
        List<Delivery> deliveries = deliveryDAO.findByStatus(DeliveryStatus.PENDENTE);

        assertNotNull(deliveries);
        assertTrue(deliveries.size() > 0);
    }

    @Test
    public void testFindDeliveriesByShipper() throws Exception {
        deliveryDAO.create(testDelivery);
        List<Delivery> deliveries = deliveryDAO.findByShipperId(shipperId);

        assertNotNull(deliveries);
        assertTrue(deliveries.size() > 0);
    }

    @Test
    public void testFindDeliveriesByRecipient() throws Exception {
        deliveryDAO.create(testDelivery);
        List<Delivery> deliveries = deliveryDAO.findByRecipientId(recipientId);

        assertNotNull(deliveries);
        assertTrue(deliveries.size() > 0);
    }

    @Test
    public void testDeliveryWithObservations() throws Exception {
        testDelivery.setObservations("Entregar com cuidado - frágil");
        Delivery created = deliveryDAO.create(testDelivery);
        Optional<Delivery> found = deliveryDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Entregar com cuidado - frágil", found.get().getObservations());
    }

    @Test
    public void testCancelledDelivery() throws Exception {
        testDelivery.setStatus(DeliveryStatus.CANCELADA);
        testDelivery.setMotivoCancelamento("Cliente solicitou");
        Delivery created = deliveryDAO.create(testDelivery);
        Optional<Delivery> found = deliveryDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(DeliveryStatus.CANCELADA, found.get().getStatus());
    }
}
