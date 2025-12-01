package com.tartarugacometasystem.model;

public enum AddressType {
    ORIGIN("Origem"), // Traduzido o valor interno para inglês, mantendo o label em português
    DESTINATION("Destino"),
    REGISTRATION("Cadastral"); // Renomeado de CADASTRAL para REGISTRATION

    private final String label; // Mantém o label em português para exibição

    AddressType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Método para converter String para AddressType (útil para DAO)
    public static AddressType fromValue(String value) {
        for (AddressType type : AddressType.values()) {
            // Compara o nome do ENUM (em inglês) com o valor fornecido (case-insensitive)
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de endereço inválido: " + value);
    }
}
