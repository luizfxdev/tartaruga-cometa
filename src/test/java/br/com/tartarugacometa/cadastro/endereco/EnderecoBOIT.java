package br.com.tartarugacometa.cadastro.endereco;

import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.enums.TipoEndereco;
import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.suporte.ContainerCompartilhado;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class EnderecoBOIT {

    private static final String[] CPFS = {
        "83228616649", "26914895740", "87159227905", "18459505502", "43487329140",
        "30295701188", "58213912101", "20399967702", "71843685256", "42987882700",
        "78867573357", "86443731611", "88815223908", "63066893707", "02681997879",
        "71852571152", "05613260028", "69538670000", "50330292749", "94671338491"
    };
    private static int contador = 0;

    private final ClienteBO clienteBO = new ClienteBO();
    private final EnderecoBO enderecoBO = new EnderecoBO();

    private Integer clienteId;

    @BeforeAll
    static void subirBanco() throws Exception {
        ContainerCompartilhado.iniciar();
    }

    @BeforeEach
    void prepararCliente() throws CadastroException {
        Cliente cliente = new Cliente();
        cliente.setPersonType(TipoPessoa.FISICA);
        cliente.setDocument(CPFS[contador++]);
        cliente.setName("Cliente Endereco Teste");
        cliente.setEmail("clienteendereco@example.com");
        clienteBO.salvar(cliente);
        clienteId = cliente.getId();
    }

    private Endereco enderecoValido() {
        Endereco endereco = new Endereco();
        endereco.setClientId(clienteId);
        endereco.setAddressType(TipoEndereco.CADASTRO);
        endereco.setStreet("Rua Teste");
        endereco.setNumber("10");
        endereco.setNeighborhood("Bairro Teste");
        endereco.setCity("Cidade Teste");
        endereco.setState("SP");
        endereco.setZipCode("01234567");
        endereco.setCountry("Brasil");
        endereco.setIsMain(false);
        return endereco;
    }

    @Test
    void salvarInsereEnderecoNovo() throws CadastroException {
        Endereco endereco = enderecoValido();

        enderecoBO.salvar(endereco);

        assertThat(endereco.getId()).isNotNull();
    }

    @Test
    void buscarPorIdRetornaEnderecoEnriquecido() throws CadastroException {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        Optional<Endereco> encontrado = enderecoBO.buscarPorId(endereco.getId());

        assertThat(encontrado).isPresent();
    }

    @Test
    void listarPorClienteIdRetornaEnderecosDoCliente() throws CadastroException {
        enderecoBO.salvar(enderecoValido());

        List<Endereco> enderecos = enderecoBO.listarPorClienteId(clienteId);

        assertThat(enderecos).isNotEmpty();
    }

    @Test
    void listarTodosRetornaEnderecosPersistidos() throws CadastroException {
        enderecoBO.salvar(enderecoValido());

        List<Endereco> todos = enderecoBO.listarTodos();

        assertThat(todos).isNotEmpty();
    }

    @Test
    void definirEnderecoPrincipalAlteraFlag() throws CadastroException {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        enderecoBO.definirEnderecoPrincipal(clienteId, endereco.getId());

        Optional<Endereco> atualizado = enderecoBO.buscarPorId(endereco.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getIsMain()).isTrue();
    }

    @Test
    void excluirRemoveEnderecoSemVinculos() throws CadastroException {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        enderecoBO.excluir(endereco.getId());

        Optional<Endereco> apagado = enderecoBO.buscarPorId(endereco.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void createAddressDelegaParaSalvar() throws Exception {
        Endereco endereco = enderecoValido();

        Endereco criado = enderecoBO.createAddress(endereco);

        assertThat(criado.getId()).isNotNull();
    }

    @Test
    void getAddressByIdDelegaParaBuscarPorId() throws Exception {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        Optional<Endereco> encontrado = enderecoBO.getAddressById(endereco.getId());

        assertThat(encontrado).isPresent();
    }

    @Test
    void updateAddressDelegaParaSalvar() throws Exception {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        endereco.setStreet("Rua Atualizada");
        enderecoBO.updateAddress(endereco);

        Optional<Endereco> atualizado = enderecoBO.getAddressById(endereco.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getStreet()).isEqualTo("Rua Atualizada");
    }

    @Test
    void deleteAddressDelegaParaExcluir() throws Exception {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        enderecoBO.deleteAddress(endereco.getId());

        Optional<Endereco> apagado = enderecoBO.getAddressById(endereco.getId());
        assertThat(apagado).isEmpty();
    }

    @Test
    void getAllAddressesDelegaParaListarTodos() throws Exception {
        enderecoBO.salvar(enderecoValido());

        List<Endereco> todos = enderecoBO.getAllAddresses();

        assertThat(todos).isNotEmpty();
    }

    @Test
    void getAddressesByClientIdDelegaParaListarPorClienteId() throws Exception {
        enderecoBO.salvar(enderecoValido());

        List<Endereco> enderecos = enderecoBO.getAddressesByClientId(clienteId);

        assertThat(enderecos).isNotEmpty();
    }

    @Test
    void setMainAddressDelegaParaDefinirEnderecoPrincipal() throws Exception {
        Endereco endereco = enderecoValido();
        enderecoBO.salvar(endereco);

        enderecoBO.setMainAddress(clienteId, endereco.getId());

        Optional<Endereco> atualizado = enderecoBO.getAddressById(endereco.getId());
        assertThat(atualizado).isPresent();
        assertThat(atualizado.get().getIsMain()).isTrue();
    }
}
