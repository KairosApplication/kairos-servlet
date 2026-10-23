package com.kairos.service;

import com.kairos.dao.EmpresaDAO;
import com.kairos.model.Empresa;
import com.kairos.utils.Regex;
import com.kairos.utils.exceptions.exists.CnpjExistsException;
import com.kairos.utils.exceptions.exists.NameExistsException;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;
import com.kairos.utils.exceptions.regex.InvalidCnpjRegexException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class EmpresaService {

    // DAO
    private final EmpresaDAO empresaDAO;

    // Construtor
    public EmpresaService() {
        this.empresaDAO = new EmpresaDAO();
    }

    // Cadastrar empresa
    public Empresa cadastrar(Empresa empresa) {

        validarCampos(empresa);

        if (empresaDAO.existePorNome(empresa.getNome())) {
            throw new NameExistsException("Nome já cadastrado");
        }

        if (empresaDAO.existePorCnpj(empresa.getCnpj())) {
            throw new CnpjExistsException("CNPJ já cadastrado");
        }

        if (!empresa.getCnpj().matches(Regex.CNPJ)) {
            throw new InvalidCnpjRegexException("Formato do CNPJ inválido");
        }

        empresa.setCnpj(empresa.getCnpj().replaceAll("[^0-9]", ""));

        return empresaDAO.inserir(empresa);
    }

    // Buscar empresa por ID
    public Empresa buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Empresa empresa = empresaDAO.buscarPorId(id);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        return empresa;
    }

    // Buscar empresa por nome
    public Empresa buscarPorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome da empresa é obrigatório");
        }

        Empresa empresa = empresaDAO.buscarPorNome(nome);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        return empresa;
    }

    // Listar empresas
    public List<Empresa> listarTodos() {

        return empresaDAO.listarTodos();
    }

    // Atualizar empresa
    public void atualizar(Empresa empresa) {

        validarCampos(empresa);

        if (empresa.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (empresaDAO.existePorNomeExcetoId(empresa.getNome(), empresa.getId())) {
            throw new NameExistsException("Nome já cadastrado");
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

    // Deletar empresa por ID
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Empresa empresa = empresaDAO.buscarPorId(id);

        if (empresa == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        empresaDAO.deletarPorId(id);
    }

    // Verificar CNPJ existente
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

    // Verificar CNPJ existente, exceto ID
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

    // Buscar empresa por CNPJ
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

    // Verificar nome existente
    public boolean existePorNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome da empresa é obrigatório");
        }

        return empresaDAO.existePorNome(nome);
    }

    // Verificar nome existente, exceto ID
    public boolean existePorNomeExcetoId(String nome, int id) {

        if (nome == null || nome.isBlank()) {
            throw new ServiceException("Nome da empresa é obrigatório");
        }

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        return empresaDAO.existePorNomeExcetoId(nome, id);
    }

    // Validar campos da empresa
    private void validarCampos(Empresa empresa) {

        if (empresa == null) {
            throw new ServiceException("Empresa não pode ser nula");
        }

        if (empresa.getNome() == null || empresa.getNome().isBlank()) {
            throw new ServiceException("Nome da empresa é obrigatório");
        }

        if (empresa.getCnpj() == null || empresa.getCnpj().isBlank()) {
            throw new ServiceException("CNPJ é obrigatório");
        }

        if (empresa.getTipoPlano() == null) {
            throw new ServiceException("Tipo Plano é obrigatório");
        }
    }

    // Pesquisar empresas
    public List<Empresa> pesquisar(String pesquisa) {

        return empresaDAO.pesquisar(pesquisa);
    }
}
