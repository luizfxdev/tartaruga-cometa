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
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig; // Import alterado
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;

public class ClientDAO {

    /**
     * Cria um novo cliente no banco de dados.
     *
     * @param client O objeto Client a ser criado.
     * @return O objeto Client com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Client save(Client client) throws SQLException {
        String sql = "INSERT INTO client (name, document, email, phone, person_type, created_at) VALUES (?, ?, ?, ?, ?, ?)"; // Nome da tabela alterado e updated_at removido do insert
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getDocument());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone());
            pstmt.setString(5, client.getPersonType().name()); // Salva o nome do enum
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            // updated_at é tratado por trigger, não precisa ser definido aqui para INSERT
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                client.setId(rs.getInt(1));
            }
            return client;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
        }
    }

    /**
     * Busca um cliente pelo ID.
     *
     * @param id O ID do cliente.
     * @return Um Optional contendo o Client se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Client> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM client WHERE id = ?"; // Nome da tabela alterado
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToClient(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
        }
    }

    /**
     * Atualiza um cliente existente no banco de dados.
     *
     * @param client O objeto Client a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Client client) throws SQLException {
        String sql = "UPDATE client SET name = ?, document = ?, email = ?, phone = ?, person_type = ? WHERE id = ?"; // Nome da tabela alterado e updated_at removido do set
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, client.getName());
            pstmt.setString(2, client.getDocument());
            pstmt.setString(3, client.getEmail());
            pstmt.setString(4, client.getPhone());
            pstmt.setString(5, client.getPersonType().name()); // Salva o nome do enum
            // updated_at é tratado por trigger, não precisa ser definido aqui explicitamente
            pstmt.setInt(6, client.getId());
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt); // Chamada alterada
        }
    }

    /**
     * Deleta um cliente pelo ID.
     *
     * @param id O ID do cliente a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM client WHERE id = ?"; // Nome da tabela alterado
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt); // Chamada alterada
        }
    }

    /**
     * Busca todos os clientes.
     *
     * @return Uma lista de todos os clientes.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Client> getAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client"; // Nome da tabela alterado
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
        }
        return clients;
    }

    /**
     * Busca clientes pelo nome.
     *
     * @param query O termo de busca para o nome do cliente.
     * @return Uma lista de clientes que correspondem ao termo de busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Client> searchByName(String query) throws SQLException {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM client WHERE name LIKE ? OR document LIKE ? OR email LIKE ?"; // Nome da tabela alterado
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            pstmt.setString(3, "%" + query + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
        }
        return clients;
    }

    /**
     * Mapeia um ResultSet para um objeto Client.
     *
     * @param rs O ResultSet.
     * @return Um objeto Client.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setName(rs.getString("name"));
        client.setDocument(rs.getString("document"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("phone"));
        client.setPersonType(PersonType.fromValue(rs.getString("person_type"))); // Converte String para PersonType
        client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        client.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return client;
    }
}
