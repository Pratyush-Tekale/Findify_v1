package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.ClaimDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ManageClaimsServlet")
public class ManageClaimsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ManageClaimsServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        int claimId =
                Integer.parseInt(request.getParameter("claimId"));

        String action =
                request.getParameter("action");

        ClaimDAO dao = new ClaimDAO();

        if ("approve".equals(action)) {

            dao.approveClaim(claimId);

        }
        else if ("reject".equals(action)) {

            dao.rejectClaim(claimId);

        }

        response.sendRedirect("AdminDashboardServlet");

    }

}