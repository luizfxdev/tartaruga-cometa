package com.tartarugacometasystem.model;

public enum DeliveryStatus {
    PENDING("Pendente"),
    IN_TRANSIT("Em Trânsito"),
    DELIVERED("Entregue"),
    CANCELED("Cancelada"),
    NOT_PERFORMED("Não Realizada");

    private final String label; // Mantém o label em português para exibição

    DeliveryStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Adicionado este método para que o JSP possa acessar o nome do enum como "value"
    public String getValue() {
        return this.name();
    }

    // Método para converter String para DeliveryStatus (útil para DAO e Servlets)
    public static DeliveryStatus fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Status de entrega não pode ser nulo ou vazio.");
        }
        for (DeliveryStatus status : DeliveryStatus.values()) {
            // Compara o nome do ENUM (em inglês) com o valor fornecido (case-insensitive)
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Status de entrega inválido: " + value);
    }
}
