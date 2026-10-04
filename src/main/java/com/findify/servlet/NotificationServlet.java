package com.findify.servlet;

import java.io.IOException;
import java.util.List;

import com.findify.dao.NotificationDAO;
import com.findify.model.Notification;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/NotificationServlet")
public class NotificationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    // =====================================================
    // GET - SHOW NOTIFICATIONS
    // =====================================================

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Get existing session
        HttpSession session =
                request.getSession(false);

        // Check session
        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        // Check user
        if (loggedInUser == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        int userId =
                loggedInUser.getUserId();


        // Create DAO
        NotificationDAO dao =
                new NotificationDAO();


        // Get notifications
        List<Notification> notifications =
                dao.getUserNotifications(userId);


        // Get unread count
        int unreadCount =
                dao.getUnreadCount(userId);


        // Send data to JSP
        request.setAttribute(
                "notifications",
                notifications
        );


        request.setAttribute(
                "unreadCount",
                unreadCount
        );


        // Open notification page
        request.getRequestDispatcher(
                "notifications.jsp"
        ).forward(request, response);
    }


    // =====================================================
    // POST - MARK ALL AS READ
    // =====================================================

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // Get existing session
        HttpSession session =
                request.getSession(false);


        // Check session
        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");


        // Check user
        if (loggedInUser == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        int userId =
                loggedInUser.getUserId();


        // Get action
        String action =
                request.getParameter("action");


        // Create DAO
        NotificationDAO dao =
                new NotificationDAO();


        // Mark all notifications as read
        if ("markAll".equals(action)) {

            dao.markAllAsRead(userId);
        }


        // Return to notifications page
        response.sendRedirect(
                "NotificationServlet"
        );
    }
}