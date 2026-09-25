package com.kairos.service;

import com.kairos.dao.EmpresaDAO;
import com.kairos.model.Empresa;
import com.kairos.model.Usuario;
import com.kairos.utils.AuthorizationValidator;
import com.kairos.utils.Regex;
import com.kairos.utils.exceptions.exists.CnpjExistsException;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;
import com.kairos.utils.exceptions.invalid.InvalidCnpjRegexException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class EmpresaService {

    private EmpresaDAO empresaDAO;

    public EmpresaService() {
        this.empresaDAO = new EmpresaDAO();
    }

    public Empresa cadastrar(Empresa empresa) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        validarCampos(empresa);

        if (empresaDAO.existePorCnpj(empresa.getCnpj())) {
            throw new CnpjExistsException("CNPJ já cadastrado");
        }

        if (!empresa.getCnpj().matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        empresa.setCnpj(empresa.getCnpj().replaceAll("[^0-9]", ""));

        return empresaDAO.inserir(empresa);
    }

    public Empresa buscarPorId(int id) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Empresa empresa = empresaDAO.buscarPorId(id);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }
        return empresa;
    }

    public List<Empresa> listarTodas(Usuario usuarioAtual) {

        AuthorizationValidator.validarAdmin(usuarioAtual);

        return empresaDAO.listarTodos();
    }

    public void atualizar(Empresa empresa) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        validarCampos(empresa);

        if (empresa.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (empresaDAO.existePorCnpjExcetoId(empresa.getCnpj(), empresa.getId())) {
            throw new CnpjExistsException("CNPJ já cadastrado");
        }

        if (!empresa.getCnpj().matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        empresa.setCnpj(empresa.getCnpj().replaceAll("[^0-9]", ""));

        if (empresaDAO.atualizar(empresa) == 0) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }
    }

    public void deletarPorId(int id, Usuario usuarioAtual) {

        AuthorizationValidator.validarAdmin(usuarioAtual);
        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Empresa empresa = empresaDAO.buscarPorId(id);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        empresaDAO.deletarPorId(id);
    }

    public boolean existePorCnpj(String cnpj) {

        if (cnpj == null || cnpj.isBlank()) {
            throw new ServiceException("CNPJ é obrigatório");
        }

        if (!cnpj.matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        return empresaDAO.existePorCnpj(cnpj);
    }

    public boolean existePorCnpjExcetoId(String cnpj, int id) {

        if (cnpj == null || cnpj.isBlank()) {
            throw new ServiceException("O CNPJ é obrigatório");
        }

        if (!cnpj.matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        return empresaDAO.existePorCnpjExcetoId(cnpj, id);
    }

    public Empresa buscarPorCnpj(String cnpj) {

        if (cnpj == null || cnpj.isBlank()) {
            throw new ServiceException("CNPJ é obrigatório");
        }

        if (!cnpj.matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        cnpj = cnpj.replaceAll("[^0-9]", "");

        Empresa empresa = empresaDAO.buscarPorCnpj(cnpj);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }
        return empresa;
    }

    private void validarCampos(Empresa empresa) {

        if (empresa == null) {
            throw new ServiceException("Empresa não pode ser nula");
        }

        if (empresa.getCnpj() == null || empresa.getCnpj().isBlank()) {
            throw new ServiceException("CNPJ é obrigatório");
        }

        if (empresa.getTipoPlano() == null) {
            throw new ServiceException("Tipo Plano é obrigatório");
        }
    }
}
