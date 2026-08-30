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

@WebServlet("/VerifyRegisterOtpServlet")
public class VerifyRegisterOtpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String enteredOTP = request.getParameter("otp");

        HttpSession session = request.getSession();

        // Get OTP and user data from session
        String sessionOTP =
                (String) session.getAttribute("registrationOTP");

        Long expiryTime =
                (Long) session.getAttribute("registrationOtpExpiry");

        User user =
                (User) session.getAttribute("registrationUser");

        // Check whether registration session exists
        if (sessionOTP == null || expiryTime == null || user == null) {

            request.setAttribute(
                    "error",
                    "Registration session expired. Please register again."
            );

            request.getRequestDispatcher("register.html")
                   .forward(request, response);

            return;
        }

        // Check OTP expiry
        if (System.currentTimeMillis() > expiryTime) {

            request.setAttribute(
                    "error",
                    "OTP has expired. Please register again."
            );

            // Remove expired registration data
            session.removeAttribute("registrationOTP");
            session.removeAttribute("registrationOtpExpiry");
            session.removeAttribute("registrationUser");

            request.getRequestDispatcher("register.html")
                   .forward(request, response);

            return;
        }

        // Check OTP
        if (!sessionOTP.equals(enteredOTP)) {

            request.setAttribute(
                    "error",
                    "Invalid OTP. Please enter the correct OTP."
            );

            request.getRequestDispatcher("verifyRegisterOtp.jsp")
                   .forward(request, response);

            return;
        }

        // OTP is correct
        UserDAO dao = new UserDAO();

        boolean success = dao.register(user);

        if (success) {

            // Remove registration data from session
            session.removeAttribute("registrationOTP");
            session.removeAttribute("registrationOtpExpiry");
            session.removeAttribute("registrationUser");

            // Registration successful
            response.sendRedirect("login.jsp");

        } else {

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
            );

            request.getRequestDispatcher("verifyRegisterOtp.jsp")
                   .forward(request, response);
        }
    }
}