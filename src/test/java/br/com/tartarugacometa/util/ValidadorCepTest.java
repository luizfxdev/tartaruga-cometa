package br.com.tartarugacometa.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValidadorCepTest {

    @ParameterizedTest
    @ValueSource(strings = {"01234567", "01234-567"})
    void aceitaCepComOitoDigitos(String cep) {
        assertThat(ValidadorCep.valido(cep)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"1234567", "123456789", "abcdefgh"})
    void rejeitaCepComTamanhoIncorreto(String cep) {
        assertThat(ValidadorCep.valido(cep)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void rejeitaCepNulo(String cep) {
        assertThat(ValidadorCep.valido(cep)).isFalse();
    }
}
