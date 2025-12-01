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
import com.tartarugacometasystem.model.Address;

public class AddressDAO {

    /**
     * Cria um novo endereço no banco de dados.
     *
     * @param address O objeto Address a ser criado.
     * @return O objeto Address com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Address save(Address address) throws SQLException {
        String sql = "INSERT INTO address (client_id, address_type, street, number, complement, neighborhood, city, state, zip_code, reference, is_main, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, address.getClientId());
            pstmt.setString(2, address.getAddressType()); // Nova coluna
            pstmt.setString(3, address.getStreet());
            pstmt.setString(4, address.getNumber());
            pstmt.setString(5, address.getComplement());
            pstmt.setString(6, address.getNeighborhood());
            pstmt.setString(7, address.getCity());
            pstmt.setString(8, address.getState());
            pstmt.setString(9, address.getZipCode());
            pstmt.setString(10, address.getReference()); // Nova coluna
            pstmt.setBoolean(11, address.getIsMain()); // Nome da coluna alterado
            pstmt.setTimestamp(12, Timestamp.valueOf(LocalDateTime.now()));
            // updated_at é tratado por trigger, não precisa ser definido aqui para INSERT

            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                address.setId(rs.getInt(1));
            }
            return address;
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
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
        String sql = "SELECT * FROM address WHERE id = ?"; // Nome da tabela alterado
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToAddress(rs));
            }
            return Optional.empty();
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
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
        String sql = "SELECT * FROM address WHERE client_id = ? ORDER BY is_main DESC, street ASC"; // Nome da tabela e coluna alterados
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, clientId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
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
        String sql = "SELECT * FROM address ORDER BY client_id, is_main DESC, street ASC"; // Nome da tabela e coluna alterados
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                addresses.add(mapResultSetToAddress(rs));
            }
        } finally {
            DatabaseConfig.close(conn, pstmt, rs); // Chamada alterada
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
        String sql = "UPDATE address SET client_id = ?, address_type = ?, street = ?, number = ?, complement = ?, neighborhood = ?, city = ?, state = ?, zip_code = ?, reference = ?, is_main = ? WHERE id = ?"; // Nome da tabela e colunas alterados
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, address.getClientId());
            pstmt.setString(2, address.getAddressType());
            pstmt.setString(3, address.getStreet());
            pstmt.setString(4, address.getNumber());
            pstmt.setString(5, address.getComplement());
            pstmt.setString(6, address.getNeighborhood());
            pstmt.setString(7, address.getCity());
            pstmt.setString(8, address.getState());
            pstmt.setString(9, address.getZipCode());
            pstmt.setString(10, address.getReference());
            pstmt.setBoolean(11, address.getIsMain());
            // updated_at é tratado por trigger, não precisa ser definido aqui explicitamente
            pstmt.setInt(12, address.getId());
            pstmt.executeUpdate();
        } finally {
            DatabaseConfig.close(conn, pstmt); // Chamada alterada
        }
    }

    /**
     * Deleta um endereço pelo ID.
     *
     * @param id O ID do endereço a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM address WHERE id = ?"; // Nome da tabela alterado
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
     * Define um endereço como principal para um cliente, desmarcando outros.
     *
     * @param clientId O ID do cliente.
     * @param addressId O ID do endereço a ser definido como principal.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void setMainAddress(Integer clientId, Integer addressId) throws SQLException { // Nome do método alterado
        Connection conn = null;
        PreparedStatement pstmtUpdateOthers = null;
        PreparedStatement pstmtUpdateMain = null; // Nome da variável alterado
        try {
            conn = DatabaseConfig.getConnection(); // Chamada alterada
            conn.setAutoCommit(false); // Inicia transação

            // 1. Desmarcar todos os outros endereços como principais para este cliente
            String sqlUpdateOthers = "UPDATE address SET is_main = FALSE WHERE client_id = ? AND id != ?"; // Nome da tabela e colunas alterados
            pstmtUpdateOthers = conn.prepareStatement(sqlUpdateOthers);
            pstmtUpdateOthers.setInt(1, clientId);
            pstmtUpdateOthers.setInt(2, addressId);
            pstmtUpdateOthers.executeUpdate();

            // 2. Marcar o endereço especificado como principal
            String sqlUpdateMain = "UPDATE address SET is_main = TRUE WHERE id = ? AND client_id = ?"; // Nome da tabela e colunas alterados
            pstmtUpdateMain = conn.prepareStatement(sqlUpdateMain);
            pstmtUpdateMain.setInt(1, addressId);
            pstmtUpdateMain.setInt(2, clientId);
            pstmtUpdateMain.executeUpdate();

            conn.commit(); // Confirma a transação
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Reverte em caso de erro
            }
            throw e;
        } finally {
            DatabaseConfig.close(pstmtUpdateOthers); // Chamada alterada
            DatabaseConfig.close(pstmtUpdateMain); // Chamada alterada
            DatabaseConfig.close(conn); // Fecha a conexão por último
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
        address.setAddressType(rs.getString("address_type")); // Nova coluna
        address.setStreet(rs.getString("street"));
        address.setNumber(rs.getString("number"));
        address.setComplement(rs.getString("complement"));
        address.setNeighborhood(rs.getString("neighborhood"));
        address.setCity(rs.getString("city"));
        address.setState(rs.getString("state"));
        address.setZipCode(rs.getString("zip_code"));
        address.setReference(rs.getString("reference")); // Nova coluna
        address.setIsMain(rs.getBoolean("is_main")); // Nome da coluna alterado
        address.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        address.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return address;
    }
}
