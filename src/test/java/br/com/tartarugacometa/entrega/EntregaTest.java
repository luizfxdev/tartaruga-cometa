package br.com.tartarugacometa.entrega;

import br.com.tartarugacometa.enums.StatusEntrega;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class EntregaTest {

    @Test
    void cria_entrega_com_status_pendente() {
        Entrega entrega = new Entrega();
        entrega.setStatus(StatusEntrega.PENDENTE);

        assertThat(entrega.getStatus()).isEqualTo(StatusEntrega.PENDENTE);
    }

    @Test
    void entrega_com_tracking_code() {
        Entrega entrega = new Entrega();
        entrega.setTrackingCode("TC000123456BR");

        assertThat(entrega.getTrackingCode()).isEqualTo("TC000123456BR");
    }

    @Test
    void entrega_com_valor_frete() {
        Entrega entrega = new Entrega();
        entrega.setFreightValue(BigDecimal.valueOf(50.00));

        assertThat(entrega.getFreightValue()).isEqualByComparingTo(BigDecimal.valueOf(50.00));
    }

    @Test
    void entrega_com_datas_importantes() {
        Entrega entrega = new Entrega();
        LocalDateTime agora = LocalDateTime.now();
        entrega.setCreationDate(agora);
        entrega.setDeliveryDate(agora.plusHours(24));

        assertThat(entrega.getCreationDate()).isEqualTo(agora);
        assertThat(entrega.getDeliveryDate()).isEqualTo(agora.plusHours(24));
    }

    @Test
    void entrega_com_remetente_destinatario() {
        Entrega entrega = new Entrega();
        entrega.setSenderId(1);
        entrega.setRecipientId(2);

        assertThat(entrega.getSenderId()).isEqualTo(1);
        assertThat(entrega.getRecipientId()).isEqualTo(2);
    }
}
