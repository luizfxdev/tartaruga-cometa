package br.com.tartarugacometa.enums;

public enum TipoEndereco {
    ORIGEM("Origem"),
    DESTINO("Destino"),
    CADASTRO("Cadastral");

    private final String rotulo;

    TipoEndereco(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getRotulo() {
        return rotulo;
    }

    public static TipoEndereco fromValue(String value) {
        for (TipoEndereco tipo : TipoEndereco.values()) {
            if (tipo.name().equalsIgnoreCase(value)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de endereço inválido: " + value);
    }
}
