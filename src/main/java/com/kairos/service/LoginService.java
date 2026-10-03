package com.kairos.service;

import com.kairos.model.Admin;
import com.kairos.utils.exceptions.system.AdminException;
import com.kairos.utils.exceptions.system.ServiceException;

public class LoginService {

    private final AdminService adminService;

    public LoginService() {
        this.adminService = new AdminService();
    }

    public Admin autenticar(String email, String senha) {

        Admin admin = adminService.buscarPorEmail(email);

        if (admin == null) {
            throw new AdminException("Email ou senha inválidos");
        }

        if (!senha.equals(admin.getSenha())) {
            throw new AdminException("Email ou senha inválidos");
        }

        return admin;
    }
}