package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.DashboardDAO;
import com.findify.model.Dashboard;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/UserDashboardServlet")
public class UserDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("USER DASHBOARD SERVLET CALLED");

        // Get existing session
        HttpSession session = request.getSession(false);

        // Check session
        if (session == null) {

            System.out.println("No session found");

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );

            return;
        }

        // Get logged-in user
        User user = (User) session.getAttribute("loggedInUser");

        // Check user
        if (user == null) {

            System.out.println("No logged-in user found");

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );

            return;
        }

        System.out.println("Dashboard user: " + user.getFullName());
        System.out.println("Dashboard user ID: " + user.getUserId());

        // Create Dashboard DAO
        DashboardDAO dao = new DashboardDAO();

        // Get dashboard data
        Dashboard dashboard =
                dao.getDashboardData(user.getUserId());

        // Send data to JSP
        request.setAttribute("user", user);
        request.setAttribute("dashboard", dashboard);

        System.out.println("Opening userDashboard.jsp");

        // Open dashboard
        request.getRequestDispatcher("/userDashboard.jsp")
               .forward(request, response);
    }
}