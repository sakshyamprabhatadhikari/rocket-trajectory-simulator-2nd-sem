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

        if (!ValidationUtil.isValidName(name)) {
            request.setAttribute("error", "Full name must contain only letters and spaces.");
            request.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(request, response);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            request.setAttribute("error", "Please enter a valid email address.");
            request.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(request, response);
            return;
        }
        if (ValidationUtil.isBlank(subject) || ValidationUtil.isBlank(message)) {
            request.setAttribute("error", "Subject and message are required.");
            request.getRequestDispatcher("/WEB-INF/pages/contact.jsp").forward(request, response);
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
}
