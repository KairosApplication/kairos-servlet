package com.kairos.dao;

import com.kairos.model.HistoricoUsuarioReposicao;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoUsuarioReposicaoDAO {

    public HistoricoUsuarioReposicao salvar(int reposicaoId, int usuarioId) {

        String sql = """
                     INSERT INTO historico_usuarios_reposicoes
                     (
                         reposicao_id,
                         usuario_id,
                         usuario_nome,
                         usuario_sobrenome,
                         empresa_nome,
                         vinculo_ativo
                     )
                     SELECT
                         ?,
                         u.id,
                         u.nome,
                         u.sobrenome,
                         e.nome,
                         TRUE
                     FROM usuarios u
                     JOIN empresas e
                       ON e.id = u.empresa_id
                     WHERE u.id = ?
                     ON CONFLICT (reposicao_id, usuario_id)
                     DO UPDATE SET
                         vinculo_ativo = TRUE
                     RETURNING
                         id,
                         reposicao_id,
                         usuario_id,
                         usuario_nome,
                         usuario_sobrenome,
                         empresa_nome,
                         vinculo_ativo
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicaoId);
            statement.setInt(2, usuarioId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new HistoricoUsuarioReposicao(
                            rs.getInt("id"),
                            rs.getInt("reposicao_id"),
                            rs.getInt("usuario_id"),
                            rs.getString("usuario_nome"),
                            rs.getString("usuario_sobrenome"),
                            rs.getString("empresa_nome"),
                            rs.getBoolean("vinculo_ativo")
                    );
                }

                return null;
            }

        } catch (SQLException e) {

            throw new DAOException(
                    "Erro ao salvar histórico do usuário na reposição",
                    e
            );
        }
    }


    public void desativarVinculo(int reposicaoId, int usuarioId) {

        String sql = """
                     UPDATE historico_usuarios_reposicoes
                     SET vinculo_ativo = FALSE
                     WHERE reposicao_id = ?
                       AND usuario_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicaoId);
            statement.setInt(2, usuarioId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new DAOException(
                    "Erro ao desativar vínculo do usuário na reposição",
                    e
            );
        }
    }


    public void atualizarDadosUsuario(int usuarioId) {

        String sql = """
                     UPDATE historico_usuarios_reposicoes h
                     SET
                         usuario_nome = u.nome,
                         usuario_sobrenome = u.sobrenome,
                         empresa_nome = e.nome
                     FROM usuarios u
                     JOIN empresas e
                       ON e.id = u.empresa_id
                     WHERE h.usuario_id = u.id
                       AND h.usuario_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuarioId);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new DAOException(
                    "Erro ao atualizar dados do usuário no histórico das reposições",
                    e
            );
        }
    }


    public List<HistoricoUsuarioReposicao> buscarPorReposicaoId(
            int reposicaoId
    ) {

        String sql = """
                     SELECT
                         id,
                         reposicao_id,
                         usuario_id,
                         usuario_nome,
                         usuario_sobrenome,
                         empresa_nome,
                         vinculo_ativo
                     FROM historico_usuarios_reposicoes
                     WHERE reposicao_id = ?
                       AND vinculo_ativo = TRUE
                     ORDER BY id
                     """;

        List<HistoricoUsuarioReposicao> historicos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicaoId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    historicos.add(
                            new HistoricoUsuarioReposicao(
                                    rs.getInt("id"),
                                    rs.getInt("reposicao_id"),
                                    rs.getInt("usuario_id"),
                                    rs.getString("usuario_nome"),
                                    rs.getString("usuario_sobrenome"),
                                    rs.getString("empresa_nome"),
                                    rs.getBoolean("vinculo_ativo")
                            )
                    );
                }
            }

            return historicos;

        } catch (SQLException e) {

            throw new DAOException(
                    "Erro ao buscar histórico de usuários da reposição",
                    e
            );
        }
    }
}