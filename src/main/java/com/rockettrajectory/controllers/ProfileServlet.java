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
 * ProfileServlet
 * --------------
 * Profile self-management for both ADMIN and ENGINEER roles.
 * The same servlet handles "view profile", "update info" and
 * "change password" via the {@code action} parameter.
 *
 * Mounted under both /admin/* and /engineer/* via two URL patterns so the
 * AuthFilter still applies role checks correctly.
 */
@WebServlet({"/admin/profile", "/engineer/profile"})
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserModel current = (UserModel) session.getAttribute("loggedUser");
        String action = request.getParameter("action");

        try {
            if ("update".equals(action)) {
                handleUpdate(request, current);
            } else if ("password".equals(action)) {
                handlePasswordChange(request, current);
            }
            // refresh session copy
            UserModel fresh = userService.findById(current.getUserId());
            if (fresh != null) session.setAttribute("loggedUser", fresh);
        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/profile.jsp")
               .forward(request, response);
    }

    private void handleUpdate(HttpServletRequest request, UserModel current)
            throws Exception {
        String name  = request.getParameter("fullName");
        String phone = request.getParameter("phone");
        String org   = request.getParameter("organization");
        String ctry  = request.getParameter("country");

        if (!ValidationUtil.isValidName(name)) {
            request.setAttribute("error", "Full name must contain only letters and spaces.");
            return;
        }
        if (!ValidationUtil.isValidPhone(phone)) {
            request.setAttribute("error", "Phone number must be exactly 10 digits.");
            return;
        }
        if (ValidationUtil.isBlank(org) || ValidationUtil.isBlank(ctry)) {
            request.setAttribute("error", "Organization and country cannot be blank.");
            return;
        }
        // phone uniqueness if changed
        if (!phone.equals(current.getPhone())
                && userService.existsByPhone(phone.trim())) {
            request.setAttribute("error", "That phone number already belongs to another account.");
            return;
        }
        current.setFullName    (name.trim());
        current.setPhone       (phone.trim());
        current.setOrganization(org.trim());
        current.setCountry     (ctry.trim());

        if (userService.updateProfile(current)) {
            request.setAttribute("success", "Profile updated successfully.");
        } else {
            request.setAttribute("error", "Could not update profile.");
        }
    }

    private void handlePasswordChange(HttpServletRequest request, UserModel current)
            throws Exception {
        String oldPwd = request.getParameter("oldPassword");
        String newPwd = request.getParameter("newPassword");
        String conf   = request.getParameter("confirmPassword");

        if (!PasswordUtil.matches(oldPwd, current.getPassword())) {
            request.setAttribute("error", "Current password is incorrect.");
            return;
        }
        if (!ValidationUtil.isValidPassword(newPwd)) {
            request.setAttribute("error",
                    "New password must be at least 8 characters and contain "
                    + "both letters and digits.");
            return;
        }
        if (!newPwd.equals(conf)) {
            request.setAttribute("error", "New password and confirmation do not match.");
            return;
        }
        if (userService.changePassword(current.getUserId(), newPwd)) {
            request.setAttribute("success", "Password changed successfully.");
        } else {
            request.setAttribute("error", "Could not change password.");
        }
    }
}
