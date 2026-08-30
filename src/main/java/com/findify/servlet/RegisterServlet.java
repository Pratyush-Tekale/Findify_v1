package com.findify.servlet;

import java.io.IOException;
import java.util.Random;

import com.findify.dao.UserDAO;
import com.findify.model.User;
import com.findify.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Get registration form data
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");

        UserDAO dao = new UserDAO();

        // Check whether email already exists
        if (dao.emailExists(email)) {

            request.setAttribute(
                    "error",
                    "An account with this email already exists."
            );

            request.getRequestDispatcher("register.html")
                   .forward(request, response);

            return;
        }

        // Create User object
        User user = new User();

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);
        user.setRole("USER");

        // Generate 6-digit OTP
        Random random = new Random();

        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        // OTP valid for 5 minutes
        long expiryTime =
                System.currentTimeMillis() + (5 * 60 * 1000);

        // Store registration information in session
        HttpSession session = request.getSession();

        session.setAttribute("registrationUser", user);
        session.setAttribute("registrationOTP", otp);
        session.setAttribute("registrationOtpExpiry", expiryTime);

        // Send OTP to email
        EmailUtil.sendOTP(email, otp, "Account Registration");

        // Open OTP verification page
        response.sendRedirect("verifyRegisterOtp.jsp");
    }
}