package com.kairos.dao;

import com.kairos.model.HistoricoReposicao;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoReposicaoDAO {

    // Salva o histórico de uma reposição
    public HistoricoReposicao salvar(HistoricoReposicao historico) {

        String sql = """
                     INSERT INTO historico_reposicoes (reposicao_id, produto_marca, produto_nome, id_gondola)
                     VALUES (?, ?, ?, ?)
                     RETURNING id, reposicao_id, produto_marca, produto_nome, id_gondola
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, historico.getReposicaoId());
            statement.setString(2, historico.getProdutoMarca());
            statement.setString(3, historico.getProdutoNome());
            statement.setInt(4, historico.getGondolaId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new HistoricoReposicao(
                            rs.getInt("id"),
                            rs.getInt("reposicao_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome"),
                            rs.getInt("id_gondola")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir histórico da reposição", e);
        }
    }

    // Busca o histórico associado a uma reposição pelo ID
    public HistoricoReposicao buscarPorReposicaoId(int reposicaoId) {

        String sql = """
                     SELECT id, reposicao_id, produto_marca, produto_nome, id_gondola
                     FROM historico_reposicoes
                     WHERE reposicao_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicaoId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new HistoricoReposicao(
                            rs.getInt("id"),
                            rs.getInt("reposicao_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome"),
                            rs.getInt("id_gondola")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico da reposição", e);
        }
    }
}
