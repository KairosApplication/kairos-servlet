package com.kairos.dao;

import com.kairos.model.Empresa;
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

public class EmpresaDAO {

    public Empresa inserir(Empresa empresa) {

        String sql = """
                     INSERT INTO empresas (nome, cnpj, tipo_plano)
                     VALUES (?, ?, ?)
                     RETURNING id, nome, cnpj, tipo_plano;
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getNome());
            statement.setString(2, empresa.getCnpj());
            statement.setString(3, empresa.getTipoPlano().name());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir a empresa", e);
        }
    }

    public Empresa buscarPorId(int id) {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar empresa pelo id " + id, e);
        }
    }

    public Empresa buscarPorNome(String nome) {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar empresa pelo nome " + nome, e);
        }
    }

    public List<Empresa> listarTodos() {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     """;

        List<Empresa> empresas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Empresa empresa = new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );

                    empresas.add(empresa);
                }

                return empresas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar empresas", e);
        }
    }

    public int atualizar(Empresa empresa) {

        String sql = """
                     UPDATE empresas
                     SET nome = ?,
                         cnpj = ?,
                         tipo_plano = ?
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getNome());
            statement.setString(2, empresa.getCnpj());
            statement.setString(3, empresa.getTipoPlano().name());
            statement.setInt(4, empresa.getId());

            return statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao atualizar empresa", e);
        }
    }

    public void deletarPorId(int id) {

        String sql = """
                     DELETE FROM empresas
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new DAOException("Erro ao excluir empresa", e);
        }
    }

    public boolean existePorCnpj(String cnpj) {

        String sql = """
                     SELECT 1
                     FROM empresas
                     WHERE cnpj = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cnpj);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar cnpj", e);
        }
    }

    public boolean existePorCnpjExcetoId(String cnpj, int id) {

        String sql = """
                     SELECT 1
                     FROM empresas
                     WHERE cnpj = ? AND id <> ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cnpj);
            statement.setInt(2, id);

            try (ResultSet rs = statement.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao verificar cnpj de outra empresa", e);
        }
    }

    public Empresa buscarPorCnpj(String cnpj) {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     WHERE cnpj = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, cnpj);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }

                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar empresa por CNPJ", e);
        }
    }

    public boolean existePorNome(String nome) {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     WHERE nome = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);

            try (ResultSet rs = statement.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar outra empresa por nome", e);
        }
    }

    public boolean existePorNomeExcetoId(String nome, int id) {

        String sql = """
                     SELECT id, nome, cnpj, tipo_plano
                     FROM empresas
                     WHERE nome = ? AND id <> ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, nome);
            statement.setInt(2, id);

            try (ResultSet rs = statement.executeQuery()) {

                return rs.next();
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar outra empresa por nome", e);
        }
    }

    public List<Empresa> pesquisar(String pesquisa) {

        String sql = """
                 SELECT id, nome, cnpj, tipo_plano
                 FROM empresas
                 WHERE CAST(id AS TEXT) ILIKE ?
                       OR nome ILIKE ?
                       OR cnpj ILIKE ?
                       OR tipo_plano ILIKE ?
                 """;

        List<Empresa> empresas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 1; i <= 4; i++) {
                statement.setString(i, pesquisa);
            }

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    Empresa empresa = new Empresa(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );

                    empresas.add(empresa);
                }

                return empresas;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao listar empresas por pesquisa");
        }
    }
}