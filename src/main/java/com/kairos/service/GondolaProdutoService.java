
package com.kairos.service;

import com.kairos.dao.GondolaProdutoDAO;
import com.kairos.model.Gondola;
import com.kairos.model.GondolaProduto;
import com.kairos.model.Produto;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.util.List;

public class GondolaProdutoService {

    private final GondolaProdutoDAO gondolaProdutoDAO;
    private final GondolaService gondolaService;
    private final ProdutoService produtoService;

    public GondolaProdutoService() {
        this.gondolaProdutoDAO = new GondolaProdutoDAO();
        this.gondolaService = new GondolaService();
        this.produtoService = new ProdutoService();
    }

    public void vincular(GondolaProduto gondolaProduto) {

        validarCampos(gondolaProduto);

        if (gondolaService.buscarPorId(gondolaProduto.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (produtoService.buscarPorId(gondolaProduto.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        gondolaProdutoDAO.vincular(gondolaProduto.getGondola(), gondolaProduto.getProduto());
    }

    public void desvincular(GondolaProduto gondolaProduto) {

        validarCampos(gondolaProduto);

        if (gondolaService.buscarPorId(gondolaProduto.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        if (produtoService.buscarPorId(gondolaProduto.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        gondolaProdutoDAO.desvincular(gondolaProduto.getGondola(), gondolaProduto.getProduto());
    }

    public List<Produto> listarProdutosPorGondola(Gondola gondola) {

        if (gondola == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (gondola.getId() <= 0) {
            throw new ServiceException("O id da gôndola deve ser maior que 0");
        }

        gondolaService.buscarPorId(gondola.getId());

        return gondolaProdutoDAO.listarProdutosPorGondola(gondola);
    }

    private void validarCampos(GondolaProduto gondolaProduto) {

        if (gondolaProduto == null) {
            throw new ServiceException(
                    "Relacionamento entre gôndola e produto não pode ser nulo"
            );
        }

        if (gondolaProduto.getGondola() == null) {
            throw new ServiceException("Gôndola não pode ser nula");
        }

        if (gondolaProduto.getGondola().getId() <= 0) {
            throw new ServiceException(
                    "O id da gôndola deve ser maior que 0"
            );
        }

        if (gondolaProduto.getProduto() == null) {
            throw new ServiceException("Produto não pode ser nulo");
        }

        if (gondolaProduto.getProduto().getId() <= 0) {
            throw new ServiceException(
                    "O id do produto deve ser maior que 0"
            );
        }
    }
}