package br.com.tartarugacometa.util;

public final class ValidadorCnpj {
    private ValidadorCnpj() {}

    public static boolean valido(String cnpj) {
        if (cnpj == null) return false;
        cnpj = cnpj.replaceAll("[^\\d]", "");
        if (cnpj.length() != 14) return false;
        if (cnpj.matches("(\\d)\\1{13}")) return false;

        int dv1 = digito1(cnpj.substring(0, 12));
        int dv2 = digito2(cnpj.substring(0, 12) + dv1);
        return cnpj.charAt(12) == ('0' + dv1) && cnpj.charAt(13) == ('0' + dv2);
    }

    private static int digito1(String sequencia) {
        int[] pesos = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 12; i++) {
            soma += (sequencia.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }

    private static int digito2(String sequencia) {
        int[] pesos = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int soma = 0;
        for (int i = 0; i < 13; i++) {
            soma += (sequencia.charAt(i) - '0') * pesos[i];
        }
        int resto = soma % 11;
        return resto < 2 ? 0 : 11 - resto;
    }
}
