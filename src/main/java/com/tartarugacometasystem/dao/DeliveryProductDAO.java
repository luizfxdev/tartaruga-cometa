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
import com.tartarugacometasystem.model.DeliveryProduct;

public class DeliveryProductDAO {

    public void save(DeliveryProduct deliveryProduct) throws SQLException {
        String sql = "INSERT INTO entrega_produto (id_entrega, id_produto, quantidade, " +
                     "peso_unitario_kg, volume_unitario_m3, valor_unitario, subtotal, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, deliveryProduct.getDeliveryId());
            stmt.setInt(2, deliveryProduct.getProductId());
            stmt.setInt(3, deliveryProduct.getQuantity());
            stmt.setBigDecimal(4, deliveryProduct.getUnitWeightKg());
            stmt.setBigDecimal(5, deliveryProduct.getUnitVolumeM3());
            stmt.setBigDecimal(6, deliveryProduct.getUnitValue());
            stmt.setBigDecimal(7, deliveryProduct.getSubtotal());
            stmt.setString(8, deliveryProduct.getObservations());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    deliveryProduct.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(DeliveryProduct deliveryProduct) throws SQLException {
        String sql = "UPDATE entrega_produto SET quantidade = ?, peso_unitario_kg = ?, " +
                     "volume_unitario_m3 = ?, valor_unitario = ?, subtotal = ?, observacoes = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryProduct.getQuantity());
            stmt.setBigDecimal(2, deliveryProduct.getUnitWeightKg());
            stmt.setBigDecimal(3, deliveryProduct.getUnitVolumeM3());
            stmt.setBigDecimal(4, deliveryProduct.getUnitValue());
            stmt.setBigDecimal(5, deliveryProduct.getSubtotal());
            stmt.setString(6, deliveryProduct.getObservations());
            stmt.setInt(7, deliveryProduct.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM entrega_produto WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void deleteByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "DELETE FROM entrega_produto WHERE id_entrega = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }

    public Optional<DeliveryProduct> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_entrega, id_produto, quantidade, peso_unitario_kg, " +
                     "volume_unitario_m3, valor_unitario, subtotal, observacoes " +
                     "FROM entrega_produto WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<DeliveryProduct> findByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "SELECT id, id_entrega, id_produto, quantidade, peso_unitario_kg, " +
                     "volume_unitario_m3, valor_unitario, subtotal, observacoes " +
                     "FROM entrega_produto WHERE id_entrega = ?";
        List<DeliveryProduct> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return products;
    }

    public Optional<DeliveryProduct> findByDeliveryAndProduct(Integer deliveryId, Integer productId) throws SQLException {
        String sql = "SELECT id, id_entrega, id_produto, quantidade, peso_unitario_kg, " +
                     "volume_unitario_m3, valor_unitario, subtotal, observacoes " +
                     "FROM entrega_produto WHERE id_entrega = ? AND id_produto = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            stmt.setInt(2, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    private DeliveryProduct mapResultSetToDeliveryProduct(ResultSet rs) throws SQLException {
        DeliveryProduct deliveryProduct = new DeliveryProduct();
        deliveryProduct.setId(rs.getInt("id"));
        deliveryProduct.setDeliveryId(rs.getInt("id_entrega"));
        deliveryProduct.setProductId(rs.getInt("id_produto"));
        deliveryProduct.setQuantity(rs.getInt("quantidade"));
        deliveryProduct.setUnitWeightKg(rs.getBigDecimal("peso_unitario_kg"));
        deliveryProduct.setUnitVolumeM3(rs.getBigDecimal("volume_unitario_m3"));
        deliveryProduct.setUnitValue(rs.getBigDecimal("valor_unitario"));
        deliveryProduct.setSubtotal(rs.getBigDecimal("subtotal"));
        deliveryProduct.setObservations(rs.getString("observacoes"));

        return deliveryProduct;
    }
}
