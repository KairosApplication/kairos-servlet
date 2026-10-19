
package com.kairos.dao;

import com.kairos.model.HistoricoAlerta;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HistoricoAlertaDAO {

    // Salva o histórico de um alerta, evitando registros duplicados
    public HistoricoAlerta salvar(HistoricoAlerta historico) {

        String sql = """
                     INSERT INTO alertas_historicos (alerta_id, id_usuario, id_gondola, id_produto)
                     VALUES (?, ?, ?, ?)
                     ON CONFLICT (alerta_id) DO NOTHING
                     RETURNING id, alerta_id, id_usuario, id_gondola, id_produto
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
                            rs.getInt("id_usuario"),
                            rs.getInt("id_gondola"),
                            rs.getInt("id_produto")
                    );
                }

                return buscarPorAlertaId(historico.getAlertaId());
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir histórico do alerta", e);
        }
    }

    // Busca o histórico associado a um alerta pelo ID
    public HistoricoAlerta buscarPorAlertaId(int alertaId) {

        String sql = """
                     SELECT id, alerta_id, id_usuario, id_gondola, id_produto
                     FROM alertas_historicos
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
                            rs.getInt("id_usuario"),
                            rs.getInt("id_gondola"),
                            rs.getInt("id_produto")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico do alerta", e);
        }
    }
}
