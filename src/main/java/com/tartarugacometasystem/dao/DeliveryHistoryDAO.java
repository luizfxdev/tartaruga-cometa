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

import com.tartarugacometasystem.config.DatabaseConfig; // Import alterado
import com.tartarugacometasystem.model.DeliveryHistory;
import com.tartarugacometasystem.model.DeliveryStatus;

public class DeliveryHistoryDAO {

    /**
     * Cria um novo registro de histórico de entrega no banco de dados.
     *
     * @param history O objeto DeliveryHistory a ser criado.
     * @return O objeto DeliveryHistory com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public DeliveryHistory save(DeliveryHistory history) throws SQLException {
        String sql = "INSERT INTO delivery_history (delivery_id, previous_status, new_status, change_date, \"user\", observations, location) VALUES (?, ?, ?, ?, ?, ?, ?)"; // Nomes das colunas alterados e novas adicionadas
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, history.getDeliveryId());
            stmt.setString(2, history.getPreviousStatus() != null ? history.getPreviousStatus().name() : null); // Nova coluna
            stmt.setString(3, history.getNewStatus().name()); // Nome da coluna alterado
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); // change_date
            stmt.setString(5, history.getUser());
            stmt.setString(6, history.getObservations()); // Nova coluna
            stmt.setString(7, history.getLocation()); // Nova coluna
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                history.setId(rs.getInt(1));
            }
            return history;
        } finally {
            DatabaseConfig.close(conn, stmt, rs); // Chamada alterada
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
        String sql = "SELECT * FROM delivery_history WHERE delivery_id = ? ORDER BY change_date ASC"; // Nome da coluna alterado
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, deliveryId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                historyList.add(mapResultSetToDeliveryHistory(rs));
            }
        } finally {
            DatabaseConfig.close(conn, stmt, rs); // Chamada alterada
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
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, stmt); // Chamada alterada
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
        history.setPreviousStatus(DeliveryStatus.fromValue(rs.getString("previous_status"))); // Nova coluna
        history.setNewStatus(DeliveryStatus.fromValue(rs.getString("new_status"))); // Nome da coluna alterado
        history.setChangeDate(rs.getTimestamp("change_date").toLocalDateTime()); // Nome da coluna alterado
        history.setUser(rs.getString("user"));
        history.setObservations(rs.getString("observations")); // Nova coluna
        history.setLocation(rs.getString("location")); // Nova coluna
        return history;
    }
}
