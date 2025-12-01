package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;

public class AddressDAO {

    /**
     * Cria um novo endereço no banco de dados.
     *
     * @param address O objeto Address a ser criado.
     * @return O objeto Address com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Address save(Address address) throws SQLException {
        String sql = "INSERT INTO address (client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
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
            pstmt.setBoolean(10, address.getIsMain());
            pstmt.setString(11, address.getAddressType().name()); // CORRIGIDO: Usar .name() para o ENUM
            pstmt.setString(12, address.getReference());
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                address.setId(rs.getInt(1));
            }
            return address;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
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
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAddress(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
    }

    /**
     * Atualiza um endereço existente no banco de dados.
     *
     * @param address O objeto Address a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Address address) throws SQLException {
        String sql = "UPDATE address SET client_id = ?, street = ?, number = ?, complement = ?, neighborhood = ?, city = ?, state = ?, zip_code = ?, country = ?, is_main = ?, address_type = ?, reference = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
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
            pstmt.setBoolean(10, address.getIsMain());
            pstmt.setString(11, address.getAddressType().name()); // CORRIGIDO: Usar .name() para o ENUM
            pstmt.setString(12, address.getReference());
            pstmt.setInt(13, address.getId());
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
        }
    }

    /**
     * Deleta um endereço pelo ID.
     *
     * @param id O ID do endereço a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM address WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt);
        }
    }

    /**
     * Busca todos os endereços.
     *
     * @return Uma lista de todos os endereços.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> getAll() throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return addresses;
    }

    /**
     * Busca endereços por ID de cliente.
     *
     * @param clientId O ID do cliente.
     * @return Uma lista de endereços associados ao cliente.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Address> findByClientId(Integer clientId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        String sql = "SELECT id, client_id, street, number, complement, neighborhood, city, state, zip_code, country, is_main, address_type, reference, created_at, updated_at FROM address WHERE client_id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs);
        }
        return addresses;
    }

    /**
     * Define um endereço como principal para um cliente, garantindo que apenas um seja principal.
     *
     * @param clientId O ID do cliente.
     * @param addressId O ID do endereço a ser definido como principal.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void setMainAddress(Integer clientId, Integer addressId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtUpdateAll = null;
        PreparedStatement pstmtUpdateOne = null;
        try {
            conn = DatabaseConfig.getConnection();
            conn.setAutoCommit(false); // Inicia transação

            // 1. Define todos os endereços do cliente como não principais
            String sqlUpdateAll = "UPDATE address SET is_main = FALSE WHERE client_id = ?";
            pstmtUpdateAll = conn.prepareStatement(sqlUpdateAll);
            pstmtUpdateAll.setInt(1, clientId);
            pstmtUpdateAll.executeUpdate();

            // 2. Define o endereço específico como principal
            String sqlUpdateOne = "UPDATE address SET is_main = TRUE WHERE id = ? AND client_id = ?";
            pstmtUpdateOne = conn.prepareStatement(sqlUpdateOne);
            pstmtUpdateOne.setInt(1, addressId);
            pstmtUpdateOne.setInt(2, clientId);
            int rowsAffected = pstmtUpdateOne.executeUpdate();

            if (rowsAffected == 0) {
                conn.rollback(); // Reverte se o endereço não foi encontrado para o cliente
                throw new SQLException("Endereço com ID " + addressId + " não encontrado para o cliente " + clientId + " ou não pertence a ele.");
            }

            conn.commit(); // Confirma transação
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Reverte em caso de erro
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Restaura o auto-commit
            }
            DatabaseConfig.close(null, pstmtUpdateAll); // conn é fechado no final
            DatabaseConfig.close(conn, pstmtUpdateOne);
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
        address.setIsMain(rs.getBoolean("is_main"));
        address.setAddressType(AddressType.fromValue(rs.getString("address_type"))); // Converte String para enum
        address.setReference(rs.getString("reference"));

        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        address.setCreatedAt(createdAtTimestamp != null ? createdAtTimestamp.toLocalDateTime() : null);
        Timestamp updatedAtTimestamp = rs.getTimestamp("updated_at");
        address.setUpdatedAt(updatedAtTimestamp != null ? updatedAtTimestamp.toLocalDateTime() : null);
        return address;
    }
}
