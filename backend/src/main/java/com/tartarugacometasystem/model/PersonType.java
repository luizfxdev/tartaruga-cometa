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

    // Método para converter String para PersonType (útil para DAO e Servlets)
    public static PersonType fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo de pessoa não pode ser nulo ou vazio.");
        }
        String normalizedValue = value.trim().toUpperCase(); // Normaliza para maiúsculas para comparação

        for (PersonType type : PersonType.values()) {
            // 1. Tenta comparar com o nome do ENUM (INDIVIDUAL, LEGAL_ENTITY)
            if (type.name().equals(normalizedValue)) {
                return type;
            }
            // 2. Tenta comparar com o label em português (Pessoa Física, Pessoa Jurídica)
            //    Remove espaços e acentos para maior robustez na comparação
            if (type.getLabel().toUpperCase().replace(" ", "").replace("Á", "A").replace("Í", "I").equals(normalizedValue.replace(" ", "").replace("Á", "A").replace("Í", "I"))) {
                return type;
            }
            // 3. Tenta comparar com as variantes "FISICA" e "JURIDICA"
            if ("FISICA".equals(normalizedValue) && type == INDIVIDUAL) {
                return INDIVIDUAL;
            }
            if ("JURIDICA".equals(normalizedValue) && type == LEGAL_ENTITY) {
                return LEGAL_ENTITY;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
