package com.kairos.dao;

import com.kairos.model.Admin;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public Admin buscarPorEmail(String email) {

        String sql = """
                SELECT id, email, senha
                FROM admins
                WHERE email = ?
                """;

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Admin(
                            rs.getInt("id"),
                            rs.getString("email"),
                            rs.getString("senha")
                    );
                }

                return null;
            }

        } catch (SQLException e) {

            throw new DAOException(
                    "Erro ao buscar administrador pelo email " + email,
                    e
            );
        }
    }
}