package com.tartarugacometasystem.util;

import java.io.PrintWriter; // Importar PrintWriter
import java.io.StringWriter; // Importar StringWriter
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    // Configurações do banco de dados PostgreSQL
    private static final String URL = "jdbc:postgresql://localhost:5432/tartaruga_cometa_db";
    private static final String USER = "tartaruga";
    private static final String PASSWORD = "12345678";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Erro ao carregar o driver JDBC do PostgreSQL", e);
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
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            System.err.println("========================================");
            System.err.println("ERRO CRÍTICO DE CONEXÃO COM O BANCO DE DADOS:");
            System.err.println("----------------------------------------");
            System.err.println("URL de Conexão: " + URL);
            System.err.println("Usuário: " + USER);
            System.err.println("Mensagem de Erro: " + e.getMessage());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("----------------------------------------");
            System.err.println("Verifique se o servidor PostgreSQL está rodando,");
            System.err.println("se as credenciais (usuário/senha) estão corretas,");
            System.err.println("e se o nome do banco de dados ('tartaruga_cometa_db') existe.");
            System.err.println("----------------------------------------");
            // >>> CORREÇÃO AQUI: Usar StringWriter e PrintWriter para imprimir o stack trace <<<
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            System.err.println("Stack Trace Completo:\n" + sw.toString());
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
                System.err.println("Erro ao fechar ResultSet: " + e.getMessage());
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
                System.err.println("Erro ao fechar Statement: " + e.getMessage());
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
            } catch (SQLException e) {
                System.err.println("Erro ao fechar Connection: " + e.getMessage());
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
}
