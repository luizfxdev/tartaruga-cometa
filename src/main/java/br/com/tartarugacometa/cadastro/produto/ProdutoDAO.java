package br.com.tartarugacometa.cadastro.produto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAO {

    public void inserir(Connection conn, Produto produto) throws SQLException {
        String sql = "INSERT INTO produto (nome, descricao, preco, peso_kg, volume_m3, valor_declarado, categoria, ativo, estoque) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, produto.getName());
            pstmt.setString(2, produto.getDescription());
            pstmt.setBigDecimal(3, produto.getPrice());
            pstmt.setBigDecimal(4, produto.getWeightKg());
            pstmt.setBigDecimal(5, produto.getVolumeM3());
            pstmt.setBigDecimal(6, produto.getDeclaredValue());
            pstmt.setString(7, produto.getCategory());
            pstmt.setBoolean(8, produto.isActive());
            pstmt.setObject(9, produto.getStockQuantity());
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }
        }
    }

    public Optional<Produto> buscarPorId(Connection conn, Integer id) throws SQLException {
        String sql = "SELECT id, nome, descricao, preco, peso_kg, volume_m3, valor_declarado, categoria, ativo, estoque, created_at, updated_at FROM produto WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearProdutoDoResultSet(rs));
                }
                return Optional.empty();
            }
        }
    }

    public void atualizar(Connection conn, Produto produto) throws SQLException {
        String sql = "UPDATE produto SET nome = ?, descricao = ?, preco = ?, peso_kg = ?, volume_m3 = ?, valor_declarado = ?, categoria = ?, ativo = ?, estoque = ? WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, produto.getName());
            pstmt.setString(2, produto.getDescription());
            pstmt.setBigDecimal(3, produto.getPrice());
            pstmt.setBigDecimal(4, produto.getWeightKg());
            pstmt.setBigDecimal(5, produto.getVolumeM3());
            pstmt.setBigDecimal(6, produto.getDeclaredValue());
            pstmt.setString(7, produto.getCategory());
            pstmt.setBoolean(8, produto.isActive());
            pstmt.setObject(9, produto.getStockQuantity());
            pstmt.setInt(10, produto.getId());
            pstmt.executeUpdate();
        }
    }

    public void excluir(Connection conn, Integer id) throws SQLException {
        String sql = "DELETE FROM produto WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public List<Produto> buscarTodos(Connection conn) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, peso_kg, volume_m3, valor_declarado, categoria, ativo, estoque, created_at, updated_at FROM produto";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                produtos.add(mapearProdutoDoResultSet(rs));
            }
        }
        return produtos;
    }

    public List<Produto> pesquisarPorNome(Connection conn, String nome) throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT id, nome, descricao, preco, peso_kg, volume_m3, valor_declarado, categoria, ativo, estoque, created_at, updated_at FROM produto WHERE nome ILIKE ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nome + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    produtos.add(mapearProdutoDoResultSet(rs));
                }
            }
        }
        return produtos;
    }

    private Produto mapearProdutoDoResultSet(ResultSet rs) throws SQLException {
        Produto produto = new Produto();
        produto.setId(rs.getInt("id"));
        produto.setName(rs.getString("nome"));
        produto.setDescription(rs.getString("descricao"));
        produto.setWeightKg(rs.getBigDecimal("peso_kg"));
        produto.setVolumeM3(rs.getBigDecimal("volume_m3"));
        produto.setDeclaredValue(rs.getBigDecimal("valor_declarado"));
        produto.setCategory(rs.getString("categoria"));
        produto.setActive(rs.getBoolean("ativo"));
        produto.setPrice(rs.getBigDecimal("preco"));
        Object estoque = rs.getObject("estoque");
        produto.setStockQuantity(estoque != null ? ((Number) estoque).intValue() : null);

        Timestamp criadoEm = rs.getTimestamp("created_at");
        if (criadoEm != null) {
            produto.setCreatedAt(criadoEm.toLocalDateTime());
        }
        Timestamp atualizadoEm = rs.getTimestamp("updated_at");
        if (atualizadoEm != null) {
            produto.setUpdatedAt(atualizadoEm.toLocalDateTime());
        }
        return produto;
    }

    public boolean contemEntregasVinculadas(Connection conn, Integer produtoId) throws SQLException {
        String sql = "SELECT 1 FROM entrega_produto WHERE id_produto = ? LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, produtoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}
