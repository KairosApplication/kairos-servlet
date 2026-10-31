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

    // Busca um histórico de compra de produto pelo ID
    public HistoricoCompraProduto buscarPorId(int id) {

        String sql = """
                     SELECT h.id, h.compra_produto_id, cp.compra_id,
                            h.id_produto AS produto_id, h.produto_marca, h.produto_nome
                     FROM historico_compras_produtos h
                     JOIN compras_produtos cp ON cp.id = h.compra_produto_id
                     WHERE h.id = ?
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

    // Busca o histórico associado a um produto de uma compra
    public HistoricoCompraProduto buscarPorCompraProdutoId(int compraProdutoId) {

        String sql = """
                     SELECT h.id, h.compra_produto_id, cp.compra_id,
                            h.id_produto AS produto_id, h.produto_marca, h.produto_nome
                     FROM historico_compras_produtos h
                     JOIN compras_produtos cp ON cp.id = h.compra_produto_id
                     WHERE h.compra_produto_id = ?
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

    // Lista todos os históricos de produtos das compras
    public List<HistoricoCompraProduto> listarTodos() {

        String sql = """
                     SELECT h.id, h.compra_produto_id, cp.compra_id,
                            h.id_produto AS produto_id, h.produto_marca, h.produto_nome
                     FROM historico_compras_produtos h
                     JOIN compras_produtos cp ON cp.id = h.compra_produto_id
                     ORDER BY h.id
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

    // Lista os históricos dos produtos vinculados a uma compra
    public List<HistoricoCompraProduto> listarPorCompraId(int compraId) {

        String sql = """
                     SELECT h.id, h.compra_produto_id, cp.compra_id,
                            h.id_produto AS produto_id, h.produto_marca, h.produto_nome
                     FROM historico_compras_produtos h
                     JOIN compras_produtos cp ON cp.id = h.compra_produto_id
                     WHERE cp.compra_id = ?
                     ORDER BY h.id
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
