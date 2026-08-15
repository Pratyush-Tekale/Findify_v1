package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.UserDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");

        HttpSession session = request.getSession(false);

        // Check session
        if (session == null) {
            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        // Check OTP verification
        Boolean otpVerified =
                (Boolean) session.getAttribute("otpVerified");

        if (otpVerified == null || !otpVerified) {
            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        // Check passwords
        if (password == null || confirmPassword == null ||
            password.isEmpty() || confirmPassword.isEmpty()) {

            request.setAttribute("error", "Please enter both passwords.");

            request.getRequestDispatcher("resetPassword.jsp")
                   .forward(request, response);

            return;
        }

        if (!password.equals(confirmPassword)) {

            request.setAttribute(
                    "error",
                    "Passwords do not match."
            );

            request.getRequestDispatcher("resetPassword.jsp")
                   .forward(request, response);

            return;
        }

        // Get email stored during forgot password
        String email =
                (String) session.getAttribute("resetEmail");

        if (email == null) {

            response.sendRedirect("forgotPassword.jsp");
            return;
        }

        // Update password
        UserDAO dao = new UserDAO();

        boolean updated =
                dao.updatePassword(email, password);

        if (updated) {

            // Remove reset information
            session.removeAttribute("resetEmail");
            session.removeAttribute("otpVerified");

            response.sendRedirect("login.jsp?reset=success");

        } else {

            request.setAttribute(
                    "error",
                    "Unable to reset password. Please try again."
            );

            request.getRequestDispatcher("resetPassword.jsp")
                   .forward(request, response);
        }
    }
}