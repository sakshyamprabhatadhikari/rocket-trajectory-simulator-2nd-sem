package com.rockettrajectory.controllers;

import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.UserService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ResetPasswordServlet
 * --------------------
 * Consumes a previously-generated reset token and lets the user set a
 * brand-new password.  Once consumed the token is cleared from the DB.
 */
@WebServlet("/reset-password")
public class ResetPasswordServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token = request.getParameter("token");
        if (ValidationUtil.isBlank(token)) {
            response.sendRedirect(request.getContextPath() + "/forgot-password");
            return;
        }
        try {
            UserModel u = userService.findByResetToken(token);
            if (u == null) {
                request.setAttribute("error", "Invalid or expired reset token.");
            }
        } catch (Exception ignored) { }
        request.setAttribute("token", token);
        request.getRequestDispatcher("/WEB-INF/pages/reset-password.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String token        = request.getParameter("token");
        String newPassword  = request.getParameter("password");
        String confirm      = request.getParameter("confirmPassword");

        request.setAttribute("token", token);

        if (!ValidationUtil.isValidPassword(newPassword)) {
            request.setAttribute("error",
                    "Password must be at least 8 characters and contain "
                    + "both letters and digits.");
            request.getRequestDispatcher("/WEB-INF/pages/reset-password.jsp")
                   .forward(request, response);
            return;
        }
        if (!newPassword.equals(confirm)) {
            request.setAttribute("error", "Passwords do not match.");
            request.getRequestDispatcher("/WEB-INF/pages/reset-password.jsp")
                   .forward(request, response);
            return;
        }
        try {
            if (userService.resetPassword(token, newPassword)) {
                response.sendRedirect(request.getContextPath()
                        + "/login?reset=1");
            } else {
                request.setAttribute("error", "Invalid or expired reset token.");
                request.getRequestDispatcher("/WEB-INF/pages/reset-password.jsp")
                       .forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/pages/reset-password.jsp")
                   .forward(request, response);
        }
    }
}
