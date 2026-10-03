package com.kairos.service;

import com.kairos.dao.AdminDAO;
import com.kairos.model.Admin;


public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService() {
        this.adminDAO = new AdminDAO();
    }

    public Admin buscarPorEmail(String email) {

        return adminDAO.buscarPorEmail(email);

    }
}