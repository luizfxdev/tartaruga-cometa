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

import br.com.tartarugacometa.config.DatabaseConfig;
import br.com.tartarugacometa.enums.TipoEndereco;

public class EnderecoDAO {

    public void inserir(Connection conn, Endereco endereco) throws SQLException {
        String sql = "INSERT INTO address (client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, endereco.getClientId());
            pstmt.setString(2, endereco.getStreet());
            pstmt.setString(3, endereco.getNumber());
            pstmt.setString(4, endereco.getComplement());
            pstmt.setString(5, endereco.getNeighborhood());
            pstmt.setString(6, endereco.getCity());
            pstmt.setString(7, endereco.getState());
            pstmt.setString(8, endereco.getZipCode());
            pstmt.setString(9, endereco.getCountry());
            pstmt.setBoolean(10, endereco.getIsMain());
            pstmt.setString(11, endereco.getAddressType().name());
            pstmt.setString(12, endereco.getReference());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    endereco.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Endereco> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address WHERE id = ?";
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
        String sql = "UPDATE address SET client_id = ?, street = ?, number = ?, complement = ?, neighborhood = ?, city = ?, state = ?, zip_code = ?, country = ?, is_main = ?, address_type = ?, reference = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, endereco.getClientId());
            pstmt.setString(2, endereco.getStreet());
            pstmt.setString(3, endereco.getNumber());
            pstmt.setString(4, endereco.getComplement());
            pstmt.setString(5, endereco.getNeighborhood());
            pstmt.setString(6, endereco.getCity());
            pstmt.setString(7, endereco.getState());
            pstmt.setString(8, endereco.getZipCode());
            pstmt.setString(9, endereco.getCountry());
            pstmt.setBoolean(10, endereco.getIsMain());
            pstmt.setString(11, endereco.getAddressType().name());
            pstmt.setString(12, endereco.getReference());
            pstmt.setInt(13, endereco.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM address WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Endereco> buscarTodos(Connection conn) throws SQLException {
        List<Endereco> enderecos = new ArrayList<>();
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address";
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
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address WHERE client_id = ?";
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
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE address SET is_main = FALSE WHERE client_id = ?")) {
            pstmt.setInt(1, clienteId);
            pstmt.executeUpdate();
        }

        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE address SET is_main = TRUE WHERE id = ? AND client_id = ?")) {
            pstmt.setInt(1, enderecoId);
            pstmt.setInt(2, clienteId);
            if (pstmt.executeUpdate() == 0) {
                throw new SQLException("Endereço não encontrado para este cliente.");
            }
        }
    }

    public Endereco save(Endereco endereco) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            inserir(conn, endereco);
            return endereco;
        }
    }

    public Optional<Endereco> findById(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorId(conn, id);
        }
    }

    public void update(Endereco endereco) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            atualizar(conn, endereco);
        }
    }

    public void delete(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            excluir(conn, id);
        }
    }

    public List<Endereco> getAll() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarTodos(conn);
        }
    }

    public List<Endereco> findByClientId(Integer clientId) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorClienteId(conn, clientId);
        }
    }

    public void setMainAddress(Integer clientId, Integer addressId) throws SQLException {
        Connection conn = DatabaseConfig.getConnection();
        conn.setAutoCommit(false);
        try {
            definirEnderecoPrincipal(conn, clientId, addressId);
            conn.commit();
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            conn.close();
        }
    }

    private Endereco mapearEnderecoDoResultSet(ResultSet rs) throws SQLException {
        Endereco endereco = new Endereco();
        endereco.setId(rs.getInt("id"));
        endereco.setClientId(rs.getInt("client_id"));
        endereco.setStreet(rs.getString("street"));
        endereco.setNumber(rs.getString("number"));
        endereco.setComplement(rs.getString("complement"));
        endereco.setNeighborhood(rs.getString("neighborhood"));
        endereco.setCity(rs.getString("city"));
        endereco.setState(rs.getString("state"));
        endereco.setZipCode(rs.getString("zip_code"));
        endereco.setCountry(rs.getString("country"));
        endereco.setIsMain(rs.getBoolean("is_main"));
        endereco.setAddressType(TipoEndereco.fromValue(rs.getString("address_type")));
        endereco.setReference(rs.getString("reference"));

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
}
