package com.tartarugacometasystem.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Arquivo database.properties não encontrado");
            }
            properties.load(input);

            // Carrega o driver PostgreSQL
            Class.forName(properties.getProperty("db.driver"));

            System.out.println("========================================");
            System.out.println("📦 Configuração do Banco de Dados");
            System.out.println("   URL: " + properties.getProperty("db.url"));
            System.out.println("   Usuário: " + properties.getProperty("db.username"));
            System.out.println("========================================");

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar configurações do banco de dados", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );

            if (conn != null) {
                System.out.println("✅ Conexão estabelecida com sucesso!");
            }

            return conn;

        } catch (SQLException e) {
            System.err.println("❌ Erro ao conectar ao banco de dados:");
            System.err.println("   URL: " + properties.getProperty("db.url"));
            System.err.println("   Usuário: " + properties.getProperty("db.username"));
            System.err.println("   Erro: " + e.getMessage());
            throw e;
        }
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("🔒 Conexão fechada com sucesso");
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar conexão: " + e.getMessage());
            }
        }
    }

    // Método para obter propriedades específicas
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
