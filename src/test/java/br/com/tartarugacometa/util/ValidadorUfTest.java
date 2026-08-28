package br.com.tartarugacometa.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class ValidadorUfTest {

    @ParameterizedTest
    @ValueSource(strings = {"SP", "RJ", "sp", "mg"})
    void aceitaUfValida(String uf) {
        assertThat(ValidadorUf.valida(uf)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"XX", "ZZZ", ""})
    void rejeitaUfInvalida(String uf) {
        assertThat(ValidadorUf.valida(uf)).isFalse();
    }

    @ParameterizedTest
    @NullSource
    void rejeitaUfNula(String uf) {
        assertThat(ValidadorUf.valida(uf)).isFalse();
    }
}
