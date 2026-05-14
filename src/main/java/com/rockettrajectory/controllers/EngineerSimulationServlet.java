package com.rockettrajectory.controllers;

import com.rockettrajectory.model.RocketModel;
import com.rockettrajectory.model.SimulationModel;
import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.RocketService;
import com.rockettrajectory.service.SimulationService;
import com.rockettrajectory.util.TrajectoryCalculator;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * EngineerSimulationServlet
 * -------------------------
 * The pay-off feature of the project — actually running a trajectory
 * simulation against any rocket from the catalogue and persisting the
 * outcome.  The same servlet also lists past simulations and lets the
 * user delete their own.
 */
@WebServlet("/engineer/simulations")
public class EngineerSimulationServlet extends HttpServlet {

    private final RocketService     rocketService     = new RocketService();
    private final SimulationService simulationService = new SimulationService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserModel user = (UserModel) session.getAttribute("loggedUser");

        String action = request.getParameter("action");

        try {
            if ("new".equals(action)) {
                request.setAttribute("rockets", rocketService.findAll());
                request.getRequestDispatcher("/WEB-INF/pages/engineer-simulation-form.jsp")
                       .forward(request, response);
                return;
            }
            // default: list this user's simulations
            request.setAttribute("simulations",
                    simulationService.findByUser(user.getUserId()));
        } catch (Exception e) {
            request.setAttribute("error", "Could not load simulations. Please try again.");
        }
        request.getRequestDispatcher("/WEB-INF/pages/engineer-simulation-list.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        UserModel user = (UserModel) session.getAttribute("loggedUser");
        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                simulationService.delete(id, user.getUserId());
                response.sendRedirect(request.getContextPath()
                        + "/engineer/simulations?msg=deleted");
                return;
            }

            // ----- run a brand-new simulation -----
            String rocketIdS  = request.getParameter("rocketId");
            String angleS     = request.getParameter("launchAngle");
            String burnS      = request.getParameter("burnTime");
            String payloadS   = request.getParameter("payload");
            String notes      = request.getParameter("notes");

            String error = validate(rocketIdS, angleS, burnS, payloadS);
            if (error != null) {
                forwardForm(request, response, error);
                return;
            }

            int rocketId = Integer.parseInt(rocketIdS);
            RocketModel rocket = rocketService.findById(rocketId);
            if (rocket == null) {
                forwardForm(request, response, "Selected rocket no longer exists.");
                return;
            }

            SimulationModel sim = new SimulationModel();
            sim.setUserId        (user.getUserId());
            sim.setRocketId      (rocketId);
            sim.setLaunchAngleDeg(Double.parseDouble(angleS));
            sim.setBurnTimeS     (Double.parseDouble(burnS));
            sim.setPayloadKg     (Double.parseDouble(payloadS));
            sim.setNotes         (notes == null ? "" : notes.trim());

            // Run physics
            TrajectoryCalculator.compute(rocket, sim);

            simulationService.save(sim);
            response.sendRedirect(request.getContextPath()
                    + "/engineer/simulations?msg=run");
        } catch (NumberFormatException nfe) {
            forwardForm(request, response, "Numeric inputs are not valid numbers.");
        } catch (Exception e) {
            forwardForm(request, response, "Something went wrong. Please try again.");
        }
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private String validate(String rocketIdS, String angle,
                            String burn, String payload) {
        if (!ValidationUtil.isPositiveInt(rocketIdS))
            return "Please choose a rocket.";
        if (!ValidationUtil.isPositiveNumber(angle))
            return "Launch angle must be a positive number.";
        try {
            double a = Double.parseDouble(angle);
            if (a <= 0 || a >= 90) return "Launch angle must be between 0 and 90 degrees.";
        } catch (NumberFormatException e) {
            return "Launch angle must be a valid number.";
        }
        if (!ValidationUtil.isPositiveNumber(burn))
            return "Burn time must be a positive number.";
        if (!ValidationUtil.isPositiveNumber(payload))
            return "Payload mass must be a positive number.";
        return null;
    }

    private void forwardForm(HttpServletRequest req, HttpServletResponse res, String error)
            throws ServletException, IOException {
        try {
            req.setAttribute("rockets", rocketService.findAll());
        } catch (Exception ignored) { }
        req.setAttribute("error", error);
        req.getRequestDispatcher("/WEB-INF/pages/engineer-simulation-form.jsp")
           .forward(req, res);
    }
}
