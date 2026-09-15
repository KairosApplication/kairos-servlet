package com.kairos.dao;

import com.kairos.model.Empresa;
import com.kairos.model.TipoPlano;
import com.kairos.utils.ConnectionFactory;
import com.kairos.utils.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmpresaDAO {

    public Empresa inserir(Empresa empresa) {

        String sql = """
                     INSERT INTO empresas (cnpj, tipo_plano)
                     VALUES (?, ?)
                     RETURNING id, cnpj, tipo_plano;
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getCnpj());
            statement.setString(2, empresa.getTipoPlano().name());

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new Empresa(
                        rs.getInt("id"),
                        rs.getString("cnpj"),
                        TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }
                throw new DAOException("Não foi possível retornar a empresa criada");
            }
        } catch (SQLException e) {
            throw new DAOException("Erro ao inserir a empresa", e);
        }
    }

    public Empresa buscarPorId(int id) {

        String sql = """
                     SELECT id, cnpj, tipo_plano
                     FROM empresas
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    Empresa empresa = new Empresa(
                            rs.getInt("id"),
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                    return empresa;
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar empresa pelo id " + id, e);
        }
    }

    public List<Empresa> listarTodas() {

        String sql = """
                     SELECT id, cnpj, tipo_plano
                     FROM empresas
                     """;

        List<Empresa> empresas = new ArrayList<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    Empresa empresa = new Empresa(
                            rs.getInt("id"),
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
                     SET cnpj = ?,
                         tipo_plano = ?,
                     WHERE id = ?
                     """;

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, empresa.getCnpj());
            statement.setString(2, empresa.getTipoPlano().name());
            statement.setInt(3, empresa.getId());

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
                     SELECT id, cnpj, tipo_plano
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
                            rs.getString("cnpj"),
                            TipoPlano.valueOf(rs.getString("tipo_plano"))
                    );
                }
                throw new DAOException("Não foi possível encontrar a empresa pelo CNPJ");
            }

        } catch (SQLException e) {
            throw new DAOException("Erro ao buscar empresa por CNPJ");
        }
    }
}
