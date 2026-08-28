package br.com.tartarugacometa.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GeradorCodigoRastreioTest {

    @Test
    void geraCodigoNoFormatoTcMaisNoveDigitosMaisBr() {
        String codigo = GeradorCodigoRastreio.gerar();

        assertThat(codigo)
            .hasSize(13)
            .matches("^TC\\d{9}BR$");
    }

    @Test
    void geraCodigosDiferentesEmChamadasSucessivas() {
        String codigo1 = GeradorCodigoRastreio.gerar();
        String codigo2 = GeradorCodigoRastreio.gerar();

        assertThat(codigo1).isNotEqualTo(codigo2);
    }
}
