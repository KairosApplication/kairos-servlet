package com.kairos.dao;

import com.kairos.model.HistoricoAlerta;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoAlertaDAO {

    public HistoricoAlerta salvar(HistoricoAlerta historico) {

        String sql = """
                     INSERT INTO historico_alertas
                     (alerta_id, usuario_id, gondola_id, produto_id)
                     VALUES (?, ?, ?, ?)
                     ON CONFLICT (alerta_id) DO NOTHING
                     RETURNING id, alerta_id, usuario_id,
                               gondola_id, produto_id
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, historico.getAlertaId());
            statement.setInt(2, historico.getUsuarioId());
            statement.setInt(3, historico.getGondolaId());
            statement.setInt(4, historico.getProdutoId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new HistoricoAlerta(
                            rs.getInt("id"),
                            rs.getInt("alerta_id"),
                            rs.getInt("usuario_id"),
                            rs.getInt("gondola_id"),
                            rs.getInt("produto_id")
                    );
                }

                return buscarPorAlertaId(historico.getAlertaId());
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir histórico do alerta", e);
        }
    }

    public HistoricoAlerta buscarPorAlertaId(int alertaId) {

        String sql = """
                     SELECT id,
                            alerta_id,
                            usuario_id,
                            gondola_id,
                            produto_id
                     FROM historico_alertas
                     WHERE alerta_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alertaId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new HistoricoAlerta(
                            rs.getInt("id"),
                            rs.getInt("alerta_id"),
                            rs.getInt("usuario_id"),
                            rs.getInt("gondola_id"),
                            rs.getInt("produto_id")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico do alerta", e);
        }
    }
}