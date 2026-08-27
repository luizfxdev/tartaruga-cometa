package br.com.tartarugacometa.util;

import java.util.Random;

public final class GeradorCodigoRastreio {
    private static final String PREFIXO = "TC";
    private static final String SUFIXO = "BR";
    private static final int COMPRIMENTO_NUMERICO = 9;
    private static final int MAX_TENTATIVAS = 3;
    private static final Random ALEATORIO = new Random();

    private GeradorCodigoRastreio() {}

    public static String gerar() {
        StringBuilder codigo = new StringBuilder(PREFIXO);
        for (int i = 0; i < COMPRIMENTO_NUMERICO; i++) {
            codigo.append(ALEATORIO.nextInt(10));
        }
        codigo.append(SUFIXO);
        return codigo.toString();
    }
}
