package br.com.tartarugacometa.enums;

public enum TipoPessoa {
    FISICA("Pessoa Física"),
    JURIDICA("Pessoa Jurídica");

    private final String rotulo;

    TipoPessoa(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public String getValue() {
        return this.name();
    }

    public static TipoPessoa fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de pessoa não pode ser nulo ou vazio.");
        }
        String normalizedValue = value.trim().toUpperCase();

        for (TipoPessoa tipo : TipoPessoa.values()) {
            if (tipo.name().equals(normalizedValue)) {
                return tipo;
            }
            if (tipo.getRotulo().toUpperCase().replace(" ", "").replace("Á", "A").replace("Í", "I")
                    .equals(normalizedValue.replace(" ", "").replace("Á", "A").replace("Í", "I"))) {
                return tipo;
            }
            if ("FISICA".equals(normalizedValue) && tipo == FISICA) {
                return FISICA;
            }
            if ("JURIDICA".equals(normalizedValue) && tipo == JURIDICA) {
                return JURIDICA;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
