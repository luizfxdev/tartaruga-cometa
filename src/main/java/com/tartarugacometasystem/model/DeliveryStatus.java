package com.tartarugacometasystem.model;

public enum DeliveryStatus {
    PENDING("Pendente"), // Traduzido o valor interno para inglês, mantendo o label em português
    IN_TRANSIT("Em Trânsito"), // Renomeado de EM_TRANSPORTE para IN_TRANSIT
    DELIVERED("Entregue"),
    CANCELED("Cancelada"),
    NOT_PERFORMED("Não Realizada"); // Renomeado de NAO_REALIZADA para NOT_PERFORMED

    private final String label; // Mantém o label em português para exibição

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Método para converter String para DeliveryStatus (útil para DAO)
    public static DeliveryStatus fromValue(String value) {
        for (DeliveryStatus status : DeliveryStatus.values()) {
            // Compara o nome do ENUM (em inglês) com o valor fornecido (case-insensitive)
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de entrega inválido: " + value);
    }
}
