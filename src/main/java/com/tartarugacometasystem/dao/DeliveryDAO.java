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

import com.tartarugacometasystem.model.Delivery;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.util.ConnectionFactory;

public class DeliveryDAO {

    /**
     * Cria uma nova entrega no banco de dados.
     *
     * @param delivery O objeto Delivery a ser criado.
     * @return O objeto Delivery com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Delivery save(Delivery delivery) throws SQLException {
        String sql = "INSERT INTO deliveries (tracking_code, shipper_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, delivery.getTrackingCode());
            stmt.setInt(2, delivery.getShipperId());
            stmt.setInt(3, delivery.getRecipientId());
            stmt.setInt(4, delivery.getOriginAddressId());
            stmt.setInt(5, delivery.getDestinationAddressId());
            stmt.setBigDecimal(6, delivery.getTotalValue());
            stmt.setBigDecimal(7, delivery.getFreightValue());
            stmt.setBigDecimal(8, delivery.getTotalWeightKg());
            stmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            stmt.setString(10, delivery.getStatus().name()); // Salva o nome do enum
            stmt.setString(11, delivery.getObservations());
            stmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(13, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                delivery.setId(rs.getInt(1));
            }
            return delivery;
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
    }

    /**
     * Busca uma entrega pelo ID.
     *
     * @param id O ID da entrega.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM deliveries WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDelivery(rs));
            }
            return Optional.empty();
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
    }

    /**
     * Atualiza uma entrega existente no banco de dados.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Delivery delivery) throws SQLException {
        String sql = "UPDATE deliveries SET tracking_code = ?, shipper_id = ?, recipient_id = ?, origin_address_id = ?, destination_address_id = ?, total_value = ?, freight_value = ?, total_weight_kg = ?, total_volume_m3 = ?, status = ?, observations = ?, updated_at = ?, delivered_at = ?, reason_not_delivered = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, delivery.getTrackingCode());
            stmt.setInt(2, delivery.getShipperId());
            stmt.setInt(3, delivery.getRecipientId());
            stmt.setInt(4, delivery.getOriginAddressId());
            stmt.setInt(5, delivery.getDestinationAddressId());
            stmt.setBigDecimal(6, delivery.getTotalValue());
            stmt.setBigDecimal(7, delivery.getFreightValue());
            stmt.setBigDecimal(8, delivery.getTotalWeightKg());
            stmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            stmt.setString(10, delivery.getStatus().name());
            stmt.setString(11, delivery.getObservations());
            stmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(13, (delivery.getDeliveredAt() != null ? Timestamp.valueOf(delivery.getDeliveredAt()) : null));
            stmt.setString(14, delivery.getReasonNotDelivered());
            stmt.setInt(15, delivery.getId());
            stmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, stmt);
        }
    }

    /**
     * Deleta uma entrega pelo ID.
     *
     * @param id O ID da entrega a ser deletada.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM deliveries WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, stmt);
        }
    }

    /**
     * Busca todas as entregas.
     *
     * @return Uma lista de todas as entregas.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getAll() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
        return deliveries;
    }

    /**
     * Busca entregas por termo de busca (código de rastreio, observações, etc.).
     *
     * @param query O termo de busca.
     * @return Uma lista de entregas que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> search(String query) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries WHERE tracking_code LIKE ? OR observations LIKE ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "%" + query + "%");
            stmt.setString(2, "%" + query + "%");
            rs = stmt.executeQuery();
            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
        return deliveries;
    }

    /**
     * Busca entregas por status.
     *
     * @param status O status da entrega.
     * @return Uma lista de entregas com o status especificado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getDeliveriesByStatus(DeliveryStatus status) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT * FROM deliveries WHERE status = ? ORDER BY created_at DESC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.name());
            rs = stmt.executeQuery();
            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
        return deliveries;
    }

    /**
     * Atualiza o status de uma entrega.
     *
     * @param id               ID da entrega.
     * @param newStatus        Novo status.
     * @param observations     Observações adicionais.
     * @param deliveredAt      Data e hora da entrega (se status for ENTREGUE).
     * @param reasonNotDelivered Motivo da não entrega (se status for NAO_REALIZADA).
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void updateStatus(Integer id, DeliveryStatus newStatus, String observations, LocalDateTime deliveredAt, String reasonNotDelivered) throws SQLException {
        String sql = "UPDATE deliveries SET status = ?, observations = ?, updated_at = ?, delivered_at = ?, reason_not_delivered = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus.name());
            stmt.setString(2, observations);
            stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setTimestamp(4, (deliveredAt != null ? Timestamp.valueOf(deliveredAt) : null));
            stmt.setString(5, reasonNotDelivered);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, stmt);
        }
    }


    /**
     * Mapeia um ResultSet para um objeto Delivery.
     *
     * @param rs O ResultSet.
     * @return Um objeto Delivery.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Delivery mapResultSetToDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setId(rs.getInt("id"));
        delivery.setTrackingCode(rs.getString("tracking_code"));
        delivery.setShipperId(rs.getInt("shipper_id"));
        delivery.setRecipientId(rs.getInt("recipient_id"));
        delivery.setOriginAddressId(rs.getInt("origin_address_id"));
        delivery.setDestinationAddressId(rs.getInt("destination_address_id"));
        delivery.setTotalValue(rs.getBigDecimal("total_value"));
        delivery.setFreightValue(rs.getBigDecimal("freight_value"));
        delivery.setTotalWeightKg(rs.getBigDecimal("total_weight_kg"));
        delivery.setTotalVolumeM3(rs.getBigDecimal("total_volume_m3"));
        delivery.setStatus(DeliveryStatus.fromValue(rs.getString("status"))); // Converte String para enum
        delivery.setObservations(rs.getString("observations"));
        delivery.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        delivery.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        // Campos novos
        Timestamp deliveredAtTimestamp = rs.getTimestamp("delivered_at");
        delivery.setDeliveredAt(deliveredAtTimestamp != null ? deliveredAtTimestamp.toLocalDateTime() : null);
        delivery.setReasonNotDelivered(rs.getString("reason_not_delivered"));
        return delivery;
    }
}
