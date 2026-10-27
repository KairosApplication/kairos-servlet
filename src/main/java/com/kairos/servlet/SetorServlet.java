
package com.kairos.servlet;

import com.kairos.model.Setor;
import com.kairos.service.SetorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/setores")
public class SetorServlet extends HttpServlet {

    private final SetorService setorService = new SetorService();

    // Listar, pesquisar e deletar setores
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String acao = request.getParameter("acao");

        // Deletar setor
        if ("deletar".equals(acao)) {

            String id = request.getParameter("id");

            if (id != null && !id.isBlank()) {
                int idSetor = Integer.parseInt(id);
                setorService.deletarPorId(idSetor);
            }

            response.sendRedirect(request.getContextPath() + "/admin/setores");
            return;
        }

        // Pesquisar ou listar setores
        String busca = request.getParameter("busca");

        List<Setor> setores;

        if (busca != null && !busca.isBlank()) {
            setores = setorService.pesquisar(busca.trim());
        } else {
            setores = setorService.listarTodos();
        }

        request.setAttribute("setores", setores);

        request.getRequestDispatcher("/WEB-INF/views/admin/setores.jsp").forward(request, response);
    }

    // Cadastrar e atualizar setores
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String id = request.getParameter("id");
        String nome = request.getParameter("nome");
        String categoria = request.getParameter("categoria");

        // Atualizar setor
        if (id != null) {

            int idSetor = Integer.parseInt(id);

            Setor setor = setorService.buscarPorId(idSetor);

            Map<String, String> erros = new HashMap<>();

            // Validar nome
            if (nome == null || nome.isBlank()) {
                erros.put("nome", "Nome é obrigatório");
            }

            // Validar categoria
            if (categoria == null || categoria.isBlank()) {
                erros.put("categoria", "Categoria é obrigatória");
            }

            // Exibir erros de atualização
            if (!erros.isEmpty()) {

                request.setAttribute("errosAtualizacao", erros);
                request.setAttribute("idSetorAtualizacao", idSetor);

                List<Setor> setores = setorService.listarTodos();

                request.setAttribute("setores", setores);

                request.getRequestDispatcher("/WEB-INF/views/admin/setores.jsp").forward(request, response);
                return;
            }

            setor.setNome(nome);
            setor.setCategoria(categoria);

            setorService.atualizar(setor);

        } else {

            // Cadastrar setor
            Map<String, String> erros = new HashMap<>();

            // Validar nome
            if (nome == null || nome.isBlank()) {
                erros.put("nome", "Nome é obrigatório");
            } else if (setorService.existePorNome(nome)) {
                erros.put("nome", "Nome já cadastrado");
            }

            // Validar categoria
            if (categoria == null || categoria.isBlank()) {
                erros.put("categoria", "Categoria é obrigatória");
            }

            // Exibir erros de cadastro
            if (!erros.isEmpty()) {

                request.setAttribute("erros", erros);

                List<Setor> setores = setorService.listarTodos();

                request.setAttribute("setores", setores);

                request.getRequestDispatcher("/WEB-INF/views/admin/setores.jsp").forward(request, response);
                return;
            }

            Setor setor = new Setor(nome, categoria);

            setorService.cadastrar(setor);
        }

        // Redirecionar para listagem
        response.sendRedirect(request.getContextPath() + "/admin/setores");
    }
}
