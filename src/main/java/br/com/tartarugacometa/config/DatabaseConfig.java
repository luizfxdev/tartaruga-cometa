package br.com.tartarugacometa.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConfig {
    private static final Logger LOG = Logger.getLogger(DatabaseConfig.class.getName());
    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new RuntimeException("Arquivo database.properties não encontrado: " + PROPERTIES_FILE);
            }
            properties.load(input);

            String dbPassword = System.getenv("DB_PASSWORD");
            if (dbPassword != null) {
                properties.setProperty("db.password", dbPassword);
            }

            String dbUsername = System.getenv("DB_USERNAME");
            if (dbUsername != null) {
                properties.setProperty("db.username", dbUsername);
            }

            String dbUrl = System.getenv("DB_URL");
            if (dbUrl != null) {
                properties.setProperty("db.url", dbUrl);
            }

            Class.forName(properties.getProperty("db.driver"));

            LOG.info("Configuração do banco de dados carregada. URL: " + properties.getProperty("db.url")
                    + ", Usuário: " + properties.getProperty("db.username"));

        } catch (IOException | ClassNotFoundException e) {
            LOG.log(Level.SEVERE, "Falha ao carregar configuração ou driver do banco de dados", e);
            throw new RuntimeException("Falha ao carregar configuração ou driver do banco de dados", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Falha ao conectar ao banco de dados. URL: "
                    + properties.getProperty("db.url"), e);
            throw e;
        }
    }

    public static void close(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Falha ao fechar ResultSet", e);
            }
        }
    }

    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Falha ao fechar Statement", e);
            }
        }
    }

    public static void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOG.log(Level.WARNING, "Falha ao fechar Connection", e);
            }
        }
    }

    public static void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        close(rs);
        close(pstmt);
        close(conn);
    }

    public static void close(Connection conn, PreparedStatement pstmt) {
        close(pstmt);
        close(conn);
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
