package br.com.tartarugacometa.suporte;

import org.testcontainers.containers.PostgreSQLContainer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;
import java.util.stream.Collectors;

public class BancoDeTeste {

    public static void executarScripts(PostgreSQLContainer<?> postgres, String... nomeScripts) throws Exception {
        try (Connection conexao = postgres.createConnection("")) {
            for (String nome : nomeScripts) {
                executarScript(conexao, nome);
            }
        }
    }

    private static void executarScript(Connection conexao, String nomeScript) throws Exception {
        String resourcePath = "db/" + nomeScript + ".sql";
        try (InputStream input = BancoDeTeste.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalStateException("Script não encontrado: " + resourcePath);
            }
            String sql = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            try (Statement stmt = conexao.createStatement()) {
                stmt.execute(sql);
            }
        }
    }
}
