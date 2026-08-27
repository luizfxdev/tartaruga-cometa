package br.com.tartarugacometa.util;

public final class ValidadorCpf {
    private ValidadorCpf() {}

    public static boolean valido(String cpf) {
        if (cpf == null) return false;
        cpf = cpf.replaceAll("[^\\d]", "");
        if (cpf.length() != 11) return false;
        if (cpf.matches("(\\d)\\1{10}")) return false;

        char dv1 = digito(cpf.substring(0, 9), 10);
        char dv2 = digito(cpf.substring(0, 9) + dv1, 11);
        return cpf.charAt(9) == dv1 && cpf.charAt(10) == dv2;
    }

    private static char digito(String sequencia, int peso) {
        int soma = 0;
        for (int i = 0; i < sequencia.length(); i++) {
            soma += Character.getNumericValue(sequencia.charAt(i)) * (peso - i);
        }
        int resto = soma % 11;
        return resto < 2 ? '0' : (char) ('0' + (11 - resto));
    }
}
