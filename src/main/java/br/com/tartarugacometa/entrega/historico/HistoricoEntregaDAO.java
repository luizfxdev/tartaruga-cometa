package br.com.tartarugacometa.entrega.historico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.tartarugacometa.config.DatabaseConfig;
import br.com.tartarugacometa.enums.StatusEntrega;

public class HistoricoEntregaDAO {

    public void inserir(Connection conn, HistoricoEntrega historico) throws SQLException {
        String sql = "INSERT INTO delivery_history (delivery_id, previous_status, new_status, change_date, location, observations) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, historico.getDeliveryId());
            stmt.setString(2, historico.getPreviousStatus() != null ? historico.getPreviousStatus().name() : null);
            stmt.setString(3, historico.getNewStatus().name());
            stmt.setTimestamp(4, Timestamp.valueOf(historico.getChangeDate()));
            stmt.setString(5, historico.getLocation());
            stmt.setString(6, historico.getObservations());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    historico.setId(rs.getInt(1));
                }
            }
        }
    }

    public List<HistoricoEntrega> buscarPorEntregaId(Connection conn, Integer deliveryId) throws SQLException {
        List<HistoricoEntrega> historicos = new ArrayList<>();
        String sql = "SELECT id, delivery_id, previous_status, new_status, change_date, location, observations FROM delivery_history WHERE delivery_id = ? ORDER BY change_date";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, deliveryId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    historicos.add(mapear(rs));
                }
            }
        }
        return historicos;
    }

    public void excluirPorEntregaId(Connection conn, Integer deliveryId) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM delivery_history WHERE delivery_id = ?")) {
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }

    public void save(HistoricoEntrega historico) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            inserir(conn, historico);
        }
    }

    public List<HistoricoEntrega> getHistoryByDeliveryId(Integer deliveryId) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorEntregaId(conn, deliveryId);
        }
    }

    public void deleteByDeliveryId(Integer deliveryId) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            excluirPorEntregaId(conn, deliveryId);
        }
    }

    private HistoricoEntrega mapear(ResultSet rs) throws SQLException {
        HistoricoEntrega hist = new HistoricoEntrega();
        hist.setId(rs.getInt("id"));
        hist.setDeliveryId(rs.getInt("delivery_id"));
        
        String prevStatus = rs.getString("previous_status");
        hist.setPreviousStatus(prevStatus != null ? StatusEntrega.valueOf(prevStatus) : null);
        
        String newStatus = rs.getString("new_status");
        hist.setNewStatus(newStatus != null ? StatusEntrega.valueOf(newStatus) : null);
        
        Timestamp changeDate = rs.getTimestamp("change_date");
        if (changeDate != null) {
            hist.setChangeDate(changeDate.toLocalDateTime());
        }
        
        hist.setLocation(rs.getString("location"));
        hist.setObservations(rs.getString("observations"));
        return hist;
    }
}
