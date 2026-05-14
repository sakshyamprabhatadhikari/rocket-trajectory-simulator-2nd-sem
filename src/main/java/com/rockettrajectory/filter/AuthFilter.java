package com.rockettrajectory.filter;

import com.rockettrajectory.model.UserModel;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * AuthFilter
 * ----------
 * Centralised redirect-management filter implementing two coursework
 * requirements:
 *   • Session-based authentication check on every protected URL.
 *   • Role-based authorisation – /admin/* requires ADMIN, /engineer/*
 *     requires ENGINEER.  Mismatched roles are sent to the unauthorised
 *     error page; unauthenticated users go to the login page.
 *
 * Public URLs (login, register, home, about, contact, static resources)
 * are skipped using a whitelist so this filter never blocks them.
 */
@WebFilter(urlPatterns = {"/admin/*", "/engineer/*"})
public class AuthFilter implements Filter {

    /** URL prefixes that bypass authentication entirely. */
    private static final Set<String> PUBLIC_PREFIXES = new HashSet<>(Arrays.asList(
            "/css/", "/js/", "/images/", "/favicon"
    ));

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  request  = (HttpServletRequest)  req;
        HttpServletResponse response = (HttpServletResponse) res;

        String ctx  = request.getContextPath();
        String path = request.getRequestURI().substring(ctx.length());

        // ----- 1. allow static resources -----
        for (String p : PUBLIC_PREFIXES) {
            if (path.startsWith(p)) { chain.doFilter(req, res); return; }
        }

        // ----- 2. require an authenticated user -----
        HttpSession session = request.getSession(false);
        UserModel user = (session == null) ? null
                : (UserModel) session.getAttribute("loggedUser");

        if (user == null) {
            response.sendRedirect(ctx + "/login?error=session");
            return;
        }

        // ----- 3. role-based authorisation -----
        if (path.startsWith("/admin/") && !"ADMIN".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(ctx + "/error/403");
            return;
        }
        if (path.startsWith("/engineer/") && !"ENGINEER".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(ctx + "/error/403");
            return;
        }

        // ----- 4. happy path -----
        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig fc) { }
    @Override public void destroy() { }
}
