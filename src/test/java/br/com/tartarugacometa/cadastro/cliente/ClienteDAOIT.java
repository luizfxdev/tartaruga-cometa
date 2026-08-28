package br.com.tartarugacometa.cadastro.cliente;

import br.com.tartarugacometa.suporte.BancoDeTeste;
import br.com.tartarugacometa.suporte.FabricaDeDados;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class ClienteDAOIT {

    @Container
    static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("tartaruga_teste");

    private final ClienteDAO clienteDAO = new ClienteDAO();

    @BeforeAll
    static void criarSchema() throws Exception {
        BancoDeTeste.executarScripts(POSTGRES, "00-banco-e-tipos", "01-cliente");
    }

    @Test
    void inserirGravaClienteERetornaIdGerado() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();

            clienteDAO.inserir(conexao, cliente);

            assertThat(cliente.getId()).isNotNull();
        }
    }

    @Test
    void buscarPorIdRetornaClienteInserido() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("52998224725");
            clienteDAO.inserir(conexao, cliente);

            Optional<Cliente> encontrado = clienteDAO.buscarPorId(conexao, cliente.getId());

            assertThat(encontrado).isPresent();
            assertThat(encontrado.get().getName()).isEqualTo(cliente.getName());
        }
    }

    @Test
    void existeDocumentoDetectaDuplicidade() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("15350946056");
            clienteDAO.inserir(conexao, cliente);

            boolean existe = clienteDAO.existeDocumento(conexao, "15350946056", null);
            boolean naoExiste = clienteDAO.existeDocumento(conexao, "00000000000", null);

            assertThat(existe).isTrue();
            assertThat(naoExiste).isFalse();
        }
    }

    @Test
    void existeDocumentoIgnoraOProprioIdAoAtualizar() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("60729990899");
            clienteDAO.inserir(conexao, cliente);

            boolean existeExcluindoProprioId = clienteDAO.existeDocumento(conexao, "60729990899", cliente.getId());

            assertThat(existeExcluindoProprioId).isFalse();
        }
    }

    @Test
    void atualizarAlteraDadosDoCliente() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("84624236086");
            clienteDAO.inserir(conexao, cliente);

            cliente.setName("Nome Atualizado");
            clienteDAO.atualizar(conexao, cliente);

            Optional<Cliente> atualizado = clienteDAO.buscarPorId(conexao, cliente.getId());
            assertThat(atualizado).isPresent();
            assertThat(atualizado.get().getName()).isEqualTo("Nome Atualizado");
        }
    }

    @Test
    void excluirRemoveClienteDoBanco() throws SQLException {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("74584231060");
            clienteDAO.inserir(conexao, cliente);

            clienteDAO.excluir(conexao, cliente.getId());

            Optional<Cliente> apagado = clienteDAO.buscarPorId(conexao, cliente.getId());
            assertThat(apagado).isEmpty();
        }
    }
}
