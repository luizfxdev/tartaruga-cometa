package br.com.tartarugacometa.entrega;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.config.DatabaseConfig;
import br.com.tartarugacometa.enums.StatusEntrega;

public class EntregaDAO {

    public void inserir(Connection conn, Entrega entrega) throws SQLException {
        String sql = "INSERT INTO delivery (tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, delivery_date, reason_not_delivered) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS delivery_status_enum), ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, entrega.getTrackingCode());
            pstmt.setObject(2, entrega.getSenderId());
            pstmt.setObject(3, entrega.getRecipientId());
            pstmt.setObject(4, entrega.getOriginAddressId());
            pstmt.setObject(5, entrega.getDestinationAddressId());
            pstmt.setBigDecimal(6, entrega.getTotalValue());
            pstmt.setBigDecimal(7, entrega.getFreightValue());
            pstmt.setBigDecimal(8, entrega.getTotalWeightKg());
            pstmt.setBigDecimal(9, entrega.getTotalVolumeM3());
            pstmt.setString(10, entrega.getStatus().name());
            pstmt.setString(11, entrega.getObservations());
            pstmt.setTimestamp(12, entrega.getDeliveryDate() != null ? Timestamp.valueOf(entrega.getDeliveryDate()) : null);
            pstmt.setString(13, entrega.getReasonNotDelivered());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    entrega.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Entrega> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, updated_at, delivery_date, reason_not_delivered FROM delivery WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void atualizar(Connection conn, Entrega entrega) throws SQLException {
        String sql = "UPDATE delivery SET tracking_code = ?, sender_id = ?, recipient_id = ?, origin_address_id = ?, destination_address_id = ?, total_value = ?, freight_value = ?, total_weight_kg = ?, total_volume_m3 = ?, status = CAST(? AS delivery_status_enum), observations = ?, delivery_date = ?, reason_not_delivered = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entrega.getTrackingCode());
            pstmt.setObject(2, entrega.getSenderId());
            pstmt.setObject(3, entrega.getRecipientId());
            pstmt.setObject(4, entrega.getOriginAddressId());
            pstmt.setObject(5, entrega.getDestinationAddressId());
            pstmt.setBigDecimal(6, entrega.getTotalValue());
            pstmt.setBigDecimal(7, entrega.getFreightValue());
            pstmt.setBigDecimal(8, entrega.getTotalWeightKg());
            pstmt.setBigDecimal(9, entrega.getTotalVolumeM3());
            pstmt.setString(10, entrega.getStatus().name());
            pstmt.setString(11, entrega.getObservations());
            pstmt.setTimestamp(12, entrega.getDeliveryDate() != null ? Timestamp.valueOf(entrega.getDeliveryDate()) : null);
            pstmt.setString(13, entrega.getReasonNotDelivered());
            pstmt.setInt(14, entrega.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM delivery WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Entrega> buscarTodos(Connection conn) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, updated_at, delivery_date, reason_not_delivered FROM delivery";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                entregas.add(mapear(rs));
            }
        }
        return entregas;
    }

    public List<Entrega> buscarPorStatus(Connection conn, StatusEntrega status) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, updated_at, delivery_date, reason_not_delivered FROM delivery WHERE status = CAST(? AS delivery_status_enum)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.name());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entregas.add(mapear(rs));
                }
            }
        }
        return entregas;
    }

    public Optional<Entrega> buscarPorCodigoRastreamento(Connection conn, String codigoRastreamento) throws SQLException {
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, updated_at, delivery_date, reason_not_delivered FROM delivery WHERE tracking_code = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, codigoRastreamento);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapear(rs));
                }
                return Optional.empty();
            }
        }
    }

    public List<Entrega> pesquisar(Connection conn, String termo) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, tracking_code, sender_id, recipient_id, origin_address_id, destination_address_id, total_value, freight_value, total_weight_kg, total_volume_m3, status, observations, creation_date, updated_at, delivery_date, reason_not_delivered FROM delivery WHERE tracking_code ILIKE ? OR observations ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String padrao = "%" + termo + "%";
            pstmt.setString(1, padrao);
            pstmt.setString(2, padrao);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entregas.add(mapear(rs));
                }
            }
        }
        return entregas;
    }

    public Entrega save(Entrega entrega) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            inserir(conn, entrega);
            return entrega;
        }
    }

    public Optional<Entrega> findById(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorId(conn, id);
        }
    }

    public void update(Entrega entrega) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            atualizar(conn, entrega);
        }
    }

    public void delete(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            excluir(conn, id);
        }
    }

    public List<Entrega> getAll() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarTodos(conn);
        }
    }

    public List<Entrega> findByStatus(StatusEntrega status) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorStatus(conn, status);
        }
    }

    public Optional<Entrega> findByTrackingCode(String trackingCode) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorCodigoRastreamento(conn, trackingCode);
        }
    }

    public List<Entrega> search(String termo) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return pesquisar(conn, termo);
        }
    }

    private Entrega mapear(ResultSet rs) throws SQLException {
        Entrega entrega = new Entrega();
        entrega.setId(rs.getInt("id"));
        entrega.setTrackingCode(rs.getString("tracking_code"));
        entrega.setSenderId(rs.getObject("sender_id") != null ? rs.getInt("sender_id") : null);
        entrega.setRecipientId(rs.getObject("recipient_id") != null ? rs.getInt("recipient_id") : null);
        entrega.setOriginAddressId(rs.getObject("origin_address_id") != null ? rs.getInt("origin_address_id") : null);
        entrega.setDestinationAddressId(rs.getObject("destination_address_id") != null ? rs.getInt("destination_address_id") : null);
        entrega.setTotalValue(rs.getBigDecimal("total_value"));
        entrega.setFreightValue(rs.getBigDecimal("freight_value"));
        entrega.setTotalWeightKg(rs.getBigDecimal("total_weight_kg"));
        entrega.setTotalVolumeM3(rs.getBigDecimal("total_volume_m3"));
        entrega.setStatus(StatusEntrega.valueOf(rs.getString("status")));
        entrega.setObservations(rs.getString("observations"));

        Timestamp criacaoDt = rs.getTimestamp("creation_date");
        if (criacaoDt != null) {
            entrega.setCreationDate(criacaoDt.toLocalDateTime());
        }

        Timestamp atualizacaoDt = rs.getTimestamp("updated_at");
        if (atualizacaoDt != null) {
            entrega.setUpdatedAt(atualizacaoDt.toLocalDateTime());
        }

        Timestamp entregaDt = rs.getTimestamp("delivery_date");
        if (entregaDt != null) {
            entrega.setDeliveryDate(entregaDt.toLocalDateTime());
        }

        entrega.setReasonNotDelivered(rs.getString("reason_not_delivered"));
        return entrega;
    }
}
