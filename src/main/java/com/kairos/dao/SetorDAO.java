package com.kairos.dao;

import com.kairos.model.Setor;
import com.kairos.utils.ConnectionFactory;
import com.kairos.utils.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {

    public void inserir(Setor setor) {

        String sql = """
                     INSERT INTO setores (nome, categoria_setor)
                     VALUES (?, ?)
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, setor.getNome());
            statement.setString(2, setor.getCategoria());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir setor", e);
        }
    }

    public Setor buscarPorId(int id) {

        String sql = """
                     SELECT id, nome, categoria
                     FROM setores
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    Setor setor = new Setor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("categoria")
                    );
                    return setor;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar setor por id", e);
        }
    }

    public List<Setor> listarTodos() {

        String sql = """
                     SELECT id, nome, categoria_setor
                     FROM setores
                     """;

        List<Setor> setores = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Setor setor = new Setor(
                                        rs.getInt("id"),
                                            rs.getString("nome"),
                                            rs.getString("categoria_setor")
                    );
                    setores.add(setor);
                }
                return setores;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar setores", e);
        }
    }

    public int atualizar(Setor setor) {

        String sql = """
                     UPDATE setores
                     SET nome = ?,
                         categoria_setor = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "nome");
            statement.setString(2, "categoria_setor");

            statement.setInt(3, setor.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM setores
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir setor", e);
        }
    }

    public boolean existePorNome(String nome) {

        String sql = """
                     SELECT 1
                     FROM setores
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

           try (ResultSet rs = statement.executeQuery()) {
               return rs.next();
           }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar setor por nome", e);
        }
    }
}
