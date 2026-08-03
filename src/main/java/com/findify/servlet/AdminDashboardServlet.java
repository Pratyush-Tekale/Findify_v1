package com.findify.servlet;

import java.io.IOException;
import java.util.List;

import com.findify.dao.ClaimDAO;
import com.findify.model.Claim;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public AdminDashboardServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

    	ClaimDAO dao = new ClaimDAO();

    	String status = request.getParameter("status");

    	List<Claim> claims;

    	if (status == null || status.isEmpty()) {

    	    claims = dao.getAllClaims();

    	} else {

    	    claims = dao.getClaimsByStatus(status);

    	}

    	request.setAttribute("pendingClaims", claims);

    	request.setAttribute("pendingClaimsCount",
    	        claims.size());

        request.setAttribute("approvedClaimsCount", 0);

        request.setAttribute("rejectedClaimsCount", 0);

        request.setAttribute("totalUsers", 0);

        request.setAttribute("totalLostItems", 0);

        request.setAttribute("totalFoundItems", 0);

        request.getRequestDispatcher("adminDashboard.jsp")
               .forward(request, response);
    }

}