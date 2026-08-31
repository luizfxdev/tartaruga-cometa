package br.com.tartarugacometa.cadastro.cliente;

import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.exception.CadastroException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class ClienteBOTest {

    @Mock
    private ClienteDAO clienteDAO;

    @InjectMocks
    private ClienteBO clienteBO;

    private Cliente clienteValido() {
        Cliente cliente = new Cliente();
        cliente.setPersonType(TipoPessoa.FISICA);
        cliente.setDocument("11144477735");
        cliente.setName("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setPhone("11999998888");
        return cliente;
    }

    @Test
    void rejeitaTipoPessoaAusente() {
        Cliente cliente = clienteValido();
        cliente.setPersonType(null);

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Tipo de pessoa");
    }

    @Test
    void rejeitaNomeVazio() {
        Cliente cliente = clienteValido();
        cliente.setName("");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Nome");
    }

    @Test
    void rejeitaNomeMenorQueTresCaracteres() {
        Cliente cliente = clienteValido();
        cliente.setName("Jo");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("mínimo 3 caracteres");
    }

    @Test
    void rejeitaCpfInvalido() {
        Cliente cliente = clienteValido();
        cliente.setDocument("11111111111");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("CPF inválido");
    }

    @Test
    void rejeitaCnpjInvalido() {
        Cliente cliente = clienteValido();
        cliente.setPersonType(TipoPessoa.JURIDICA);
        cliente.setDocument("11111111111111");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("CNPJ inválido");
    }

    @Test
    void rejeitaEmailInvalido() {
        Cliente cliente = clienteValido();
        cliente.setEmail("email-invalido-sem-arroba");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Email inválido");
    }

    @Test
    void rejeitaClienteSemCanalDeContato() {
        Cliente cliente = clienteValido();
        cliente.setEmail(null);
        cliente.setPhone(null);

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("canal de contato");
    }

    @Test
    void rejeitaDocumentoAusente() {
        Cliente cliente = clienteValido();
        cliente.setDocument("");

        assertThatThrownBy(() -> clienteBO.salvar(cliente))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Documento");
    }
}
