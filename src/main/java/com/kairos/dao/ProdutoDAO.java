package com.kairos.dao;

import com.kairos.model.Produto;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

    // Insere um novo produto
    public Produto inserir(Produto produto) {

        String sql = """
                     INSERT INTO produtos (marca, nome, quantidade_estoque)
                     VALUES (?, ?, ?)
                     RETURNING id, marca, nome, quantidade_estoque
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produto.getMarca());
            statement.setString(2, produto.getNome());
            statement.setInt(3, produto.getQuantidadeEstoque());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir produto", e);
        }
    }

    // Busca um produto pelo ID
    public Produto buscarPorId(int id) {

        String sql = """
                     SELECT id, marca, nome, quantidade_estoque
                     FROM produtos
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto por id", e);
        }
    }

    // Busca um produto pelo nome
    public Produto buscarPorNome(String nome) {

        String sql = """
                     SELECT id, marca, nome, quantidade_estoque
                     FROM produtos
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto pelo nome", e);
        }
    }

    // Busca um produto pelo nome e pela marca
    public Produto buscarPorNomeEMarca(String nome, String marca) {

        String sql = """
                     SELECT id, marca, nome, quantidade_estoque
                     FROM produtos
                     WHERE nome = ? AND marca = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);
            statement.setString(2, marca);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto por nome e marca", e);
        }
    }

    // Lista todos os produtos cadastrados
    public List<Produto> listarTodos() {

        String sql = """
                     SELECT id, marca, nome, quantidade_estoque
                     FROM produtos
                     """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Produto produto = new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );

                    produtos.add(produto);
                }

                return produtos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todos os produtos", e);
        }
    }

    // Atualiza os dados de um produto
    public int atualizar(Produto produto) {

        String sql = """
                     UPDATE produtos
                     SET marca = ?,
                         nome = ?,
                         quantidade_estoque = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, produto.getMarca());
            statement.setString(2, produto.getNome());
            statement.setInt(3, produto.getQuantidadeEstoque());
            statement.setInt(4, produto.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar produto", e);
        }
    }

    // Exclui um produto pelo ID
    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM produtos
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir produto", e);
        }
    }

    // Verifica se existe um produto cadastrado com o nome informado
    public boolean existePorNome(String nome) {

        String sql = """
                     SELECT 1
                     FROM produtos
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar produto por nome", e);
        }
    }

    // Verifica se o nome pertence a outro produto
    public boolean existeNomeExcetoId(String nome, int id) {

        String sql = """
                     SELECT 1
                     FROM produtos
                     WHERE id <> ? AND nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, nome);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar nome de outro produto", e);
        }
    }

    // Verifica se existe um produto com o nome e a marca informados
    public boolean existePorNomeEMarca(String nome, String marca) {

        String sql = """
                     SELECT 1
                     FROM produtos
                     WHERE nome = ? AND marca = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);
            statement.setString(2, marca);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar produto por nome e marca", e);
        }
    }

    // Verifica se existe algum produto cadastrado com a marca informada
    public boolean existePorMarca(String marca) {

        String sql = """
                     SELECT 1
                     FROM produtos
                     WHERE marca = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, marca);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar existência da marca", e);
        }
    }

    // Pesquisa produtos pelo ID, marca, nome ou quantidade em estoque
    public List<Produto> pesquisar(String pesquisa) {

        String sql = """
                     SELECT id, marca, nome, quantidade_estoque
                     FROM produtos
                     WHERE CAST(id AS TEXT) ILIKE ?
                           OR marca ILIKE ?
                           OR nome ILIKE ?
                           OR CAST(quantidade_estoque AS TEXT) ILIKE ?
                     """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 4; i++) {
                statement.setString(i, pesquisa);
            }

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Produto produto = new Produto(
                            rs.getInt("id"),
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );

                    produtos.add(produto);
                }

                return produtos;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar produtos por pesquisa", e);
        }
    }
}
