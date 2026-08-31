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

import br.com.tartarugacometa.enums.StatusEntrega;

public class EntregaDAO {

    public void inserir(Connection conn, Entrega entrega) throws SQLException {
        String sql = "INSERT INTO entrega (codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_entrega, motivo_cancelamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CAST(? AS status_entrega), ?, ?, ?)";
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
            pstmt.setString(10, entrega.getStatus().paraColuna());
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
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_criacao, updated_at, data_entrega, motivo_cancelamento FROM entrega WHERE id = ?";
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
        String sql = "UPDATE entrega SET codigo_rastreio = ?, id_remetente = ?, id_destinatario = ?, id_endereco_origem = ?, id_endereco_destino = ?, valor_total = ?, valor_frete = ?, peso_total_kg = ?, volume_total_m3 = ?, status = CAST(? AS status_entrega), observacoes = ?, data_entrega = ?, motivo_cancelamento = ? WHERE id = ?";
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
            pstmt.setString(10, entrega.getStatus().paraColuna());
            pstmt.setString(11, entrega.getObservations());
            pstmt.setTimestamp(12, entrega.getDeliveryDate() != null ? Timestamp.valueOf(entrega.getDeliveryDate()) : null);
            pstmt.setString(13, entrega.getReasonNotDelivered());
            pstmt.setInt(14, entrega.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM entrega WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Entrega> buscarTodos(Connection conn) throws SQLException {
        List<Entrega> entregas = new ArrayList<>();
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_criacao, updated_at, data_entrega, motivo_cancelamento FROM entrega";
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
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_criacao, updated_at, data_entrega, motivo_cancelamento FROM entrega WHERE status = CAST(? AS status_entrega)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status.paraColuna());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    entregas.add(mapear(rs));
                }
            }
        }
        return entregas;
    }

    public Optional<Entrega> buscarPorCodigoRastreamento(Connection conn, String codigoRastreamento) throws SQLException {
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_criacao, updated_at, data_entrega, motivo_cancelamento FROM entrega WHERE codigo_rastreio = ?";
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
        String sql = "SELECT id, codigo_rastreio, id_remetente, id_destinatario, id_endereco_origem, id_endereco_destino, valor_total, valor_frete, peso_total_kg, volume_total_m3, status, observacoes, data_criacao, updated_at, data_entrega, motivo_cancelamento FROM entrega WHERE codigo_rastreio ILIKE ? OR observacoes ILIKE ?";
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

    private Entrega mapear(ResultSet rs) throws SQLException {
        Entrega entrega = new Entrega();
        entrega.setId(rs.getInt("id"));
        entrega.setTrackingCode(rs.getString("codigo_rastreio"));
        entrega.setSenderId(rs.getObject("id_remetente") != null ? rs.getInt("id_remetente") : null);
        entrega.setRecipientId(rs.getObject("id_destinatario") != null ? rs.getInt("id_destinatario") : null);
        entrega.setOriginAddressId(rs.getObject("id_endereco_origem") != null ? rs.getInt("id_endereco_origem") : null);
        entrega.setDestinationAddressId(rs.getObject("id_endereco_destino") != null ? rs.getInt("id_endereco_destino") : null);
        entrega.setTotalValue(rs.getBigDecimal("valor_total"));
        entrega.setFreightValue(rs.getBigDecimal("valor_frete"));
        entrega.setTotalWeightKg(rs.getBigDecimal("peso_total_kg"));
        entrega.setTotalVolumeM3(rs.getBigDecimal("volume_total_m3"));
        entrega.setStatus(StatusEntrega.fromValue(rs.getString("status")));
        entrega.setObservations(rs.getString("observacoes"));

        Timestamp criacaoDt = rs.getTimestamp("data_criacao");
        if (criacaoDt != null) {
            entrega.setCreationDate(criacaoDt.toLocalDateTime());
        }

        Timestamp atualizacaoDt = rs.getTimestamp("updated_at");
        if (atualizacaoDt != null) {
            entrega.setUpdatedAt(atualizacaoDt.toLocalDateTime());
        }

        Timestamp entregaDt = rs.getTimestamp("data_entrega");
        if (entregaDt != null) {
            entrega.setDeliveryDate(entregaDt.toLocalDateTime());
        }

        entrega.setReasonNotDelivered(rs.getString("motivo_cancelamento"));
        return entrega;
    }

    public boolean existeCodigoRastreio(Connection conn, String trackingCode) throws SQLException {
        String sql = "SELECT 1 FROM entrega WHERE codigo_rastreio = ? LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, trackingCode);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
