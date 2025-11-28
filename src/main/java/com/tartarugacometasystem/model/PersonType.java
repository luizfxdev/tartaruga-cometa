package com.tartarugacometasystem.model;

public enum PersonType {
    FISICA("Pessoa Física"),
    JURIDICA("Pessoa Jurídica");

    private final String label;

    PersonType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return this.name();
    }

    public static PersonType fromValue(String value) {
        for (PersonType type : PersonType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
