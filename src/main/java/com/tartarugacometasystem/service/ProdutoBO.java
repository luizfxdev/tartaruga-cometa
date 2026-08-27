// ProdutoBO.java
package com.tartarugacometasystem.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import com.tartarugacometasystem.dao.ProdutoDAO;
import com.tartarugacometasystem.model.Produto;
// import com.tartarugacometasystem.util.DateFormatter; // Não é mais necessário aqui

public class ProdutoBO {
    private ProdutoDAO productDAO;

    public ProdutoBO() {
        this.productDAO = new ProdutoDAO();
    }

    /**
     * Cria um novo produto.
     *
     * @param product O objeto Produto a ser criado.
     * @return O objeto Produto criado com o ID.
     * @throws SQLException          Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o produto for inválido.
     */
    public Produto createProduct(Produto product) throws SQLException {
        validateProduct(product);
        return productDAO.save(product);
    }

    /**
     * Busca um produto pelo ID.
     *
     * @param id O ID do produto.
     * @return Um Optional contendo o Produto se encontrado, ou Optional.empty().
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public Optional<Produto> getProductById(Integer id) throws SQLException {
        Optional<Produto> product = productDAO.findById(id);
        // product.ifPresent(this::enrichProduct); // Removido: não precisamos mais enriquecer com strings formatadas
        return product;
    }

    /**
     * Atualiza um produto existente.
     *
     * @param product O objeto Produto a ser atualizado.
     * @throws SQLException          Se ocorrer um erro de SQL.
     * @throws IllegalArgumentException Se o produto for inválido ou não existir.
     */
    public void updateProduct(Produto product) throws SQLException {
        if (product.getId() == null) {
            throw new IllegalArgumentException("ID do produto é obrigatório para atualização.");
        }
        validateProduct(product);
        Optional<Produto> existingProduct = productDAO.findById(product.getId());
        if (existingProduct.isEmpty()) {
            throw new IllegalArgumentException("Produto com ID " + product.getId() + " não encontrado.");
        }
        productDAO.update(product);
    }

    /**
     * Deleta um produto pelo ID.
     *
     * @param id O ID do produto a ser deletado.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public void deleteProduct(Integer id) throws SQLException {
        productDAO.delete(id);
    }

    /**
     * Busca todos os produtos.
     *
     * @return Uma lista de todos os produtos.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Produto> getAllProducts() throws SQLException {
        List<Produto> products = productDAO.getAll();
        // products.forEach(this::enrichProduct); // Removido: não precisamos mais enriquecer com strings formatadas
        return products;
    }

    /**
     * Busca produtos por nome (ou parte do nome).
     *
     * @param searchTerm O termo de busca.
     * @return Uma lista de produtos que correspondem à busca.
     * @throws SQLException Se ocorrer um erro de SQL.
     */
    public List<Produto> searchProductsByName(String searchTerm) throws SQLException {
        List<Produto> products = productDAO.searchByName(searchTerm);
        // products.forEach(this::enrichProduct); // Removido: não precisamos mais enriquecer com strings formatadas
        return products;
    }

    /**
     * Valida os campos de um produto.
     *
     * @param product O objeto Produto a ser validado.
     * @throws IllegalArgumentException Se algum campo for inválido.
     */
    private void validateProduct(Produto product) {
        if (product.getName() == null || product.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        }
        if (product.getPrice() == null || product.getPrice().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preço deve ser um número positivo.");
        }
        if (product.getStockQuantity() == null || product.getStockQuantity() < 0) {
            throw new IllegalArgumentException("Quantidade em estoque deve ser um número não negativo.");
        }
        if (product.getWeightKg() == null || product.getWeightKg().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Peso em Kg deve ser um número positivo.");
        }
        if (product.getVolumeM3() == null || product.getVolumeM3().compareTo(java.math.BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Volume em M3 deve ser um número positivo.");
        }
    }

    /**
     * Enriquecer um objeto Produto com dados formatados.
     *
     * @param product O objeto Produto a ser enriquecido.
     */
    // private void enrichProduct(Produto product) { // Método removido
    //     if (product == null) return;
    //
    //     if (product.getCreatedAt() != null) {
    //         product.setFormattedCreatedAt(DateFormatter.formatLocalDateTime(product.getCreatedAt()));
    //     }
    //     if (product.getUpdatedAt() != null) {
    //         product.setFormattedUpdatedAt(DateFormatter.formatLocalDateTime(product.getUpdatedAt()));
    //     }
    // }
}
