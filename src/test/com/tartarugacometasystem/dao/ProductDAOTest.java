package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Product;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class ProductDAOTest {

    private ProductDAO productDAO;
    private Product testProduct;

    @Before
    public void setUp() {
        productDAO = new ProductDAO();
        testProduct = new Product();
        testProduct.setName("Produto Teste");
        testProduct.setDescription("Descrição do produto teste");
        testProduct.setCategory("Eletrônicos");
        testProduct.setWeightKg(2.5);
        testProduct.setVolumeM3(0.1);
        testProduct.setDeclaredValue(500.00);
        testProduct.setActive(true);
    }

    @Test
    public void testCreateProduct() {
        try {
            productDAO.create(testProduct);
            assertNotNull(testProduct.getId());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetProductById() {
        try {
            productDAO.create(testProduct);
            Optional<Product> retrieved = productDAO.getById(testProduct.getId());
            assertTrue(retrieved.isPresent());
            assertEquals(testProduct.getName(), retrieved.get().getName());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testUpdateProduct() {
        try {
            productDAO.create(testProduct);
            testProduct.setName("Produto Atualizado");
            productDAO.update(testProduct);

            Optional<Product> updated = productDAO.getById(testProduct.getId());
            assertTrue(updated.isPresent());
            assertEquals("Produto Atualizado", updated.get().getName());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testDeleteProduct() {
        try {
            productDAO.create(testProduct);
            Integer id = testProduct.getId();
            productDAO.delete(id);

            Optional<Product> deleted = productDAO.getById(id);
            assertFalse(deleted.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetAllProducts() {
        try {
            List<Product> products = productDAO.getAll();
            assertNotNull(products);
            assertTrue(products.size() >= 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testSearchProductByName() {
        try {
            productDAO.create(testProduct);
            List<Product> results = productDAO.searchByName("Produto");
            assertNotNull(results);
            assertTrue(results.size() > 0);
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }

    @Test
    public void testGetProductByInvalidId() {
        try {
            Optional<Product> result = productDAO.getById(99999);
            assertFalse(result.isPresent());
        } catch (Exception e) {
            fail("Não deveria lançar exceção: " + e.getMessage());
        }
    }
}
