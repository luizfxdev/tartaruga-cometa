package br.com.tartarugacometa.entrega;

import br.com.tartarugacometa.cadastro.cliente.Cliente;
import br.com.tartarugacometa.cadastro.cliente.ClienteBO;
import br.com.tartarugacometa.cadastro.endereco.Endereco;
import br.com.tartarugacometa.cadastro.endereco.EnderecoBO;
import br.com.tartarugacometa.cadastro.produto.Produto;
import br.com.tartarugacometa.cadastro.produto.ProdutoBO;
import br.com.tartarugacometa.entrega.item.ItemEntrega;
import br.com.tartarugacometa.enums.StatusEntrega;
import br.com.tartarugacometa.enums.TipoEndereco;
import br.com.tartarugacometa.enums.TipoPessoa;
import br.com.tartarugacometa.exception.CadastroException;
import br.com.tartarugacometa.exception.EntregaException;
import br.com.tartarugacometa.suporte.ContainerCompartilhado;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Testcontainers
class EntregaBOIT {

    private final ClienteBO clienteBO = new ClienteBO();
    private final EnderecoBO enderecoBO = new EnderecoBO();
    private final ProdutoBO produtoBO = new ProdutoBO();
    private final EntregaBO entregaBO = new EntregaBO();

    private Integer remetenteId;
    private Integer destinatarioId;
    private Integer enderecoOrigemId;
    private Integer enderecoDestinoId;
    private Integer produtoId;

    @BeforeAll
    static void subirBanco() throws Exception {
        ContainerCompartilhado.iniciar();
    }

    @BeforeEach
    void prepararDados() throws CadastroException {
        Cliente remetente = new Cliente();
        remetente.setPersonType(TipoPessoa.FISICA);
        remetente.setDocument(cpfUnico());
        remetente.setName("Remetente Teste");
        remetente.setEmail("remetente@example.com");
        clienteBO.salvar(remetente);
        remetenteId = remetente.getId();

        Cliente destinatario = new Cliente();
        destinatario.setPersonType(TipoPessoa.FISICA);
        destinatario.setDocument(cpfUnico());
        destinatario.setName("Destinatario Teste");
        destinatario.setEmail("destinatario@example.com");
        clienteBO.salvar(destinatario);
        destinatarioId = destinatario.getId();

        Endereco origem = new Endereco();
        origem.setClientId(remetenteId);
        origem.setAddressType(TipoEndereco.ORIGEM);
        origem.setStreet("Rua A");
        origem.setNumber("100");
        origem.setNeighborhood("Centro");
        origem.setCity("São Paulo");
        origem.setState("SP");
        origem.setZipCode("01234567");
        origem.setCountry("Brasil");
        origem.setIsMain(true);
        enderecoBO.salvar(origem);
        enderecoOrigemId = origem.getId();

        Endereco destino = new Endereco();
        destino.setClientId(destinatarioId);
        destino.setAddressType(TipoEndereco.DESTINO);
        destino.setStreet("Avenida B");
        destino.setNumber("200");
        destino.setNeighborhood("Vila Nova");
        destino.setCity("Rio de Janeiro");
        destino.setState("RJ");
        destino.setZipCode("20000000");
        destino.setCountry("Brasil");
        destino.setIsMain(true);
        enderecoBO.salvar(destino);
        enderecoDestinoId = destino.getId();

        Produto produto = new Produto();
        produto.setName("Produto Teste");
        produto.setCategory("Geral");
        produto.setPrice(BigDecimal.valueOf(25));
        produto.setWeightKg(BigDecimal.valueOf(1.0));
        produto.setVolumeM3(BigDecimal.valueOf(0.01));
        produto.setDeclaredValue(BigDecimal.valueOf(50));
        produto.setStockQuantity(1000);
        produtoBO.salvar(produto);
        produtoId = produto.getId();
    }

    private static final String[] CPFS_VALIDOS = {
        "06225902198", "20514475609", "93499252902", "37232519869",
        "57247983795", "03025114827", "65362971212", "54410462520",
        "23028188228", "95303264084", "40823013057", "99266632228",
        "69949449804", "66687943600", "50802548318", "17763057289",
        "75510393840", "43756387704", "17050064696", "74475245575",
        "21944113304", "45679440970", "20326296077", "87513943400",
        "70021853819", "69817136329", "57892802002", "17520079643",
        "20795685408", "64032805890", "06519751405", "10935170936",
        "36696807055", "31250282640", "41484793528", "45469777278",
        "19598907279", "87016391531", "48234194992", "85358234250"
    };
    private static int contador = 0;

    private String cpfUnico() {
        return CPFS_VALIDOS[contador++];
    }

