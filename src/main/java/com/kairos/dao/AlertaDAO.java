package com.kairos.dao;

import com.kairos.model.Alerta;
import com.kairos.model.Empresa;
import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.model.Setor;
import com.kairos.model.Usuario;
import com.kairos.model.enums.StatusAlerta;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlertaDAO {

    // Insere um novo alerta
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

    // Busca um alerta pelo ID
    public Alerta buscarPorId(int id) {

        String sql = """
                     SELECT a.id AS aid, a.data_ruptura, a.descricao_alerta,
                            a.status_alerta, a.data_resolucao,
                            u.id AS uid, u.cpf, u.nome AS usuario_nome,
                            u.sobrenome, u.data_nascimento, u.cep,
                            u.tipo_usuario, u.email,
                            e.id AS eid, e.nome AS empresa_nome,
                            e.cnpj, e.tipo_plano,
                            g.id AS gid, g.capacidade_maxima,
                            s.id AS sid, s.nome AS setor_nome,
                            s.categoria_setor,
                            p.id AS pid, p.marca,
                            p.nome AS produto_nome, p.quantidade_estoque
                     FROM alertas a
                     LEFT JOIN usuarios u ON u.id = a.usuario_id
                     LEFT JOIN empresas e ON e.id = u.empresa_id
                     LEFT JOIN gondolas g ON g.id = a.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN produtos p ON p.id = a.produto_id
                     WHERE a.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Usuario usuario = null;
                    Gondola gondola = null;
                    Produto produto = null;

                    if (rs.getObject("uid") != null) {

                        Empresa empresa = null;

                        if (rs.getObject("eid") != null) {

                            empresa = new Empresa(
                                    rs.getInt("eid"),
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            );
                        }

                        usuario = new Usuario(
                                rs.getInt("uid"),
                                rs.getString("cpf"),
                                rs.getString("usuario_nome"),
                                rs.getString("sobrenome"),
                                rs.getDate("data_nascimento").toLocalDate(),
                                rs.getString("cep"),
                                TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                rs.getString("email"),
                                empresa
                        );
                    }

                    if (rs.getObject("gid") != null) {

                        Setor setor = null;

                        if (rs.getObject("sid") != null) {

                            setor = new Setor(
                                    rs.getInt("sid"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gid"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    if (rs.getObject("pid") != null) {

                        produto = new Produto(
                                rs.getInt("pid"),
                                rs.getString("marca"),
                                rs.getString("produto_nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Alerta alerta = new Alerta(
                            rs.getInt("aid"),
                            usuario,
                            gondola,
                            produto,
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );

                    HistoricoAlertaDAO historicoDAO = new HistoricoAlertaDAO();

                    alerta.setHistoricoAlerta(historicoDAO.buscarPorAlertaId(alerta.getId()));

                    return alerta;
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar alerta por id", e);
        }
    }

    // Lista todos os alertas cadastrados
    public List<Alerta> listarTodos() {

        String sql = """
                     SELECT a.id AS aid, a.data_ruptura, a.descricao_alerta,
                            a.status_alerta, a.data_resolucao,
                            u.id AS uid, u.cpf, u.nome AS usuario_nome,
                            u.sobrenome, u.data_nascimento, u.cep,
                            u.tipo_usuario, u.email,
                            e.id AS eid, e.nome AS empresa_nome,
                            e.cnpj, e.tipo_plano,
                            g.id AS gid, g.capacidade_maxima,
                            s.id AS sid, s.nome AS setor_nome,
                            s.categoria_setor,
                            p.id AS pid, p.marca,
                            p.nome AS produto_nome, p.quantidade_estoque
                     FROM alertas a
                     LEFT JOIN usuarios u ON u.id = a.usuario_id
                     LEFT JOIN empresas e ON e.id = u.empresa_id
                     LEFT JOIN gondolas g ON g.id = a.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN produtos p ON p.id = a.produto_id
                     ORDER BY a.id
                     """;

        List<Alerta> alertas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                HistoricoAlertaDAO historicoDAO = new HistoricoAlertaDAO();

                while (rs.next()) {

                    Usuario usuario = null;
                    Gondola gondola = null;
                    Produto produto = null;

                    if (rs.getObject("uid") != null) {

                        Empresa empresa = null;

                        if (rs.getObject("eid") != null) {

                            empresa = new Empresa(
                                    rs.getInt("eid"),
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            );
                        }

                        usuario = new Usuario(
                                rs.getInt("uid"),
                                rs.getString("cpf"),
                                rs.getString("usuario_nome"),
                                rs.getString("sobrenome"),
                                rs.getDate("data_nascimento").toLocalDate(),
                                rs.getString("cep"),
                                TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                rs.getString("email"),
                                empresa
                        );
                    }

                    if (rs.getObject("gid") != null) {

                        Setor setor = null;

                        if (rs.getObject("sid") != null) {

                            setor = new Setor(
                                    rs.getInt("sid"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gid"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    if (rs.getObject("pid") != null) {

                        produto = new Produto(
                                rs.getInt("pid"),
                                rs.getString("marca"),
                                rs.getString("produto_nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Alerta alerta = new Alerta(
                            rs.getInt("aid"),
                            usuario,
                            gondola,
                            produto,
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );

                    alerta.setHistoricoAlerta(historicoDAO.buscarPorAlertaId(alerta.getId()));

                    alertas.add(alerta);
                }

                return alertas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar todos os alertas", e);
        }
    }

    // Atualiza os dados de um alerta
    public int atualizar(Alerta alerta) {

        String sql = """
                     UPDATE alertas
                     SET descricao_alerta = ?,
                         status_alerta = ?,
                         data_resolucao = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, alerta.getDescricaoAlerta());
            statement.setString(2, alerta.getStatusAlerta().name());
            statement.setTimestamp(3, alerta.getDataResolucao());
            statement.setInt(4, alerta.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar alerta", e);
        }
    }

    // Exclui um alerta pelo ID
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

    // Pesquisa alertas por seus dados e informações relacionadas
    public List<Alerta> pesquisar(String pesquisa) {

        String sql = """
                     SELECT a.id AS aid, a.data_ruptura, a.descricao_alerta,
                            a.status_alerta, a.data_resolucao,
                            u.id AS uid, u.cpf, u.nome AS usuario_nome,
                            u.sobrenome, u.data_nascimento, u.cep,
                            u.tipo_usuario, u.email,
                            e.id AS eid, e.nome AS empresa_nome,
                            e.cnpj, e.tipo_plano,
                            g.id AS gid, g.capacidade_maxima,
                            s.id AS sid, s.nome AS setor_nome,
                            s.categoria_setor,
                            p.id AS pid, p.marca,
                            p.nome AS produto_nome, p.quantidade_estoque
                     FROM alertas a
                     LEFT JOIN usuarios u ON u.id = a.usuario_id
                     LEFT JOIN empresas e ON e.id = u.empresa_id
                     LEFT JOIN gondolas g ON g.id = a.gondola_id
                     LEFT JOIN setores s ON s.id = g.setor_id
                     LEFT JOIN produtos p ON p.id = a.produto_id
                     LEFT JOIN alertas_historicos ha ON ha.alerta_id = a.id
                     WHERE CAST(a.id AS TEXT) ILIKE ?
                        OR CAST(ha.id_usuario AS TEXT) ILIKE ?
                        OR CAST(ha.id_gondola AS TEXT) ILIKE ?
                        OR CAST(ha.id_produto AS TEXT) ILIKE ?
                        OR CAST(a.data_ruptura AS TEXT) ILIKE ?
                        OR a.descricao_alerta ILIKE ?
                        OR a.status_alerta ILIKE ?
                        OR CAST(a.data_resolucao AS TEXT) ILIKE ?
                        OR u.nome ILIKE ?
                        OR u.sobrenome ILIKE ?
                        OR p.nome ILIKE ?
                        OR s.nome ILIKE ?
                     ORDER BY a.id
                     """;

        List<Alerta> alertas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 12; i++) {
                statement.setString(i, pesquisa);
            }

            try (ResultSet rs = statement.executeQuery()) {

                HistoricoAlertaDAO historicoDAO = new HistoricoAlertaDAO();

                while (rs.next()) {

                    Usuario usuario = null;
                    Gondola gondola = null;
                    Produto produto = null;

                    if (rs.getObject("uid") != null) {

                        Empresa empresa = null;

                        if (rs.getObject("eid") != null) {

                            empresa = new Empresa(
                                    rs.getInt("eid"),
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            );
                        }

                        usuario = new Usuario(
                                rs.getInt("uid"),
                                rs.getString("cpf"),
                                rs.getString("usuario_nome"),
                                rs.getString("sobrenome"),
                                rs.getDate("data_nascimento").toLocalDate(),
                                rs.getString("cep"),
                                TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                                rs.getString("email"),
                                empresa
                        );
                    }

                    if (rs.getObject("gid") != null) {

                        Setor setor = null;

                        if (rs.getObject("sid") != null) {

                            setor = new Setor(
                                    rs.getInt("sid"),
                                    rs.getString("setor_nome"),
                                    rs.getString("categoria_setor")
                            );
                        }

                        gondola = new Gondola(
                                rs.getInt("gid"),
                                rs.getInt("capacidade_maxima"),
                                setor
                        );
                    }

                    if (rs.getObject("pid") != null) {

                        produto = new Produto(
                                rs.getInt("pid"),
                                rs.getString("marca"),
                                rs.getString("produto_nome"),
                                rs.getInt("quantidade_estoque")
                        );
                    }

                    Alerta alerta = new Alerta(
                            rs.getInt("aid"),
                            usuario,
                            gondola,
                            produto,
                            rs.getDate("data_ruptura"),
                            rs.getString("descricao_alerta"),
                            StatusAlerta.valueOf(rs.getString("status_alerta")),
                            rs.getTimestamp("data_resolucao")
                    );

                    alerta.setHistoricoAlerta(historicoDAO.buscarPorAlertaId(alerta.getId()));

                    alertas.add(alerta);
                }

                return alertas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao pesquisar alertas", e);
        }
    }
}
