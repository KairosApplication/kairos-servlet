package com.kairos.dao;

import com.kairos.model.Empresa;
import com.kairos.model.Reposicao;
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

public class UsuarioReposicaoDAO {

    public void vincular(Usuario usuario, Reposicao reposicao) {

        String sql = """
                     INSERT INTO usuarios_reposicoes (usuario_id, reposicao_id)
                     VALUES (?, ?)
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setInt(2, reposicao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao vincular usuário à reposição", e);
        }
    }

    public void desvincular(Usuario usuario, Reposicao reposicao) {

        String sql = """
                     DELETE FROM usuarios_reposicoes
                     WHERE usuario_id = ?
                       AND reposicao_id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, usuario.getId());
            statement.setInt(2, reposicao.getId());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao desvincular usuário da reposição", e);
        }
    }

    public List<Usuario> listarUsuariosPorReposicao(Reposicao reposicao) {

        String sql = """
                     SELECT u.id as uid, u.cpf, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id as eid, e.nome as empresa_nome,
                            e.cnpj, e.tipo_plano
                     FROM usuarios_reposicoes ur
                     JOIN usuarios u ON u.id = ur.usuario_id
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE ur.reposicao_id = ?
                     ORDER BY u.id
                     """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, reposicao.getId());

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
            throw new DAOException("Erro ao listar usuários da reposição", e);
        }
    }
}