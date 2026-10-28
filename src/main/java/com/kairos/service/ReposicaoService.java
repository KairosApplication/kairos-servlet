package com.kairos.service;

import com.kairos.dao.HistoricoReposicaoDAO;
import com.kairos.dao.ReposicaoDAO;
import com.kairos.model.HistoricoReposicao;
import com.kairos.model.Reposicao;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.ReposicaoNotFoundException;
import com.kairos.utils.exceptions.quantity.ReplenishmentQuantityException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;

public class ReposicaoService {

    // Atributos
    private final ReposicaoDAO reposicaoDAO;
    private final HistoricoReposicaoDAO historicoReposicaoDAO;
    private final ProdutoService produtoService;
    private final GondolaService gondolaService;

    // Construtor
    public ReposicaoService() {
        this.reposicaoDAO = new ReposicaoDAO();
        this.historicoReposicaoDAO = new HistoricoReposicaoDAO();
        this.produtoService = new ProdutoService();
        this.gondolaService = new GondolaService();
    }

    // Cadastra uma reposição e salva seu histórico
    public Reposicao cadastrar(Reposicao reposicao) {

        validarCampos(reposicao);

        if (produtoService.buscarPorId(reposicao.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException("Produto não encontrado");
        }

        if (gondolaService.buscarPorId(reposicao.getGondola().getId()) == null) {
            throw new GondolaNotFoundException("Gôndola não encontrada");
        }

        Reposicao reposicaoSalva = reposicaoDAO.inserir(reposicao);

        if (reposicaoSalva == null) {
            throw new ServiceException("Não foi possível salvar a reposição");
        }

        HistoricoReposicao historico = new HistoricoReposicao(
                reposicaoSalva.getId(),
                reposicaoSalva.getProduto().getMarca(),
                reposicaoSalva.getProduto().getNome(),
                reposicaoSalva.getGondola().getId()
        );

        historicoReposicaoDAO.salvar(historico);

        return reposicaoSalva;
    }

    // Busca uma reposição pelo ID
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

    // Lista todas as reposições
    public List<Reposicao> listarTodos() {

        return reposicaoDAO.listarTodos();
    }

    // Atualiza os dados de uma reposição
    public void atualizar(Reposicao reposicao) {

        validarCamposAtualizacao(reposicao);

        if (reposicao.getId() <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (reposicaoDAO.atualizar(reposicao) == 0) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }
    }

    // Deleta uma reposição pelo ID
    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException("O id deve ser maior que 0");
        }

        if (reposicaoDAO.buscarPorId(id) == null) {
            throw new ReposicaoNotFoundException("Reposição não encontrada");
        }

        reposicaoDAO.deletarPorId(id);
    }

    // Valida os campos obrigatórios da reposição
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

    // Pesquisa reposições pelo termo informado
    public List<Reposicao> pesquisar(String pesquisa) {

        return reposicaoDAO.pesquisar(pesquisa);
    }

    // Valida os campos permitidos na atualização
    private void validarCamposAtualizacao(Reposicao reposicao) {

        if (reposicao == null) {
            throw new ServiceException("Reposição não pode ser nula");
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
