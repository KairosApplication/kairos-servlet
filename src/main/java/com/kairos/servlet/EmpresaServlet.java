package com.kairos.servlet;

import com.kairos.model.Empresa;
import com.kairos.model.TipoPlano;
import com.kairos.service.EmpresaService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/empresas")
public class EmpresaServlet extends HttpServlet {

    private final EmpresaService empresaService = new EmpresaService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String cnpj = request.getParameter("cnpj");
        TipoPlano tipoPlano = TipoPlano.valueOf(request.getParameter("tipo_plano"));

        Empresa empresa = new Empresa(cnpj, tipoPlano);

    }

}
