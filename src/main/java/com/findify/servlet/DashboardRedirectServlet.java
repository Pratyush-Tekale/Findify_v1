package com.findify.servlet;

import java.io.IOException;

import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DashboardRedirectServlet")
public class DashboardRedirectServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // User is not logged in
        if (session == null ||
            session.getAttribute("loggedInUser") == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // Check role
        if ("ADMIN".equalsIgnoreCase(loggedInUser.getRole())) {

            // Admin → Admin Dashboard
            response.sendRedirect("AdminDashboardServlet");

        } else {

            // User → User Dashboard
            response.sendRedirect("UserDashboardServlet");
        }
    }
}