package br.com.tartarugacometa.entrega.historico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import br.com.tartarugacometa.enums.StatusEntrega;

public class HistoricoEntregaDAO {

    public void inserir(Connection conn, HistoricoEntrega historico) throws SQLException {
        String sql = "INSERT INTO historico_entrega (id_entrega, status_anterior, status_novo, data_mudanca, localizacao, observacoes) VALUES (?, CAST(? AS status_entrega), CAST(? AS status_entrega), ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, historico.getDeliveryId());
            stmt.setString(2, historico.getPreviousStatus() != null ? historico.getPreviousStatus().paraColuna() : null);
            stmt.setString(3, historico.getNewStatus().paraColuna());
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
        String sql = "SELECT id, id_entrega, status_anterior, status_novo, data_mudanca, localizacao, observacoes FROM historico_entrega WHERE id_entrega = ? ORDER BY data_mudanca";
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
        try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM historico_entrega WHERE id_entrega = ?")) {
            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }

    private HistoricoEntrega mapear(ResultSet rs) throws SQLException {
        HistoricoEntrega hist = new HistoricoEntrega();
        hist.setId(rs.getInt("id"));
        hist.setDeliveryId(rs.getInt("id_entrega"));

        String prevStatus = rs.getString("status_anterior");
        hist.setPreviousStatus(prevStatus != null ? StatusEntrega.fromValue(prevStatus) : null);

        String newStatus = rs.getString("status_novo");
        hist.setNewStatus(newStatus != null ? StatusEntrega.fromValue(newStatus) : null);

        Timestamp changeDate = rs.getTimestamp("data_mudanca");
        if (changeDate != null) {
            hist.setChangeDate(changeDate.toLocalDateTime());
        }

        hist.setLocation(rs.getString("localizacao"));
        hist.setObservations(rs.getString("observacoes"));
        return hist;
    }
}
