package com.tartarugacometasystem.model;

public enum DeliveryStatus {
    PENDENTE("Pendente"),
    EM_TRANSPORTE("Em Transporte"),
    ENTREGUE("Entregue"),
    NAO_REALIZADA("Não Realizada"),
    CANCELADA("Cancelada");

    private final String label;

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Método para converter String para DeliveryStatus (útil para DAO)
    public static DeliveryStatus fromValue(String value) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de entrega inválido: " + value);
    }
}
