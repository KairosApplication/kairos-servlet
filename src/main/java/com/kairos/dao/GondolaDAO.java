package com.kairos.dao;

import com.kairos.model.Gondola;
import com.kairos.model.Setor;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GondolaDAO {

    public Gondola inserir(Gondola gondola) {

        String sql = """
                     INSERT INTO gondolas (capacidade_maxima, setor_id)
                     VALUES (?, ?)
                     RETURNING id as gondola_id, capacidade_maxima
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gondola.getCapacidadeMaxima());
            statement.setInt(2, gondola.getSetor().getId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Gondola(
                            rs.getInt("gondola_id"),
                            rs.getInt("capacidade_maxima"),
                            gondola.getSetor()
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir gondola", e);
        }
    }

    public Gondola buscarPorId(int id) {

        String sql = """
                     SELECT g.id as gondola_id, g.capacidade_maxima, g.setor_id,
                            s.id as setor_id, s.nome, s.categoria_setor
                     FROM gondolas g
                     JOIN setores s ON s.id = g.setor_id
                     WHERE g.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Gondola(
                            rs.getInt("gondola_id"),
                            rs.getInt("capacidade_maxima"),
                            new Setor(
                                    rs.getInt("setor_id"),
                                    rs.getString("nome"),
                                    rs.getString("categoria_setor")
                            )
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar gondola por id", e);
        }
    }

    public List<Gondola> listarTodos() {

        String sql = """
                         SELECT g.id as gondola_id, g.capacidade_maxima, g.setor_id,
                                s.id as setor_id, s.nome, s.categoria_setor
                         FROM gondolas g
                         JOIN setores s ON s.id = g.setor_id
                         """;

        List<Gondola> gondolas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Gondola gondola = new Gondola(
                            rs.getInt("gondola_id"),
                            rs.getInt("capacidade_maxima"),
                            new Setor(
                                    rs.getInt("setor_id"),
                                    rs.getString("nome"),
                                    rs.getString("categoria_setor")
                            )
                    );
                    gondolas.add(gondola);
                }
                return gondolas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar todas as gôndolas");
        }
    }

    public int atualizar(Gondola gondola) {

        String sql = """
                     UPDATE gondolas
                     SET capacidade_maxima = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gondola.getCapacidadeMaxima());
            statement.setInt(2, gondola.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar gôndola", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM gondolas
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir gôndola", e);
        }
    }
}
