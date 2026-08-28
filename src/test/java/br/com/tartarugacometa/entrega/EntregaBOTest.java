package br.com.tartarugacometa.entrega;

import br.com.tartarugacometa.cadastro.produto.ProdutoDAO;
import br.com.tartarugacometa.entrega.historico.HistoricoEntregaDAO;
import br.com.tartarugacometa.entrega.item.ItemEntrega;
import br.com.tartarugacometa.entrega.item.ItemEntregaDAO;
import br.com.tartarugacometa.exception.EntregaException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class EntregaBOTest {

    @Mock
    private EntregaDAO entregaDAO;

    @Mock
    private ItemEntregaDAO itemEntregaDAO;

    @Mock
    private HistoricoEntregaDAO historicoDAO;

    @Mock
    private ProdutoDAO produtoDAO;

    @InjectMocks
    private EntregaBO entregaBO;

    private Entrega entregaValida() {
        Entrega entrega = new Entrega();
        entrega.setSenderId(1);
        entrega.setRecipientId(2);
        entrega.setOriginAddressId(1);
        entrega.setDestinationAddressId(2);
        entrega.setFreightValue(BigDecimal.valueOf(50));
        return entrega;
    }

    private ItemEntrega itemValido() {
        ItemEntrega item = new ItemEntrega();
        item.setProductId(1);
        item.setQuantity(1);
        item.setUnitValue(BigDecimal.TEN);
        item.setUnitWeightKg(BigDecimal.ONE);
        item.setUnitVolumeM3(BigDecimal.valueOf(0.01));
        return item;
    }

    @Test
    void rejeitaRemetenteIgualDestinatario() {
        Entrega entrega = entregaValida();
        entrega.setRecipientId(entrega.getSenderId());

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("Remetente e destinatário");
    }

    @Test
    void rejeitaRemetenteOuDestinatarioAusente() {
        Entrega entrega = entregaValida();
        entrega.setSenderId(null);

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("Remetente e destinatário");
    }

    @Test
    void rejeitaEnderecoOrigemIgualDestino() {
        Entrega entrega = entregaValida();
        entrega.setDestinationAddressId(entrega.getOriginAddressId());

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("Endereço de origem e destino");
    }

    @Test
    void rejeitaEntregaSemItens() {
        Entrega entrega = entregaValida();

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("pelo menos um produto");
    }

    @Test
    void rejeitaItemComQuantidadeMenorQueUm() {
        Entrega entrega = entregaValida();
        ItemEntrega item = itemValido();
        item.setQuantity(0);

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(item), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("Quantidade");
    }

    @Test
    void rejeitaFreteNegativo() {
        Entrega entrega = entregaValida();
        entrega.setFreightValue(BigDecimal.valueOf(-10));

        assertThatThrownBy(() -> entregaBO.criarComTransacao(entrega, List.of(itemValido()), "operador"))
            .isInstanceOf(EntregaException.class)
            .hasMessageContaining("frete");
    }
}
