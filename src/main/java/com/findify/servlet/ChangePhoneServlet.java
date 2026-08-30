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

@WebServlet("/ChangePhoneServlet")
public class ChangePhoneServlet extends HttpServlet {

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

        // Get new phone number
        String phone = request.getParameter("phone");

        if (phone == null || phone.trim().isEmpty()) {

            response.sendRedirect("changePhone.jsp?error=empty");

            return;
        }

        phone = phone.trim();

        // Validate 10 digit phone number
        if (!phone.matches("[0-9]{10}")) {

            response.sendRedirect("changePhone.jsp?error=invalid");

            return;
        }

        // Update database
        UserDAO dao = new UserDAO();

        boolean success =
                dao.updatePhone(loggedInUser.getUserId(), phone);

        if (success) {

            // Update session object
            loggedInUser.setPhone(phone);

            session.setAttribute("loggedInUser", loggedInUser);

            // Redirect to settings with success message
            response.sendRedirect("settings.jsp?success=phone");

        } else {

            response.sendRedirect("changePhone.jsp?error=failed");

        }
    }
}