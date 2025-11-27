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
        testProduct.setName("Notebook Dell");
        testProduct.setDescription("Notebook i5, 8GB RAM");
        testProduct.setWeightKg(2.5);
        testProduct.setVolumeM3(0.015);
        testProduct.setDeclaredValue(3500.00);
        testProduct.setCategory("Eletrônicos");
        testProduct.setActive(true);
    }

    @Test
    public void testCreateProduct() throws Exception {
        Product created = productDAO.create(testProduct);
        assertNotNull(created.getId());
        assertTrue(created.getId() > 0);
    }

    @Test
    public void testFindProductById() throws Exception {
        Product created = productDAO.create(testProduct);
        Optional<Product> found = productDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertEquals(testProduct.getName(), found.get().getName());
        assertEquals(testProduct.getCategory(), found.get().getCategory());
    }

    @Test
    public void testFindAllProducts() throws Exception {
        productDAO.create(testProduct);
        List<Product> products = productDAO.findAll();

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product created = productDAO.create(testProduct);
        created.setName("Notebook Dell Atualizado");
        created.setDeclaredValue(4000.00);

        productDAO.update(created);
        Optional<Product> updated = productDAO.findById(created.getId());

        assertTrue(updated.isPresent());
        assertEquals("Notebook Dell Atualizado", updated.get().getName());
        assertEquals(4000.00, updated.get().getDeclaredValue(), 0.01);
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product created = productDAO.create(testProduct);
        Integer id = created.getId();

        productDAO.delete(id);
        Optional<Product> deleted = productDAO.findById(id);

        assertFalse(deleted.isPresent());
    }

    @Test
    public void testFindProductsByCategory() throws Exception {
        productDAO.create(testProduct);
        List<Product> products = productDAO.findByCategory("Eletrônicos");

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testSearchProductByName() throws Exception {
        productDAO.create(testProduct);
        List<Product> products = productDAO.searchByName("Notebook");

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testFindActiveProducts() throws Exception {
        productDAO.create(testProduct);
        List<Product> products = productDAO.findActive();

        assertNotNull(products);
        assertTrue(products.size() > 0);
    }

    @Test
    public void testInactiveProduct() throws Exception {
        testProduct.setActive(false);
        Product created = productDAO.create(testProduct);
        Optional<Product> found = productDAO.findById(created.getId());

        assertTrue(found.isPresent());
        assertFalse(found.get().getActive());
    }

    @Test
    public void testProductWithoutDescription() throws Exception {
        testProduct.setDescription(null);
        Product created = productDAO.create(testProduct);

        assertNotNull(created.getId());
        assertNull(created.getDescription());
    }

    @Test
    public void testPositiveWeightAndVolume() throws Exception {
        testProduct.setWeightKg(0.5);
        testProduct.setVolumeM3(0.001);
        Product created = productDAO.create(testProduct);

        assertNotNull(created.getId());
        assertEquals(0.5, created.getWeightKg(), 0.01);
        assertEquals(0.001, created.getVolumeM3(), 0.0001);
    }
}
