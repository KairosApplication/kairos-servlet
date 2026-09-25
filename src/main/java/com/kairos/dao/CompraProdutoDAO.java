package com.kairos.dao;

import com.kairos.model.*;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompraProdutoDAO {

    public CompraProduto inserir(CompraProduto compraProduto) {

        String sql = """
                     INSERT INTO compras_produtos (produto_id, compra_id, quantidade_item)
                     VALUES (?, ?, ?)
                     RETURNING id, produto_id, compra_id, quantidade_item
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraProduto.getProduto().getId());
            statement.setInt(2, compraProduto.getCompra().getId());
            statement.setInt(3, compraProduto.getQuantidadeItem());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new CompraProduto(
                            rs.getInt("id"),
                            compraProduto.getProduto(),
                            compraProduto.getCompra(),
                            rs.getInt("quantidade_item")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir produto na compra", e);
        }
    }

    public CompraProduto buscarPorId(int id) {

        String sql = """
                     SELECT cp.id as cpid, cp.produto_id, cp.compra_id,
                            cp.quantidade_item,
                            p.id as pid, p.marca, p.nome, p.quantidade_estoque,
                            c.id as cid, c.data_compra
                     FROM compras_produtos cp
                     JOIN produtos p ON p.id = cp.produto_id
                     JOIN compras c ON c.id = cp.compra_id
                     WHERE cp.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new CompraProduto(
                            rs.getInt("cpid"),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            new Compra(
                                    rs.getInt("cid"),
                                    rs.getTimestamp("data_compra")
                            ),
                            rs.getInt("quantidade_item")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto da compra por id", e);
        }
    }

    public List<CompraProduto> listarTodos() {

        String sql = """
                     SELECT cp.id as cpid, cp.produto_id, cp.compra_id,
                            cp.quantidade_item,
                            p.id as pid, p.marca, p.nome, p.quantidade_estoque,
                            c.id as cid, c.data_compra
                     FROM compras_produtos cp
                     JOIN produtos p ON p.id = cp.produto_id
                     JOIN compras c ON c.id = cp.compra_id
                     ORDER BY cp.id
                     """;

        List<CompraProduto> comprasProdutos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    CompraProduto compraProduto = new CompraProduto(
                            rs.getInt("cpid"),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            new Compra(
                                    rs.getInt("cid"),
                                    rs.getTimestamp("data_compra")
                            ),
                            rs.getInt("quantidade_item")
                    );

                    comprasProdutos.add(compraProduto);
                }

                return comprasProdutos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todos os produtos das compras", e);
        }
    }

    public int atualizar(CompraProduto compraProduto) {

        String sql = """
                     UPDATE compras_produtos
                     SET produto_id = ?,
                         compra_id = ?,
                         quantidade_item = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, compraProduto.getProduto().getId());
            statement.setInt(2, compraProduto.getCompra().getId());
            statement.setInt(3, compraProduto.getQuantidadeItem());

            statement.setInt(4, compraProduto.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar produto da compra", e);
        }
    }

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