package com.kairos.servlet;

import com.kairos.model.Alerta;
import com.kairos.model.Gondola;
import com.kairos.model.Produto;
import com.kairos.model.Usuario;
import com.kairos.model.enums.StatusAlerta;
import com.kairos.service.AlertaService;
import com.kairos.service.GondolaService;
import com.kairos.service.ProdutoService;
import com.kairos.service.UsuarioService;
import com.kairos.utils.exceptions.notfound.GondolaNotFoundException;
import com.kairos.utils.exceptions.notfound.ProdutoNotFoundException;
import com.kairos.utils.exceptions.notfound.UsuarioNotFoundException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/alertas")
public class AlertaServlet extends HttpServlet {

    private final AlertaService alertaService = new AlertaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final GondolaService gondolaService = new GondolaService();
    private final ProdutoService produtoService = new ProdutoService();

    // Carrega os dados necessários para a página de alertas
    private void carregarDados(HttpServletRequest request) {

        List<Alerta> alertas = alertaService.listarTodos();
        List<Usuario> usuarios = usuarioService.listarTodos();
        List<Gondola> gondolas = gondolaService.listarTodos();
        List<Produto> produtos = produtoService.listarTodos();

        request.setAttribute("alertas", alertas);
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("gondolas", gondolas);
        request.setAttribute("produtos", produtos);
    }

    // Exibe os erros de validação no formulário correspondente
    private void exibirErros(HttpServletRequest request, HttpServletResponse response,
                             Map<String, String> erros, Integer idAlerta) throws ServletException, IOException {

        if (idAlerta == null) {

            request.setAttribute("erros", erros);

        } else {

            request.setAttribute("errosAtualizacao", erros);
            request.setAttribute("idAlertaAtualizacao", idAlerta);
        }

        carregarDados(request);

        request.getRequestDispatcher("/WEB-INF/views/admin/alertas.jsp")
                .forward(request, response);
    }

    // Valida os IDs informados no cadastro
    private Integer validarId(String valor, String campo, String nome, Map<String, String> erros) {

        if (valor == null || valor.isBlank()) {

            erros.put(campo, nome + " é obrigatório");

            return null;
        }

        try {

            int id = Integer.parseInt(valor.trim());

            if (id <= 0) {

                erros.put(campo, "ID de " + nome.toLowerCase() + " deve ser maior que zero");

                return null;
            }

            return id;

        } catch (NumberFormatException e) {

            erros.put(campo, "ID de " + nome.toLowerCase() + " inválido");

            return null;
        }
    }

    // Valida a descrição do alerta
    private void validarDescricao(String descricao, Map<String, String> erros) {

        if (descricao == null || descricao.isBlank()) {
            erros.put("descricaoAlerta", "Descrição do alerta é obrigatória");
        }
    }

    // Valida o status informado para o alerta
    private StatusAlerta validarStatus(String valor, Map<String, String> erros) {

        if (valor == null || valor.isBlank()) {

            erros.put("statusAlerta", "Status do alerta é obrigatório");

            return null;
        }

        try {

            return StatusAlerta.valueOf(valor);

        } catch (IllegalArgumentException e) {

            erros.put("statusAlerta", "Status do alerta inválido");

            return null;
        }
    }

    // Valida a data de resolução dos alertas concluídos
    private Timestamp validarDataResolucao(String valor, StatusAlerta status,
                                           Date dataRuptura, Map<String, String> erros) {

        if (status != StatusAlerta.CONCLUIDO) {
            return null;
        }

        if (valor == null || valor.isBlank()) {

            erros.put("dataResolucao", "Data da resolução é obrigatória para alertas concluídos");

            return null;
        }

        try {

            LocalDateTime data = LocalDateTime.parse(valor);

            if (data.isAfter(LocalDateTime.now())) {

                erros.put("dataResolucao", "Data da resolução não pode ser futura");

                return null;
            }

            if (dataRuptura != null && data.toLocalDate().isBefore(dataRuptura.toLocalDate())) {

                erros.put("dataResolucao", "Data da resolução não pode ser anterior à ruptura");

                return null;
            }

            return Timestamp.valueOf(data);

        } catch (DateTimeParseException e) {

            erros.put("dataResolucao", "Data da resolução inválida");

            return null;
        }
    }

