package br.com.tartarugacometa.cadastro.produto;

import br.com.tartarugacometa.exception.CadastroException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ProdutoBOTest {

    @Mock
    private ProdutoDAO produtoDAO;

    @InjectMocks
    private ProdutoBO produtoBO;

    private Produto produtoValido() {
        Produto produto = new Produto();
        produto.setName("Caixa de Som");
        produto.setCategory("Eletrônicos");
        produto.setWeightKg(BigDecimal.valueOf(1.5));
        produto.setVolumeM3(BigDecimal.valueOf(0.01));
        produto.setDeclaredValue(BigDecimal.valueOf(100));
        return produto;
    }

    @Test
    void rejeitaNomeAusente() {
        Produto produto = produtoValido();
        produto.setName(null);

        assertThatThrownBy(() -> produtoBO.salvar(produto))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Nome");
    }

    @Test
    void rejeitaCategoriaAusente() {
        Produto produto = produtoValido();
        produto.setCategory(null);

        assertThatThrownBy(() -> produtoBO.salvar(produto))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Categoria");
    }

    @Test
    void rejeitaPesoZeroOuNegativo() {
        Produto produto = produtoValido();
        produto.setWeightKg(BigDecimal.ZERO);

        assertThatThrownBy(() -> produtoBO.salvar(produto))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Peso");
    }

    @Test
    void rejeitaVolumeZeroOuNegativo() {
        Produto produto = produtoValido();
        produto.setVolumeM3(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> produtoBO.salvar(produto))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Volume");
    }

    @Test
    void rejeitaValorDeclaradoNegativo() {
        Produto produto = produtoValido();
        produto.setDeclaredValue(BigDecimal.valueOf(-1));

        assertThatThrownBy(() -> produtoBO.salvar(produto))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Valor declarado");
    }
}
