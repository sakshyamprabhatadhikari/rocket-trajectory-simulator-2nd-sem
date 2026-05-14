package com.rockettrajectory.controllers;

import com.rockettrajectory.service.InquiryService;
import com.rockettrajectory.service.RocketService;
import com.rockettrajectory.service.SimulationService;
import com.rockettrajectory.service.UserService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminDashboardServlet
 * ---------------------
 * Loads aggregate counts for the admin's main dashboard widget grid:
 *   • Total rockets / active rockets / retired
 *   • Total users
 *   • Total simulations
 *   • Total inquiries
 *   • Most-recently added rockets
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final RocketService     rocketService     = new RocketService();
    private final UserService       userService       = new UserService();
    private final SimulationService simulationService = new SimulationService();
    private final InquiryService    inquiryService    = new InquiryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("totalRockets",      rocketService.countAll());
            request.setAttribute("activeRockets",     rocketService.countByStatus("ACTIVE"));
            request.setAttribute("retiredRockets",    rocketService.countByStatus("RETIRED"));
            request.setAttribute("inDevRockets",      rocketService.countByStatus("IN_DEVELOPMENT"));
            request.setAttribute("totalUsers",        userService.countAll());
            request.setAttribute("totalSimulations",  simulationService.countAll());
            request.setAttribute("totalInquiries",    inquiryService.countAll());
            request.setAttribute("recentRockets",     rocketService.findRecent(5));
        } catch (Exception e) {
            request.setAttribute("error",
                    "Could not load dashboard data. Please refresh the page.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/admin-dashboard.jsp")
               .forward(request, response);
    }
}
