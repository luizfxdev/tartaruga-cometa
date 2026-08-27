package br.com.tartarugacometa.cadastro.produto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.util.Conexao;

public class ProdutoBO {
    private final ProdutoDAO produtoDAO;

    public ProdutoBO() {
        this.produtoDAO = new ProdutoDAO();
    }

    public ProdutoBO(ProdutoDAO produtoDAO) {
        this.produtoDAO = produtoDAO;
    }

    public void salvar(Produto produto) throws CadastroException {
        validar(produto);
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                if (produto.getId() == null) {
                    produtoDAO.inserir(conexao, produto);
                } else {
                    produtoDAO.atualizar(conexao, produto);
                }
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível salvar o produto.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public Optional<Produto> buscarPorId(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            return produtoDAO.buscarPorId(conexao, id);
        } catch (SQLException e) {
            throw new CadastroException("Falha ao buscar produto.", e);
        }
    }

    public void excluir(Integer id) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            conexao.setAutoCommit(false);
            try {
                produtoDAO.excluir(conexao, id);
                conexao.commit();
            } catch (SQLException e) {
                conexao.rollback();
                throw new CadastroException("Não foi possível excluir o produto.", e);
            }
        } catch (SQLException e) {
            throw new CadastroException("Falha de conexão com o banco.", e);
        }
    }

    public List<Produto> listarTodos() throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            return produtoDAO.buscarTodos(conexao);
        } catch (SQLException e) {
            throw new CadastroException("Falha ao listar produtos.", e);
        }
    }

    public List<Produto> pesquisarPorNome(String nome) throws CadastroException {
        try (Connection conexao = Conexao.abrir()) {
            return produtoDAO.pesquisarPorNome(conexao, nome);
        } catch (SQLException e) {
            throw new CadastroException("Falha na pesquisa de produtos.", e);
        }
    }

    public Produto createProduct(Produto product) throws SQLException {
        try {
            salvar(product);
            return product;
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public Optional<Produto> getProductById(Integer id) throws SQLException {
        try {
            return buscarPorId(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void updateProduct(Produto product) throws SQLException {
        try {
            salvar(product);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public void deleteProduct(Integer id) throws SQLException {
        try {
            excluir(id);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Produto> getAllProducts() throws SQLException {
        try {
            return listarTodos();
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    public List<Produto> searchProductsByName(String nome) throws SQLException {
        try {
            return pesquisarPorNome(nome);
        } catch (CadastroException e) {
            throw new SQLException(e);
        }
    }

    private void validar(Produto produto) throws CadastroException {
        if (produto.getName() == null || produto.getName().trim().isEmpty()) {
            throw new CadastroException("Nome do produto é obrigatório.");
        }
        if (produto.getPrice() == null) {
            throw new CadastroException("Preço do produto é obrigatório.");
        }
        if (produto.getCategory() == null || produto.getCategory().trim().isEmpty()) {
            throw new CadastroException("Categoria do produto é obrigatória.");
        }
    }
}
