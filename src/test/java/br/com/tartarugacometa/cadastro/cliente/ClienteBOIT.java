package br.com.tartarugacometa.cadastro.cliente;

import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.suporte.ContainerCompartilhado;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class ClienteBOIT {

    private final ClienteBO clienteBO = new ClienteBO();

    @BeforeAll
    static void subirBanco() throws Exception {
        ContainerCompartilhado.iniciar();
    }

    private Cliente clientePessoaFisica(String documento) {
        Cliente cliente = new Cliente();
        cliente.setPersonType(TipoPessoa.FISICA);
        cliente.setDocument(documento);
        cliente.setName("Cliente Teste " + documento);
        cliente.setEmail("cliente" + documento + "@example.com");
        cliente.setPhone("11999998888");
        return cliente;
    }

    @Test
    void salvarInsereClienteNovoERetornaId() throws CadastroException {
        Cliente cliente = clientePessoaFisica("11144477735");

        clienteBO.salvar(cliente);

        assertThat(cliente.getId()).isNotNull();
    }

    @Test
    void salvarRejeitaDocumentoDuplicado() throws CadastroException {
        Cliente primeiro = clientePessoaFisica("52998224725");
        clienteBO.salvar(primeiro);

        Cliente duplicado = clientePessoaFisica("52998224725");

        assertThatThrownBy(() -> clienteBO.salvar(duplicado))
            .isInstanceOf(CadastroException.class)
            .hasMessageContaining("Já existe cliente");
    }

    @Test
    void buscarPorIdRetornaClienteEnriquecido() throws CadastroException {
        Cliente cliente = clientePessoaFisica("15350946056");
        clienteBO.salvar(cliente);

        Optional<Cliente> encontrado = clienteBO.buscarPorId(cliente.getId());

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getFormattedPersonType()).isNotBlank();
    }

    @Test
    void buscarPorIdRetornaVazioQuandoNaoExiste() throws CadastroException {
        Optional<Cliente> resultado = clienteBO.buscarPorId(999999);

        assertThat(resultado).isEmpty();
    }

    @Test
    void listarTodosRetornaClientesEnriquecidos() throws CadastroException {
        Cliente cliente = clientePessoaFisica("03840558930");
        clienteBO.salvar(cliente);

        List<Cliente> todos = clienteBO.listarTodos();

        assertThat(todos).isNotEmpty();
        assertThat(todos).allSatisfy(c -> assertThat(c.getFormattedPersonType()).isNotBlank());
    }

    @Test
    void pesquisarEncontraClientePorNome() throws CadastroException {
        Cliente cliente = clientePessoaFisica("32262626022");
        cliente.setName("Fulano de Tal Pesquisa");
        clienteBO.salvar(cliente);

        List<Cliente> resultado = clienteBO.pesquisar("Pesquisa");

        assertThat(resultado).isNotEmpty();
    }

    @Test
    void atualizarAlteraDadosPersistidos() throws CadastroException {
        Cliente cliente = clientePessoaFisica("39036331005");
        clienteBO.salvar(cliente);

        cliente.setName("Nome Alterado");
        clienteBO.salvar(cliente);

        Optional<Cliente> atualizado = clienteBO.buscarPorId(cliente.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getName()).isEqualTo("Nome Alterado");
    }

    @Test
    void excluirRemoveClienteSemVinculos() throws CadastroException {
        Cliente cliente = clientePessoaFisica("87606057745");
        clienteBO.salvar(cliente);

        clienteBO.excluir(cliente.getId());

        Optional<Cliente> apagado = clienteBO.buscarPorId(cliente.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void createClientDelegaParaSalvar() throws Exception {
        Cliente cliente = clientePessoaFisica("23349537502");

        Cliente criado = clienteBO.createClient(cliente);

        assertThat(criado.getId()).isNotNull();
    }

    @Test
    void getClientByIdDelegaParaBuscarPorId() throws Exception {
        Cliente cliente = clientePessoaFisica("47573308639");
        clienteBO.salvar(cliente);

        Optional<Cliente> encontrado = clienteBO.getClientById(cliente.getId());

        assertThat(encontrado).isPresent();
    }

    @Test
    void updateClientDelegaParaSalvar() throws Exception {
        Cliente cliente = clientePessoaFisica("20695747509");
        clienteBO.salvar(cliente);

        cliente.setName("Atualizado Via Wrapper");
        clienteBO.updateClient(cliente);

        Optional<Cliente> atualizado = clienteBO.getClientById(cliente.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getName()).isEqualTo("Atualizado Via Wrapper");
    }

    @Test
    void deleteClientDelegaParaExcluir() throws Exception {
        Cliente cliente = clientePessoaFisica("39625790527");
        clienteBO.salvar(cliente);

        clienteBO.deleteClient(cliente.getId());

        Optional<Cliente> apagado = clienteBO.getClientById(cliente.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void getAllClientsDelegaParaListarTodos() throws Exception {
        clienteBO.salvar(clientePessoaFisica("46276083810"));

        List<Cliente> todos = clienteBO.getAllClients();

        assertThat(todos).isNotEmpty();
    }

    @Test
    void searchDelegaParaPesquisar() throws Exception {
        Cliente cliente = clientePessoaFisica("21412032520");
        cliente.setName("Nome Buscavel Wrapper");
        clienteBO.salvar(cliente);

        List<Cliente> resultado = clienteBO.search("Buscavel Wrapper");

        assertThat(resultado).isNotEmpty();
    }
}
