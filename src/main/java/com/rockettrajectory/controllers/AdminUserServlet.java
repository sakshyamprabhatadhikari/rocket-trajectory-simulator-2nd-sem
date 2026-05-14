package com.rockettrajectory.controllers;

import com.rockettrajectory.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminUserServlet
 * ----------------
 * Lets the admin view all engineer accounts and delete a non-admin one.
 */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("users", userService.findAll());
        } catch (Exception e) {
            request.setAttribute("error", "Could not load users. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/admin-user-list.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (!"delete".equals(action)) {
            response.sendRedirect(request.getContextPath() + "/admin/users");
            return;
        }
        try {
            int id  = Integer.parseInt(request.getParameter("id"));
            boolean ok = userService.delete(id);
            response.sendRedirect(request.getContextPath()
                    + "/admin/users?msg=" + (ok ? "deleted" : "deletefail"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath()
                    + "/admin/users?msg=deletefail");
        }
    }
}