    private ItemEntrega itemValido() {
        ItemEntrega item = new ItemEntrega();
        item.setProductId(produtoId);
        item.setQuantity(2);
        item.setUnitValue(BigDecimal.valueOf(25));
        item.setUnitWeightKg(BigDecimal.valueOf(1.0));
        item.setUnitVolumeM3(BigDecimal.valueOf(0.01));
        return item;
    }

    private Entrega entregaBase() {
        Entrega entrega = new Entrega();
        entrega.setSenderId(remetenteId);
        entrega.setRecipientId(destinatarioId);
        entrega.setOriginAddressId(enderecoOrigemId);
        entrega.setDestinationAddressId(enderecoDestinoId);
        entrega.setFreightValue(BigDecimal.valueOf(15));
        return entrega;
    }

    @Test
    void criarComTransacaoPersisteEntregaComStatusPendente() throws EntregaException {
        Entrega entrega = entregaBase();

        Entrega criada = entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador");

        assertThat(criada.getId()).isNotNull();
        assertThat(criada.getStatus()).isEqualTo(StatusEntrega.PENDENTE);
        assertThat(criada.getTrackingCode()).matches("^TC\\d{9}BR$");
    }

    @Test
    void criarComTransacaoCalculaTotaisAPartirDosItens() throws EntregaException {
        Entrega entrega = entregaBase();

        Entrega criada = entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador");

        assertThat(criada.getTotalValue()).isEqualByComparingTo(BigDecimal.valueOf(50).setScale(2));
    }

    @Test
    void transicionarStatusDePendenteParaEmTransito() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        entregaBO.transicionarStatus(entrega.getId(), StatusEntrega.EM_TRANSITO, "operador", "Coletado");

        Optional<Entrega> atualizada = entregaBO.getDeliveryById(entrega.getId());
        assertThat(atualizada).isPresent();
        assertThat(atualizada.get().getStatus()).isEqualTo(StatusEntrega.EM_TRANSITO);
    }

    @Test
    void transicionarStatusRejeitaTransicaoInvalida() throws EntregaException {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        assertThatThrownBy(() ->
            entregaBO.transicionarStatus(entrega.getId(), StatusEntrega.ENTREGUE, "operador", null))
            .isInstanceOf(EntregaException.class);
    }

    @Test
    void cancelarEntregaComMotivoValidoAlteraStatus() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        entregaBO.transicionarStatus(entrega.getId(), StatusEntrega.CANCELADA, "operador", "Cliente desistiu da compra");

        Optional<Entrega> atualizada = entregaBO.getDeliveryById(entrega.getId());
        assertThat(atualizada).isPresent();
        assertThat(atualizada.get().getStatus()).isEqualTo(StatusEntrega.CANCELADA);
    }

    @Test
    void createDeliveryDelegaParaCriarComTransacao() throws Exception {
        Entrega entrega = entregaBase();
        entrega.setItens(List.of(itemValido()));

        Entrega criada = entregaBO.createDelivery(entrega);

        assertThat(criada.getId()).isNotNull();
    }

    @Test
    void getAllDeliveriesDelegaParaBuscarTodos() throws Exception {
        entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        List<Entrega> todas = entregaBO.getAllDeliveries();

        assertThat(todas).isNotEmpty();
    }

    @Test
    void updateDeliveryPersisteAlteracoes() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        entrega.setObservations("Observação atualizada");
        entregaBO.updateDelivery(entrega);

        Optional<Entrega> atualizada = entregaBO.getDeliveryById(entrega.getId());
        assertThat(atualizada).isPresent();
        assertThat(atualizada.get().getObservations()).isEqualTo("Observação atualizada");
    }

    @Test
    void updateDeliveryStatusDelegaParaAtualizarStatusComTransacao() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        entregaBO.updateDeliveryStatus(entrega.getId(), StatusEntrega.EM_TRANSITO, null, "operador");

        Optional<Entrega> atualizada = entregaBO.getDeliveryById(entrega.getId());
        assertThat(atualizada).isPresent();
        assertThat(atualizada.get().getStatus()).isEqualTo(StatusEntrega.EM_TRANSITO);
    }

    @Test
    void searchEncontraEntregaPorCodigoRastreio() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        List<Entrega> resultado = entregaBO.search(entrega.getTrackingCode());

        assertThat(resultado).isNotEmpty();
    }

    @Test
    void deleteDeliveryRemoveEntregaEItens() throws Exception {
        Entrega entrega = entregaBO.criarComTransacao(entregaBase(), List.of(itemValido()), "operador");

        entregaBO.deleteDelivery(entrega.getId());

        Optional<Entrega> apagada = entregaBO.getDeliveryById(entrega.getId());
        assertThat(apagada).isEmpty();
    }
}
