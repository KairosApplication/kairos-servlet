package com.kairos.servlet.auth;

import com.kairos.utils.connection.ConnectionFactory;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/** Opt-in guard for the isolated fair instance; normal deployment is unchanged. */
@WebFilter("/*")
public class RpaDemoFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        if (ConnectionFactory.isRpaDemo()) {
            try (Connection connection = ConnectionFactory.getConnection()) {
                response.setHeader("X-Kairos-Demo-Database", connection.getCatalog());
            } catch (SQLException | RuntimeException e) {
                response.sendError(503, "Configuracao do banco demo indisponivel");
                return;
            }
            response.setHeader("Cache-Control", "no-store");
            String path = request.getRequestURI().substring(request.getContextPath().length());
            if (path.startsWith("/admin/") && (request.getSession(false) == null
                    || request.getSession(false).getAttribute("authenticatedAdminId") == null)) {
                response.sendRedirect(request.getContextPath() + "/login-page");
                return;
            }
        }
        chain.doFilter(req, res);
    }
}
