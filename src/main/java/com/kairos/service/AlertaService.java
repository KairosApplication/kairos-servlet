package com.kairos.service;

import com.kairos.dao.AlertaDAO;
import com.kairos.model.Alerta;
import com.kairos.utils.exceptions.notfound.AlertaNotFoundException;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;

public class AlertaService {

    private final AlertaDAO alertaDAO;
    private final UsuarioService usuarioService;
    private final GondolaService gondolaService;
    private final ProdutoService produtoService;

    public AlertaService() {
        this.alertaDAO = new AlertaDAO();
        this.usuarioService = new UsuarioService();
        this.gondolaService = new GondolaService();
        this.produtoService = new ProdutoService();
    }

    public Alerta cadastrar(Alerta alerta) {
        validarCampos(alerta);

        if (usuarioService.buscarPorId(alerta.getUsuario().getId()) == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        if (gondolaService.buscarPorId(alerta.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (produtoService.buscarPorId(alerta.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        return alertaDAO.inserir(alerta);
    }

    public Alerta buscarPorId(int id) {
        if (id <= 0) throw new ServiceException("O id deve ser maior que 0");

        Alerta alerta = alertaDAO.buscarPorId(id);

        if (alerta == null) {
            throw new AlertaNotFoundException("Alerta não encontrado");
        }

        return alerta;
    }

    public List<Alerta> listarTodos() {
        return alertaDAO.listarTodos();
    }

    public void atualizar(Alerta alerta) {
        validarCampos(alerta);

        if (alerta.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (usuarioService.buscarPorId(alerta.getUsuario().getId()) == null) {
            throw new UsuarioNotFoundException("Usuário não encontrado");
        }

        if (gondolaService.buscarPorId(alerta.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (produtoService.buscarPorId(alerta.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (alertaDAO.atualizar(alerta) == 0) {
            throw new AlertaNotFoundException("Alerta não encontrado");
        }
    }

    public void deletarPorId(int id) {
        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (alertaDAO.buscarPorId(id) == null) {
            throw new AlertaNotFoundException("Alerta não encontrado");
        }

        alertaDAO.deletarPorId(id);
    }

    private void validarCampos(Alerta alerta) {

        if (alerta == null) {
            throw new ServiceException("Alerta não pode ser nulo");
        }

        if (alerta.getUsuario() == null) {
            throw new ServiceException("Usuário não pode ser nulo");
        }

        if (alerta.getGondola() == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (alerta.getProduto() == null) {
            throw new ServiceException("Produto não pode ser nulo");
        }

        if (alerta.getDataRuptura() == null) {
            throw new ServiceException("A data da ruptura não pode ser nula");
        }

        if (alerta.getDataRuptura().after(new java.sql.Date(System.currentTimeMillis()))) {
            throw new ServiceException("A data da ruptura não pode ser futura");
        }

        if (alerta.getDescricaoAlerta() == null ||
                alerta.getDescricaoAlerta().isBlank()) {
            throw new ServiceException("Descrição do alerta é obrigatória");
        }

        if (alerta.getStatusAlerta() == null) {
            throw new ServiceException("Status do alerta não pode ser nulo");
        }

        if (alerta.getDataResolucao() == null) {
            throw new ServiceException("A data da resolução não pode ser nula");
        }

        if (alerta.getDataResolucao().after(new Timestamp(System.currentTimeMillis()))) {
            throw new ServiceException("A data da resolução não pode ser futura");
        }
    }
}