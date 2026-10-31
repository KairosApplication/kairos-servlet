package com.kairos.dao;

import com.kairos.model.Setor;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SetorDAO {

    // Insere um novo setor
    public Setor inserir(Setor setor) {

        String sql = """
                     INSERT INTO setores (nome, categoria_setor)
                     VALUES (?, ?)
                     RETURNING id, nome, categoria_setor
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, setor.getNome());
            statement.setString(2, setor.getCategoria());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Setor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("categoria_setor")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir setor", e);
        }
    }

    // Busca um setor pelo ID
    public Setor buscarPorId(int id) {

        String sql = """
                     SELECT id, nome, categoria_setor
                     FROM setores
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Setor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("categoria_setor")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar setor por ID", e);
        }
    }

    // Busca um setor pelo nome
    public Setor buscarPorNome(String nome) {

        String sql = """
                     SELECT id, nome, categoria_setor
                     FROM setores
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Setor(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("categoria_setor")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar setor pelo nome", e);
        }
    }

    // Lista todos os setores cadastrados
    public List<Setor> listarTodos() {

        String sql = """
                     SELECT id, nome, categoria_setor
                     FROM setores
                     ORDER BY id
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

    // Atualiza os dados de um setor
    public int atualizar(Setor setor) {

        String sql = """
                     UPDATE setores
                     SET nome = ?,
                         categoria_setor = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, setor.getNome());
            statement.setString(2, setor.getCategoria());
            statement.setInt(3, setor.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar setor", e);
        }
    }

    // Exclui um setor pelo ID
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

    // Verifica se existe um setor cadastrado com o nome informado
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

    // Verifica se o nome pertence a outro setor
    public boolean existePorNomeExcetoId(String nome, int id) {

        String sql = """
                     SELECT 1
                     FROM setores
                     WHERE nome = ? AND id <> ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);
            statement.setInt(2, id);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar nome de outro setor", e);
        }
    }

    // Pesquisa setores pelo ID, nome ou categoria
    public List<Setor> pesquisar(String pesquisa) {

        String sql = """
                     SELECT id, nome, categoria_setor
                     FROM setores
                     WHERE CAST(id AS TEXT) ILIKE ?
                        OR nome ILIKE ?
                        OR categoria_setor ILIKE ?
                     ORDER BY id
                     """;

        List<Setor> setores = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 3; i++) {
                statement.setString(i, pesquisa);
            }

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
            throw new DAOException("Erro ao listar setores por pesquisa", e);
        }
    }
}
