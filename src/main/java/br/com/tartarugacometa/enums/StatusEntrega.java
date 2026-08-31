package br.com.tartarugacometa.enums;

import java.util.EnumSet;
import java.util.Map;

public enum StatusEntrega {
    PENDENTE("Pendente"),
    EM_TRANSITO("Em Trânsito"),
    ENTREGUE("Entregue"),
    CANCELADA("Cancelada"),
    NAO_REALIZADA("Não Realizada");

    private static final Map<StatusEntrega, EnumSet<StatusEntrega>> TRANSICOES = Map.of(
        PENDENTE,       EnumSet.of(EM_TRANSITO, CANCELADA),
        EM_TRANSITO,    EnumSet.of(ENTREGUE, NAO_REALIZADA, CANCELADA),
        ENTREGUE,       EnumSet.noneOf(StatusEntrega.class),
        CANCELADA,      EnumSet.noneOf(StatusEntrega.class),
        NAO_REALIZADA,  EnumSet.noneOf(StatusEntrega.class)
    );

    private final String rotulo;

    StatusEntrega(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public String getValue() {
        return this.name();
    }

    public boolean podeTransicionarPara(StatusEntrega novo) {
        return TRANSICOES.get(this).contains(novo);
    }

    public boolean ehTerminal() {
        return TRANSICOES.get(this).isEmpty();
    }

    public String paraColuna() {
        return this.name();
    }

    public static StatusEntrega fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Status de entrega não pode ser nulo ou vazio.");
        }
        String normalized = value.trim().toUpperCase();

        for (StatusEntrega status : StatusEntrega.values()) {
            if (status.name().equals(normalized)) {
                return status;
            }
        }

        if ("DELIVERED".equals(normalized)) return ENTREGUE;
        if ("NOT_PERFORMED".equals(normalized)) return NAO_REALIZADA;
        if ("PENDING".equals(normalized)) return PENDENTE;
        if ("IN_TRANSIT".equals(normalized)) return EM_TRANSITO;
        if ("CANCELED".equals(normalized)) return CANCELADA;

        throw new IllegalArgumentException("Status de entrega inválido: " + value);
    }
}
