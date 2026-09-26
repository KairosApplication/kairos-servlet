package com.kairos.service;

import com.kairos.dao.ReposicaoDAO;
import com.kairos.model.Reposicao;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.ReposicaoNotFoundException;
import com.kairos.utils.exceptions.quantity.ReplenishmentQuantityException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;

public class ReposicaoService {

    private final ReposicaoDAO reposicaoDAO;
    private final ProdutoService produtoService;
    private final GondolaService gondolaService;

    public ReposicaoService() {
        this.reposicaoDAO = new ReposicaoDAO();
        this.produtoService = new ProdutoService();
        this.gondolaService = new GondolaService();
    }

    public Reposicao cadastrar(Reposicao reposicao) {

        validarCampos(reposicao);

        if (produtoService.buscarPorId(reposicao.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (gondolaService.buscarPorId(reposicao.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        return reposicaoDAO.inserir(reposicao);
    }

    public Reposicao buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        Reposicao reposicao = reposicaoDAO.buscarPorId(id);

        if (reposicao == null) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }

        return reposicao;
    }

    public List<Reposicao> listarTodos() {

        return reposicaoDAO.listarTodos();
    }

    public void atualizar(Reposicao reposicao) {

        validarCampos(reposicao);

        if (reposicao.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (produtoService.buscarPorId(reposicao.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (gondolaService.buscarPorId(reposicao.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (reposicaoDAO.atualizar(reposicao) == 0) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (reposicaoDAO.buscarPorId(id) == null) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }

        reposicaoDAO.deletarPorId(id);
    }

    private void validarCampos(Reposicao reposicao) {

        if (reposicao == null) {
            throw new ServiceException("Reposição não pode ser nula");
        }

        if (reposicao.getProduto() == null) {
            throw new ServiceException("Produto não pode ser nulo");
        }

        if (reposicao.getGondola() == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (reposicao.getDataReposicao() == null) {
            throw new ServiceException("A data da reposição não pode ser nula");
        }

        if (reposicao.getDataReposicao().after(new Timestamp(System.currentTimeMillis()))) {
            throw new ServiceException("A data da reposição não pode ser futura");
        }

        if (reposicao.getQuantidadeReposto() < 1) {
            throw new ReplenishmentQuantityException(
                    "A quantidade reposta deve ser maior que 0"
            );
        }

        if (reposicao.getMotivoReposicao() == null || reposicao.getMotivoReposicao().isBlank()) {
            throw new ServiceException("Motivo da reposição é obrigatório");
        }
    }
}