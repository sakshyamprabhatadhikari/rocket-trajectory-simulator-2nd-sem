package com.rockettrajectory.controllers;

import com.rockettrajectory.service.InquiryService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminInquiryServlet
 * -------------------
 * Lists contact-form submissions and allows the admin to delete one.
 */
@WebServlet("/admin/inquiries")
public class AdminInquiryServlet extends HttpServlet {

    private final InquiryService inquiryService = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("inquiries", inquiryService.findAll());
        } catch (Exception e) {
            request.setAttribute("error", "Could not load inquiries. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/admin-inquiry-list.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            inquiryService.delete(id);
        } catch (Exception ignored) { }
        response.sendRedirect(request.getContextPath() + "/admin/inquiries");
    }
}
