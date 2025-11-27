package com.tartarugacometasystem.service;

import com.tartarugacometasystem.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class DeliveryServiceTest {

    private DeliveryService deliveryService;
    private ClientService clientService;
    private AddressService addressService;
    private Delivery testDelivery;
    private Integer shipperId;
    private Integer recipientId;
    private Integer originAddressId;
    private Integer destinationAddressId;

    @Before
    public void setUp() throws Exception {
        deliveryService = new DeliveryService();
        clientService = new ClientService();
        addressService = new AddressService();

        Client shipper = new Client();
        shipper.setPersonType(PersonType.PJ);
        shipper.setDocument("12345678000190");
        shipper.setName("TechStore LTDA");
        Client createdShipper = clientService.createClient(shipper);
        shipperId = createdShipper.getId();

        Client recipient = new Client();
        recipient.setPersonType(PersonType.PF);
        recipient.setDocument("11122233344");
        recipient.setName("Pedro Lima");
        Client createdRecipient = clientService.createClient(recipient);
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
        Address createdOrigin = addressService.createAddress(originAddr);
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
        Address createdDest = addressService.createAddress(destAddr);
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
    }

    @Test
    public void testCreateDelivery() throws Exception {
        Delivery created = deliveryService.createDelivery(testDelivery);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testGetDeliveryById() throws Exception {
        Delivery created = deliveryService.createDelivery(testDelivery);
        Optional<Delivery> found = deliveryService.getDeliveryById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testDelivery.getTrackingCode(), found.get().getTrackingCode());
    }

    @Test
    public void testGetAllDeliveries() throws Exception {
        deliveryService.createDelivery(testDelivery);
        List<Delivery> deliveries = deliveryService.getAllDeliveries();

        assertNotNull(deliveries);
        assertTrue(deliveries.size() > 0);
    }

    @Test
    public void testUpdateDeliveryStatus() throws Exception {
        Delivery created = deliveryService.createDelivery(testDelivery);
        created.setStatus(DeliveryStatus.EM_TRANSITO);

        deliveryService.updateDelivery(created);
        Optional<Delivery> updated = deliveryService.getDeliveryById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals(DeliveryStatus.EM_TRANSITO, updated.get().getStatus());
    }

    @Test
    public void testDeleteDelivery() throws Exception {
        Delivery created = deliveryService.createDelivery(testDelivery);
        Integer id = created.getId();

        deliveryService.deleteDelivery(id);
        Optional<Delivery> deleted = deliveryService.getDeliveryById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testGetDeliveryByTrackingCode() throws Exception {
        deliveryService.createDelivery(testDelivery);
        Optional<Delivery> found = deliveryService.getDelivery

