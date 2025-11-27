package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AddressDAOTest {

    private AddressDAO addressDAO;
    private ClientDAO clientDAO;
    private Address testAddress;
    private Integer clientId;

    @Before
    public void setUp() throws Exception {
        addressDAO = new AddressDAO();
        clientDAO = new ClientDAO();

        Client client = new Client();
        client.setPersonType(PersonType.PF);
        client.setDocument("98765432109");
        client.setName("Maria Silva");
        Client created = clientDAO.create(client);
        clientId = created.getId();

        testAddress = new Address();
        testAddress.setClientId(clientId);
        testAddress.setAddressType(AddressType.CADASTRAL);
        testAddress.setStreet("Rua das Flores");
        testAddress.setNumber("123");
        testAddress.setNeighborhood("Centro");
        testAddress.setCity("São Paulo");
        testAddress.setState("SP");
        testAddress.setZipCode("01310-100");
        testAddress.setIsPrincipal(true);
    }

    @Test
    public void testCreateAddress() throws Exception {
        Address created = addressDAO.create(testAddress);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testFindAddressById() throws Exception {
        Address created = addressDAO.create(testAddress);
        Optional<Address> found = addressDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testAddress.getStreet(), found.get().getStreet());
        assertEquals(testAddress.getCity(), found.get().getCity());
    }

    @Test
    public void testFindAddressesByClient() throws Exception {
        addressDAO.create(testAddress);
        List<Address> addresses = addressDAO.findByClientId(clientId);

        assertNotNull(addresses);
        assertTrue(addresses.size() > 0);
    }

    @Test
    public void testUpdateAddress() throws Exception {
        Address created = addressDAO.create(testAddress);
        created.setStreet("Avenida Paulista");
        created.setNumber("1000");

        addressDAO.update(created);
        Optional<Address> updated = addressDAO.findById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals("Avenida Paulista", updated.get().getStreet());
        assertEquals("1000", updated.get().getNumber());
    }

    @Test
    public void testDeleteAddress() throws Exception {
        Address created = addressDAO.create(testAddress);
        Integer id = created.getId();

        addressDAO.delete(id);
        Optional<Address> deleted = addressDAO.findById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testFindPrincipalAddress() throws Exception {
        addressDAO.create(testAddress);
        Optional<Address> principal = addressDAO.findPrincipalByClientId(clientId);

        assertTrue(principal.isPresent());
        assertTrue(principal.get().getIsPrincipal());
    }

    @Test
    public void testAddressWithComplement() throws Exception {
        testAddress.setComplement("Apto 201");
        Address created = addressDAO.create(testAddress);
        Optional<Address> found = addressDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("Apto 201", found.get().getComplement());
    }

    @Test
    public void testMultipleAddressesPerClient() throws Exception {
        addressDAO.create(testAddress);

        Address address2 = new Address();
        address2.setClientId(clientId);
        address2.setAddressType(AddressType.DESTINO);
        address2.setStreet("Rua B");
        address2.setNumber("456");
        address2.setNeighborhood("Bairro B");
        address2.setCity("Rio de Janeiro");
        address2.setState("RJ");
        address2.setZipCode("20000-000");
        addressDAO.create(address2);

        List<Address> addresses = addressDAO.findByClientId(clientId);
        assertEquals(2, addresses.size());
    }

    @Test
    public void testValidCEP() throws Exception {
        testAddress.setZipCode("12345-678");
        Address created = addressDAO.create(testAddress);
        assertNotNull(created.getId());
    }
}
