package com.tartarugacometasystem.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseConfig {
    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Arquivo database.properties não encontrado: " + PROPERTIES_FILE);
            }
            properties.load(input);

            // Carrega o driver JDBC do PostgreSQL
            Class.forName(properties.getProperty("db.driver"));

            System.out.println("========================================");
            System.out.println("📦 Configuração do Banco de Dados Carregada");
            System.out.println("   URL: " + properties.getProperty("db.url"));
            System.out.println("   Usuário: " + properties.getProperty("db.username"));
            System.out.println("========================================");

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erro ao carregar configurações do banco de dados ou driver JDBC:");
            System.err.println("   Mensagem: " + e.getMessage());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("   Stack Trace Completo:\n" + sw.toString());
            throw new RuntimeException("Falha ao carregar configuração ou driver do banco de dados", e);
        }
    }

    /**
     * Obtém uma nova conexão com o banco de dados.
     *
     * @return Uma instância de Connection.
     * @throws SQLException Se ocorrer um erro ao obter a conexão.
     */
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
            System.err.println("❌ ERRO CRÍTICO DE CONEXÃO COM O BANCO DE DADOS:");
            System.err.println("----------------------------------------");
            System.err.println("   URL de Conexão: " + properties.getProperty("db.url"));
            System.err.println("   Usuário: " + properties.getProperty("db.username"));
            System.err.println("   Mensagem de Erro: " + e.getMessage());
            System.err.println("   SQL State: " + e.getSQLState());
            System.err.println("   Error Code: " + e.getErrorCode());
            System.err.println("----------------------------------------");
            System.err.println("   Verifique se o servidor PostgreSQL está rodando,");
            System.err.println("   se as credenciais (usuário/senha) estão corretas,");
            System.err.println("   e se o nome do banco de dados ('" + properties.getProperty("db.url").substring(properties.getProperty("db.url").lastIndexOf('/') + 1) + "') existe.");
            System.err.println("----------------------------------------");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("   Stack Trace Completo:\n" + sw.toString());
            System.err.println("========================================");
            throw e; // Re-lança a exceção para que o fluxo normal de erro continue
        }
    }

    /**
     * Fecha um ResultSet.
     *
     * @param rs O ResultSet a ser fechado.
     */
    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar ResultSet: " + e.getMessage());
            }
        }
    }

    /**
     * Fecha um Statement ou PreparedStatement.
     *
     * @param stmt O Statement ou PreparedStatement a ser fechado.
     */
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar Statement: " + e.getMessage());
            }
        }
    }

    /**
     * Fecha uma conexão.
     *
     * @param conn A conexão a ser fechada.
     */
    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("🔒 Conexão fechada com sucesso.");
            } catch (SQLException e) {
                System.err.println("❌ Erro ao fechar Connection: " + e.getMessage());
            }
        }
    }

    /**
     * Fecha uma conexão, um PreparedStatement e um ResultSet.
     *
     * @param conn A conexão a ser fechada.
     * @param pstmt O PreparedStatement a ser fechado.
     * @param rs    O ResultSet a ser fechado.
     */
    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        close(rs);
        close(pstmt);
        close(conn);
    }

    /**
     * Fecha uma conexão e um PreparedStatement.
     *
     * @param conn A conexão a ser fechada.
     * @param pstmt O PreparedStatement a ser fechado.
     */
    public static void close(Connection conn, PreparedStatement pstmt) {
        close(pstmt);
        close(conn);
    }

    /**
     * Método para obter propriedades específicas.
     * @param key A chave da propriedade.
     * @return O valor da propriedade.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
