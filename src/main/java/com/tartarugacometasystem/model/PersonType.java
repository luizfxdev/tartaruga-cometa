package com.tartarugacometasystem.model;

public enum PersonType {
    PF("PF"),
    PJ("PJ");

    private final String value;

    PersonType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static PersonType fromValue(String value) {
        for (PersonType type : PersonType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de pessoa inválido: " + value);
    }
}
