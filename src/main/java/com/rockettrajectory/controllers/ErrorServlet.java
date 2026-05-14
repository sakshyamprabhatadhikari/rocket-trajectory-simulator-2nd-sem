package com.rockettrajectory.controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ErrorServlet
 * ------------
 * Routes the user to a clean, branded error page rather than displaying a
 * raw stack trace.  Three codes are handled:
 *   /error/404 → page-not-found
 *   /error/500 → server-error
 *   /error/403 → unauthorised
 */
@WebServlet("/error/*")
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getPathInfo();           // "/404", "/500", "/403"
        String code = (path == null) ? "" : path.replace("/", "");

        String title;
        String message;
        int    status;

        switch (code) {
            case "403":
                title   = "Access Denied";
                message = "You do not have permission to view this resource.";
                status  = HttpServletResponse.SC_FORBIDDEN;
                break;
            case "500":
                title   = "Something Went Wrong";
                message = "An internal error occurred. Please try again in a moment.";
                status  = HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
                break;
            default:
                title   = "Page Not Found";
                message = "The page you are looking for does not exist.";
                status  = HttpServletResponse.SC_NOT_FOUND;
                break;
        }

        response.setStatus(status);
        request.setAttribute("errorTitle",   title);
        request.setAttribute("errorMessage", message);
        request.setAttribute("errorCode",    code.isEmpty() ? "404" : code);
        request.getRequestDispatcher("/WEB-INF/pages/error.jsp").forward(request, response);
    }
}
