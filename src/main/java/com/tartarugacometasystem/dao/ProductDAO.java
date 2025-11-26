package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.Product;

public class ProductDAO {

    public void save(Product product) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, peso_kg, volume_m3, " +
                     "valor_declarado, categoria, ativo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getWeightKg());
            stmt.setBigDecimal(4, product.getVolumeM3());
            stmt.setBigDecimal(5, product.getDeclaredValue());
            stmt.setString(6, product.getCategory());

            Boolean active = product.getActive();
            stmt.setBoolean(7, active != null ? active : true);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    product.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Product product) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, peso_kg = ?, " +
                     "volume_m3 = ?, valor_declarado = ?, categoria = ?, ativo = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getWeightKg());
            stmt.setBigDecimal(4, product.getVolumeM3());
            stmt.setBigDecimal(5, product.getDeclaredValue());
            stmt.setString(6, product.getCategory());

            Boolean active = product.getActive();
            stmt.setBoolean(7, active != null ? active : true);
            stmt.setInt(8, product.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<Product> findById(Integer id) throws SQLException {
        String sql = "SELECT id, nome, descricao, peso_kg, volume_m3, valor_declarado, " +
                     "categoria, ativo, created_at, updated_at FROM produto WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Product> findAll() throws SQLException {
        String sql = "SELECT id, nome, descricao, peso_kg, volume_m3, valor_declarado, " +
                     "categoria, ativo, created_at, updated_at FROM produto ORDER BY nome";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    public List<Product> findAllActive() throws SQLException {
        String sql = "SELECT id, nome, descricao, peso_kg, volume_m3, valor_declarado, " +
                     "categoria, ativo, created_at, updated_at FROM produto WHERE ativo = true ORDER BY nome";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        }
        return products;
    }

    public List<Product> findByName(String name) throws SQLException {
        String sql = "SELECT id, nome, descricao, peso_kg, volume_m3, valor_declarado, " +
                     "categoria, ativo, created_at, updated_at FROM produto WHERE nome ILIKE ? ORDER BY nome";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }
        return products;
    }

    public List<Product> findByCategory(String category) throws SQLException {
        String sql = "SELECT id, nome, descricao, peso_kg, volume_m3, valor_declarado, " +
                     "categoria, ativo, created_at, updated_at FROM produto WHERE categoria = ? ORDER BY nome";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToProduct(rs));
                }
            }
        }
        return products;
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("nome"));
        product.setDescription(rs.getString("descricao"));
        product.setWeightKg(rs.getBigDecimal("peso_kg"));
        product.setVolumeM3(rs.getBigDecimal("volume_m3"));
        product.setDeclaredValue(rs.getBigDecimal("valor_declarado"));
        product.setCategory(rs.getString("categoria"));
        product.setActive(rs.getBoolean("ativo"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            product.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return product;
    }
}
