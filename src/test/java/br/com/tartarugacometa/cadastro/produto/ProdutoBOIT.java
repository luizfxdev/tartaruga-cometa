package br.com.tartarugacometa.cadastro.produto;

import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.suporte.ContainerCompartilhado;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class ProdutoBOIT {

    private final ProdutoBO produtoBO = new ProdutoBO();

    @BeforeAll
    static void subirBanco() throws Exception {
        ContainerCompartilhado.iniciar();
    }

    private Produto produtoValido(String nome) {
        Produto produto = new Produto();
        produto.setName(nome);
        produto.setCategory("Geral");
        produto.setPrice(BigDecimal.valueOf(10));
        produto.setWeightKg(BigDecimal.valueOf(1));
        produto.setVolumeM3(BigDecimal.valueOf(0.01));
        produto.setDeclaredValue(BigDecimal.valueOf(20));
        produto.setStockQuantity(100);
        return produto;
    }

    @Test
    void salvarInsereProdutoNovo() throws CadastroException {
        Produto produto = produtoValido("Produto A");

        produtoBO.salvar(produto);

        assertThat(produto.getId()).isNotNull();
    }

    @Test
    void buscarPorIdRetornaProdutoInserido() throws CadastroException {
        Produto produto = produtoValido("Produto B");
        produtoBO.salvar(produto);

        Optional<Produto> encontrado = produtoBO.buscarPorId(produto.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getName()).isEqualTo("Produto B");
    }

    @Test
    void listarTodosRetornaProdutosPersistidos() throws CadastroException {
        produtoBO.salvar(produtoValido("Produto C"));

        List<Produto> todos = produtoBO.listarTodos();

        assertThat(todos).isNotEmpty();
    }

    @Test
    void pesquisarPorNomeEncontraProduto() throws CadastroException {
        produtoBO.salvar(produtoValido("Caixa Especial Pesquisa"));

        List<Produto> resultado = produtoBO.pesquisarPorNome("Especial Pesquisa");

        assertThat(resultado).isNotEmpty();
    }

    @Test
    void atualizarAlteraDadosDoProduto() throws CadastroException {
        Produto produto = produtoValido("Produto D");
        produtoBO.salvar(produto);

        produto.setName("Produto D Atualizado");
        produtoBO.salvar(produto);

        Optional<Produto> atualizado = produtoBO.buscarPorId(produto.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getName()).isEqualTo("Produto D Atualizado");
    }

    @Test
    void excluirRemoveProdutoSemVinculos() throws CadastroException {
        Produto produto = produtoValido("Produto E");
        produtoBO.salvar(produto);

        produtoBO.excluir(produto.getId());

        Optional<Produto> apagado = produtoBO.buscarPorId(produto.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void inativarMarcaProdutoComoInativo() throws CadastroException {
        Produto produto = produtoValido("Produto F");
        produtoBO.salvar(produto);

        produtoBO.inativar(produto.getId());

        Optional<Produto> inativado = produtoBO.buscarPorId(produto.getId());
        assertThat(inativado).isPresent();
        assertThat(inativado.get().isActive()).isFalse();
    }

    @Test
    void createProductDelegaParaSalvar() throws Exception {
        Produto produto = produtoValido("Produto G");

        Produto criado = produtoBO.createProduct(produto);

        assertThat(criado.getId()).isNotNull();
    }

    @Test
    void getProductByIdDelegaParaBuscarPorId() throws Exception {
        Produto produto = produtoValido("Produto H");
        produtoBO.salvar(produto);

        Optional<Produto> encontrado = produtoBO.getProductById(produto.getId());

        assertThat(encontrado).isPresent();
    }

    @Test
    void updateProductDelegaParaSalvar() throws Exception {
        Produto produto = produtoValido("Produto I");
        produtoBO.salvar(produto);

        produto.setName("Produto I Atualizado");
        produtoBO.updateProduct(produto);

        Optional<Produto> atualizado = produtoBO.getProductById(produto.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getName()).isEqualTo("Produto I Atualizado");
    }

    @Test
    void deleteProductDelegaParaExcluir() throws Exception {
        Produto produto = produtoValido("Produto J");
        produtoBO.salvar(produto);

        produtoBO.deleteProduct(produto.getId());

        Optional<Produto> apagado = produtoBO.getProductById(produto.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void getAllProductsDelegaParaListarTodos() throws Exception {
        produtoBO.salvar(produtoValido("Produto K"));

        List<Produto> todos = produtoBO.getAllProducts();

        assertThat(todos).isNotEmpty();
    }

    @Test
    void searchProductsByNameDelegaParaPesquisarPorNome() throws Exception {
        produtoBO.salvar(produtoValido("Produto Buscavel Wrapper"));

        List<Produto> resultado = produtoBO.searchProductsByName("Buscavel Wrapper");

        assertThat(resultado).isNotEmpty();
    }
}
