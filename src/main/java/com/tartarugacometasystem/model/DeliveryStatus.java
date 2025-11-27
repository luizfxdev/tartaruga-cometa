package com.tartarugacometasystem.model;

public enum DeliveryStatus {
    PENDENTE("PENDENTE"),
    EM_TRANSITO("EM_TRANSITO"),
    ENTREGUE("ENTREGUE"),
    CANCELADA("CANCELADA"),
    NAO_REALIZADA("NAO_REALIZADA");

    private final String value;

    DeliveryStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static DeliveryStatus fromValue(String value) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status inválido: " + value);
    }
}
