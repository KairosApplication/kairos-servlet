package com.kairos.utils;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConnectionFactory {

    private static final Dotenv dotenv = Dotenv.load();

    private static final String DRIVER = dotenv.get("DB_DRIVER");
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);

            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver não encontrado: " + DRIVER, e);
        }

        catch (SQLException e) {
            throw new SQLException("Não foi possível conectar ao banco", e);
        }
    }
}