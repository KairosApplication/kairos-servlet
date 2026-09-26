package com.kairos.dao;

import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.utils.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GondolaProdutoDAO {

    public void vincular(Gondola gondola, Produto produto) {
        String sql = """
                INSERT INTO gondolas_produtos (gondola_id, produto_id)
                VALUES (?, ?)
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gondola.getId());
            stmt.setInt(2, produto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao vincular produto à gôndola.", e);
        }
    }

    public void desvincular(Gondola gondola, Produto produto) {
        String sql = """
                DELETE FROM gondolas_produtos
                WHERE gondola_id = ? AND produto_id = ?
                """;

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gondola.getId());
            stmt.setInt(2, produto.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao desvincular produto da gôndola.", e);
        }
    }

    public List<Produto> listarProdutosPorGondola(Gondola gondola) {
        String sql = """
            SELECT p.id, p.marca, p.nome, p.quantidade_estoque
            FROM produtos p
            INNER JOIN gondolas_produtos gp ON gp.produto_id = p.id
            WHERE gp.gondola_id = ?
            """;

        List<Produto> produtos = new ArrayList<>();

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gondola.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Produto produto = new Produto(
                            rs.getString("marca"),
                            rs.getString("nome"),
                            rs.getInt("quantidade_estoque")
                    );

                    produto.setId(rs.getInt("id"));
                    produtos.add(produto);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar produtos da gôndola.", e);
        }

        return produtos;
    }
}