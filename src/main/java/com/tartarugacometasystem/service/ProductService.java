package com.tartarugacometasystem.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.ProductDAO;
import com.tartarugacometasystem.model.Product;

public class ProductService {
    private final ProductDAO productDAO;

    public ProductService() {
        this.productDAO = new ProductDAO();
    }

    public void createProduct(Product product) throws SQLException, IllegalArgumentException {
        validateProduct(product);
        productDAO.save(product);
    }

    public void updateProduct(Product product) throws SQLException, IllegalArgumentException {
        validateProduct(product);

        if (product.getId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para atualização");
        }

        productDAO.update(product);
    }

    public void deleteProduct(Integer productId) throws SQLException {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }

        productDAO.delete(productId);
    }

    public Optional<Product> getProductById(Integer productId) throws SQLException {
        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException("ID do produto inválido");
        }

        return productDAO.findById(productId);
    }

    public List<Product> getAllProducts() throws SQLException {
        return productDAO.findAll();
    }

    public List<Product> getAllActiveProducts() throws SQLException {
        return productDAO.findAllActive();
    }

    public List<Product> searchProductsByName(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome de busca não pode estar vazio");
        }

        return productDAO.findByName(name.trim());
    }

    public List<Product> getProductsByCategory(String category) throws SQLException {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Categoria não pode estar vazia");
        }

        return productDAO.findByCategory(category.trim());
    }

    private void validateProduct(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório");
        }

        if (product.getWeightKg() == null || product.getWeightKg().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Peso deve ser maior que zero");
        }

        if (product.getVolumeM3() == null || product.getVolumeM3().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Volume deve ser maior que zero");
        }

        if (product.getDeclaredValue() == null || product.getDeclaredValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor declarado deve ser maior que zero");
        }
    }
}
