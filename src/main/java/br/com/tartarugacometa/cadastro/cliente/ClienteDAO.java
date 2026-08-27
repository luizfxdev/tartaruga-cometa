package br.com.tartarugacometa.cadastro.cliente;

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
import br.com.tartarugacometa.enums.TipoPessoa;

public class ClienteDAO {

    public void inserir(Connection conn, Cliente cliente) throws SQLException {
        String sql = "INSERT INTO client (name, document, email, phone, person_type) VALUES (?, ?, ?, ?, ?::person_type_enum)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, cliente.getName());
            pstmt.setString(2, cliente.getDocument());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getPhone());
            pstmt.setString(5, cliente.getPersonType().name());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Cliente> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, name, document, email, phone, person_type, created_at, updated_at FROM client WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearClienteDoResultSet(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void atualizar(Connection conn, Cliente cliente) throws SQLException {
        String sql = "UPDATE client SET name = ?, document = ?, email = ?, phone = ?, person_type = ?::person_type_enum WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getName());
            pstmt.setString(2, cliente.getDocument());
            pstmt.setString(3, cliente.getEmail());
            pstmt.setString(4, cliente.getPhone());
            pstmt.setString(5, cliente.getPersonType().name());
            pstmt.setInt(6, cliente.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Cliente> buscarTodos(Connection conn) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, name, document, email, phone, person_type, created_at, updated_at FROM client";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                clientes.add(mapearClienteDoResultSet(rs));
            }
        }
        return clientes;
    }

    public List<Cliente> pesquisar(Connection conn, String termo) throws SQLException {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT id, name, document, email, phone, person_type, created_at, updated_at FROM client " +
                     "WHERE name ILIKE ? OR document ILIKE ? OR email ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String padrao = "%" + termo + "%";
            pstmt.setString(1, padrao);
            pstmt.setString(2, padrao);
            pstmt.setString(3, padrao);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    clientes.add(mapearClienteDoResultSet(rs));
                }
            }
        }
        return clientes;
    }

    public boolean existeDocumento(Connection conn, String documento, Integer idExcluido) throws SQLException {
        String sql = "SELECT 1 FROM client WHERE document = ?";
        if (idExcluido != null) {
            sql += " AND id != ?";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, documento);
            if (idExcluido != null) {
                pstmt.setInt(2, idExcluido);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    public Cliente save(Cliente cliente) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            inserir(conn, cliente);
            return cliente;
        }
    }

    public Optional<Cliente> findById(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarPorId(conn, id);
        }
    }

    public void update(Cliente cliente) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            atualizar(conn, cliente);
        }
    }

    public void delete(Integer id) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            excluir(conn, id);
        }
    }

    public List<Cliente> getAll() throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return buscarTodos(conn);
        }
    }

    public List<Cliente> search(String termo) throws SQLException {
        try (Connection conn = DatabaseConfig.getConnection()) {
            return pesquisar(conn, termo);
        }
    }

    private Cliente mapearClienteDoResultSet(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(rs.getInt("id"));
        cliente.setName(rs.getString("name"));
        cliente.setDocument(rs.getString("document"));
        cliente.setEmail(rs.getString("email"));
        cliente.setPhone(rs.getString("phone"));
        cliente.setPersonType(TipoPessoa.fromValue(rs.getString("person_type")));

        Timestamp criadoEm = rs.getTimestamp("created_at");
        if (criadoEm != null) {
            cliente.setCreatedAt(criadoEm.toLocalDateTime());
        }
        Timestamp atualizadoEm = rs.getTimestamp("updated_at");
        if (atualizadoEm != null) {
            cliente.setUpdatedAt(atualizadoEm.toLocalDateTime());
        }
        return cliente;
    }
}
