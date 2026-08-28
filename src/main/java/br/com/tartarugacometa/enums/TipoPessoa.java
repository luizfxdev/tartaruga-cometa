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

    public String paraColuna() {
        return this == FISICA ? "INDIVIDUAL" : "LEGAL_ENTITY";
    }

    public static TipoPessoa fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de pessoa não pode ser nulo ou vazio.");
        }
        String normalizedValue = value.trim().toUpperCase();

        if ("INDIVIDUAL".equals(normalizedValue)) {
            return FISICA;
        }
        if ("LEGAL_ENTITY".equals(normalizedValue)) {
            return JURIDICA;
        }

        for (TipoPessoa tipo : TipoPessoa.values()) {
            if (tipo.name().equals(normalizedValue)) {
                return tipo;
            }
            if (tipo.getRotulo().toUpperCase().replace(" ", "").replace("Á", "A").replace("Í", "I")
                    .equals(normalizedValue.replace(" ", "").replace("Á", "A").replace("Í", "I"))) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
