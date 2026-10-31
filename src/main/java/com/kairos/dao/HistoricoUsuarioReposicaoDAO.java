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

    // Salva o histórico de um usuário vinculado a uma reposição
    public HistoricoUsuarioReposicao salvar(int reposicaoId, int usuarioId) {

        String sql = """
                     INSERT INTO historicos_reposicoes_usuarios (
                         reposicao_id, id_usuario, usuario_nome,
                         usuario_sobrenome, empresa_nome, vinculo_ativo
                     )
                     SELECT ?, u.id, u.nome, u.sobrenome, e.nome, TRUE
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE u.id = ?
                     ON CONFLICT (reposicao_id, id_usuario)
                     DO UPDATE SET vinculo_ativo = TRUE
                     RETURNING id, reposicao_id, id_usuario, usuario_nome,
                               usuario_sobrenome, empresa_nome, vinculo_ativo
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
                            rs.getInt("id_usuario"),
                            rs.getString("usuario_nome"),
                            rs.getString("usuario_sobrenome"),
                            rs.getString("empresa_nome"),
                            rs.getBoolean("vinculo_ativo")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao salvar histórico do usuário na reposição", e);
        }
    }

    // Desativa o vínculo de um usuário com uma reposição
    public void desativarVinculo(int reposicaoId, int usuarioId) {

        String sql = """
                     UPDATE historicos_reposicoes_usuarios
                     SET vinculo_ativo = FALSE
                     WHERE reposicao_id = ?
                       AND id_usuario = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicaoId);
            statement.setInt(2, usuarioId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao desativar vínculo do usuário na reposição", e);
        }
    }

    // Atualiza os dados do usuário no histórico das reposições
    public void atualizarDadosUsuario(int usuarioId) {

        String sql = """
                     UPDATE historicos_reposicoes_usuarios h
                     SET usuario_nome = u.nome,
                         usuario_sobrenome = u.sobrenome,
                         empresa_nome = e.nome
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE h.id_usuario = u.id
                       AND h.id_usuario = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuarioId);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar dados do usuário no histórico das reposições", e);
        }
    }

    // Busca os históricos ativos dos usuários vinculados a uma reposição
    public List<HistoricoUsuarioReposicao> buscarPorReposicaoId(int reposicaoId) {

        String sql = """
                     SELECT id, reposicao_id, id_usuario, usuario_nome,
                            usuario_sobrenome, empresa_nome, vinculo_ativo
                     FROM historicos_reposicoes_usuarios
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
                                    rs.getInt("id_usuario"),
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
            throw new DAOException("Erro ao buscar histórico de usuários da reposição", e);
        }
    }
}
