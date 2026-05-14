package com.rockettrajectory.controllers;

import com.rockettrajectory.service.RocketService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * EngineerRocketServlet
 * ---------------------
 * Read-only catalogue browse + search for engineers.  Required by the
 * coursework's "search bar to find specific information" feature.
 */
@WebServlet("/engineer/rockets")
public class EngineerRocketServlet extends HttpServlet {

    private final RocketService rocketService = new RocketService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String search = request.getParameter("search");
            if (!ValidationUtil.isBlank(search)) {
                request.setAttribute("rockets", rocketService.search(search.trim()));
                request.setAttribute("search",  search);
            } else {
                request.setAttribute("rockets", rocketService.findAll());
            }
        } catch (Exception e) {
            request.setAttribute("error", "Could not load rockets. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/engineer-rocket-list.jsp")
               .forward(request, response);
    }
}
