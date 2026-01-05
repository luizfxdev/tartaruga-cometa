package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;

public class DeliveryHistoryDAO {

    /**
     * Salva um novo registro de histórico de entrega no banco de dados.
     *
     * @param history O objeto DeliveryHistory a ser salvo.
     * @return O objeto DeliveryHistory com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public DeliveryHistory save(DeliveryHistory history) throws SQLException {
        String sql = "INSERT INTO delivery_history (delivery_id, previous_status, new_status, change_date, location, observations) VALUES (?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, history.getDeliveryId());
            pstmt.setString(2, history.getPreviousStatus() != null ? history.getPreviousStatus().name() : null);
            pstmt.setString(3, history.getNewStatus() != null ? history.getNewStatus().name() : null);
            pstmt.setTimestamp(4, Timestamp.valueOf(history.getChangeDate()));
            pstmt.setString(5, history.getLocation());
            pstmt.setString(6, history.getObservations());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                history.setId(rs.getInt(1));
            }
            return history;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca o histórico de uma entrega pelo ID da entrega.
     *
     * @param deliveryId O ID da entrega.
     * @return Uma lista de registros de histórico para a entrega especificada.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<DeliveryHistory> getHistoryByDeliveryId(Integer deliveryId) throws SQLException {
        List<DeliveryHistory> historyList = new ArrayList<>();
        String sql = "SELECT id, delivery_id, previous_status, new_status, change_date, location, observations FROM delivery_history WHERE delivery_id = ? ORDER BY change_date ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliveryId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                historyList.add(mapResultSetToDeliveryHistory(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return historyList;
    }

    /**
     * Deleta todos os registros de histórico associados a uma entrega.
     *
     * @param deliveryId O ID da entrega cujos históricos serão deletados.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "DELETE FROM delivery_history WHERE delivery_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, deliveryId);
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
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

        String previousStatusStr = rs.getString("previous_status");
        history.setPreviousStatus(previousStatusStr != null ? DeliveryStatus.fromValue(previousStatusStr) : null);

        String newStatusStr = rs.getString("new_status");
        history.setNewStatus(newStatusStr != null ? DeliveryStatus.fromValue(newStatusStr) : null);

        Timestamp changeDateTimestamp = rs.getTimestamp("change_date");
        history.setChangeDate(changeDateTimestamp != null ? changeDateTimestamp.toLocalDateTime() : null);

        history.setLocation(rs.getString("location"));
        history.setObservations(rs.getString("observations"));
        return history;
    }
}
