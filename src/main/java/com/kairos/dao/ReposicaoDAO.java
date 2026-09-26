package com.kairos.dao;

import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.model.Reposicao;
import com.kairos.model.Setor;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReposicaoDAO {

    public Reposicao inserir(Reposicao reposicao) {

        String sql = """
                     INSERT INTO reposicoes (produto_id, data_reposicao,
                                             quantidade_reposto, motivo_reposicao,
                                             gondola_id)
                     VALUES (?, ?, ?, ?, ?)
                     RETURNING id, produto_id, data_reposicao,
                               quantidade_reposto, motivo_reposicao, gondola_id
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicao.getProduto().getId());
            statement.setTimestamp(2, reposicao.getDataReposicao());
            statement.setInt(3, reposicao.getQuantidadeReposto());
            statement.setString(4, reposicao.getMotivoReposicao());
            statement.setInt(5, reposicao.getGondola().getId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Reposicao(
                            rs.getInt("id"),
                            reposicao.getProduto(),
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            reposicao.getGondola()
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir reposição", e);
        }
    }

    public Reposicao buscarPorId(int id) {

        String sql = """
                     SELECT r.id AS rid,
                            r.produto_id,
                            r.data_reposicao,
                            r.quantidade_reposto,
                            r.motivo_reposicao,
                            r.gondola_id,
                            p.id AS pid,
                            p.marca,
                            p.nome,
                            p.quantidade_estoque,
                            g.id AS gid,
                            g.capacidade_maxima,
                            g.setor_id,
                            s.id AS sid,
                            s.nome AS setor_nome,
                            s.categoria_setor
                     FROM reposicoes r
                     JOIN produtos p ON p.id = r.produto_id
                     JOIN gondolas g ON g.id = r.gondola_id
                     JOIN setores s ON s.id = g.setor_id
                     WHERE r.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Reposicao(
                            rs.getInt("rid"),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            new Gondola(
                                    rs.getInt("gid"),
                                    rs.getInt("capacidade_maxima"),
                                    new Setor(
                                            rs.getInt("sid"),
                                            rs.getString("setor_nome"),
                                            rs.getString("categoria_setor")
                                    )
                            )
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar reposição por id", e);
        }
    }

    public List<Reposicao> listarTodos() {

        String sql = """
                     SELECT r.id AS rid,
                            r.produto_id,
                            r.data_reposicao,
                            r.quantidade_reposto,
                            r.motivo_reposicao,
                            r.gondola_id,
                            p.id AS pid,
                            p.marca,
                            p.nome,
                            p.quantidade_estoque,
                            g.id AS gid,
                            g.capacidade_maxima,
                            g.setor_id,
                            s.id AS sid,
                            s.nome AS setor_nome,
                            s.categoria_setor
                     FROM reposicoes r
                     JOIN produtos p ON p.id = r.produto_id
                     JOIN gondolas g ON g.id = r.gondola_id
                     JOIN setores s ON s.id = g.setor_id
                     ORDER BY r.id
                     """;

        List<Reposicao> reposicoes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Reposicao reposicao = new Reposicao(
                            rs.getInt("rid"),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            new Gondola(
                                    rs.getInt("gid"),
                                    rs.getInt("capacidade_maxima"),
                                    new Setor(
                                            rs.getInt("sid"),
                                            rs.getString("setor_nome"),
                                            rs.getString("categoria_setor")
                                    )
                            )
                    );

                    reposicoes.add(reposicao);
                }

                return reposicoes;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todas as reposições", e);
        }
    }

    public int atualizar(Reposicao reposicao) {

        String sql = """
                     UPDATE reposicoes
                     SET produto_id = ?,
                         data_reposicao = ?,
                         quantidade_reposto = ?,
                         motivo_reposicao = ?,
                         gondola_id = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicao.getProduto().getId());
            statement.setTimestamp(2, reposicao.getDataReposicao());
            statement.setInt(3, reposicao.getQuantidadeReposto());
            statement.setString(4, reposicao.getMotivoReposicao());
            statement.setInt(5, reposicao.getGondola().getId());
            statement.setInt(6, reposicao.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar reposição", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM reposicoes
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir reposição", e);
        }
    }
}