package com.kairos.dao;

import com.kairos.model.Gondola;
import com.kairos.model.HistoricoReposicao;
import com.kairos.model.Produto;
import com.kairos.model.Reposicao;
import com.kairos.model.Setor;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReposicaoDAO {

    private final HistoricoUsuarioReposicaoDAO historicoUsuarioReposicaoDAO;

    public ReposicaoDAO() {
        this.historicoUsuarioReposicaoDAO = new HistoricoUsuarioReposicaoDAO();
    }

    // Insere uma nova reposição
    public Reposicao inserir(Reposicao reposicao) {

        String sql = """
                     INSERT INTO reposicoes (produto_id, data_reposicao,
                                             quantidade_reposto, motivo_reposicao, gondola_id)
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

    // Busca uma reposição pelo ID
    public Reposicao buscarPorId(int id) {

        String sql = """
                     SELECT r.id AS rid, r.produto_id, r.data_reposicao,
                            r.quantidade_reposto, r.motivo_reposicao, r.gondola_id,
                            p.id AS pid, p.marca, p.nome, p.quantidade_estoque,
                            g.id AS gid, g.capacidade_maxima, g.setor_id,
                            s.id AS sid, s.nome AS setor_nome, s.categoria_setor,
                            h.id AS historico_id, h.reposicao_id AS historico_reposicao_id,
                            h.produto_marca AS historico_produto_marca,
                            h.produto_nome AS historico_produto_nome,
                            h.id_gondola AS historico_gondola_id
                     FROM reposicoes r
                     LEFT JOIN produtos p ON p.id = r.produto_id
                     LEFT JOIN gondolas g ON g.id = r.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN historico_reposicoes h ON h.reposicao_id = r.id
                     WHERE r.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Produto produto = null;

                    if (rs.getObject("pid") != null) {

                        produto = new Produto(
                                rs.getInt("pid"),
                                rs.getString("marca"),
                                rs.getString("nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Gondola gondola = null;

                    if (rs.getObject("gid") != null) {

                        Setor setor = null;

                        if (rs.getObject("sid") != null) {

                            setor = new Setor(
                                    rs.getInt("sid"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gid"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    Reposicao reposicao = new Reposicao(
                            rs.getInt("rid"),
                            produto,
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            gondola
                    );

                    if (rs.getObject("historico_id") != null) {

                        HistoricoReposicao historico = new HistoricoReposicao(
                                rs.getInt("historico_id"),
                                rs.getInt("historico_reposicao_id"),
                                rs.getString("historico_produto_marca"),
                                rs.getString("historico_produto_nome"),
                                rs.getInt("historico_gondola_id")
                        );

                        reposicao.setHistoricoReposicao(historico);
                    }

                    reposicao.setHistoricoUsuariosReposicoes(historicoUsuarioReposicaoDAO.buscarPorReposicaoId(reposicao.getId()));

                    return reposicao;
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar reposição por id", e);
        }
    }

    // Lista todas as reposições cadastradas
    public List<Reposicao> listarTodos() {

        String sql = """
                     SELECT r.id AS rid, r.produto_id, r.data_reposicao,
                            r.quantidade_reposto, r.motivo_reposicao, r.gondola_id,
                            p.id AS pid, p.marca, p.nome, p.quantidade_estoque,
                            g.id AS gid, g.capacidade_maxima, g.setor_id,
                            s.id AS sid, s.nome AS setor_nome, s.categoria_setor,
                            h.id AS historico_id, h.reposicao_id AS historico_reposicao_id,
                            h.produto_marca AS historico_produto_marca,
                            h.produto_nome AS historico_produto_nome,
                            h.id_gondola AS historico_gondola_id
                     FROM reposicoes r
                     LEFT JOIN produtos p ON p.id = r.produto_id
                     LEFT JOIN gondolas g ON g.id = r.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN historico_reposicoes h ON h.reposicao_id = r.id
                     ORDER BY r.id
                     """;

        List<Reposicao> reposicoes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Produto produto = null;

                    if (rs.getObject("pid") != null) {

                        produto = new Produto(
                                rs.getInt("pid"),
                                rs.getString("marca"),
                                rs.getString("nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Gondola gondola = null;

                    if (rs.getObject("gid") != null) {

                        Setor setor = null;

                        if (rs.getObject("sid") != null) {

                            setor = new Setor(
                                    rs.getInt("sid"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gid"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    Reposicao reposicao = new Reposicao(
                            rs.getInt("rid"),
                            produto,
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            gondola
                    );

                    if (rs.getObject("historico_id") != null) {

                        HistoricoReposicao historico = new HistoricoReposicao(
                                rs.getInt("historico_id"),
                                rs.getInt("historico_reposicao_id"),
                                rs.getString("historico_produto_marca"),
                                rs.getString("historico_produto_nome"),
                                rs.getInt("historico_gondola_id")
                        );

                        reposicao.setHistoricoReposicao(historico);
                    }

                    reposicao.setHistoricoUsuariosReposicoes(historicoUsuarioReposicaoDAO.buscarPorReposicaoId(reposicao.getId()));

                    reposicoes.add(reposicao);
                }

                return reposicoes;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todas as reposições", e);
        }
    }

    // Atualiza os dados de uma reposição
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

    // Exclui uma reposição pelo ID
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

    // Pesquisa reposições por seus dados e informações relacionadas
    public List<Reposicao> pesquisar(String pesquisa) {

        String sql = """
                     SELECT r.id AS reposicao_id, r.produto_id, r.data_reposicao,
                            r.quantidade_reposto, r.motivo_reposicao, r.gondola_id,
                            p.id AS produto_id, p.marca, p.nome, p.quantidade_estoque,
                            g.id AS gondola_id, g.capacidade_maxima, g.setor_id,
                            s.id AS setor_id, s.nome AS setor_nome, s.categoria_setor,
                            h.id AS historico_id, h.reposicao_id AS historico_reposicao_id,
                            h.produto_marca AS historico_produto_marca,
                            h.produto_nome AS historico_produto_nome,
                            h.id_gondola AS historico_gondola_id
                     FROM reposicoes r
                     LEFT JOIN produtos p ON p.id = r.produto_id
                     LEFT JOIN gondolas g ON g.id = r.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN historico_reposicoes h ON h.reposicao_id = r.id
                     WHERE CAST(r.id AS TEXT) ILIKE ?
                        OR CAST(r.produto_id AS TEXT) ILIKE ?
                        OR COALESCE(p.marca, '') ILIKE ?
                        OR COALESCE(p.nome, '') ILIKE ?
                        OR COALESCE(h.produto_marca, '') ILIKE ?
                        OR COALESCE(h.produto_nome, '') ILIKE ?
                        OR TO_CHAR(r.data_reposicao, 'DD/MM/YYYY') ILIKE ?
                        OR CAST(r.quantidade_reposto AS TEXT) ILIKE ?
                        OR r.motivo_reposicao ILIKE ?
                        OR CAST(r.gondola_id AS TEXT) ILIKE ?
                        OR COALESCE(CAST(g.capacidade_maxima AS TEXT), '') ILIKE ?
                        OR COALESCE(s.nome, '') ILIKE ?
                        OR COALESCE(s.categoria_setor, '') ILIKE ?
                        OR COALESCE(CAST(h.id_gondola AS TEXT), '') ILIKE ?
                     ORDER BY r.id
                     """;

        List<Reposicao> reposicoes = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 14; i++) {
                statement.setString(i, pesquisa);
            }

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Produto produto = null;

                    if (rs.getObject("produto_id") != null) {

                        produto = new Produto(
                                rs.getInt("produto_id"),
                                rs.getString("marca"),
                                rs.getString("nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Gondola gondola = null;

                    if (rs.getObject("gondola_id") != null) {

                        Setor setor = null;

                        if (rs.getObject("setor_id") != null) {

                            setor = new Setor(
                                    rs.getInt("setor_id"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gondola_id"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    Reposicao reposicao = new Reposicao(
                            rs.getInt("reposicao_id"),
                            produto,
                            rs.getTimestamp("data_reposicao"),
                            rs.getInt("quantidade_reposto"),
                            rs.getString("motivo_reposicao"),
                            gondola
                    );

                    if (rs.getObject("historico_id") != null) {

                        HistoricoReposicao historico = new HistoricoReposicao(
                                rs.getInt("historico_id"),
                                rs.getInt("historico_reposicao_id"),
                                rs.getString("historico_produto_marca"),
                                rs.getString("historico_produto_nome"),
                                rs.getInt("historico_gondola_id")
                        );

                        reposicao.setHistoricoReposicao(historico);
                    }

                    reposicao.setHistoricoUsuariosReposicoes(historicoUsuarioReposicaoDAO.buscarPorReposicaoId(reposicao.getId()));

                    reposicoes.add(reposicao);
                }

                return reposicoes;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao pesquisar reposições", e);
        }
    }
}
