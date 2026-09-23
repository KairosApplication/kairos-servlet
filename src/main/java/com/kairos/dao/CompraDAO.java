package com.kairos.dao;

import com.kairos.model.Compra;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CompraDAO {

    public Compra inserir(Compra compra) {

        String sql = """
                     INSERT INTO compras (data_compra)
                     VALUES (?)
                     RETURNING id, data_compra;
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, compra.getDataCompra());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Compra(
                            rs.getTimestamp("data_compra")
                    );

                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir a compra", e);
        }
    }

    public Compra buscarPorId(int id) {

        String sql = """
                     SELECT id, data_compra
                     FROM compras
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Compra(
                            rs.getInt("id"),
                            rs.getTimestamp("data_compra")
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar compra pelo id " + id, e);
        }
    }

    public List<Compra> listarTodos() {

        String sql = """
                     SELECT id, data_compra
                     FROM compras
                     """;

        List<Compra> compras = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    Compra compra = new Compra(
                            rs.getInt("id"),
                            rs.getTimestamp("data_compra")
                    );
                    compras.add(compra);
                }
                return compras;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar compras", e);
        }
    }

    public int atualizar(Compra compra) {

        String sql = """
                     UPDATE compras
                     SET data_compra = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setTimestamp(1, compra.getDataCompra());
            statement.setInt(2, compra.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar compra", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM compras
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir compra", e);
        }
    }
}
