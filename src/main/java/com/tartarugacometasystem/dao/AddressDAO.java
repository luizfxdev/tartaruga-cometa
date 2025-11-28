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

import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.util.ConnectionFactory;

public class AddressDAO {

    /**
     * Cria um novo endereço no banco de dados.
     *
     * @param address O objeto Address a ser criado.
     * @return O objeto Address com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Address save(Address address) throws SQLException {
        String sql = "INSERT INTO addresses (client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_principal, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, address.getClientId());
            pstmt.setString(2, address.getStreet());
            pstmt.setString(3, address.getNumber());
            pstmt.setString(4, address.getComplement());
            pstmt.setString(5, address.getNeighborhood());
            pstmt.setString(6, address.getCity());
            pstmt.setString(7, address.getState());
            pstmt.setString(8, address.getZipCode());
            pstmt.setString(9, address.getCountry());
            pstmt.setBoolean(10, address.getIsPrincipal());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                address.setId(rs.getInt(1));
            }
            return address;
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt, rs)
            ConnectionFactory.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca um endereço pelo ID.
     *
     * @param id O ID do endereço.
     * @return Um Optional contendo o Address se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Address> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM addresses WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAddress(rs));
            }
            return Optional.empty();
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt, rs)
            ConnectionFactory.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca todos os endereços de um cliente específico.
     *
     * @param clientId O ID do cliente.
     * @return Uma lista de endereços do cliente.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> findByClientId(Integer clientId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses WHERE client_id = ? ORDER BY is_principal DESC, street ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt, rs)
            ConnectionFactory.close(conn, pstmt, rs);
        }
        return addresses;
    }

    /**
     * Busca todos os endereços.
     *
     * @return Uma lista de todos os endereços.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> getAll() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT * FROM addresses ORDER BY client_id, is_principal DESC, street ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt, rs)
            ConnectionFactory.close(conn, pstmt, rs);
        }
        return addresses;
    }

    /**
     * Atualiza um endereço existente no banco de dados.
     *
     * @param address O objeto Address a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Address address) throws SQLException {
        String sql = "UPDATE addresses SET client_id = ?, street = ?, number = ?, complement = ?, neighborhood = ?, city = ?, state = ?, zip_code = ?, country = ?, is_principal = ?, updated_at = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, address.getClientId());
            pstmt.setString(2, address.getStreet());
            pstmt.setString(3, address.getNumber());
            pstmt.setString(4, address.getComplement());
            pstmt.setString(5, address.getNeighborhood());
            pstmt.setString(6, address.getCity());
            pstmt.setString(7, address.getState());
            pstmt.setString(8, address.getZipCode());
            pstmt.setString(9, address.getCountry());
            pstmt.setBoolean(10, address.getIsPrincipal());
            pstmt.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(12, address.getId());
            pstmt.executeUpdate();
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt)
            ConnectionFactory.close(conn, pstmt);
        }
    }

    /**
     * Deleta um endereço pelo ID.
     *
     * @param id O ID do endereço a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM addresses WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmt)
            ConnectionFactory.close(conn, pstmt);
        }
    }

    /**
     * Define um endereço como principal para um cliente, desmarcando outros.
     *
     * @param clientId O ID do cliente.
     * @param addressId O ID do endereço a ser definido como principal.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void setPrincipalAddress(Integer clientId, Integer addressId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtUpdateOthers = null;
        PreparedStatement pstmtUpdatePrincipal = null;
        try {
            conn = ConnectionFactory.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Desmarcar todos os outros endereços como principais para este cliente
            String sqlUpdateOthers = "UPDATE addresses SET is_principal = FALSE, updated_at = ? WHERE client_id = ? AND id != ?";
            pstmtUpdateOthers = conn.prepareStatement(sqlUpdateOthers);
            pstmtUpdateOthers.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmtUpdateOthers.setInt(2, clientId);
            pstmtUpdateOthers.setInt(3, addressId);
            pstmtUpdateOthers.executeUpdate();

            // 2. Marcar o endereço especificado como principal
            String sqlUpdatePrincipal = "UPDATE addresses SET is_principal = TRUE, updated_at = ? WHERE id = ? AND client_id = ?";
            pstmtUpdatePrincipal = conn.prepareStatement(sqlUpdatePrincipal);
            pstmtUpdatePrincipal.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmtUpdatePrincipal.setInt(2, addressId);
            pstmtUpdatePrincipal.setInt(3, clientId);
            pstmtUpdatePrincipal.executeUpdate();

            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Reverte em caso de erro
            }
            throw e;
        } finally {
            // Correção aqui: Usar ConnectionFactory.close(conn, pstmtUpdateOthers) e ConnectionFactory.close(pstmtUpdatePrincipal)
            ConnectionFactory.close(pstmtUpdateOthers);
            ConnectionFactory.close(pstmtUpdatePrincipal);
            ConnectionFactory.close(conn); // Fecha a conexão por último
        }
    }

    /**
     * Mapeia um ResultSet para um objeto Address.
     *
     * @param rs O ResultSet.
     * @return Um objeto Address.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setClientId(rs.getInt("client_id"));
        address.setStreet(rs.getString("street"));
        address.setNumber(rs.getString("number"));
        address.setComplement(rs.getString("complement"));
        address.setNeighborhood(rs.getString("neighborhood"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZipCode(rs.getString("zip_code"));
        address.setCountry(rs.getString("country"));
        address.setIsPrincipal(rs.getBoolean("is_principal"));
        address.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        address.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return address;
    }
}
