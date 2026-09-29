package com.kairos.service;

import com.kairos.dao.EmpresaDAO;
import com.kairos.dao.UsuarioDAO;

import com.kairos.model.enums.TipoUsuario;
import com.kairos.model.Usuario;

import com.kairos.utils.AuthorizationValidator;
import com.kairos.utils.Regex;
import com.kairos.utils.exceptions.exists.CpfExistsException;
import com.kairos.utils.exceptions.exists.EmailExistsException;
import com.kairos.utils.exceptions.regex.InvalidCepRegexException;
import com.kairos.utils.exceptions.regex.InvalidCpfRegexException;
import com.kairos.utils.exceptions.regex.InvalidEmailRegexException;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.AdminException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.time.LocalDate;
import java.util.List;

public class UsuarioService {

//    Atributos

    private final UsuarioDAO usuarioDAO;
    private final EmpresaDAO empresaDAO;

//    Construtor

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
        this.empresaDAO = new EmpresaDAO();
    }

//    Metodo que faz as validacoes para o cadastro de usuario
    public Usuario cadastrar(Usuario usuario) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        validarCampos(usuario);

        if (usuarioDAO.existePorEmail(usuario.getEmail())) {
            throw new EmailExistsException("Email já cadastrado");
        }

        if (usuarioDAO.existePorCpf(usuario.getCpf())) {
            throw new CpfExistsException("CPF já cadastrado");
        }

        if (usuario.getTipoUsuario() != TipoUsuario.FUNCIONARIO) {
            throw new ServiceException("Cadastro comum deve ser do tipo FUNCIONARIO");
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

//    Metodo que faz as validacoes da busca de usuario por id
    public Usuario buscarPorId(int id) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }
        return usuario;
    }

//    Metodo que busca listarTodos do DAO (sem validacoes)
    public List<Usuario> listarTodos(Usuario usuarioAtual) {

        AuthorizationValidator.validarAdmin(usuarioAtual);

        return usuarioDAO.listarTodos();
    }

//    Metodo que faz as validacoes de atualizar usuario
    public void atualizar(Usuario usuario) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        validarCampos(usuario);

        if (usuario.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (usuario.getTipoUsuario() != TipoUsuario.FUNCIONARIO) {
            throw new ServiceException("Tipo usuário comum deve ser FUNCIONARIO");
        }

        if (usuarioDAO.existePorEmailExcetoId(usuario.getEmail(), usuario.getId())) {
            throw new EmailExistsException("Email já cadastrado");
        }

        if (usuarioDAO.existePorCpfExcetoId(usuario.getCpf(), usuario.getId())) {
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

        if (empresaDAO.buscarPorId(usuario.getEmpresa().getId()) == null) {
            throw new EmpresaNotFoundException("Empresa não encontrada");
        }

        usuario.setCpf(usuario.getCpf().replaceAll("[^0-9]", ""));
        usuario.setCep(usuario.getCep().replaceAll("[^0-9]", ""));

        if (usuarioDAO.atualizar(usuario) == 0) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }
    }

//    Metodo que faz as validações para deletar usuario por id
    public void deletarPorId(int id) {

//        AuthorizationValidator.validarAdmin(usuarioAtual);

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Usuario usuario = usuarioDAO.buscarPorId(id);

        if (usuario == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        if (usuario.getTipoUsuario() == TipoUsuario.ADMIN) {
            throw new AdminException("Não é permitido deletar o Admin");
        }

        usuarioDAO.deletarPorId(id);
    }

    public boolean existePorEmail(String email) {

        if (email == null || email.isBlank()) {
            throw new ServiceException("Email é obrigatório");
        }

        if (!email.matches(Regex.EMAIL)) {
            throw new InvalidEmailRegexException("Formato do Email inválido");
        }

        return usuarioDAO.existePorEmail(email);
    }

    public boolean existePorCpf(String cpf) {

        if (cpf == null || cpf.isBlank()) {
            throw new ServiceException("O CPF é obrigatório");
        }

        if (!cpf.matches(Regex.CPF)) {
            throw new InvalidCpfRegexException("Formato do CPF inválido");
        }

        cpf = cpf.replaceAll("^[0-9]", "");

        return usuarioDAO.existePorCpf(cpf);
    }

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

        cpf = cpf.replaceAll("^[0-9]", "");

        return usuarioDAO.existePorCpfExcetoId(cpf, id);
    }

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

//    Metodo que valida os campos do usuario
    private void validarCampos(Usuario usuario) {

        if (usuario == null) {
            throw new ServiceException("Usuário não pode ser nulo");
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

        if (usuario.getNome() == null || usuario.getNome().isBlank()) {
            throw new ServiceException("Nome é obrigatório");
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

    public List<Usuario> pesquisar(String pesquisa) {

        return usuarioDAO.pesquisar(pesquisa);

    }
}
