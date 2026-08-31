package br.com.tartarugacometa.suporte;

import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.enums.TipoPessoa;

public class FabricaDeDados {

    public static Cliente clientePessoaFisica() {
        Cliente cliente = new Cliente();
        cliente.setPersonType(TipoPessoa.FISICA);
        cliente.setDocument("11144477735");
        cliente.setName("João Silva");
        cliente.setEmail("joao@example.com");
        cliente.setPhone("11999998888");
        return cliente;
    }
}
