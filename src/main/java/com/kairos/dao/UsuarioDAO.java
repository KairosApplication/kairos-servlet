package com.kairos.dao;

import com.kairos.model.Empresa;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.model.Usuario;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Date;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

//    Metodo para inserir um usuario
    public Usuario inserir(Usuario usuario) {

        String sql = """
                     INSERT INTO usuarios (
                                           cpf, senha, nome, sobrenome, data_nascimento, 
                                           cep, tipo_usuario, email, empresa_id
                                           )
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                     RETURNING id, cpf, senha, nome, sobrenome, data_nascimento,
                               cep, tipo_usuario, email
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());
            statement.setString(2, usuario.getSenha());
            statement.setString(3, usuario.getNome());
            statement.setString(4, usuario.getSobrenome());
            statement.setDate(5, Date.valueOf(usuario.getDataNascimento()));
            statement.setString(6, usuario.getCep());
            statement.setString(7, usuario.getTipoUsuario().name());
            statement.setString(8, usuario.getEmail());
            statement.setInt(9, usuario.getEmpresa().getId());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("id"),
                            rs.getString("cpf"),
                            rs.getString("senha"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            usuario.getEmpresa()
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir o usuário", e);
        }
    }

//    Metodo que busca usuario pelo id
    public Usuario buscarPorId(int id) {

        String sql = """
                     SELECT u.id as usuario_id, u.cpf, u.senha, u.nome, u.sobrenome, 
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email, 
                            e.id as empresa_id, e.cnpj, e.tipo_plano
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE u.id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("cpf"),
                            rs.getString("senha"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            new Empresa(
                                    rs.getInt("empresa_id"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano")))
                    );
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário pelo id " + id, e);
        }
    }

//    Metodo que busca todos os usuarios
    public List<Usuario> listarTodos() {

        String sql = """
                     SELECT u.id as usuario_id, u.cpf, u.senha, u.nome, u.sobrenome, 
                     u.data_nascimento, u.cep, u.tipo_usuario, u.email, e.id as empresa_id,
                     e.cnpj, e.tipo_plano
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     ORDER BY u.id
                     """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Usuario usuario = new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("cpf"),
                            rs.getString("senha"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            new Empresa(
                                    rs.getInt("empresa_id"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            )
                    );
                    usuarios.add(usuario);
                }

                return usuarios;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar usuários", e);
        }
    }

//    Metodo que atualiza um usuario
    public int atualizar(Usuario usuario) {

        String sql = """
                     UPDATE usuarios
                     SET cpf = ?,
                         senha = ?,
                         nome = ?,
                         sobrenome = ?,
                         data_nascimento = ?,
                         cep = ?,
                         email = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());
            statement.setString(2, usuario.getSenha());
            statement.setString(3, usuario.getNome());
            statement.setString(4, usuario.getSobrenome());
            statement.setDate(5, Date.valueOf(usuario.getDataNascimento()));
            statement.setString(6, usuario.getCep());
            statement.setString(7, usuario.getEmail());

            statement.setInt(8, usuario.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar usuário", e);
        }
    }

//    Metodo que deleta um usuario por id
    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM usuarios
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir usuário", e);
        }
    }

    public boolean existePorEmail(String email) {

        String sql = """
                     SELECT 1
                     FROM usuarios
                     WHERE email = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar email", e);
        }
    }

    public boolean existePorCpf(String cpf) {

        String sql = """
                     SELECT 1
                     FROM usuarios
                     WHERE cpf = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cpf);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar CPF", e);
        }
    }

//    Metodo que busca o email de outro usuario
    public boolean existePorEmailExcetoId(String email, int id) {

        String sql = """
                     SELECT 1
                     FROM usuarios
                     WHERE id <> ? AND email = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, email);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar e-mail de outro usuário", e);
        }
    }
    public boolean existePorCpfExcetoId(String cpf, int id) {

        String sql = """
                     SELECT 1
                     FROM usuarios
                     WHERE id <> ? and cpf = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.setString(2, cpf);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar CPF de outro usuário", e);
        }
    }



//    Precisa saber as regras do front
//    public Usuario login(String email, String senha) {
//
//        String sql = """
//                     SELECT id, cpf, senha, nome, sobrenome, data_nascimento,
//                     cep, tipo_usuario, email
//                     FROM usuarios
//                     WHERE email = ? AND senha = ?
//                     """;
//
//
//    }
}

