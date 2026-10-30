package com.kairos.service;

import com.kairos.dao.AdminDAO;
import com.kairos.model.Admin;

public class AdminService {

    // Atributos
    private final AdminDAO adminDAO;

    // Construtor
    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    // Busca um administrador pelo email
    public Admin buscarPorEmail(String email) {

        return adminDAO.buscarPorEmail(email);
    }
}
