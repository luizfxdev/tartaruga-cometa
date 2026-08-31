package br.com.tartarugacometa.entrega;

import br.com.tartarugacometa.enums.StatusEntrega;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Máquina de estados de entrega")
class TransicaoStatusTest {

    @ParameterizedTest(name = "{0} -> {1} = {2}")
    @CsvSource({
        "PENDENTE,EM_TRANSITO,true",
        "PENDENTE,CANCELADA,true",
        "PENDENTE,ENTREGUE,false",
        "PENDENTE,NAO_REALIZADA,false",

        "EM_TRANSITO,ENTREGUE,true",
        "EM_TRANSITO,NAO_REALIZADA,true",
        "EM_TRANSITO,CANCELADA,true",
        "EM_TRANSITO,PENDENTE,false",

        "ENTREGUE,PENDENTE,false",
        "ENTREGUE,EM_TRANSITO,false",
        "ENTREGUE,CANCELADA,false",
        "ENTREGUE,NAO_REALIZADA,false",
        "ENTREGUE,ENTREGUE,false",

        "CANCELADA,PENDENTE,false",
        "CANCELADA,EM_TRANSITO,false",
        "CANCELADA,ENTREGUE,false",
        "CANCELADA,CANCELADA,false",
        "CANCELADA,NAO_REALIZADA,false",

        "NAO_REALIZADA,PENDENTE,false",
        "NAO_REALIZADA,EM_TRANSITO,false",
        "NAO_REALIZADA,ENTREGUE,false",
        "NAO_REALIZADA,CANCELADA,false",
        "NAO_REALIZADA,NAO_REALIZADA,false"
    })
    @DisplayName("Valida transições de status")
    void valida_transicoes(StatusEntrega de, StatusEntrega para, boolean esperado) {
        assertThat(de.podeTransicionarPara(para)).isEqualTo(esperado);
    }

    @ParameterizedTest(name = "{0} é terminal = {1}")
    @CsvSource({
        "PENDENTE,false",
        "EM_TRANSITO,false",
        "ENTREGUE,true",
        "CANCELADA,true",
        "NAO_REALIZADA,true"
    })
    @DisplayName("Valida estados terminais")
    void valida_estados_terminais(StatusEntrega status, boolean ehTerminal) {
        assertThat(status.ehTerminal()).isEqualTo(ehTerminal);
    }
}
