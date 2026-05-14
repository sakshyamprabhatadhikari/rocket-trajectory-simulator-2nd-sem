package com.rockettrajectory.controllers;

import com.rockettrajectory.service.UserService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ForgotPasswordServlet
 * ---------------------
 * Generates a one-time reset token for the supplied email.  In a real
 * deployment the token would be e-mailed; for coursework demonstration it
 * is shown directly on the next page so the marker can verify the flow.
 */
@WebServlet("/forgot-password")
public class ForgotPasswordServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp")
                   .forward(request, response);
            return;
        }

        try {
            if (userService.findByEmail(email.trim()) == null) {
                request.setAttribute("error", "No account is registered with that email.");
                request.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp")
                       .forward(request, response);
                return;
            }
            String token = userService.generateResetToken(email.trim());
            request.setAttribute("token", token);
            request.setAttribute("success",
                    "A reset link has been generated. Use the link below to set a new password.");
            request.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp")
                   .forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again later.");
            request.getRequestDispatcher("/WEB-INF/pages/forgot-password.jsp")
                   .forward(request, response);
        }
    }
}
