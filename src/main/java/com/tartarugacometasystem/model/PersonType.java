package com.tartarugacometasystem.model;

public enum PersonType {
    INDIVIDUAL("Pessoa Física"), // Traduzido o valor interno para inglês, mantendo o label em português
    LEGAL_ENTITY("Pessoa Jurídica"); // Traduzido o valor interno para inglês, mantendo o label em português

    private final String label; // Mantém o label em português para exibição

    PersonType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Retorna o nome do ENUM (em inglês) para ser usado no banco de dados
    public String getValue() {
        return this.name();
    }

    // Método para converter String para PersonType (útil para DAO)
    public static PersonType fromValue(String value) {
        for (PersonType type : PersonType.values()) {
            // Compara o nome do ENUM (em inglês) com o valor fornecido (case-insensitive)
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
