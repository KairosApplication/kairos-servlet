package com.kairos.service;

import com.kairos.dao.EmpresaDAO;
import com.kairos.dao.HistoricoUsuarioReposicaoDAO;
import com.kairos.dao.UsuarioDAO;
import com.kairos.dao.UsuarioGondolaDAO;
import com.kairos.model.Gondola;
import com.kairos.model.Usuario;
import com.kairos.utils.Regex;
import com.kairos.utils.exceptions.exists.CpfExistsException;
import com.kairos.utils.exceptions.exists.EmailExistsException;
import com.kairos.utils.exceptions.regex.InvalidCepRegexException;
import com.kairos.utils.exceptions.regex.InvalidCpfRegexException;
import com.kairos.utils.exceptions.regex.InvalidEmailRegexException;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

    // Atributos
    private final UsuarioDAO usuarioDAO;
    private final EmpresaDAO empresaDAO;
    private final UsuarioGondolaDAO usuarioGondolaDAO;
    private final HistoricoUsuarioReposicaoDAO historicoUsuarioReposicaoDAO;

    // Construtor
    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.empresaDAO = new EmpresaDAO();
        this.usuarioGondolaDAO = new UsuarioGondolaDAO();
        this.historicoUsuarioReposicaoDAO = new HistoricoUsuarioReposicaoDAO();
    }

    // Cadastrar usuário
    public Usuario cadastrar(Usuario usuario) {

        validarCampos(usuario);

        if (usuarioDAO.existePorEmail(usuario.getEmail())) {
            throw new EmailExistsException("Email já cadastrado");
        }

        if (usuarioDAO.existePorCpf(usuario.getCpf())) {
            throw new CpfExistsException("CPF já cadastrado");
        }

        if (!usuario.getCpf().matches(Regex.CPF)) {
            throw new InvalidCpfRegexException("Formato do CPF inválido");
        }

        if (!usuario.getCep().matches(Regex.CEP)) {
            throw new InvalidCepRegexException("Formato do CEP inválido");
        }

        if (!usuario.getEmail().matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));
        usuario.setCep(usuario.getCep().replaceAll("[^0-9]", ""));

        return usuarioDAO.inserir(usuario);
    }

    // Buscar usuário por ID
    public Usuario buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        return usuario;
    }

    // Listar usuários
    public List<Usuario> listarTodos() {

        return usuarioDAO.listarTodos();
    }

    // Atualizar usuário
    public void atualizar(Usuario usuario) {

        validarCampos(usuario);

        if (usuario.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (usuarioDAO.existePorEmailExcetoId(
                usuario.getEmail(),
                usuario.getId()
        )) {
            throw new EmailExistsException("Email já cadastrado");
        }

        if (!usuario.getCep().matches(Regex.CEP)) {
            throw new InvalidCepRegexException("Formato do CEP inválido");
        }

        if (!usuario.getEmail().matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        if (empresaDAO.buscarPorId(usuario.getEmpresa().getId()) == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        usuario.setCep(usuario.getCep().replaceAll("[^0-9]", ""));

        if (usuarioDAO.atualizar(usuario) == 0) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        historicoUsuarioReposicaoDAO.atualizarDadosUsuario(usuario.getId());
    }

    // Deletar usuário por ID
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        usuarioDAO.deletarPorId(id);
    }

    // Verificar email existente
    public boolean existePorEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ServiceException("Email é obrigatório");
        }

        if (!email.matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        return usuarioDAO.existePorEmail(email);
    }

    // Verificar CPF existente
    public boolean existePorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new ServiceException("O CPF é obrigatório");
        }

        if (!cpf.matches(Regex.CPF)) {
            throw new InvalidCpfRegexException("Formato do CPF inválido");
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        return usuarioDAO.existePorCpf(cpf);
    }

    // Verificar email existente, exceto ID
    public boolean existePorEmailExcetoId(String email, int id) {

        if (email == null || email.isBlank()) {
            throw new ServiceException("Email é obrigatório");
        }

        if (!email.matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        return usuarioDAO.existePorEmailExcetoId(email, id);
    }

    // Verificar CPF existente, exceto ID
    public boolean existePorCpfExcetoId(String cpf, int id) {

        if (cpf == null || cpf.isBlank()) {
            throw new ServiceException("CPF é obrigatório");
        }

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (!cpf.matches(Regex.CPF)) {
            throw new InvalidCpfRegexException("Formato do CPF inválido");
        }

        cpf = cpf.replaceAll("[^0-9]", "");

        return usuarioDAO.existePorCpfExcetoId(cpf, id);
    }

    // Buscar usuário por email
    public Usuario buscarPorEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ServiceException("Email é obrigatório");
        }

        if (!email.matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        return usuario;
    }

    // Validar campos do usuário
    private void validarCampos(Usuario usuario) {

        if (usuario == null) {
            throw new ServiceException("Usuário não pode ser nulo");
        }

        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ServiceException("Nome é obrigatório");
        }

        if (usuario.getNome().matches(".*\\d.*")) {
            throw new ServiceException("O nome não pode conter números");
        }

        if (usuario.getEmpresa() == null) {
            throw new ServiceException("Empresa não pode ser nula");
        }

        if (usuario.getSenha() == null || usuario.getSenha().length() < 8) {
            throw new ServiceException("A senha deve ter pelo menos 8 caracteres");
        }

        if (usuario.getSobrenome() == null || usuario.getSobrenome().isBlank()) {
            throw new ServiceException("Sobrenome é obrigatório");
        }

        if (usuario.getDataNascimento() == null) {
            throw new ServiceException("Data de nascimento é obrigatória");
        }

        if (usuario.getDataNascimento().isAfter(LocalDate.now())) {
            throw new ServiceException("Data de nascimento não pode ser futura");
        }

        if (usuario.getCep() == null || usuario.getCep().isBlank()) {
            throw new ServiceException("CEP é obrigatório");
        }

        if (usuario.getEmail() == null || usuario.getEmail().isBlank()) {
            throw new ServiceException("Email é obrigatório");
        }
    }

    // Listar gôndolas do usuário
    public List<Gondola> listarGondolasPorUsuario(Usuario usuario) {

        return usuarioGondolaDAO.listarGondolasPorUsuario(usuario);
    }

    // Pesquisar usuários
    public List<Usuario> pesquisar(String pesquisa) {

        return usuarioDAO.pesquisar(pesquisa);
    }
}
