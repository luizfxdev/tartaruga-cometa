package br.com.tartarugacometa.util;

import java.sql.Connection;
import java.sql.SQLException;

import br.com.tartarugacometa.config.DatabaseConfig;

public final class Conexao {
    private Conexao() {
    }

    public static Connection abrir() throws SQLException {
        return DatabaseConfig.getConnection();
    }
}
