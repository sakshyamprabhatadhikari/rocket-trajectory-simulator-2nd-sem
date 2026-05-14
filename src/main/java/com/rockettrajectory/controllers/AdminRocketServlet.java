package com.rockettrajectory.controllers;

import com.rockettrajectory.model.RocketModel;
import com.rockettrajectory.model.UserModel;
import com.rockettrajectory.service.RocketService;
import com.rockettrajectory.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminRocketServlet
 * ------------------
 * Single front-controller for the four CRUD actions an admin needs:
 *
 *   GET  /admin/rockets                 → list (with optional ?search=)
 *   GET  /admin/rockets?action=add      → add form
 *   GET  /admin/rockets?action=edit&id= → edit form
 *   POST /admin/rockets   action=save   → insert/update
 *   POST /admin/rockets   action=delete → delete
 */
@WebServlet("/admin/rockets")
public class AdminRocketServlet extends HttpServlet {

    private final RocketService rocketService = new RocketService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("add".equals(action)) {
                request.getRequestDispatcher("/WEB-INF/pages/admin-rocket-form.jsp")
                       .forward(request, response);
                return;
            }
            if ("edit".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                RocketModel r = rocketService.findById(id);
                if (r == null) {
                    response.sendRedirect(request.getContextPath()
                            + "/admin/rockets?msg=notfound");
                    return;
                }
                request.setAttribute("rocket", r);
                request.getRequestDispatcher("/WEB-INF/pages/admin-rocket-form.jsp")
                       .forward(request, response);
                return;
            }
            // default: list
            String search = request.getParameter("search");
            if (!ValidationUtil.isBlank(search)) {
                request.setAttribute("rockets", rocketService.search(search.trim()));
                request.setAttribute("search",  search);
            } else {
                request.setAttribute("rockets", rocketService.findAll());
            }
            request.getRequestDispatcher("/WEB-INF/pages/admin-rocket-list.jsp")
                   .forward(request, response);

        } catch (NumberFormatException nfe) {
            response.sendRedirect(request.getContextPath() + "/admin/rockets");
        } catch (Exception e) {
            request.setAttribute("error", "Could not load rocket data. Please try again.");
            request.getRequestDispatcher("/WEB-INF/pages/admin-rocket-list.jsp")
                   .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            if ("delete".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                boolean ok = rocketService.delete(id);
                response.sendRedirect(request.getContextPath()
                        + "/admin/rockets?msg=" + (ok ? "deleted" : "deletefail"));
                return;
            }

            // -------- save (insert or update) --------
            String idStr        = request.getParameter("rocketId");
            String code         = request.getParameter("rocketCode");
            String name         = request.getParameter("rocketName");
            String country      = request.getParameter("country");
            String manufacturer = request.getParameter("manufacturer");
            String heightS      = request.getParameter("heightM");
            String diameterS    = request.getParameter("diameterM");
            String massS        = request.getParameter("massKg");
            String thrustS      = request.getParameter("thrustKn");
            String stagesS      = request.getParameter("stages");
            String status       = request.getParameter("status");
            String yearS        = request.getParameter("launchYear");
            String description  = request.getParameter("description");

            // ---- validation ----
            String error = validate(code, name, country, heightS, diameterS,
                                    massS, thrustS, stagesS, status, yearS);
            if (error != null) {
                forwardForm(request, response, error,
                            buildFromParams(idStr, code, name, country, manufacturer,
                                            heightS, diameterS, massS, thrustS,
                                            stagesS, status, yearS, description));
                return;
            }

            RocketModel r = new RocketModel();
            if (!ValidationUtil.isBlank(idStr)) {
                r.setRocketId(Integer.parseInt(idStr));
            }
            r.setRocketCode  (code.trim());
            r.setRocketName  (name.trim());
            r.setCountry     (country.trim());
            r.setManufacturer(manufacturer == null ? "" : manufacturer.trim());
            r.setHeightM     (Double.parseDouble(heightS));
            r.setDiameterM   (Double.parseDouble(diameterS));
            r.setMassKg      (Double.parseDouble(massS));
            r.setThrustKn    (Double.parseDouble(thrustS));
            r.setStages      (Integer.parseInt(stagesS));
            r.setStatus      (status);
            r.setLaunchYear  (Integer.parseInt(yearS));
            r.setDescription (description == null ? "" : description.trim());

            HttpSession session = request.getSession(false);
            UserModel admin = (UserModel) session.getAttribute("loggedUser");
            r.setAddedBy(admin == null ? 0 : admin.getUserId());

            boolean isUpdate = r.getRocketId() > 0;

            // duplicate code check (insert only)
            if (!isUpdate && rocketService.codeExists(code.trim())) {
                forwardForm(request, response,
                        "A rocket with this code already exists.", r);
                return;
            }

            boolean ok = isUpdate ? rocketService.update(r) : rocketService.create(r);
            response.sendRedirect(request.getContextPath()
                    + "/admin/rockets?msg=" + (ok ? (isUpdate ? "updated" : "added")
                                                  : "savefail"));
        } catch (NumberFormatException nfe) {
            forwardForm(request, response,
                    "Numeric fields contain invalid values.", null);
        } catch (Exception e) {
            forwardForm(request, response,
                    "Something went wrong. Please try again.", null);
        }
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private String validate(String code, String name, String country,
                            String h, String d, String m, String th,
                            String st, String status, String yr) {
        if (ValidationUtil.isBlank(code) || ValidationUtil.isBlank(name)
                || ValidationUtil.isBlank(country))
            return "Code, name and country are required.";
        if (!ValidationUtil.isPositiveNumber(h)
                || !ValidationUtil.isPositiveNumber(d)
                || !ValidationUtil.isPositiveNumber(m)
                || !ValidationUtil.isPositiveNumber(th))
            return "Height, diameter, mass and thrust must be positive numbers.";
        if (!ValidationUtil.isPositiveInt(st))
            return "Stages must be a positive integer.";
        if (!"ACTIVE".equals(status) && !"RETIRED".equals(status)
                && !"IN_DEVELOPMENT".equals(status))
            return "Please choose a valid status.";
        try {
            int year = Integer.parseInt(yr);
            if (year < 1900 || year > 2100)
                return "Launch year must be between 1900 and 2100.";
        } catch (NumberFormatException e) {
            return "Launch year must be a number.";
        }
        return null;
    }

    private RocketModel buildFromParams(String idS, String code, String name,
                                        String country, String mfr,
                                        String h, String d, String m, String th,
                                        String st, String status, String yr, String desc) {
        RocketModel r = new RocketModel();
        try { if (!ValidationUtil.isBlank(idS)) r.setRocketId(Integer.parseInt(idS)); } catch (Exception ignored) {}
        r.setRocketCode  (code);
        r.setRocketName  (name);
        r.setCountry     (country);
        r.setManufacturer(mfr);
        try { r.setHeightM  (Double.parseDouble(h));  } catch (Exception ignored) {}
        try { r.setDiameterM(Double.parseDouble(d));  } catch (Exception ignored) {}
        try { r.setMassKg   (Double.parseDouble(m));  } catch (Exception ignored) {}
        try { r.setThrustKn (Double.parseDouble(th)); } catch (Exception ignored) {}
        try { r.setStages   (Integer.parseInt(st));   } catch (Exception ignored) {}
        r.setStatus(status);
        try { r.setLaunchYear(Integer.parseInt(yr));  } catch (Exception ignored) {}
        r.setDescription(desc);
        return r;
    }

    private void forwardForm(HttpServletRequest req, HttpServletResponse res,
                             String error, RocketModel r)
            throws ServletException, IOException {
        if (error != null) req.setAttribute("error", error);
        if (r     != null) req.setAttribute("rocket", r);
        req.getRequestDispatcher("/WEB-INF/pages/admin-rocket-form.jsp").forward(req, res);
    }
}
