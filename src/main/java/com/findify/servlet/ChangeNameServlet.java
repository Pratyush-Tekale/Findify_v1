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

@WebServlet("/ChangeNameServlet")
public class ChangeNameServlet extends HttpServlet {

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

        User loggedInUser =
            (User) session.getAttribute("loggedInUser");

        // Check logged-in user
        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Get new name
        String fullName =
            request.getParameter("fullName");

        if (fullName == null ||
            fullName.trim().isEmpty()) {

            response.sendRedirect("changeName.jsp?error=invalid");
            return;
        }

        fullName = fullName.trim();

        // Update database
        UserDAO dao = new UserDAO();

        boolean success =
            dao.updateName(
                loggedInUser.getUserId(),
                fullName
            );

        if (success) {

            // Update session object
            loggedInUser.setFullName(fullName);

            session.setAttribute(
                "loggedInUser",
                loggedInUser
            );

            response.sendRedirect(
                "settings.jsp?success=name"
            );

        } else {

            response.sendRedirect(
                "changeName.jsp?error=failed"
            );
        }
    }
}