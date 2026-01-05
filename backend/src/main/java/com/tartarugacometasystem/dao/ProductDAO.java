// ProductDAO.java
package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.Product;

public class ProductDAO {

    /**
     * Cria um novo produto no banco de dados.
     *
     * @param product O objeto Product a ser criado.
     * @return O objeto Product com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Product save(Product product) throws SQLException {
        String sql = "INSERT INTO product (name, description, price, weight_kg, volume_m3, declared_value, category, is_active, stock_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setBigDecimal(4, product.getWeightKg());
            pstmt.setBigDecimal(5, product.getVolumeM3());
            pstmt.setBigDecimal(6, product.getDeclaredValue());
            pstmt.setBigDecimal(7, product.getDeclaredValue()); // Parece um erro aqui, deveria ser category
            pstmt.setString(7, product.getCategory()); // Correção: Mover para a posição correta e usar getCategory()

            // Correção: Usar o getter isActive() e não precisa mais verificar null se for boolean primitivo
            pstmt.setBoolean(8, product.isActive()); // Mudança: de getIsActive() para isActive()

            // Correção para evitar unboxing de null
            Integer stockQuantity = product.getStockQuantity();
            pstmt.setInt(9, stockQuantity != null ? stockQuantity : 0);

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
            return product;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id O ID do produto.
     * @return Um Optional contendo o Product se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Product> findById(Integer id) throws SQLException {
        String sql = "SELECT id, name, description, price, weight_kg, volume_m3, declared_value, category, is_active, stock_quantity, created_at, updated_at FROM product WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProduct(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Atualiza um produto existente no banco de dados.
     *
     * @param product O objeto Product a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Product product) throws SQLException {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, weight_kg = ?, volume_m3 = ?, declared_value = ?, category = ?, is_active = ?, stock_quantity = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setBigDecimal(4, product.getWeightKg());
            pstmt.setBigDecimal(5, product.getVolumeM3());
            pstmt.setBigDecimal(6, product.getDeclaredValue());
            pstmt.setString(7, product.getCategory());

            // Correção: Usar o getter isActive() e não precisa mais verificar null se for boolean primitivo
            pstmt.setBoolean(8, product.isActive()); // Mudança: de getIsActive() para isActive()

            // Correção para evitar unboxing de null
            Integer stockQuantity = product.getStockQuantity();
            pstmt.setInt(9, stockQuantity != null ? stockQuantity : 0);

            pstmt.setInt(10, product.getId());
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
        }
    }

    /**
     * Deleta um produto pelo ID.
     *
     * @param id O ID do produto a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM product WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
        }
    }

    /**
     * Busca todos os produtos.
     *
     * @return Uma lista de todos os produtos.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, weight_kg, volume_m3, declared_value, category, is_active, stock_quantity, created_at, updated_at FROM product";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return products;
    }

    /**
     * Busca produtos pelo nome ou descrição.
     *
     * @param query O termo de busca.
     * @return Uma lista de produtos que correspondem ao termo de busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Product> searchByName(String query) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT id, name, description, price, weight_kg, volume_m3, declared_value, category, is_active, stock_quantity, created_at, updated_at FROM product WHERE name ILIKE ? OR description ILIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return products;
    }

    /**
     * Mapeia um ResultSet para um objeto Product.
     *
     * @param rs O ResultSet.
     * @return Um objeto Product.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setWeightKg(rs.getBigDecimal("weight_kg"));
        product.setVolumeM3(rs.getBigDecimal("volume_m3"));
        product.setDeclaredValue(rs.getBigDecimal("declared_value"));
        product.setCategory(rs.getString("category"));
        product.setActive(rs.getBoolean("is_active")); // Mudança: de setIsActive() para setActive()
        product.setStockQuantity(rs.getInt("stock_quantity"));
        // É importante verificar se as colunas de data não são nulas no banco de dados
        // antes de chamar toLocalDateTime(), para evitar NullPointerException.
        // Ou garantir que o banco sempre retorne um valor.
        if (rs.getTimestamp("created_at") != null) {
            product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }
        if (rs.getTimestamp("updated_at") != null) {
            product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        }
        return product;
    }
}
