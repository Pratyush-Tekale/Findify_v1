package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.DashboardDAO;
import com.findify.dao.NotificationDAO;
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
        User user =
                (User) session.getAttribute("loggedInUser");

        // Check user
        if (user == null) {

            System.out.println("No logged-in user found");

            response.sendRedirect(
                request.getContextPath() + "/login.jsp"
            );

            return;
        }

        // =====================================================
        // DASHBOARD DATA
        // =====================================================

        DashboardDAO dashboardDAO =
                new DashboardDAO();

        Dashboard dashboard =
                dashboardDAO.getDashboardData(
                        user.getUserId()
                );


        // =====================================================
        // NOTIFICATION DATA
        // =====================================================

        NotificationDAO notificationDAO =
                new NotificationDAO();

        int unreadCount =
                notificationDAO.getUnreadCount(
                        user.getUserId()
                );


        // =====================================================
        // SEND DATA TO JSP
        // =====================================================

        request.setAttribute(
                "user",
                user
        );

        request.setAttribute(
                "dashboard",
                dashboard
        );

        request.setAttribute(
                "unreadCount",
                unreadCount
        );


        // =====================================================
        // OPEN DASHBOARD
        // =====================================================

        request.getRequestDispatcher(
                "/userDashboard.jsp"
        ).forward(request, response);
    }
}