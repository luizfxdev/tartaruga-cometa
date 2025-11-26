package com.tartarugacometasystem.model;

public enum AddressType {
    ORIGEM("ORIGEM"),
    DESTINO("DESTINO"),
    CADASTRAL("CADASTRAL");

    private final String value;

    AddressType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static AddressType fromValue(String value) {
        for (AddressType type : AddressType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Tipo de endereço inválido: " + value);
    }
}
