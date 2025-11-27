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
import com.tartarugacometasystem.model.Client;
import com.tartarugacometasystem.model.PersonType;

public class ClientDAO {

    public void save(Client client) throws SQLException {
        String sql = "INSERT INTO cliente (tipo_pessoa, documento, nome, email, telefone) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, client.getPersonType().getValue());
            stmt.setString(2, client.getDocument());
            stmt.setString(3, client.getName());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getPhone());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }
        }
    }

    public void update(Client client) throws SQLException {
        String sql = "UPDATE cliente SET tipo_pessoa = ?, documento = ?, nome = ?, " +
                     "email = ?, telefone = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, client.getPersonType().getValue());
            stmt.setString(2, client.getDocument());
            stmt.setString(3, client.getName());
            stmt.setString(4, client.getEmail());
            stmt.setString(5, client.getPhone());
            stmt.setInt(6, client.getId());

            stmt.executeUpdate();
        }
    }

    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM cliente WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    public Optional<Client> findById(Integer id) throws SQLException {
        String sql = "SELECT id, tipo_pessoa, documento, nome, email, telefone, " +
                     "created_at, updated_at FROM cliente WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Client> findByDocument(String document) throws SQLException {
        String sql = "SELECT id, tipo_pessoa, documento, nome, email, telefone, " +
                     "created_at, updated_at FROM cliente WHERE documento = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, document);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToClient(rs));
                }
            }
        }
        return Optional.empty();
    }

    public List<Client> findAll() throws SQLException {
        String sql = "SELECT id, tipo_pessoa, documento, nome, email, telefone, " +
                     "created_at, updated_at FROM cliente ORDER BY nome";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                clients.add(mapResultSetToClient(rs));
            }
        }
        return clients;
    }

    public List<Client> findByPersonType(PersonType personType) throws SQLException {
        String sql = "SELECT id, tipo_pessoa, documento, nome, email, telefone, " +
                     "created_at, updated_at FROM cliente WHERE tipo_pessoa = ? ORDER BY nome";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, personType.getValue());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }
        }
        return clients;
    }

    public List<Client> findByName(String name) throws SQLException {
        String sql = "SELECT id, tipo_pessoa, documento, nome, email, telefone, " +
                     "created_at, updated_at FROM cliente WHERE nome ILIKE ? ORDER BY nome";
        List<Client> clients = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    clients.add(mapResultSetToClient(rs));
                }
            }
        }
        return clients;
    }

    private Client mapResultSetToClient(ResultSet rs) throws SQLException {
        Client client = new Client();
        client.setId(rs.getInt("id"));
        client.setPersonType(PersonType.fromValue(rs.getString("tipo_pessoa")));
        client.setDocument(rs.getString("documento"));
        client.setName(rs.getString("nome"));
        client.setEmail(rs.getString("email"));
        client.setPhone(rs.getString("telefone"));
        client.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            client.setUpdatedAt(updatedAt.toLocalDateTime());
        }

        return client;
    }
}
