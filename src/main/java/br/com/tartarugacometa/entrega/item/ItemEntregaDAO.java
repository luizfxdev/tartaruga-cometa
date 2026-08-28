package br.com.tartarugacometa.entrega.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemEntregaDAO {

    public void inserir(Connection conn, ItemEntrega item) throws SQLException {
        String sql = "INSERT INTO delivery_product (delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal, observations) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, item.getDeliveryId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getUnitWeightKg());
            stmt.setBigDecimal(5, item.getUnitVolumeM3());
            stmt.setBigDecimal(6, item.getUnitValue());
            stmt.setBigDecimal(7, item.getSubtotal());
            stmt.setString(8, item.getObservations());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    item.setId(rs.getInt(1));
                }
            }
        }
    }

    public void atualizar(Connection conn, ItemEntrega item) throws SQLException {
        String sql = "UPDATE delivery_product SET quantity = ?, unit_weight_kg = ?, unit_volume_m3 = ?, unit_value = ?, subtotal = ?, observations = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, item.getQuantity());
            stmt.setBigDecimal(2, item.getUnitWeightKg());
            stmt.setBigDecimal(3, item.getUnitVolumeM3());
            stmt.setBigDecimal(4, item.getUnitValue());
            stmt.setBigDecimal(5, item.getSubtotal());
            stmt.setString(6, item.getObservations());
            stmt.setInt(7, item.getId());
            stmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM delivery_product WHERE id = ?")) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public void excluirPorEntregaId(Connection conn, Integer deliveryId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM delivery_product WHERE delivery_id = ?")) {
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }

    public Optional<ItemEntrega> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal, observations FROM delivery_product WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }
        }
    }

    public List<ItemEntrega> buscarPorEntregaId(Connection conn, Integer deliveryId) throws SQLException {
        List<ItemEntrega> itens = new ArrayList<>();
        String sql = "SELECT id, delivery_id, product_id, quantity, unit_weight_kg, unit_volume_m3, unit_value, subtotal, observations FROM delivery_product WHERE delivery_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    itens.add(mapear(rs));
                }
            }
        }
        return itens;
    }

    private ItemEntrega mapear(ResultSet rs) throws SQLException {
        ItemEntrega item = new ItemEntrega();
        item.setId(rs.getInt("id"));
        item.setDeliveryId(rs.getInt("delivery_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitWeightKg(rs.getBigDecimal("unit_weight_kg"));
        item.setUnitVolumeM3(rs.getBigDecimal("unit_volume_m3"));
        item.setUnitValue(rs.getBigDecimal("unit_value"));
        item.setSubtotal(rs.getBigDecimal("subtotal"));
        item.setObservations(rs.getString("observations"));
        return item;
    }
}
