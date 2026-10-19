
package com.kairos.dao;

import com.kairos.model.Compra;
import com.kairos.model.CompraProduto;
import com.kairos.model.HistoricoCompraProduto;
import com.kairos.model.Produto;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CompraProdutoDAO {

    // Insere um produto em uma compra e registra seu histórico
    public CompraProduto inserir(CompraProduto compraProduto) {

        String sql = """
                     INSERT INTO compras_produtos (produto_id, compra_id, quantidade_item)
                     VALUES (?, ?, ?)
                     RETURNING id, produto_id, compra_id, quantidade_item
                     """;

        String sqlHistorico = """
                              INSERT INTO historico_compras_produtos (
                                  compra_produto_id, id_produto,
                                  produto_marca, produto_nome
                              )
                              SELECT ?, p.id, p.marca, p.nome
                              FROM produtos p
                              WHERE p.id = ?
                              ON CONFLICT (compra_produto_id) DO NOTHING
                              """;

        try (Connection connection = ConnectionFactory.getConnection()) {

            connection.setAutoCommit(false);

            try {

                CompraProduto resultado = null;

                try (PreparedStatement statement = connection.prepareStatement(sql)) {

                    statement.setInt(1, compraProduto.getProduto().getId());
                    statement.setInt(2, compraProduto.getCompra().getId());
                    statement.setInt(3, compraProduto.getQuantidadeItem());

                    try (ResultSet rs = statement.executeQuery()) {

                        if (rs.next()) {

                            resultado = new CompraProduto(
                                    rs.getInt("id"),
                                    compraProduto.getProduto(),
                                    compraProduto.getCompra(),
                                    rs.getInt("quantidade_item")
                            );
                        }
                    }
                }

                if (resultado == null) {
                    connection.rollback();
                    return null;
                }

                try (PreparedStatement statement = connection.prepareStatement(sqlHistorico)) {

                    statement.setInt(1, resultado.getId());
                    statement.setInt(2, resultado.getProduto().getId());

                    statement.executeUpdate();
                }

                connection.commit();

                return resultado;

            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir produto na compra", e);
        }
    }

    // Busca um produto vinculado a uma compra pelo ID
    public CompraProduto buscarPorId(int id) {

        String sql = """
                     SELECT cp.id AS cpid, cp.produto_id, cp.compra_id,
                            cp.quantidade_item,
                            p.id AS pid, p.marca, p.nome, p.quantidade_estoque,
                            h.id AS historico_id,
                            h.compra_produto_id AS historico_compra_produto_id,
                            cp.compra_id AS historico_compra_id,
                            h.id_produto AS historico_produto_id,
                            h.produto_marca AS historico_produto_marca,
                            h.produto_nome AS historico_produto_nome,
                            c.id AS cid, c.data_compra
                     FROM compras_produtos cp
                     LEFT JOIN produtos p ON p.id = cp.produto_id
                     LEFT JOIN historico_compras_produtos h
                         ON h.compra_produto_id = cp.id
                     JOIN compras c ON c.id = cp.compra_id
                     WHERE cp.id = ?
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

                    CompraProduto compraProduto = new CompraProduto(
                            rs.getInt("cpid"),
                            produto,
                            new Compra(
                                    rs.getInt("cid"),
                                    rs.getTimestamp("data_compra")
                            ),
                            rs.getInt("quantidade_item")
                    );

                    if (rs.getObject("historico_id") != null) {

                        HistoricoCompraProduto historico = new HistoricoCompraProduto(
                                rs.getInt("historico_id"),
                                rs.getInt("historico_compra_produto_id"),
                                rs.getInt("historico_compra_id"),
                                rs.getInt("historico_produto_id"),
                                rs.getString("historico_produto_marca"),
                                rs.getString("historico_produto_nome")
                        );

                        compraProduto.setHistoricoCompraProduto(historico);
                    }

                    return compraProduto;
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto da compra por id", e);
        }
    }

    // Lista todos os produtos vinculados às compras
    public List<CompraProduto> listarTodos() {

        String sql = """
                     SELECT cp.id AS cpid, cp.produto_id, cp.compra_id,
                            cp.quantidade_item,
                            p.id AS pid, p.marca, p.nome, p.quantidade_estoque,
                            h.id AS historico_id,
                            h.compra_produto_id AS historico_compra_produto_id,
                            cp.compra_id AS historico_compra_id,
                            h.id_produto AS historico_produto_id,
                            h.produto_marca AS historico_produto_marca,
                            h.produto_nome AS historico_produto_nome,
                            c.id AS cid, c.data_compra
                     FROM compras_produtos cp
                     LEFT JOIN produtos p ON p.id = cp.produto_id
                     LEFT JOIN historico_compras_produtos h
                         ON h.compra_produto_id = cp.id
                     JOIN compras c ON c.id = cp.compra_id
                     ORDER BY cp.id
                     """;

        List<CompraProduto> comprasProdutos = new ArrayList<>();

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

                    CompraProduto compraProduto = new CompraProduto(
                            rs.getInt("cpid"),
                            produto,
                            new Compra(
                                    rs.getInt("cid"),
                                    rs.getTimestamp("data_compra")
                            ),
                            rs.getInt("quantidade_item")
                    );

                    if (rs.getObject("historico_id") != null) {

                        HistoricoCompraProduto historico = new HistoricoCompraProduto(
                                rs.getInt("historico_id"),
                                rs.getInt("historico_compra_produto_id"),
                                rs.getInt("historico_compra_id"),
                                rs.getInt("historico_produto_id"),
                                rs.getString("historico_produto_marca"),
                                rs.getString("historico_produto_nome")
                        );

                        compraProduto.setHistoricoCompraProduto(historico);
                    }

                    comprasProdutos.add(compraProduto);
                }

                return comprasProdutos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todos os produtos das compras", e);
        }
    }

    // Verifica se um produto já está vinculado a uma compra
    public boolean existeVinculo(int compraId, int produtoId) {

        String sql = """
                     SELECT 1
                     FROM compras_produtos
                     WHERE compra_id = ?
                       AND produto_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraId);
            statement.setInt(2, produtoId);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar vínculo do produto com a compra", e);
        }
    }

    // Atualiza a quantidade de um produto em uma compra
    public int atualizar(CompraProduto compraProduto) {

        String sql = """
                     UPDATE compras_produtos
                     SET quantidade_item = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraProduto.getQuantidadeItem());
            statement.setInt(2, compraProduto.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar produto da compra", e);
        }
    }

    // Exclui o vínculo de um produto com uma compra pelo ID
    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM compras_produtos
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir produto da compra", e);
        }
    }
}
