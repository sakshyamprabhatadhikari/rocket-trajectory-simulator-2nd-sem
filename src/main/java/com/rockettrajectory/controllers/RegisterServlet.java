package com.rockettrajectory.controllers;

import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.UserService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * RegisterServlet
 * ---------------
 * Handles new-engineer registration.  Validates every field and prevents
 * duplicates by cross-referencing email + phone (the unique identifier
 * called for in the coursework brief).
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String fullName     = request.getParameter("fullName");
        String email        = request.getParameter("email");
        String phone        = request.getParameter("phone");
        String password     = request.getParameter("password");
        String confirm      = request.getParameter("confirmPassword");
        String organization = request.getParameter("organization");
        String country      = request.getParameter("country");

        // Preserve form values for re-rendering on error
        request.setAttribute("fullName",     fullName);
        request.setAttribute("email",        email);
        request.setAttribute("phone",        phone);
        request.setAttribute("organization", organization);
        request.setAttribute("country",      country);

        // ---------- validation ----------
        if (!ValidationUtil.isValidName(fullName)) {
            forwardWithError(request, response,
                    "Full name must contain only letters and spaces.");
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            forwardWithError(request, response,
                    "Please enter a valid email address.");
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            forwardWithError(request, response,
                    "Phone number must be exactly 10 digits.");
            return;
        }
        if (!ValidationUtil.isValidPassword(password)) {
            forwardWithError(request, response,
                    "Password must be at least 8 characters and contain "
                    + "both letters and digits.");
            return;
        }
        if (!password.equals(confirm)) {
            forwardWithError(request, response,
                    "Password and confirm-password do not match.");
            return;
        }
        if (ValidationUtil.isBlank(organization) || ValidationUtil.isBlank(country)) {
            forwardWithError(request, response,
                    "Organization and country are required.");
            return;
        }

        try {
            // ---------- duplicate check ----------
            if (userService.existsByEmail(email.trim())) {
                forwardWithError(request, response,
                        "An account with this email already exists.");
                return;
            }
            if (userService.existsByPhone(phone.trim())) {
                forwardWithError(request, response,
                        "An account with this phone number already exists.");
                return;
            }

            UserModel user = new UserModel(
                    fullName.trim(), email.trim(), phone.trim(),
                    password, "ENGINEER",
                    organization.trim(), country.trim());

            if (userService.register(user)) {
                response.sendRedirect(request.getContextPath()
                        + "/login?registered=1");
            } else {
                forwardWithError(request, response,
                        "Registration failed. Please try again.");
            }
        } catch (Exception e) {
            forwardWithError(request, response,
                    "Something went wrong. Please try again later.");
        }
    }

    /** Forwards back to the registration page with a friendly error message. */
    private void forwardWithError(HttpServletRequest req, HttpServletResponse res, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/pages/register.jsp").forward(req, res);
    }
}
