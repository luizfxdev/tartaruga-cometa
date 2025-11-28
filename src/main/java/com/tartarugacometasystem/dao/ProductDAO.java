package com.tartarugacometasystem.dao;

import com.tartarugacometasystem.model.Product;
import com.tartarugacometasystem.util.ConnectionFactory; // Importar ConnectionFactory

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime; // Importar LocalDateTime
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO {

    /**
     * Cria um novo produto no banco de dados.
     *
     * @param product O objeto Product a ser criado.
     * @return O objeto Product com o ID gerado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Product save(Product product) throws SQLException {
        String sql = "INSERT INTO products (name, description, price, weight_kg, volume_m3, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setBigDecimal(4, product.getWeightKg());
            pstmt.setBigDecimal(5, product.getVolumeM3());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();

            rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
            return product;
        } finally {
            ConnectionFactory.close(conn, pstmt, rs);
        }
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id O ID do produto.
     * @return Um Optional contendo o Product se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Product> findById(Integer id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return Optional.of(mapResultSetToProduct(rs));
            }
            return Optional.empty();
        } finally {
            ConnectionFactory.close(conn, pstmt, rs);
        }
    }

    /**
     * Atualiza um produto existente no banco de dados.
     *
     * @param product O objeto Product a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(Product product) throws SQLException {
        String sql = "UPDATE products SET name = ?, description = ?, price = ?, weight_kg = ?, volume_m3 = ?, updated_at = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setBigDecimal(3, product.getPrice());
            pstmt.setBigDecimal(4, product.getWeightKg());
            pstmt.setBigDecimal(5, product.getVolumeM3());
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(7, product.getId());
            pstmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, pstmt);
        }
    }

    /**
     * Deleta um produto pelo ID.
     *
     * @param id O ID do produto a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM products WHERE id = ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            ConnectionFactory.close(conn, pstmt);
        }
    }

    /**
     * Busca todos os produtos.
     *
     * @return Uma lista de todos os produtos.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } finally {
            ConnectionFactory.close(conn, pstmt, rs);
        }
        return products;
    }

    /**
     * Busca produtos pelo nome.
     *
     * @param query O termo de busca.
     * @return Uma lista de produtos que correspondem ao termo de busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Product> searchByName(String query) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE name LIKE ? OR description LIKE ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = ConnectionFactory.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + query + "%");
            pstmt.setString(2, "%" + query + "%");
            rs = pstmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } finally {
            ConnectionFactory.close(conn, pstmt, rs);
        }
        return products;
    }

    /**
     * Mapeia um ResultSet para um objeto Product.
     *
     * @param rs O ResultSet.
     * @return Um objeto Product.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setWeightKg(rs.getBigDecimal("weight_kg"));
        product.setVolumeM3(rs.getBigDecimal("volume_m3"));
        product.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        product.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return product;
    }
}
