package com.kairos.dao;

import com.kairos.model.*;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReposicaoDAO {

    public Reposicao inserir(Reposicao reposicao) {

        String sql = """
                     INSERT INTO reposicoes (usuario_id, produto_id, data_reposicao,
                                             quantidade_reposto, motivo_reposicao,
                                             gondola_id)
                     VALUES (?, ?, ?, ?, ?, ?)
                     RETURNING id, usuario_id, produto_id, data_reposicao,
                               quantidade_reposto, motivo_reposicao, gondola_id
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicao.getUsuario().getId());
            statement.setInt(2, reposicao.getProduto().getId());
            statement.setTimestamp(3, reposicao.getDataReposicao());
            statement.setInt(4, reposicao.getQuantidadeReposto());
            statement.setString(5, reposicao.getMotivoReposicao());
            statement.setInt(6, reposicao.getGondola().getId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Reposicao(
                            rs.getInt("id"),
                            reposicao.getUsuario(),
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
                     SELECT r.id as rid, r.usuario_id, r.produto_id, r.data_reposicao,
                            r.quantidade_reposto, r.motivo_reposicao, r.gondola_id,
                            u.id as uid, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            u.empresa_id, p.id as pid, p.marca, p.nome, p.quantidade_estoque,
                            e.id as eid, e.cnpj, e.tipo_plano, g.id as gid, g.capacidade_maxima,
                            g.setor_id, s.id as sid, s.nome as setor_nome, s.categoria_setor
                     FROM reposicoes r
                     JOIN usuarios u ON u.id = r.usuario_id
                     JOIN produtos p ON p.id = r.produto_id
                     JOIN empresas e ON e.id = u.empresa_id
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
                            new Usuario(
                                    rs.getInt("uid"),
                                    rs.getString("cpf"),
                                    rs.getString("senha"),
                                    rs.getString("nome"),
                                    rs.getString("sobrenome"),
                                    rs.getDate("data_nascimento").toLocalDate(),
                                    rs.getString("cep"),
                                    TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                    rs.getString("email"),
                                    new Empresa(
                                            rs.getInt("eid"),
                                            rs.getString("cnpj"),
                                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                                    )
                            ),
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
                     SELECT r.id as rid, r.usuario_id, r.produto_id, r.data_reposicao,
                            r.quantidade_reposto, r.motivo_reposicao, r.gondola_id,
                            u.id as uid, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            u.empresa_id, p.id as pid, p.marca, p.nome, p.quantidade_estoque,
                            e.id as eid, e.cnpj, e.tipo_plano, g.id as gid, g.capacidade_maxima,
                            g.setor_id, s.id as sid, s.nome as setor_nome, s.categoria_setor
                     FROM reposicoes r
                     JOIN usuarios u ON u.id = r.usuario_id
                     JOIN produtos p ON p.id = r.produto_id
                     JOIN empresas e ON e.id = u.empresa_id
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
                            new Usuario(
                                    rs.getInt("uid"),
                                    rs.getString("cpf"),
                                    rs.getString("senha"),
                                    rs.getString("nome"),
                                    rs.getString("sobrenome"),
                                    rs.getDate("data_nascimento").toLocalDate(),
                                    rs.getString("cep"),
                                    TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                    rs.getString("email"),
                                    new Empresa(
                                            rs.getInt("eid"),
                                            rs.getString("cnpj"),
                                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                                    )
                            ),
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
                     SET usuario_id = ?,
                         produto_id = ?,
                         data_reposicao = ?,
                         quantidade_reposto = ?,
                         motivo_reposicao = ?,
                         gondola_id = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicao.getUsuario().getId());
            statement.setInt(2, reposicao.getProduto().getId());
            statement.setTimestamp(3, reposicao.getDataReposicao());
            statement.setInt(4, reposicao.getQuantidadeReposto());
            statement.setString(5, reposicao.getMotivoReposicao());
            statement.setInt(6, reposicao.getGondola().getId());

            statement.setInt(7, reposicao.getId());

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