package br.com.tartarugacometa.performance;

import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.cliente.ClienteDAO;
import br.com.tartarugacometa.suporte.BancoDeTeste;
import br.com.tartarugacometa.suporte.FabricaDeDados;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
class PerformanceTest {

    @Container
    static PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("tartaruga_teste");

    private static final ClienteDAO clienteDAO = new ClienteDAO();

    @BeforeAll
    static void criarSchema() throws Exception {
        BancoDeTeste.executarScripts(POSTGRES, "00-banco-e-tipos", "01-cliente");
    }

    @Test
    void buscaPorIdDentroDoLimite() throws Exception {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            clienteDAO.inserir(conexao, cliente);

            long inicio = System.currentTimeMillis();
            clienteDAO.buscarPorId(conexao, cliente.getId());
            long duracao = System.currentTimeMillis() - inicio;

            assertThat(duracao).isLessThan(200);
        }
    }

    @Test
    void listagemDentroDoLimite() throws Exception {
        try (Connection conexao = POSTGRES.createConnection("")) {
            for (int i = 0; i < 20; i++) {
                Cliente cliente = FabricaDeDados.clientePessoaFisica();
                cliente.setDocument(String.format("%011d", 10000000000L + i));
                clienteDAO.inserir(conexao, cliente);
            }

            long inicio = System.currentTimeMillis();
            clienteDAO.buscarTodos(conexao);
            long duracao = System.currentTimeMillis() - inicio;

            assertThat(duracao).isLessThan(300);
        }
    }

    @Test
    void insertComCommitDentroDoLimite() throws Exception {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("15350946056");

            long inicio = System.currentTimeMillis();
            clienteDAO.inserir(conexao, cliente);
            long duracao = System.currentTimeMillis() - inicio;

            assertThat(duracao).isLessThan(500);
        }
    }

    @Test
    void existeDocumentoDentroDoLimite() throws Exception {
        try (Connection conexao = POSTGRES.createConnection("")) {
            Cliente cliente = FabricaDeDados.clientePessoaFisica();
            cliente.setDocument("60729990850");
            clienteDAO.inserir(conexao, cliente);

            long inicio = System.currentTimeMillis();
            clienteDAO.existeDocumento(conexao, "60729990850", null);
            long duracao = System.currentTimeMillis() - inicio;

            assertThat(duracao).isLessThan(200);
        }
    }
}
