package com.kairos.dao;

import com.kairos.model.Empresa;
import com.kairos.model.Usuario;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.utils.PasswordHasher;
import com.kairos.utils.connection.ConnectionFactory;
import com.kairos.utils.exceptions.system.DAOException;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    // Insere um novo usuário com a senha criptografada
    public Usuario inserir(Usuario usuario) {

        String senhaHash = PasswordHasher.hash(usuario.getSenha());

        String sql = """
                     INSERT INTO usuarios (cpf, senha, nome, sobrenome, data_nascimento,
                                          cep, tipo_usuario, email, empresa_id)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                     RETURNING id, cpf, senha, nome, sobrenome, data_nascimento,
                               cep, tipo_usuario, email
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, usuario.getCpf());
            statement.setString(2, senhaHash);
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

    // Busca um usuário pelo ID
    public Usuario buscarPorId(int id) {

        String sql = """
                     SELECT u.id as usuario_id, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id as empresa_id, e.nome as empresa_nome, e.cnpj, e.tipo_plano
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
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            )
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário pelo id " + id, e);
        }
    }

    // Lista todos os usuários cadastrados
    public List<Usuario> listarTodos() {

        String sql = """
                     SELECT u.id as usuario_id, u.cpf, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id as empresa_id, e.nome as empresa_nome, e.cnpj, e.tipo_plano
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
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            new Empresa(
                                    rs.getInt("empresa_id"),
                                    rs.getString("empresa_nome"),
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

    // Atualiza os dados de um usuário
    public int atualizar(Usuario usuario) {

        String sql = """
                     UPDATE usuarios
                     SET cpf = ?,
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
            statement.setString(2, usuario.getNome());
            statement.setString(3, usuario.getSobrenome());
            statement.setDate(4, Date.valueOf(usuario.getDataNascimento()));
            statement.setString(5, usuario.getCep());
            statement.setString(6, usuario.getEmail());
            statement.setInt(7, usuario.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar usuário", e);
        }
    }

    // Exclui um usuário pelo ID
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

    // Verifica se existe um usuário cadastrado com o email informado
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

    // Verifica se existe um usuário cadastrado com o CPF informado
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

    // Verifica se o email pertence a outro usuário
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

    // Verifica se o CPF pertence a outro usuário
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

    // Busca um usuário pelo email
    public Usuario buscarPorEmail(String email) {

        String sql = """
                     SELECT u.id as usuario_id, u.cpf, u.senha, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id as empresa_id, e.nome as empresa_nome, e.cnpj, e.tipo_plano
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE u.email = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

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
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            )
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar usuário pelo email " + email, e);
        }
    }

    // Pesquisa usuários por seus dados e informações da empresa
    public List<Usuario> pesquisar(String pesquisa) {

        String sql = """
                     SELECT u.id AS usuario_id, u.cpf, u.nome, u.sobrenome,
                            u.data_nascimento, u.cep, u.tipo_usuario, u.email,
                            e.id AS empresa_id, e.nome AS empresa_nome, e.cnpj, e.tipo_plano
                     FROM usuarios u
                     JOIN empresas e ON e.id = u.empresa_id
                     WHERE CAST(u.id AS TEXT) ILIKE ?
                        OR u.cpf ILIKE REGEXP_REPLACE(?, '[^0-9]', '', 'g')
                        OR u.nome ILIKE ?
                        OR u.sobrenome ILIKE ?
                        OR TO_CHAR(u.data_nascimento, 'DD/MM/YYYY') ILIKE ?
                        OR u.cep ILIKE REGEXP_REPLACE(?, '[^0-9]', '', 'g')
                        OR u.email ILIKE ?
                        OR u.tipo_usuario ILIKE ?
                        OR e.nome ILIKE ?
                     ORDER BY u.id
                     """;

        List<Usuario> usuarios = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 9; i++) {
                statement.setString(i, pesquisa);
            }

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Usuario usuario = new Usuario(
                            rs.getInt("usuario_id"),
                            rs.getString("cpf"),
                            rs.getString("nome"),
                            rs.getString("sobrenome"),
                            rs.getDate("data_nascimento").toLocalDate(),
                            rs.getString("cep"),
                            TipoUsuario.valueOf(rs.getString("tipo_usuario")),
                            rs.getString("email"),
                            new Empresa(
                                    rs.getInt("empresa_id"),
                                    rs.getString("empresa_nome"),
                                    rs.getString("cnpj"),
                                    TipoPlano.valueOf(rs.getString("tipo_plano"))
                            )
                    );

                    usuarios.add(usuario);
                }

                return usuarios;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao pesquisar usuários", e);
        }
    }
}
