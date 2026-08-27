package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Endereco;
import br.com.tartarugacometa.enums.TipoEndereco;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class AddressDAOTest {

    private EnderecoDAO addressDAO;
    private Endereco testAddress;

    @Before
    public void setUp() {
        addressDAO = new EnderecoDAO();
        testAddress = new Endereco();
        testAddress.setStreet("Rua Teste");
        testAddress.setNumber("123");
        testAddress.setNeighborhood("Bairro Teste");
        testAddress.setCity("São Paulo");
        testAddress.setState("SP");
        testAddress.setZipCode("01234-567");
        testAddress.setAddressType(TipoEndereco.COMERCIAL);
        testAddress.setIsPrincipal(false);
    }

    @Test
    public void testCreateAddress() {
        try {
            addressDAO.create(testAddress);
            assertNotNull(testAddress.getId());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAddressById() {
        try {
            addressDAO.create(testAddress);
            Optional<Endereco> retrieved = addressDAO.getById(testAddress.getId());
            assertTrue(retrieved.isPresent());
            assertEquals(testAddress.getStreet(), retrieved.get().getStreet());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateAddress() {
        try {
            addressDAO.create(testAddress);
            testAddress.setStreet("Rua Atualizada");
            addressDAO.update(testAddress);

            Optional<Endereco> updated = addressDAO.getById(testAddress.getId());
            assertTrue(updated.isPresent());
            assertEquals("Rua Atualizada", updated.get().getStreet());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteAddress() {
        try {
            addressDAO.create(testAddress);
            Integer id = testAddress.getId();
            addressDAO.delete(id);

            Optional<Endereco> deleted = addressDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllAddresses() {
        try {
            List<Endereco> addresses = addressDAO.getAll();
            assertNotNull(addresses);
            assertTrue(addresses.size() >= 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAddressByInvalidId() {
        try {
            Optional<Endereco> result = addressDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