    // Processa a exclusão, pesquisa e listagem de alertas
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                try {

                    int idAlerta = Integer.parseInt(id);

                    if (idAlerta > 0) {
                        alertaService.deletarPorId(idAlerta);
                    }

                } catch (NumberFormatException e) {

                }
            }

            response.sendRedirect(request.getContextPath() + "/admin/alertas");

            return;
        }

        String busca = request.getParameter("busca");

        List<Alerta> alertas;

        if (busca != null && !busca.isBlank()) {
            alertas = alertaService.pesquisar(busca.trim());
        } else {
            alertas = alertaService.listarTodos();
        }

        request.setAttribute("alertas", alertas);
        request.setAttribute("usuarios", usuarioService.listarTodos());
        request.setAttribute("gondolas", gondolaService.listarTodos());
        request.setAttribute("produtos", produtoService.listarTodos());
        request.setAttribute("busca", busca);

        request.getRequestDispatcher("/WEB-INF/views/admin/alertas.jsp")
                .forward(request, response);
    }

    // Processa o cadastro ou a atualização de alertas
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String descricaoAlerta = request.getParameter("descricaoAlerta");
        String statusParam = request.getParameter("statusAlerta");
        String dataResolucaoParam = request.getParameter("dataResolucao");

        Map<String, String> erros = new HashMap<>();

        // Valida os campos comuns do formulário
        validarDescricao(descricaoAlerta, erros);

        StatusAlerta statusAlerta = validarStatus(statusParam, erros);

        if (id != null) {

            // Atualiza os dados de um alerta existente
            int idAlerta;

            try {

                idAlerta = Integer.parseInt(id);

                if (idAlerta <= 0) {

                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do alerta inválido");

                    return;
                }

            } catch (NumberFormatException e) {

                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID do alerta inválido");

                return;
            }

            Alerta alerta = alertaService.buscarPorId(idAlerta);

            // Valida a data de resolução do alerta
            Timestamp dataResolucao = validarDataResolucao(
                    dataResolucaoParam,
                    statusAlerta,
                    alerta.getDataRuptura(),
                    erros
            );

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                exibirErros(request, response, erros, idAlerta);

                return;
            }

            // Mantém usuário, gôndola, produto e data da ruptura originais
            alerta.setDescricaoAlerta(descricaoAlerta.trim());
            alerta.setStatusAlerta(statusAlerta);
            alerta.setDataResolucao(dataResolucao);

            alertaService.atualizar(alerta);

        } else {

            // Cadastra um novo alerta
            String idUsuario = request.getParameter("idUsuario");
            String idGondola = request.getParameter("idGondola");
            String idProduto = request.getParameter("idProduto");
            String dataRupturaParam = request.getParameter("dataRuptura");

            // Valida os IDs informados
            Integer usuarioId = validarId(idUsuario, "idUsuario", "Usuário", erros);
            Integer gondolaId = validarId(idGondola, "idGondola", "Gôndola", erros);
            Integer produtoId = validarId(idProduto, "idProduto", "Produto", erros);

            // Busca e valida o usuário informado
            Usuario usuario = null;

            if (usuarioId != null) {

                try {

                    usuario = usuarioService.buscarPorId(usuarioId);

                } catch (UsuarioNotFoundException e) {
                    erros.put("idUsuario", "Usuário não encontrado");
                }
            }

            // Busca e valida a gôndola informada
            Gondola gondola = null;

            if (gondolaId != null) {

                try {

                    gondola = gondolaService.buscarPorId(gondolaId);

                } catch (GondolaNotFoundException e) {
                    erros.put("idGondola", "Gôndola não encontrada");
                }
            }

            // Busca e valida o produto informado
            Produto produto = null;

            if (produtoId != null) {

                try {

                    produto = produtoService.buscarPorId(produtoId);

                } catch (ProdutoNotFoundException e) {
                    erros.put("idProduto", "Produto não encontrado");
                }
            }

            // Converte e valida a data da ruptura
            Date dataRuptura = null;

            if (dataRupturaParam == null || dataRupturaParam.isBlank()) {

                erros.put("dataRuptura", "Data da ruptura é obrigatória");

            } else {

                try {

                    LocalDate data = LocalDate.parse(dataRupturaParam);

                    if (data.isAfter(LocalDate.now())) {

                        erros.put("dataRuptura", "Data da ruptura não pode ser futura");

                    } else {

                        dataRuptura = Date.valueOf(data);
                    }

                } catch (DateTimeParseException e) {
                    erros.put("dataRuptura", "Data da ruptura inválida");
                }
            }

            // Valida a data de resolução do alerta
            Timestamp dataResolucao = validarDataResolucao(
                    dataResolucaoParam,
                    statusAlerta,
                    dataRuptura,
                    erros
            );

            // Retorna ao formulário caso existam erros de validação
            if (!erros.isEmpty()) {

                exibirErros(request, response, erros, null);

                return;
            }

            // Cria e cadastra o alerta validado
            Alerta alerta = new Alerta(
                    usuario,
                    gondola,
                    produto,
                    dataRuptura,
                    descricaoAlerta.trim(),
                    statusAlerta,
                    dataResolucao
            );

            alertaService.cadastrar(alerta);
        }

        response.sendRedirect(request.getContextPath() + "/admin/alertas");
    }
}
