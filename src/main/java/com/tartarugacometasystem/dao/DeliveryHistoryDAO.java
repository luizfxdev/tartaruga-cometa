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

import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;
import com.tartarugacometasystem.util.ConnectionFactory;

public class DeliveryHistoryDAO {

    /**
     * Cria um novo registro de histórico de entrega no banco de dados.
     *
     * @param history O objeto DeliveryHistory a ser criado.
     * @return O objeto DeliveryHistory com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public DeliveryHistory save(DeliveryHistory history) throws SQLException {
        String sql = "INSERT INTO delivery_history (delivery_id, status, observations, user, created_at) VALUES (?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, history.getDeliveryId());
            stmt.setString(2, history.getStatus().name()); // Salva o nome do enum
            stmt.setString(3, history.getObservations());
            stmt.setString(4, history.getUser());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                history.setId(rs.getInt(1));
            }
            return history;
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
    }

    /**
     * Busca registros de histórico de entrega pelo ID da entrega.
     *
     * @param deliveryId O ID da entrega.
     * @return Uma lista de registros de histórico para a entrega.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<DeliveryHistory> getHistoryByDeliveryId(Integer deliveryId) throws SQLException {
        List<DeliveryHistory> historyList = new ArrayList<>();
        String sql = "SELECT * FROM delivery_history WHERE delivery_id = ? ORDER BY created_at ASC";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, deliveryId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                historyList.add(mapResultSetToDeliveryHistory(rs));
            }
        } finally {
            ConnectionFactory.close(conn, stmt, rs);
        }
        return historyList;
    }

    /**
     * Deleta todos os registros de histórico para uma dada entrega.
     *
     * @param deliveryId O ID da entrega.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "DELETE FROM delivery_history WHERE delivery_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, stmt);
        }
    }

    /**
     * Mapeia um ResultSet para um objeto DeliveryHistory.
     *
     * @param rs O ResultSet.
     * @return Um objeto DeliveryHistory.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private DeliveryHistory mapResultSetToDeliveryHistory(ResultSet rs) throws SQLException {
        DeliveryHistory history = new DeliveryHistory();
        history.setId(rs.getInt("id"));
        history.setDeliveryId(rs.getInt("delivery_id"));
        history.setStatus(DeliveryStatus.fromValue(rs.getString("status"))); // Converte String para enum
        history.setObservations(rs.getString("observations"));
        history.setUser(rs.getString("user"));
        history.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return history;
    }
}
