package br.com.tartarugacometa.util;

import java.util.Set;

public final class ValidadorUf {
    private static final Set<String> UFS = Set.of(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA",
        "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
        "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    private ValidadorUf() {}

    public static boolean valida(String uf) {
        if (uf == null) return false;
        return UFS.contains(uf.toUpperCase().trim());
    }
}
