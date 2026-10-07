package com.kairos.dao;

import com.kairos.model.HistoricoReposicao;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoReposicaoDAO {

    public HistoricoReposicao salvar(HistoricoReposicao historico) {

        String sql = """
                     INSERT INTO historico_reposicoes
                     (reposicao_id, produto_marca, produto_nome, gondola_id)
                     VALUES (?, ?, ?, ?)
                     RETURNING id, reposicao_id, produto_marca,
                               produto_nome, gondola_id
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
                            rs.getInt("gondola_id")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir histórico da reposição", e);
        }
    }

    public HistoricoReposicao buscarPorReposicaoId(int reposicaoId) {

        String sql = """
                     SELECT id,
                            reposicao_id,
                            produto_marca,
                            produto_nome,
                            gondola_id
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
                            rs.getInt("gondola_id")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico da reposição", e);
        }
    }
}