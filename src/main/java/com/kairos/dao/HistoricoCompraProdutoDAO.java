
package com.kairos.dao;

import com.kairos.model.HistoricoCompraProduto;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HistoricoCompraProdutoDAO {

    public HistoricoCompraProduto buscarPorId(int id) {

        String sql = """
                     SELECT id, compra_produto_id, compra_id,
                            produto_id, produto_marca, produto_nome
                     FROM historico_compras_produtos
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new HistoricoCompraProduto(
                            rs.getInt("id"),
                            rs.getInt("compra_produto_id"),
                            rs.getInt("compra_id"),
                            rs.getInt("produto_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico da compra por id", e);
        }
    }

    public HistoricoCompraProduto buscarPorCompraProdutoId(int compraProdutoId) {

        String sql = """
                     SELECT id, compra_produto_id, compra_id,
                            produto_id, produto_marca, produto_nome
                     FROM historico_compras_produtos
                     WHERE compra_produto_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraProdutoId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new HistoricoCompraProduto(
                            rs.getInt("id"),
                            rs.getInt("compra_produto_id"),
                            rs.getInt("compra_id"),
                            rs.getInt("produto_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar histórico por compra produto", e);
        }
    }

    public List<HistoricoCompraProduto> listarTodos() {

        String sql = """
                     SELECT id, compra_produto_id, compra_id,
                            produto_id, produto_marca, produto_nome
                     FROM historico_compras_produtos
                     ORDER BY id
                     """;

        List<HistoricoCompraProduto> historicos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    HistoricoCompraProduto historico = new HistoricoCompraProduto(
                            rs.getInt("id"),
                            rs.getInt("compra_produto_id"),
                            rs.getInt("compra_id"),
                            rs.getInt("produto_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome")
                    );

                    historicos.add(historico);
                }

                return historicos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar histórico de compras", e);
        }
    }

    public List<HistoricoCompraProduto> listarPorCompraId(int compraId) {

        String sql = """
                     SELECT id, compra_produto_id, compra_id,
                            produto_id, produto_marca, produto_nome
                     FROM historico_compras_produtos
                     WHERE compra_id = ?
                     ORDER BY id
                     """;

        List<HistoricoCompraProduto> historicos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    HistoricoCompraProduto historico = new HistoricoCompraProduto(
                            rs.getInt("id"),
                            rs.getInt("compra_produto_id"),
                            rs.getInt("compra_id"),
                            rs.getInt("produto_id"),
                            rs.getString("produto_marca"),
                            rs.getString("produto_nome")
                    );

                    historicos.add(historico);
                }

                return historicos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar histórico dos produtos da compra", e);
        }
    }
}
