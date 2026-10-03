package com.kairos.servlet;

import com.kairos.model.Empresa;
import com.kairos.model.enums.TipoPlano;
import com.kairos.service.EmpresaService;
import com.kairos.utils.Regex;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/empresas")
public class EmpresaServlet extends HttpServlet {

    private final EmpresaService empresaService = new EmpresaService();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        // ==========================
        // DELETAR EMPRESA
        // ==========================

        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {

                int idEmpresa = Integer.parseInt(id);

                empresaService.deletarPorId(idEmpresa);
            }

            response.sendRedirect(
                    request.getContextPath() + "/admin/empresas"
            );

            return;
        }

        // ==========================
        // LISTAR EMPRESAS
        // ==========================

        List<Empresa> empresas = empresaService.listarTodas();

        request.setAttribute(
                "empresas",
                empresas
        );

        request.getRequestDispatcher(
                "/WEB-INF/views/admin/empresa.jsp"
        ).forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String id = request.getParameter("id");

        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj");
        String tipoPlano = request.getParameter("tipoPlano");

        // ==========================
        // ATUALIZAR EMPRESA
        // ==========================

        if (id != null) {

            int idEmpresa = Integer.parseInt(id);

            Empresa empresa =
                    empresaService.buscarPorId(idEmpresa);

            Map<String, String> erros = new HashMap<>();

            // ==========================
            // NOME
            // ==========================

            if (nome == null || nome.isBlank()) {

                erros.put(
                        "nome",
                        "Nome é obrigatório"
                );

            }

            // ==========================
            // CNPJ
            // ==========================

            if (cnpj == null || cnpj.isBlank()) {

                erros.put(
                        "cnpj",
                        "CNPJ é obrigatório"
                );

            } else if (!cnpj.matches(Regex.CNPJ)) {

                erros.put(
                        "cnpj",
                        "Formato do CNPJ inválido"
                );

            } else if (
                    empresaService.existePorCnpjExcetoId(
                            cnpj,
                            idEmpresa
                    )
            ) {

                erros.put(
                        "cnpj",
                        "CNPJ já cadastrado"
                );
            }

            // ==========================
            // TIPO PLANO
            // ==========================

            if (tipoPlano == null || tipoPlano.isBlank()) {

                erros.put(
                        "tipoPlano",
                        "Tipo de plano é obrigatório"
                );
            }

            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "errosAtualizacao",
                        erros
                );

                request.setAttribute(
                        "idEmpresaAtualizacao",
                        idEmpresa
                );

                List<Empresa> empresas =
                        empresaService.listarTodas();

                request.setAttribute(
                        "empresas",
                        empresas
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/empresa.jsp"
                ).forward(request, response);

                return;
            }

            // ==========================
            // ATUALIZAR
            // ==========================

            empresa.setNome(nome);
            empresa.setCnpj(cnpj);
            empresa.setTipoPlano(
                    TipoPlano.valueOf(tipoPlano)
            );

            empresaService.atualizar(empresa);

        } else {

            // ==========================
            // ADICIONAR EMPRESA
            // ==========================

            Map<String, String> erros =
                    new HashMap<>();

            // ==========================
            // NOME
            // ==========================

            if (nome == null || nome.isBlank()) {

                erros.put(
                        "nome",
                        "Nome é obrigatório"
                );
            }

            // ==========================
            // CNPJ
            // ==========================

            if (cnpj == null || cnpj.isBlank()) {

                erros.put(
                        "cnpj",
                        "CNPJ é obrigatório"
                );

            } else if (!cnpj.matches(Regex.CNPJ)) {

                erros.put(
                        "cnpj",
                        "Formato do CNPJ inválido"
                );

            } else if (empresaService.existePorCnpj(cnpj)) {

                erros.put(
                        "cnpj",
                        "CNPJ já cadastrado"
                );
            }

            // ==========================
            // TIPO PLANO
            // ==========================

            if (tipoPlano == null || tipoPlano.isBlank()) {

                erros.put(
                        "tipoPlano",
                        "Tipo de plano é obrigatório"
                );
            }

            // ==========================
            // NOME DUPLICADO
            // ==========================

            if (
                    nome != null &&
                            !nome.isBlank() &&
                            empresaService.existePorNome(nome)
            ) {

                erros.put(
                        "nome",
                        "Nome já cadastrado"
                );
            }

            // ==========================
            // EXISTEM ERROS
            // ==========================

            if (!erros.isEmpty()) {

                request.setAttribute(
                        "erros",
                        erros
                );

                List<Empresa> empresas =
                        empresaService.listarTodas();

                request.setAttribute(
                        "empresas",
                        empresas
                );

                request.getRequestDispatcher(
                        "/WEB-INF/views/admin/empresa.jsp"
                ).forward(request, response);

                return;
            }

            // ==========================
            // CRIAR EMPRESA
            // ==========================

            Empresa empresa = new Empresa(
                    nome,
                    cnpj,
                    TipoPlano.valueOf(tipoPlano)
            );

            empresaService.cadastrar(empresa);
        }

        response.sendRedirect(
                request.getContextPath() + "/admin/empresas"
        );
    }
}