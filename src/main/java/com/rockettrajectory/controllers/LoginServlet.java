package com.rockettrajectory.controllers;

import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.UserService;
import com.rockettrajectory.util.PasswordUtil;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * LoginServlet
 * ------------
 * Handles authentication.  Implements the coursework requirements:
 *   • Session-based login.
 *   • Account lockout after N consecutive wrong attempts.
 *   • Role-based redirect (ADMIN vs ENGINEER).
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // If already logged-in, push the user to their dashboard.
        HttpSession s = request.getSession(false);
        if (s != null && s.getAttribute("loggedUser") != null) {
            UserModel u = (UserModel) s.getAttribute("loggedUser");
            response.sendRedirect(request.getContextPath()
                    + ("ADMIN".equals(u.getRole()) ? "/admin/dashboard"
                                                   : "/engineer/dashboard"));
            return;
        }

        if ("session".equals(request.getParameter("error"))) {
            request.setAttribute("error",
                    "Please log in to continue.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email    = request.getParameter("email");
        String password = request.getParameter("password");

        // ----- basic validation -----
        if (ValidationUtil.isBlank(email) || ValidationUtil.isBlank(password)) {
            request.setAttribute("error", "Email and password are required.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            return;
        }

        try {
            UserModel user = userService.findByEmail(email.trim());
            if (user == null) {
                request.setAttribute("error", "Invalid credentials.");
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
                return;
            }

            // Auto-unlock if the lock window has expired
            userService.unlockIfExpired(user);

            if (user.isAccountLocked()) {
                request.setAttribute("error",
                        "Your account is temporarily locked due to too many failed "
                        + "attempts. Please try again later or reset your password.");
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
                return;
            }

            if (!PasswordUtil.matches(password, user.getPassword())) {
                userService.registerFailedAttempt(user);
                int remaining = UserService.MAX_FAILED_ATTEMPTS
                              - (user.getFailedAttempts() + 1);
                String msg = remaining > 0
                        ? "Invalid credentials. " + remaining + " attempt(s) remaining."
                        : "Account locked due to repeated wrong attempts.";
                request.setAttribute("error", msg);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
                return;
            }

            // ----- success -----
            userService.resetFailedAttempts(user.getUserId());
            HttpSession session = request.getSession(true);
            session.setAttribute("loggedUser", user);
            session.setMaxInactiveInterval(30 * 60);   // 30 minutes

            String target = "ADMIN".equals(user.getRole())
                    ? "/admin/dashboard" : "/engineer/dashboard";
            response.sendRedirect(request.getContextPath() + target);

        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again.");
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }
}
