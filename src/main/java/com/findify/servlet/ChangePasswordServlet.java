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

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Check session
        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get logged-in user
        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get form values
        String currentPassword =
                request.getParameter("currentPassword");

        String newPassword =
                request.getParameter("newPassword");

        String confirmPassword =
                request.getParameter("confirmPassword");


        // Basic validation
        if (currentPassword == null ||
            newPassword == null ||
            confirmPassword == null) {

            response.sendRedirect(
                    "changePassword.jsp?error=invalid"
            );

            return;
        }


        // Check current password
        if (!loggedInUser.getPassword().equals(currentPassword)) {

            response.sendRedirect(
                    "changePassword.jsp?error=wrong"
            );

            return;
        }


        // Check password length
        if (newPassword.length() < 6) {

            response.sendRedirect(
                    "changePassword.jsp?error=invalid"
            );

            return;
        }


        // Check confirmation
        if (!newPassword.equals(confirmPassword)) {

            response.sendRedirect(
                    "changePassword.jsp?error=mismatch"
            );

            return;
        }


        // Update password
        UserDAO dao = new UserDAO();

        boolean success =
                dao.updatePassword(
                        loggedInUser.getEmail(),
                        newPassword
                );


        if (success) {

            // Update session user object
            loggedInUser.setPassword(newPassword);

            session.setAttribute(
                    "loggedInUser",
                    loggedInUser
            );


            // Redirect with success message
            response.sendRedirect(
                    "settings.jsp?success=password"
            );

        } else {

            response.sendRedirect(
                    "changePassword.jsp?error=failed"
            );
        }
    }
}