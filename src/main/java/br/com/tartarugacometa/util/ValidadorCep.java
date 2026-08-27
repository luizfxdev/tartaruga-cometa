package br.com.tartarugacometa.util;

public final class ValidadorCep {
    private ValidadorCep() {}

    public static boolean valido(String cep) {
        if (cep == null) return false;
        String limpo = cep.replaceAll("[^\\d]", "");
        return limpo.length() == 8;
    }
}
