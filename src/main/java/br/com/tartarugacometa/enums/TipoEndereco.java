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

    public String paraColuna() {
        return this.name();
    }

    public static TipoEndereco fromValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Tipo de endereço inválido: null");
        }
        String normalizado = value.trim().toUpperCase();

        return switch (normalizado) {
            case "ORIGIN" -> ORIGEM;
            case "DESTINATION" -> DESTINO;
            case "REGISTRATION" -> CADASTRO;
            default -> {
                for (TipoEndereco tipo : TipoEndereco.values()) {
                    if (tipo.name().equals(normalizado)) {
                        yield tipo;
                    }
                }
                throw new IllegalArgumentException("Tipo de endereço inválido: " + value);
            }
        };
    }
}
