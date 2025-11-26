package com.tartarugacometasystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.config.DatabaseConfig;
import com.tartarugacometasystem.model.Address;
import com.tartarugacometasystem.model.AddressType;

public class AddressDAO {

    public void save(Address address) throws SQLException {
        String sql = "INSERT INTO endereco (id_cliente, tipo_endereco, logradouro, numero, " +
                     "complemento, bairro, cidade, estado, cep, referencia, is_principal) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, address.getClientId());
            stmt.setString(2, address.getAddressType().getValue());
            stmt.setString(3, address.getStreet());
            stmt.setString(4, address.getNumber());
            stmt.setString(5, address.getComplement());
            stmt.setString(6, address.getNeighborhood());
            stmt.setString(7, address.getCity());
            stmt.setString(8, address.getState());
            stmt.setString(9, address.getZipCode());
            stmt.setString(10, address.getReference());

            Boolean isPrincipal = address.getIsPrincipal();
            stmt.setBoolean(11, isPrincipal != null ? isPrincipal : false);

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    address.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Address address) throws SQLException {
        String sql = "UPDATE endereco SET tipo_endereco = ?, logradouro = ?, numero = ?, " +
                     "complemento = ?, bairro = ?, cidade = ?, estado = ?, cep = ?, " +
                     "referencia = ?, is_principal = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, address.getAddressType().getValue());
            stmt.setString(2, address.getStreet());
            stmt.setString(3, address.getNumber());
            stmt.setString(4, address.getComplement());
            stmt.setString(5, address.getNeighborhood());
            stmt.setString(6, address.getCity());
            stmt.setString(7, address.getState());
            stmt.setString(8, address.getZipCode());
            stmt.setString(9, address.getReference());

            Boolean isPrincipal = address.getIsPrincipal();
            stmt.setBoolean(10, isPrincipal != null ? isPrincipal : false);
            stmt.setInt(11, address.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM endereco WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<Address> findById(Integer id) throws SQLException {
        String sql = "SELECT id, id_cliente, tipo_endereco, logradouro, numero, complemento, " +
                     "bairro, cidade, estado, cep, referencia, is_principal, created_at " +
                     "FROM endereco WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAddress(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Address> findByClientId(Integer clientId) throws SQLException {
        String sql = "SELECT id, id_cliente, tipo_endereco, logradouro, numero, complemento, " +
                     "bairro, cidade, estado, cep, referencia, is_principal, created_at " +
                     "FROM endereco WHERE id_cliente = ? ORDER BY is_principal DESC, created_at DESC";
        List<Address> addresses = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapResultSetToAddress(rs));
                }
            }
        }
        return addresses;
    }

    public List<Address> findByClientIdAndType(Integer clientId, AddressType addressType) throws SQLException {
        String sql = "SELECT id, id_cliente, tipo_endereco, logradouro, numero, complemento, " +
                     "bairro, cidade, estado, cep, referencia, is_principal, created_at " +
                     "FROM endereco WHERE id_cliente = ? AND tipo_endereco = ? ORDER BY is_principal DESC";
        List<Address> addresses = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);
            stmt.setString(2, addressType.getValue());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapResultSetToAddress(rs));
                }
            }
        }
        return addresses;
    }

    public Optional<Address> findPrincipalByClientId(Integer clientId) throws SQLException {
        String sql = "SELECT id, id_cliente, tipo_endereco, logradouro, numero, complemento, " +
                     "bairro, cidade, estado, cep, referencia, is_principal, created_at " +
                     "FROM endereco WHERE id_cliente = ? AND is_principal = true LIMIT 1";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, clientId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToAddress(rs));
                }
            }
        }
        return Optional.empty();
    }

    private Address mapResultSetToAddress(ResultSet rs) throws SQLException {
        Address address = new Address();
        address.setId(rs.getInt("id"));
        address.setClientId(rs.getInt("id_cliente"));
        address.setAddressType(AddressType.fromValue(rs.getString("tipo_endereco")));
        address.setStreet(rs.getString("logradouro"));
        address.setNumber(rs.getString("numero"));
        address.setComplement(rs.getString("complemento"));
        address.setNeighborhood(rs.getString("bairro"));
        address.setCity(rs.getString("cidade"));
        address.setState(rs.getString("estado"));
        address.setZipCode(rs.getString("cep"));
        address.setReference(rs.getString("referencia"));
        address.setIsPrincipal(rs.getBoolean("is_principal"));
        address.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        return address;
    }
}
