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
        String sql = "INSERT INTO delivery (tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, delivery_date, reason_not_delivered) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS delivery_status_enum), ?, ?, ?)"; // Status já corrigido

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, delivery.getTrackingCode());

            // --- CORREÇÃO AQUI para sender_id, recipient_id, origin_address_id, destination_address_id ---
            if (delivery.getSenderId() != null) {
                pstmt.setInt(2, delivery.getSenderId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (delivery.getRecipientId() != null) {
                pstmt.setInt(3, delivery.getRecipientId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            if (delivery.getOriginAddressId() != null) {
                pstmt.setInt(4, delivery.getOriginAddressId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (delivery.getDestinationAddressId() != null) {
                pstmt.setInt(5, delivery.getDestinationAddressId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            // --- FIM DA CORREÇÃO ---

            pstmt.setBigDecimal(6, delivery.getTotalValue());
            pstmt.setBigDecimal(7, delivery.getFreightValue());
            pstmt.setBigDecimal(8, delivery.getTotalWeightKg());
            pstmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            pstmt.setString(10, delivery.getStatus().name()); // O CAST na SQL já cuida da conversão para enum
            pstmt.setString(11, delivery.getObservations());
            pstmt.setTimestamp(12, delivery.getDeliveryDate() != null ? Timestamp.valueOf(delivery.getDeliveryDate()) : null);
            pstmt.setString(13, delivery.getReasonNotDelivered());

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                delivery.setId(rs.getInt(1));
            }
            return delivery;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
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
                     "updated_at, delivery_date, reason_not_delivered FROM delivery WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDelivery(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Atualiza uma entrega existente no banco de dados.
     *
     * @param delivery O objeto Delivery a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Delivery delivery) throws SQLException {
        String sql = "UPDATE delivery SET tracking_code = ?, sender_id = ?, recipient_id = ?, origin_address_id = ?, " +
                     "destination_address_id = ?, total_value = ?, freight_value = ?, total_weight_kg = ?, " +
                     "total_volume_m3 = ?, status = CAST(? AS delivery_status_enum), observations = ?, delivery_date = ?, reason_not_delivered = ? WHERE id = ?"; // Status já corrigido

        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);

            pstmt.setString(1, delivery.getTrackingCode());

            // --- CORREÇÃO AQUI para sender_id, recipient_id, origin_address_id, destination_address_id ---
            if (delivery.getSenderId() != null) {
                pstmt.setInt(2, delivery.getSenderId());
            } else {
                pstmt.setNull(2, java.sql.Types.INTEGER);
            }

            if (delivery.getRecipientId() != null) {
                pstmt.setInt(3, delivery.getRecipientId());
            } else {
                pstmt.setNull(3, java.sql.Types.INTEGER);
            }

            if (delivery.getOriginAddressId() != null) {
                pstmt.setInt(4, delivery.getOriginAddressId());
            } else {
                pstmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (delivery.getDestinationAddressId() != null) {
                pstmt.setInt(5, delivery.getDestinationAddressId());
            } else {
                pstmt.setNull(5, java.sql.Types.INTEGER);
            }
            // --- FIM DA CORREÇÃO ---

            pstmt.setBigDecimal(6, delivery.getTotalValue());
            pstmt.setBigDecimal(7, delivery.getFreightValue());
            pstmt.setBigDecimal(8, delivery.getTotalWeightKg());
            pstmt.setBigDecimal(9, delivery.getTotalVolumeM3());
            pstmt.setString(10, delivery.getStatus().name()); // O CAST na SQL já cuida da conversão para enum
            pstmt.setString(11, delivery.getObservations());
            pstmt.setTimestamp(12, delivery.getDeliveryDate() != null ? Timestamp.valueOf(delivery.getDeliveryDate()) : null);
            pstmt.setString(13, delivery.getReasonNotDelivered());
            pstmt.setInt(14, delivery.getId());

            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
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
     * Busca todas as entregas.
     *
     * @return Uma lista de todas as entregas.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> getAll() throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "updated_at, delivery_date, reason_not_delivered FROM delivery";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
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
    public List<Delivery> findByStatus(DeliveryStatus status) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "updated_at, delivery_date, reason_not_delivered FROM delivery WHERE status = CAST(? AS delivery_status_enum)"; // Status já corrigido
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status.name());
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return deliveries;
    }

    /**
     * Busca entregas por código de rastreio.
     *
     * @param trackingCode O código de rastreio da entrega.
     * @return Um Optional contendo o Delivery se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Delivery> findByTrackingCode(String trackingCode) throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "updated_at, delivery_date, reason_not_delivered FROM delivery WHERE tracking_code = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, trackingCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToDelivery(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca entregas que correspondem a um termo de busca em tracking_code, status ou observações.
     *
     * @param searchTerm O termo de busca.
     * @return Uma lista de entregas que correspondem ao termo de busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Delivery> search(String searchTerm) throws SQLException {
        List<Delivery> deliveries = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, " +
                     "total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, " +
                     "updated_at, delivery_date, reason_not_delivered FROM delivery " +
                     "WHERE tracking_code ILIKE ? OR CAST(status AS TEXT) ILIKE ? OR observations ILIKE ?"; // Status já corrigido
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + searchTerm + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                deliveries.add(mapResultSetToDelivery(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return deliveries;
    }

    /**
     * Mapeia um ResultSet para um objeto Delivery.
     *
     * @param rs O ResultSet.
     * @return Um objeto Delivery preenchido.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Delivery mapResultSetToDelivery(ResultSet rs) throws SQLException {
        Delivery delivery = new Delivery();
        delivery.setId(rs.getInt("id"));
        delivery.setTrackingCode(rs.getString("tracking_code"));

        // --- CORREÇÃO AQUI para ler IDs que podem ser NULL do banco de dados ---
        int senderId = rs.getInt("sender_id");
        if (!rs.wasNull()) {
            delivery.setSenderId(senderId);
        }

        int recipientId = rs.getInt("recipient_id");
        if (!rs.wasNull()) {
            delivery.setRecipientId(recipientId);
        }

        int originAddressId = rs.getInt("origin_address_id");
        if (!rs.wasNull()) {
            delivery.setOriginAddressId(originAddressId);
        }

        int destinationAddressId = rs.getInt("destination_address_id");
        if (!rs.wasNull()) {
            delivery.setDestinationAddressId(destinationAddressId);
        }
        // --- FIM DA CORREÇÃO ---

        delivery.setTotalValue(rs.getBigDecimal("total_value"));
        delivery.setFreightValue(rs.getBigDecimal("freight_value"));
        delivery.setTotalWeightKg(rs.getBigDecimal("total_weight_kg"));
        delivery.setTotalVolumeM3(rs.getBigDecimal("total_volume_m3"));
        delivery.setStatus(DeliveryStatus.valueOf(rs.getString("status")));

        delivery.setObservations(rs.getString("observations"));

        Timestamp creationDateTimestamp = rs.getTimestamp("creation_date");
        delivery.setCreationDate(creationDateTimestamp != null ? creationDateTimestamp.toLocalDateTime() : null);

        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        delivery.setUpdatedAt(updatedAtTimestamp != null ? updatedAtTimestamp.toLocalDateTime() : null);

        Timestamp deliveryDateTimestamp = rs.getTimestamp("delivery_date");
        delivery.setDeliveryDate(deliveryDateTimestamp != null ? deliveryDateTimestamp.toLocalDateTime() : null);

        delivery.setReasonNotDelivered(rs.getString("reason_not_delivered"));
        return delivery;
    }
}
