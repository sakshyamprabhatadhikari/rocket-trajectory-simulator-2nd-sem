package com.rockettrajectory.controllers;

import com.rockettrajectory.model.InquiryModel;
import com.rockettrajectory.service.InquiryService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ContactServlet
 * --------------
 * Renders the public Contact page and persists submitted inquiries.
 *
 * Validation runs in two stages:
 *   1. Blank-field checks — show clear "X is required" messages first.
 *   2. Format checks — only run on non-blank fields, validate format
 *      via ValidationUtil (name regex, email regex).
 */
@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name    = request.getParameter("fullName");
        String email   = request.getParameter("email");
        String subject = request.getParameter("subject");
        String message = request.getParameter("message");

        request.setAttribute("fullName", name);
        request.setAttribute("email",    email);
        request.setAttribute("subject",  subject);
        request.setAttribute("message",  message);

        // ---------- Stage 1: blank-field checks (clear UX first) ----------
        if (ValidationUtil.isBlank(name)
            && ValidationUtil.isBlank(email)
            && ValidationUtil.isBlank(subject)
            && ValidationUtil.isBlank(message)) {
            forwardWithError(request, response, "All fields are required.");
            return;
        }
        if (ValidationUtil.isBlank(name)) {
            forwardWithError(request, response, "Full name is required.");
            return;
        }
        if (ValidationUtil.isBlank(email)) {
            forwardWithError(request, response, "Email is required.");
            return;
        }
        if (ValidationUtil.isBlank(subject)) {
            forwardWithError(request, response, "Subject is required.");
            return;
        }
        if (ValidationUtil.isBlank(message)) {
            forwardWithError(request, response, "Message is required.");
            return;
        }

        // ---------- Stage 2: format validation ----------
        if (!ValidationUtil.isValidName(name)) {
            forwardWithError(request, response,
                    "Full name must contain only letters and spaces.");
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            forwardWithError(request, response,
                    "Please enter a valid email address.");
            return;
        }

        try {
            InquiryModel inquiry = new InquiryModel(
                    name.trim(), email.trim(), subject.trim(), message.trim());

            if (inquiryService.save(inquiry)) {
                request.setAttribute("success",
                        "Thank you! Your inquiry has been submitted. We will get back to you shortly.");
                // clear form on success
                request.setAttribute("fullName", "");
                request.setAttribute("email",    "");
                request.setAttribute("subject",  "");
                request.setAttribute("message",  "");
            } else {
                request.setAttribute("error", "Could not submit your inquiry. Please try again.");
            }
        } catch (Exception e) {
            request.setAttribute("error", "Something went wrong. Please try again later.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(request, response);
    }

    /** Forwards back to the contact page with a friendly error message. */
    private void forwardWithError(HttpServletRequest req, HttpServletResponse res, String msg)
            throws ServletException, IOException {
        req.setAttribute("error", msg);
        req.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(req, res);
    }
}