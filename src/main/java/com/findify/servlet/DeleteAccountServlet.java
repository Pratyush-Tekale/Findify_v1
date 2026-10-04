package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.UserDAO;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/DeleteAccountServlet")
public class DeleteAccountServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // Get existing session
        HttpSession session = request.getSession(false);

        // No session
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        // User not logged in
        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get user ID
        int userId = loggedInUser.getUserId();

        // Delete account
        UserDAO dao = new UserDAO();

        boolean success = dao.deleteUser(userId);

        if (success) {

            // Destroy session
            session.invalidate();

            // Redirect to login page
            response.sendRedirect("login.jsp?success=deleted");

        } else {

            // Deletion failed
            response.sendRedirect("settings.jsp?error=delete");

        }
    }
}