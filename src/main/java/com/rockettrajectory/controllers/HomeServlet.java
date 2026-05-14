package com.rockettrajectory.controllers;

import com.rockettrajectory.service.RocketService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * HomeServlet
 * -----------
 * Public landing page.  Lists the most recently added rockets so a
 * first-time visitor immediately understands what the platform does.
 */
@WebServlet(urlPatterns = {"", "/", "/home"})
public class HomeServlet extends HttpServlet {

    private final RocketService rocketService = new RocketService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("recentRockets", rocketService.findRecent(4));
            request.setAttribute("totalRockets",  rocketService.countAll());
            request.setAttribute("activeRockets", rocketService.countByStatus("ACTIVE"));
        } catch (Exception e) {
            request.setAttribute("recentRockets", java.util.Collections.emptyList());
            request.setAttribute("totalRockets",  0);
            request.setAttribute("activeRockets", 0);
        }
        request.getRequestDispatcher("/WEB-INF/pages/home.jsp").forward(request, response);
    }
}
