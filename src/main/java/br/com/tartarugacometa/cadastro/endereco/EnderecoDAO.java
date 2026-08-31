package br.com.tartarugacometa.cadastro.endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.enums.TipoEndereco;

public class EnderecoDAO {

    public void inserir(Connection conn, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO endereco (id_cliente, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal, tipo_endereco, referencia, pais) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, endereco.getClientId());
            pstmt.setString(2, endereco.getStreet());
            pstmt.setString(3, endereco.getNumber());
            pstmt.setString(4, endereco.getComplement());
            pstmt.setString(5, endereco.getNeighborhood());
            pstmt.setString(6, endereco.getCity());
            pstmt.setString(7, endereco.getState());
            pstmt.setString(8, endereco.getZipCode());
            pstmt.setBoolean(9, endereco.getIsMain());
            pstmt.setString(10, endereco.getAddressType().paraColuna());
            pstmt.setString(11, endereco.getReference());
            pstmt.setString(12, endereco.getCountry());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    endereco.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Endereco> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, id_cliente, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal, tipo_endereco, referencia, pais, created_at, updated_at FROM endereco WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearEnderecoDoResultSet(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void atualizar(Connection conn, Endereco endereco) throws SQLException {
        String sql = "UPDATE endereco SET id_cliente = ?, logradouro = ?, numero = ?, complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, is_principal = ?, tipo_endereco = ?, referencia = ?, pais = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, endereco.getClientId());
            pstmt.setString(2, endereco.getStreet());
            pstmt.setString(3, endereco.getNumber());
            pstmt.setString(4, endereco.getComplement());
            pstmt.setString(5, endereco.getNeighborhood());
            pstmt.setString(6, endereco.getCity());
            pstmt.setString(7, endereco.getState());
            pstmt.setString(8, endereco.getZipCode());
            pstmt.setBoolean(9, endereco.getIsMain());
            pstmt.setString(10, endereco.getAddressType().paraColuna());
            pstmt.setString(11, endereco.getReference());
            pstmt.setString(12, endereco.getCountry());
            pstmt.setInt(13, endereco.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM endereco WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Endereco> buscarTodos(Connection conn) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT id, id_cliente, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal, tipo_endereco, referencia, pais, created_at, updated_at FROM endereco";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                enderecos.add(mapearEnderecoDoResultSet(rs));
            }
        }
        return enderecos;
    }

    public List<Endereco> buscarPorClienteId(Connection conn, Integer clienteId) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT id, id_cliente, logradouro, numero, complemento, bairro, cidade, estado, cep, is_principal, tipo_endereco, referencia, pais, created_at, updated_at FROM endereco WHERE id_cliente = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    enderecos.add(mapearEnderecoDoResultSet(rs));
                }
            }
        }
        return enderecos;
    }

    public void definirEnderecoPrincipal(Connection conn, Integer clienteId, Integer enderecoId) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE endereco SET is_principal = FALSE WHERE id_cliente = ?")) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE endereco SET is_principal = TRUE WHERE id = ? AND id_cliente = ?")) {
            pstmt.setInt(1, enderecoId);
            pstmt.setInt(2, clienteId);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Endereço não encontrado para este cliente.");
            }
        }
    }

    private Endereco mapearEnderecoDoResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("id"));
        endereco.setClientId(rs.getInt("id_cliente"));
        endereco.setStreet(rs.getString("logradouro"));
        endereco.setNumber(rs.getString("numero"));
        endereco.setComplement(rs.getString("complemento"));
        endereco.setNeighborhood(rs.getString("bairro"));
        endereco.setCity(rs.getString("cidade"));
        endereco.setState(rs.getString("estado"));
        endereco.setZipCode(rs.getString("cep"));
        endereco.setIsMain(rs.getBoolean("is_principal"));
        endereco.setAddressType(TipoEndereco.fromValue(rs.getString("tipo_endereco")));
        endereco.setReference(rs.getString("referencia"));
        endereco.setCountry(rs.getString("pais"));

        Timestamp criadoEm = rs.getTimestamp("created_at");
        if (criadoEm != null) {
            endereco.setCreatedAt(criadoEm.toLocalDateTime());
        }
        Timestamp atualizadoEm = rs.getTimestamp("updated_at");
        if (atualizadoEm != null) {
            endereco.setUpdatedAt(atualizadoEm.toLocalDateTime());
        }
        return endereco;
    }

    public boolean contemEntregasVinculadas(Connection conn, Integer enderecoId) throws SQLException {
        String sql = "SELECT 1 FROM entrega WHERE id_endereco_origem = ? OR id_endereco_destino = ? LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enderecoId);
            pstmt.setInt(2, enderecoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
