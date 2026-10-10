package com.kairos.service;

import com.kairos.dao.AlertaDAO;
import com.kairos.dao.HistoricoAlertaDAO;
import com.kairos.model.Alerta;
import com.kairos.model.HistoricoAlerta;
import com.kairos.model.enums.StatusAlerta;
import com.kairos.utils.exceptions.notfound.AlertaNotFoundException;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Timestamp;
import java.util.List;

public class AlertaService {

    private final AlertaDAO alertaDAO;
    private final HistoricoAlertaDAO historicoAlertaDAO;
    private final UsuarioService usuarioService;
    private final GondolaService gondolaService;
    private final ProdutoService produtoService;

    public AlertaService() {
        this.alertaDAO = new AlertaDAO();
        this.historicoAlertaDAO = new HistoricoAlertaDAO();
        this.usuarioService = new UsuarioService();
        this.gondolaService = new GondolaService();
        this.produtoService = new ProdutoService();
    }

    public Alerta cadastrar(Alerta alerta) {

        validarCampos(alerta);

        if (usuarioService.buscarPorId(
                alerta.getUsuario().getId()) == null) {
            throw new UsuarioNotFoundException(
                    "Usuário não encontrado");
        }

        if (gondolaService.buscarPorId(
                alerta.getGondola().getId()) == null) {
            throw new GondolaNotFoundException(
                    "Gôndola não encontrada");
        }

        if (produtoService.buscarPorId(
                alerta.getProduto().getId()) == null) {
            throw new ProdutoNotFoundException(
                    "Produto não encontrado");
        }

        Alerta alertaSalvo = alertaDAO.inserir(alerta);

        if (alertaSalvo == null) {
            throw new ServiceException(
                    "Não foi possível salvar o alerta");
        }

        HistoricoAlerta historico = new HistoricoAlerta(
                alertaSalvo.getId(),
                alertaSalvo.getUsuario().getId(),
                alertaSalvo.getGondola().getId(),
                alertaSalvo.getProduto().getId()
        );

        HistoricoAlerta historicoSalvo =
                historicoAlertaDAO.salvar(historico);

        alertaSalvo.setHistoricoAlerta(historicoSalvo);

        return alertaSalvo;
    }

    public Alerta buscarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException(
                    "O id deve ser maior que 0");
        }

        Alerta alerta = alertaDAO.buscarPorId(id);

        if (alerta == null) {
            throw new AlertaNotFoundException(
                    "Alerta não encontrado");
        }

        return alerta;
    }

    public List<Alerta> listarTodos() {
        return alertaDAO.listarTodos();
    }

    public List<Alerta> pesquisar(String busca) {
        return alertaDAO.pesquisar(busca.trim());
    }

    public void atualizar(Alerta alerta) {

        if (alerta == null) {
            throw new ServiceException(
                    "Alerta não pode ser nulo");
        }

        if (alerta.getId() <= 0) {
            throw new ServiceException(
                    "O id deve ser maior que 0");
        }

        validarDescricaoStatusResolucao(alerta);

        if (alertaDAO.atualizar(alerta) == 0) {
            throw new AlertaNotFoundException(
                    "Alerta não encontrado");
        }
    }

    public void deletarPorId(int id) {

        if (id <= 0) {
            throw new ServiceException(
                    "O id deve ser maior que 0");
        }

        if (alertaDAO.buscarPorId(id) == null) {
            throw new AlertaNotFoundException(
                    "Alerta não encontrado");
        }

        alertaDAO.deletarPorId(id);
    }

    private void validarCampos(Alerta alerta) {

        if (alerta == null) {
            throw new ServiceException(
                    "Alerta não pode ser nulo");
        }

        if (alerta.getUsuario() == null) {
            throw new ServiceException(
                    "Usuário não pode ser nulo");
        }

        if (alerta.getGondola() == null) {
            throw new ServiceException(
                    "Gôndola não pode ser nula");
        }

        if (alerta.getProduto() == null) {
            throw new ServiceException(
                    "Produto não pode ser nulo");
        }

        if (alerta.getDataRuptura() == null) {
            throw new ServiceException(
                    "A data da ruptura não pode ser nula");
        }

        if (alerta.getDataRuptura().after(
                new java.sql.Date(System.currentTimeMillis()))) {
            throw new ServiceException(
                    "A data da ruptura não pode ser futura");
        }

        validarDescricaoStatusResolucao(alerta);
    }

    private void validarDescricaoStatusResolucao(Alerta alerta) {

        if (alerta.getDescricaoAlerta() == null
                || alerta.getDescricaoAlerta().isBlank()) {
            throw new ServiceException(
                    "Descrição do alerta é obrigatória");
        }

        if (alerta.getStatusAlerta() == null) {
            throw new ServiceException(
                    "Status do alerta não pode ser nulo");
        }

        if (alerta.getStatusAlerta() == StatusAlerta.CONCLUIDO) {

            if (alerta.getDataResolucao() == null) {
                throw new ServiceException(
                        "A data da resolução é obrigatória para alertas concluídos");
            }

            if (alerta.getDataResolucao().after(
                    new Timestamp(System.currentTimeMillis()))) {
                throw new ServiceException(
                        "A data da resolução não pode ser futura");
            }

            if (alerta.getDataRuptura() != null
                    && alerta.getDataResolucao().toLocalDateTime()
                    .toLocalDate().isBefore(
                            alerta.getDataRuptura().toLocalDate())) {
                throw new ServiceException(
                        "A data da resolução não pode ser anterior à ruptura");
            }
        }
    }
}