package com.rockettrajectory.controllers;

import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.RocketService;
import com.rockettrajectory.service.SimulationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * EngineerDashboardServlet
 * ------------------------
 * Displays real-time data on the engineer's home: total rockets in the
 * catalogue, active rockets, and the engineer's own simulation count
 * along with the most recently added rockets.
 */
@WebServlet("/engineer/dashboard")
public class EngineerDashboardServlet extends HttpServlet {

    private final RocketService     rocketService     = new RocketService();
    private final SimulationService simulationService = new SimulationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        UserModel user = (UserModel) session.getAttribute("loggedUser");
        try {
            request.setAttribute("totalRockets",     rocketService.countAll());
            request.setAttribute("activeRockets",    rocketService.countByStatus("ACTIVE"));
            request.setAttribute("recentRockets",    rocketService.findRecent(5));
            request.setAttribute("mySimulations",    simulationService.countByUser(user.getUserId()));
        } catch (Exception e) {
            request.setAttribute("error", "Could not load dashboard data.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/engineer-dashboard.jsp")
               .forward(request, response);
    }
}
