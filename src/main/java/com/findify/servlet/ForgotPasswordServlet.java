package com.findify.servlet;

import java.io.IOException;
import java.util.Random;

import com.findify.dao.UserDAO;
import com.findify.util.EmailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");

        UserDAO dao = new UserDAO();

        if (!dao.emailExists(email)) {

            request.setAttribute(
                    "error",
                    "No account found with this email address."
            );

            request.getRequestDispatcher("forgotpassword.jsp")
                   .forward(request, response);

            return;
        }

        // Generate 6-digit OTP
        Random random = new Random();

        String otp = String.format(
                "%06d",
                random.nextInt(1000000)
        );

        HttpSession session = request.getSession();

        session.setAttribute("resetEmail", email);
        session.setAttribute("resetOTP", otp);

        // OTP expiry = 5 minutes
        long expiryTime = System.currentTimeMillis()
                + (5 * 60 * 1000);

        session.setAttribute("otpExpiry", expiryTime);

        // Send OTP
        EmailUtil.sendOTP(email, otp, "Password Reset");

        response.sendRedirect("verifyOtp.jsp");
    }
}