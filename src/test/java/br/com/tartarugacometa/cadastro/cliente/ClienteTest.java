package br.com.tartarugacometa.cadastro.cliente;

import br.com.tartarugacometa.enums.TipoPessoa;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClienteTest {

    @Test
    void cria_cliente_pessoa_fisica() {
        Cliente cliente = new Cliente();
        cliente.setName("João Silva");
        cliente.setDocument("12345678901");
        cliente.setPersonType(TipoPessoa.FISICA);

        assertThat(cliente.getName()).isEqualTo("João Silva");
        assertThat(cliente.getDocument()).isEqualTo("12345678901");
        assertThat(cliente.getPersonType()).isEqualTo(TipoPessoa.FISICA);
    }

    @Test
    void cria_cliente_pessoa_juridica() {
        Cliente cliente = new Cliente();
        cliente.setName("Empresa X Ltda");
        cliente.setDocument("12345678901234");
        cliente.setPersonType(TipoPessoa.JURIDICA);

        assertThat(cliente.getName()).isEqualTo("Empresa X Ltda");
        assertThat(cliente.getDocument()).isEqualTo("12345678901234");
        assertThat(cliente.getPersonType()).isEqualTo(TipoPessoa.JURIDICA);
    }

    @Test
    void cliente_com_email_valido() {
        Cliente cliente = new Cliente();
        cliente.setEmail("cliente@example.com");

        assertThat(cliente.getEmail()).isEqualTo("cliente@example.com");
    }

    @Test
    void cliente_com_telefone() {
        Cliente cliente = new Cliente();
        cliente.setPhone("1133334444");

        assertThat(cliente.getPhone()).isEqualTo("1133334444");
    }
}
