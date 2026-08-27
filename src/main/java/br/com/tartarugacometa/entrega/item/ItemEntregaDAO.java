package br.com.tartarugacometa.entrega.item;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.config.DatabaseConfig;
import br.com.tartarugacometa.entrega.item.ItemEntrega;

public class ItemEntregaDAO {

    /**
     * Cria um novo produto de entrega no banco de dados.
     *
     * @param deliveryProduct O objeto ItemEntrega a ser criado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void save(ItemEntrega deliveryProduct) throws SQLException {
        String sql = "INSERT INTO delivery_product (delivery_id, product_id, quantity, " + // Nome da tabela e colunas alterados
                     "unit_weight_kg, unit_volume_m3, unit_value, subtotal, observations) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, deliveryProduct.getDeliveryId());
            stmt.setInt(2, deliveryProduct.getProductId());
            stmt.setInt(3, deliveryProduct.getQuantity());
            stmt.setBigDecimal(4, deliveryProduct.getUnitWeightKg());
            stmt.setBigDecimal(5, deliveryProduct.getUnitVolumeM3());
            stmt.setBigDecimal(6, deliveryProduct.getUnitValue());
            stmt.setBigDecimal(7, deliveryProduct.getSubtotal());
            stmt.setString(8, deliveryProduct.getObservations());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    deliveryProduct.setId(rs.getInt(1));
                }
            }
        }
    }

    /**
     * Atualiza um produto de entrega existente no banco de dados.
     *
     * @param deliveryProduct O objeto ItemEntrega a ser atualizado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void update(ItemEntrega deliveryProduct) throws SQLException {
        String sql = "UPDATE delivery_product SET quantity = ?, unit_weight_kg = ?, " + // Nome da tabela e colunas alterados
                     "unit_volume_m3 = ?, unit_value = ?, subtotal = ?, observations = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryProduct.getQuantity());
            stmt.setBigDecimal(2, deliveryProduct.getUnitWeightKg());
            stmt.setBigDecimal(3, deliveryProduct.getUnitVolumeM3());
            stmt.setBigDecimal(4, deliveryProduct.getUnitValue());
            stmt.setBigDecimal(5, deliveryProduct.getSubtotal());
            stmt.setString(6, deliveryProduct.getObservations());
            stmt.setInt(7, deliveryProduct.getId());

            stmt.executeUpdate();
        }
    }

    /**
     * Deleta um produto de entrega pelo ID.
     *
     * @param id O ID do produto de entrega a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void delete(Integer id) throws SQLException {
        String sql = "DELETE FROM delivery_product WHERE id = ?"; // Nome da tabela alterado

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    /**
     * Deleta todos os produtos de entrega associados a uma entrega específica.
     *
     * @param deliveryId O ID da entrega.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "DELETE FROM delivery_product WHERE delivery_id = ?"; // Nome da tabela e colunas alterados

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            stmt.executeUpdate();
        }
    }

    /**
     * Busca um produto de entrega pelo ID.
     *
     * @param id O ID do produto de entrega.
     * @return Um Optional contendo o ItemEntrega se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<ItemEntrega> findById(Integer id) throws SQLException {
        String sql = "SELECT id, delivery_id, product_id, quantity, unit_weight_kg, " + // Nome da tabela e colunas alterados
                     "unit_volume_m3, unit_value, subtotal, observations " +
                     "FROM delivery_product WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Busca produtos de entrega por ID da entrega.
     *
     * @param deliveryId O ID da entrega.
     * @return Uma lista de produtos de entrega associados à entrega.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<ItemEntrega> findByDeliveryId(Integer deliveryId) throws SQLException {
        String sql = "SELECT id, delivery_id, product_id, quantity, unit_weight_kg, " + // Nome da tabela e colunas alterados
                     "unit_volume_m3, unit_value, subtotal, observations " +
                     "FROM delivery_product WHERE delivery_id = ?";
        List<ItemEntrega> products = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    products.add(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return products;
    }

    /**
     * Busca um produto de entrega por ID da entrega e ID do produto.
     *
     * @param deliveryId O ID da entrega.
     * @param productId O ID do produto.
     * @return Um Optional contendo o ItemEntrega se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<ItemEntrega> findByDeliveryAndProduct(Integer deliveryId, Integer productId) throws SQLException {
        String sql = "SELECT id, delivery_id, product_id, quantity, unit_weight_kg, " + // Nome da tabela e colunas alterados
                     "unit_volume_m3, unit_value, subtotal, observations " +
                     "FROM delivery_product WHERE delivery_id = ? AND product_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, deliveryId);
            stmt.setInt(2, productId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapResultSetToDeliveryProduct(rs));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Mapeia um ResultSet para um objeto ItemEntrega.
     *
     * @param rs O ResultSet.
     * @return Um objeto ItemEntrega.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    private ItemEntrega mapResultSetToDeliveryProduct(ResultSet rs) throws SQLException {
        ItemEntrega deliveryProduct = new ItemEntrega();
        deliveryProduct.setId(rs.getInt("id"));
        deliveryProduct.setDeliveryId(rs.getInt("delivery_id")); // Nome da coluna alterado
        deliveryProduct.setProductId(rs.getInt("product_id")); // Nome da coluna alterado
        deliveryProduct.setQuantity(rs.getInt("quantity"));
        deliveryProduct.setUnitWeightKg(rs.getBigDecimal("unit_weight_kg"));
        deliveryProduct.setUnitVolumeM3(rs.getBigDecimal("unit_volume_m3"));
        deliveryProduct.setUnitValue(rs.getBigDecimal("unit_value"));
        deliveryProduct.setSubtotal(rs.getBigDecimal("subtotal"));
        deliveryProduct.setObservations(rs.getString("observations"));

        return deliveryProduct;
    }
}
