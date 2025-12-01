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

    /**
     * Cria uma nova entrega no banco de dados.
     *
     * @param delivery O objeto Delivery a ser criado.
     * @return O objeto Delivery com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Delivery save(Delivery delivery) throws SQLException {
        // Removido 'creation_date' da lista de colunas e valores, pois o banco de dados gerencia o DEFAULT.
        // updated_at é tratado por trigger, não precisa ser definido aqui para INSERT.
        String sql = "INSERT INTO delivery (tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, delivery_date, reason_not_delivered) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, delivery.getTrackingCode());
            stmt.setInt(2, delivery.getSenderId()); // Corrigido para getSenderId()
            stmt.setInt(3, delivery.getRecipientId());
            stmt.setInt(4, delivery.getOriginAddressId());
            stmt.setInt(5, delivery.getDestinationAddressId());
            stmt.setBigDecimal(6, delivery.getTotalValue());
            stmt.setBigDecimal(7, delivery.getFreightValue());
            stmt.setBigDecimal(8, delivery.getTotalWeightKg());
            stmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            stmt.setString(10, delivery.getStatus().name()); // Salva o nome do enum
            stmt.setString(11, delivery.getObservations());
            stmt.setTimestamp(12, (delivery.getDeliveryDate() != null ? Timestamp.valueOf(delivery.getDeliveryDate()) : null));
            stmt.setString(13, delivery.getReasonNotDelivered());

            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                delivery.setId(rs.getInt(1));
                // O creation_date e updated_at serão definidos pelo banco de dados.
                // Para ter os valores atualizados no objeto, seria necessário um SELECT após o INSERT,
                // ou confiar nos defaults do banco e triggers. Por simplicidade, não faremos um SELECT aqui.
            }
            return delivery;
        } finally {
            DatabaseConfig.close(conn, stmt, rs);
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
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "delivery_date, reason_not_delivered, updated_at " + // Incluindo updated_at
                     "FROM delivery WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDelivery(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, stmt, rs);
        }
    }

    /**
     * Atualiza uma entrega existente no banco de dados.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Delivery delivery) throws SQLException {
        // updated_at é tratado por trigger, não precisa ser definido aqui explicitamente.
        String sql = "UPDATE delivery SET tracking_code = ?, sender_id = ?, recipient_id = ?, origin_address_id = ?, " +
                     "destination_address_id = ?, total_value = ?, freight_value = ?, total_weight_kg = ?, " +
                     "total_volume_m3 = ?, status = ?, observations = ?, delivery_date = ?, reason_not_delivered = ? " +
                     "WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, delivery.getTrackingCode());
            stmt.setInt(2, delivery.getSenderId()); // Corrigido para getSenderId()
            stmt.setInt(3, delivery.getRecipientId());
            stmt.setInt(4, delivery.getOriginAddressId());
            stmt.setInt(5, delivery.getDestinationAddressId());
            stmt.setBigDecimal(6, delivery.getTotalValue());
            stmt.setBigDecimal(7, delivery.getFreightValue());
            stmt.setBigDecimal(8, delivery.getTotalWeightKg());
            stmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            stmt.setString(10, delivery.getStatus().name());
            stmt.setString(11, delivery.getObservations());
            stmt.setTimestamp(12, (delivery.getDeliveryDate() != null ? Timestamp.valueOf(delivery.getDeliveryDate()) : null));
            stmt.setString(13, delivery.getReasonNotDelivered());
            stmt.setInt(14, delivery.getId());
            stmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, stmt);
        }
    }

    /**
     * Deleta uma entrega pelo ID.
     *
     * @param id O ID da entrega a ser deletada.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM delivery WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, stmt);
        }
    }

    /**
     * Busca todas as entregas.
     *
     * @return Uma lista de todas as entregas.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getAll() throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "delivery_date, reason_not_delivered, updated_at " + // Incluindo updated_at
                     "FROM delivery";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();

            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
            return deliveries;
        } finally {
            DatabaseConfig.close(conn, stmt, rs);
        }
    }

    /**
     * Busca entregas por status.
     *
     * @param status O status da entrega.
     * @return Uma lista de entregas com o status especificado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> findByStatus(DeliveryStatus status) throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "delivery_date, reason_not_delivered, updated_at " + // Incluindo updated_at
                     "FROM delivery WHERE status = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, status.name());
            rs = stmt.executeQuery();

            List<Delivery> deliveries = new ArrayList<>();
            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
            return deliveries;
        } finally {
            DatabaseConfig.close(conn, stmt, rs);
        }
    }

    /**
     * Busca entregas por código de rastreamento.
     *
     * @param trackingCode O código de rastreamento.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> findByTrackingCode(String trackingCode) throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "delivery_date, reason_not_delivered, updated_at " + // Incluindo updated_at
                     "FROM delivery WHERE tracking_code = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, trackingCode);
            rs = stmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDelivery(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, stmt, rs);
        }
    }

    /**
     * Atualiza apenas o status de uma entrega, suas observações, data de entrega e razão de não entrega.
     *
     * @param id                 O ID da entrega.
     * @param newStatus          O novo status da entrega.
     * @param observations       Observações adicionais sobre a mudança de status.
     * @param deliveryDate       Data e hora da entrega (se o status for DELIVERED).
     * @param reasonNotDelivered Razão para não entrega (se o status for NOT_PERFORMED).
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void updateStatus(Integer id, DeliveryStatus newStatus, String observations, LocalDateTime deliveryDate, String reasonNotDelivered) throws SQLException {
        // updated_at é tratado por trigger, não precisa ser definido aqui.
        String sql = "UPDATE delivery SET status = ?, observations = ?, delivery_date = ?, reason_not_delivered = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newStatus.name());
            stmt.setString(2, observations);
            stmt.setTimestamp(3, (deliveryDate != null ? Timestamp.valueOf(deliveryDate) : null));
            stmt.setString(4, reasonNotDelivered);
            stmt.setInt(5, id);
            stmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, stmt);
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
        delivery.setSenderId(rs.getInt("sender_id")); // Corrigido para setSenderId()
        delivery.setRecipientId(rs.getInt("recipient_id"));
        delivery.setOriginAddressId(rs.getInt("origin_address_id"));
        delivery.setDestinationAddressId(rs.getInt("destination_address_id"));
        delivery.setTotalValue(rs.getBigDecimal("total_value"));
        delivery.setFreightValue(rs.getBigDecimal("freight_value"));
        delivery.setTotalWeightKg(rs.getBigDecimal("total_weight_kg"));
        delivery.setTotalVolumeM3(rs.getBigDecimal("total_volume_m3"));
        delivery.setStatus(DeliveryStatus.fromValue(rs.getString("status"))); // Converte String para enum
        delivery.setObservations(rs.getString("observations"));
        delivery.setCreationDate(rs.getTimestamp("creation_date").toLocalDateTime()); // Corrigido para setCreationDate()
        delivery.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        // Novos campos
        Timestamp deliveryDateTimestamp = rs.getTimestamp("delivery_date");
        delivery.setDeliveryDate(deliveryDateTimestamp != null ? deliveryDateTimestamp.toLocalDateTime() : null);
        delivery.setReasonNotDelivered(rs.getString("reason_not_delivered"));
        return delivery;
    }
}
