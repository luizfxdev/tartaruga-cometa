package br.com.tartarugacometa.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static final Logger LOG = Logger.getLogger(DatabaseConfig.class.getName());
    private static final String PROPERTIES_FILE = "database.properties";
    private static final Properties properties = new Properties();
    private static final HikariDataSource dataSource;

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

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setDriverClassName(properties.getProperty("db.driver"));
            hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
            hikariConfig.setUsername(properties.getProperty("db.username"));
            hikariConfig.setPassword(properties.getProperty("db.password"));
            hikariConfig.setMinimumIdle(
                    Integer.parseInt(properties.getProperty("db.pool.minSize", "5")));
            hikariConfig.setMaximumPoolSize(
                    Integer.parseInt(properties.getProperty("db.pool.maxSize", "20")));
            hikariConfig.setConnectionTimeout(
                    Long.parseLong(properties.getProperty("db.pool.timeout", "30000")));
            hikariConfig.setPoolName("tartaruga-cometa-pool");

            dataSource = new HikariDataSource(hikariConfig);

            LOG.info("Configuração do banco de dados carregada. URL: " + properties.getProperty("db.url")
                    + ", Usuário: " + properties.getProperty("db.username"));

        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Falha ao carregar configuração do banco de dados", e);
            throw new RuntimeException("Falha ao carregar configuração do banco de dados", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            return dataSource.getConnection();
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

    public static void shutdown() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            LOG.info("Pool de conexões encerrado");
        }
    }
}
