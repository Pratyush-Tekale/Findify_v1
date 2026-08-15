package com.findify.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyOTPServlet")
public class VerifyOTPServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String enteredOTP = request.getParameter("otp");

        HttpSession session = request.getSession(false);

        // Check session
        if (session == null) {

            response.sendRedirect("forgotpassword.jsp");
            return;
        }

        String storedOTP =
                (String) session.getAttribute("resetOTP");

        Long expiryTime =
                (Long) session.getAttribute("otpExpiry");

        // Check OTP data exists
        if (storedOTP == null || expiryTime == null) {

            request.setAttribute(
                    "error",
                    "OTP expired. Please request a new OTP."
            );

            request.getRequestDispatcher("verifyOtp.jsp")
                   .forward(request, response);

            return;
        }

        // Check OTP expiry
        if (System.currentTimeMillis() > expiryTime) {

            session.removeAttribute("resetOTP");
            session.removeAttribute("otpExpiry");

            request.setAttribute(
                    "error",
                    "OTP has expired. Please request a new OTP."
            );

            request.getRequestDispatcher("verifyOTP.jsp")
                   .forward(request, response);

            return;
        }

        // Check entered OTP
        if (enteredOTP != null &&
            enteredOTP.equals(storedOTP)) {

            // OTP verified successfully
            session.setAttribute("otpVerified", true);

            // OTP should not be usable again
            session.removeAttribute("resetOTP");
            session.removeAttribute("otpExpiry");

            response.sendRedirect("resetPassword.jsp");

        } else {

            request.setAttribute(
                    "error",
                    "Invalid OTP. Please try again."
            );

            request.getRequestDispatcher("verifyOtp.jsp")
                   .forward(request, response);
        }
    }
}