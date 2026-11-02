package com.kairos.utils.connection;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    // Carregar variáveis de ambiente
    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    // Configurações do banco
    private static String setting(String name) {
        String value = System.getenv(name);
        return value != null ? value : dotenv.get(name);
    }
    private static final String DRIVER = setting("DB_DRIVER");
    private static final String URL = setting("DB_URL");
    private static final String USER = setting("DB_USER");
    private static final String PASSWORD = setting("DB_PASSWORD");

    public static boolean isRpaDemo() {
        return "true".equalsIgnoreCase(setting("KAIROS_RPA_DEMO"));
    }

    // Obter conexão com o banco
    public static Connection getConnection() throws SQLException {

        try {

            Class.forName(DRIVER);
            if (isRpaDemo()) {
                // Never run demo operations against either main Kairos database.
                if (URL == null || !URL.startsWith("jdbc:postgresql://")
                        || !"/kairos_servlet_demo".equals(java.net.URI.create(URL.substring(5)).getPath())) {
                    throw new SQLException("RPA exige banco exclusivo kairos_servlet_demo");
                }
            }
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (isRpaDemo() && !"kairos_servlet_demo".equals(connection.getCatalog())) {
                connection.close();
                throw new SQLException("Banco real diferente do banco demo autorizado");
            }
            return connection;

        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver não encontrado: " + DRIVER, e);

        } catch (SQLException e) {
            throw new SQLException("Não foi possível conectar ao banco", e);
        }
    }
}
