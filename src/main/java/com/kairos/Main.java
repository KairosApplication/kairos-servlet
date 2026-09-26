package com.kairos;

import com.kairos.dao.CompraDAO;
import com.kairos.dao.ProdutoDAO;
import com.kairos.dao.ReposicaoDAO;
import com.kairos.model.*;
import com.kairos.model.enums.TipoPlano;
import com.kairos.model.enums.TipoUsuario;
import com.kairos.service.*;
import com.kairos.utils.exceptions.exists.CnpjExistsException;
import com.kairos.utils.exceptions.invalid.InvalidCnpjRegexException;
import com.kairos.utils.exceptions.notfound.EmpresaNotFoundException;
import com.kairos.utils.exceptions.system.DAOException;
import com.kairos.utils.exceptions.system.ServiceException;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EmpresaService empresaService = new EmpresaService();

        try {
            Empresa empresa = empresaService.buscarPorId(1);
            empresa.setTipoPlano(null);

            empresaService.atualizar(empresa);

        } catch (ServiceException e) {
            System.out.println(e.getMessage());
        }

    }
}
