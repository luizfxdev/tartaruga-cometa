package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;

public class DeliveryDAO {

    public void save(Delivery delivery) throws SQLException {
        String sql = "INSERT INTO entrega (codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, delivery.getTrackingCode());
            stmt.setInt(2, delivery.getShipperId());
            stmt.setInt(3, delivery.getRecipientId());
            stmt.setInt(4, delivery.getOriginAddressId());
            stmt.setInt(5, delivery.getDestinationAddressId());
            stmt.setString(6, delivery.getStatus().getValue());
            stmt.setBigDecimal(7, delivery.getTotalValue());
            stmt.setBigDecimal(8, delivery.getTotalWeightKg());
            stmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            stmt.setBigDecimal(10, delivery.getFreightValue());
            stmt.setString(11, delivery.getObservations());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    delivery.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Delivery delivery) throws SQLException {
        String sql = "UPDATE entrega SET status = ?, valor_total = ?, peso_total_kg = ?, " +
                     "volume_total_m3 = ?, valor_frete = ?, observacoes = ?, " +
                     "data_coleta = ?, data_envio = ?, data_entrega = ?, " +
                     "data_cancelamento = ?, motivo_cancelamento = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, delivery.getStatus().getValue());
            stmt.setBigDecimal(2, delivery.getTotalValue());
            stmt.setBigDecimal(3, delivery.getTotalWeightKg());
            stmt.setBigDecimal(4, delivery.getTotalVolumeM3());
            stmt.setBigDecimal(5, delivery.getFreightValue());
            stmt.setString(6, delivery.getObservations());
            stmt.setTimestamp(7, delivery.getPickupDate() != null ? 
                Timestamp.valueOf(delivery.getPickupDate()) : null);
            stmt.setTimestamp(8, delivery.getShipmentDate() != null ? 
                Timestamp.valueOf(delivery.getShipmentDate()) : null);
            stmt.setTimestamp(9, delivery.getDeliveryDate() != null ? 
                Timestamp.valueOf(delivery.getDeliveryDate()) : null);
            stmt.setTimestamp(10, delivery.getCancellationDate() != null ? 
                Timestamp.valueOf(delivery.getCancellationDate()) : null);
            stmt.setString(11, delivery.getCancellationReason());
            stmt.setInt(12, delivery.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM entrega WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<Delivery> findById(Integer id) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDelivery(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Delivery> findByTrackingCode(String trackingCode) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE codigo_rastreio = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, trackingCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDelivery(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Delivery> findAll() throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega ORDER BY data_criacao DESC";
        List<Delivery> deliveries = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        }
        return deliveries;
    }

    public List<Delivery> findByStatus(DeliveryStatus status) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE status = ? ORDER BY data_criacao DESC";
        List<Delivery> deliveries = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.getValue());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deliveries.add(mapResultSetToDelivery(rs));
                }
            }
        }
        return deliveries;
    }

    public List<Delivery> findByShipperId(Integer shipperId) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE id_remetente = ? ORDER BY data_criacao DESC";
        List<Delivery> deliveries = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, shipperId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deliveries.add(mapResultSetToDelivery(rs));
                }
            }
        }
        return deliveries;
    }

    public List<Delivery> findByRecipientId(Integer recipientId) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE id_destinatario = ? ORDER BY data_criacao DESC";
        List<Delivery> deliveries = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, recipientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deliveries.add(mapResultSetToDelivery(rs));
                }
            }
        }
        return deliveries;
    }

    public List<Delivery> findByDateRange(LocalDateTime startDate, LocalDateTime endDate) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, " +
                     "id_endereco_origem, id_endereco_destino, status, valor_total, " +
                     "peso_total_kg, volume_total_m3, valor_frete, observacoes, " +
                     "data_criacao, data_coleta, data_envio, data_entrega, " +
                     "data_cancelamento, motivo_cancelamento, created_at, updated_at " +
                     "FROM entrega WHERE data_criacao BETWEEN ? AND ? ORDER BY data_criacao DESC";
        List<Delivery> deliveries = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(startDate));
            stmt.setTimestamp(2, Timestamp.valueOf(endDate));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    deliveries.add(mapResultSetToDelivery(rs));
                }
            }
        }
        return deliveries;
    }

    private Delivery mapResultSetToDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setId(rs.getInt("id"));
        delivery.setTrackingCode(rs.getString("codigo_rastreio"));
        delivery.setShipperId(rs.getInt("id_remetente"));
        delivery.setRecipientId(rs.getInt("id_destinatario"));
        delivery.setOriginAddressId(rs.getInt("id_endereco_origem"));
        delivery.setDestinationAddressId(rs.getInt("id_endereco_destino"));
        delivery.setStatus(DeliveryStatus.fromValue(rs.getString("status")));
        delivery.setTotalValue(rs.getBigDecimal("valor_total"));
        delivery.setTotalWeightKg(rs.getBigDecimal("peso_total_kg"));
        delivery.setTotalVolumeM3(rs.getBigDecimal("volume_total_m3"));
        delivery.setFreightValue(rs.getBigDecimal("valor_frete"));
        delivery.setObservations(rs.getString("observacoes"));
        delivery.setCreatedAt(rs.getTimestamp("data_criacao").toLocalDateTime());

        Timestamp pickupDate = rs.getTimestamp("data_coleta");
        if (pickupDate != null) {
            delivery.setPickupDate(pickupDate.toLocalDateTime());
        }

        Timestamp shipmentDate = rs.getTimestamp("data_envio");
        if (shipmentDate != null) {
            delivery.setShipmentDate(shipmentDate.toLocalDateTime());
        }

        Timestamp deliveryDate = rs.getTimestamp("data_entrega");
        if (deliveryDate != null) {
            delivery.setDeliveryDate(deliveryDate.toLocalDateTime());
        }

        Timestamp cancellationDate = rs.getTimestamp("data_cancelamento");
        if (cancellationDate != null) {
            delivery.setCancellationDate(cancellationDate.toLocalDateTime());
        }

        delivery.setCancellationReason(rs.getString("motivo_cancelamento"));

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            delivery.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return delivery;
    }
}
