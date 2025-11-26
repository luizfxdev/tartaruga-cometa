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
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;

public class DeliveryHistoryDAO {

    public void save(DeliveryHistory history) throws SQLException {
        String sql = "INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, " +
                     "usuario, observacoes, localizacao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, history.getDeliveryId());
            stmt.setString(2, history.getPreviousStatus() != null ? 
                history.getPreviousStatus().getValue() : null);
            stmt.setString(3, history.getNewStatus().getValue());
            stmt.setString(4, history.getUser());
            stmt.setString(5, history.getObservations());
            stmt.setString(6, history.getLocation());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    history.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<DeliveryHistory> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_entrega, status_anterior, status_novo, data_mudanca, " +
                     "usuario, observacoes, localizacao FROM historico_entrega WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeliveryHistory(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<DeliveryHistory> findByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "SELECT id, id_entrega, status_anterior, status_novo, data_mudanca, " +
                     "usuario, observacoes, localizacao FROM historico_entrega " +
                     "WHERE id_entrega = ? ORDER BY data_mudanca DESC";
        List<DeliveryHistory> histories = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    histories.add(mapResultSetToDeliveryHistory(rs));
                }
            }
        }
        return histories;
    }

    public List<DeliveryHistory> findByStatus(DeliveryStatus status) throws SQLException {
        String sql = "SELECT id, id_entrega, status_anterior, status_novo, data_mudanca, " +
                     "usuario, observacoes, localizacao FROM historico_entrega " +
                     "WHERE status_novo = ? ORDER BY data_mudanca DESC";
        List<DeliveryHistory> histories = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.getValue());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    histories.add(mapResultSetToDeliveryHistory(rs));
                }
            }
        }
        return histories;
    }

    public List<DeliveryHistory> findAll() throws SQLException {
        String sql = "SELECT id, id_entrega, status_anterior, status_novo, data_mudanca, " +
                     "usuario, observacoes, localizacao FROM historico_entrega " +
                     "ORDER BY data_mudanca DESC";
        List<DeliveryHistory> histories = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                histories.add(mapResultSetToDeliveryHistory(rs));
            }
        }
        return histories;
    }

    private DeliveryHistory mapResultSetToDeliveryHistory(ResultSet rs) throws SQLException {
        DeliveryHistory history = new DeliveryHistory();
        history.setId(rs.getInt("id"));
        history.setDeliveryId(rs.getInt("id_entrega"));

        String previousStatus = rs.getString("status_anterior");
        if (previousStatus != null) {
            history.setPreviousStatus(DeliveryStatus.fromValue(previousStatus));
        }

        history.setNewStatus(DeliveryStatus.fromValue(rs.getString("status_novo")));
        history.setChangeDate(rs.getTimestamp("data_mudanca").toLocalDateTime());
        history.setUser(rs.getString("usuario"));
        history.setObservations(rs.getString("observacoes"));
        history.setLocation(rs.getString("localizacao"));

        return history;
    }
}
