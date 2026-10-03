package com.kairos.dao;

import com.kairos.model.*;
import com.kairos.model.enums.StatusAlerta;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    public Alerta inserir(Alerta alerta) {

        String sql = """
                     INSERT INTO alertas (usuario_id, gondola_id, produto_id,
                                          data_ruptura, descricao_alerta,
                                          status_alerta, data_resolucao)
                     VALUES (?, ?, ?, ?, ?, ?, ?)
                     RETURNING id, usuario_id, gondola_id, produto_id,
                               data_ruptura, descricao_alerta,
                               status_alerta, data_resolucao
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alerta.getUsuario().getId());
            statement.setInt(2, alerta.getGondola().getId());
            statement.setInt(3, alerta.getProduto().getId());
            statement.setDate(4, alerta.getDataRuptura());
            statement.setString(5, alerta.getDescricaoAlerta());
            statement.setString(6, alerta.getStatusAlerta().name());
            statement.setTimestamp(7, alerta.getDataResolucao());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Alerta(
                            rs.getInt("id"),
                            alerta.getUsuario(),
                            alerta.getGondola(),
                            alerta.getProduto(),
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir alerta", e);
        }
    }

    public Alerta buscarPorId(int id) {

        String sql = """
                     SELECT a.id as aid, a.usuario_id, a.gondola_id, a.produto_id,
                            a.data_ruptura, a.descricao_alerta, a.status_alerta,
                            a.data_resolucao,
                            u.id as uid, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            u.empresa_id,
                            e.id as eid, e.cnpj, e.tipo_plano,
                            g.id as gid, g.capacidade_maxima, g.setor_id,
                            s.id as sid, s.nome as setor_nome, s.categoria_setor,
                            p.id as pid, p.marca, p.nome, p.quantidade_estoque
                     FROM alertas a
                     JOIN usuarios u ON u.id = a.usuario_id
                     JOIN empresas e ON e.id = u.empresa_id
                     JOIN gondolas g ON g.id = a.gondola_id
                     JOIN setores s ON s.id = g.setor_id
                     JOIN produtos p ON p.id = a.produto_id
                     WHERE a.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Alerta(
                            rs.getInt("aid"),
                            new Usuario(
                                    rs.getInt("uid"),
                                    rs.getString("cpf"),
                                    rs.getString("nome"),
                                    rs.getString("sobrenome"),
                                    rs.getDate("data_nascimento").toLocalDate(),
                                    rs.getString("cep"),
                                    TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                    rs.getString("email"),
                                    new Empresa(
                                            rs.getInt("eid"),
                                            rs.getString("nome"),
                                            rs.getString("cnpj"),
                                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                                    )
                            ),
                            new Gondola(
                                    rs.getInt("gid"),
                                    rs.getInt("capacidade_maxima"),
                                    new Setor(
                                            rs.getInt("sid"),
                                            rs.getString("setor_nome"),
                                            rs.getString("categoria_setor")
                                    )
                            ),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar alerta por id", e);
        }
    }

    public List<Alerta> listarTodos() {

        String sql = """
                     SELECT a.id as aid, a.usuario_id, a.gondola_id, a.produto_id,
                            a.data_ruptura, a.descricao_alerta, a.status_alerta,
                            a.data_resolucao,
                            u.id as uid, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            u.empresa_id,
                            e.id as eid, e.nome, e.cnpj, e.tipo_plano,
                            g.id as gid, g.capacidade_maxima, g.setor_id,
                            s.id as sid, s.nome as setor_nome, s.categoria_setor,
                            p.id as pid, p.marca, p.nome, p.quantidade_estoque
                     FROM alertas a
                     JOIN usuarios u ON u.id = a.usuario_id
                     JOIN empresas e ON e.id = u.empresa_id
                     JOIN gondolas g ON g.id = a.gondola_id
                     JOIN setores s ON s.id = g.setor_id
                     JOIN produtos p ON p.id = a.produto_id
                     ORDER BY a.id
                     """;

        List<Alerta> alertas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Alerta alerta = new Alerta(
                            rs.getInt("aid"),
                            new Usuario(
                                    rs.getInt("uid"),
                                    rs.getString("cpf"),
                                    rs.getString("nome"),
                                    rs.getString("sobrenome"),
                                    rs.getDate("data_nascimento").toLocalDate(),
                                    rs.getString("cep"),
                                    TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                    rs.getString("email"),
                                    new Empresa(
                                            rs.getInt("eid"),
                                            rs.getString("nome"),
                                            rs.getString("cnpj"),
                                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                                    )
                            ),
                            new Gondola(
                                    rs.getInt("gid"),
                                    rs.getInt("capacidade_maxima"),
                                    new Setor(
                                            rs.getInt("sid"),
                                            rs.getString("setor_nome"),
                                            rs.getString("categoria_setor")
                                    )
                            ),
                            new Produto(
                                    rs.getInt("pid"),
                                    rs.getString("marca"),
                                    rs.getString("nome"),
                                    rs.getInt("quantidade_estoque")
                            ),
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );

                    alertas.add(alerta);
                }

                return alertas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todos os alertas", e);
        }
    }

    public int atualizar(Alerta alerta) {

        String sql = """
                     UPDATE alertas
                     SET usuario_id = ?,
                         gondola_id = ?,
                         produto_id = ?,
                         data_ruptura = ?,
                         descricao_alerta = ?,
                         status_alerta = ?,
                         data_resolucao = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, alerta.getUsuario().getId());
            statement.setInt(2, alerta.getGondola().getId());
            statement.setInt(3, alerta.getProduto().getId());
            statement.setDate(4, alerta.getDataRuptura());
            statement.setString(5, alerta.getDescricaoAlerta());
            statement.setString(6, alerta.getStatusAlerta().name());
            statement.setTimestamp(7, alerta.getDataResolucao());

            statement.setInt(8, alerta.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar alerta", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM alertas
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir alerta", e);
        }
    }
}