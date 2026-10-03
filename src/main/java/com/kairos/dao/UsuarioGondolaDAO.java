package com.kairos.dao;

import com.kairos.model.Empresa;
import com.kairos.model.Gondola;
import com.kairos.model.Setor;
import com.kairos.model.Usuario;
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

public class UsuarioGondolaDAO {

    public void vincular(Usuario usuario, Gondola gondola) {

        String sql = """
                     INSERT INTO usuarios_gondolas (usuario_id, gondola_id)
                     VALUES (?, ?)
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setInt(2, gondola.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao vincular usuário à gôndola", e);
        }
    }

    public void desvincular(Usuario usuario, Gondola gondola) {

        String sql = """
                     DELETE FROM usuarios_gondolas
                     WHERE usuario_id = ?
                       AND gondola_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setInt(2, gondola.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao desvincular usuário da gôndola", e);
        }
    }

    public List<Gondola> listarGondolasPorUsuario(Usuario usuario) {

        String sql = """
                     SELECT g.id as gid, g.capacidade_maxima,
                            s.id as sid, s.nome as setor_nome, s.categoria_setor
                     FROM usuarios_gondolas ug
                     JOIN gondolas g ON g.id = ug.gondola_id
                     JOIN setores s ON s.id = g.setor_id
                     WHERE ug.usuario_id = ?
                     ORDER BY g.id
                     """;

        List<Gondola> gondolas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Setor setor = new Setor(
                            rs.getInt("sid"),
                            rs.getString("setor_nome"),
                            rs.getString("categoria_setor")
                    );

                    Gondola gondola = new Gondola(
                            rs.getInt("gid"),
                            rs.getInt("capacidade_maxima"),
                            setor
                    );

                    gondolas.add(gondola);
                }

                return gondolas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar gôndolas do usuário", e);
        }
    }

    public List<Usuario> listarUsuariosPorGondola(Gondola gondola) {

        String sql = """
                     SELECT u.id as uid, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id as eid, e.nome as empresa_nome,
                            e.cnpj, e.tipo_plano
                     FROM usuarios_gondolas ug
                     JOIN usuarios u ON u.id = ug.usuario_id
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE ug.gondola_id = ?
                     ORDER BY u.id
                     """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, gondola.getId());

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Empresa empresa = new Empresa(
                            rs.getInt("eid"),
                            rs.getString("empresa_nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );

                    Usuario usuario = new Usuario(
                            rs.getInt("uid"),
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            empresa
                    );

                    usuarios.add(usuario);
                }

                return usuarios;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar usuários da gôndola", e);
        }
    }

    public boolean existeVinculo(Usuario usuario, Gondola gondola) {

        String sql = """
                 SELECT 1
                 FROM usuarios_gondolas
                 WHERE usuario_id = ?
                   AND gondola_id = ?
                 """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setInt(2, gondola.getId());

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException(
                    "Erro ao verificar vínculo entre usuário e gôndola",
                    e
            );
        }
    }
}