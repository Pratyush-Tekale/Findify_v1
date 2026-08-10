package com.findify.servlet;
import com.findify.dao.UserDAO;

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
    	String search = request.getParameter("search");
    	List<Claim> claims;
    	if(search != null && !search.isEmpty()){

    	    claims = dao.searchClaims(search);

    	}
    	else if(status != null && !status.isEmpty()){

    	    claims = dao.getClaimsByStatus(status);

    	}
    	else{

    	    claims = dao.getAllClaims();

    	}
    	
    	request.setAttribute("pendingClaims",
    	        claims);

    	request.setAttribute("pendingClaimsCount",
    	        dao.getPendingClaimsCount());

        request.setAttribute("approvedClaimsCount", dao.getApprovedClaimsCount());

        request.setAttribute("rejectedClaimsCount", dao.getRejectedClaimsCount());

        UserDAO userDao = new UserDAO();

        request.setAttribute("totalUsers", userDao.getTotalUsers());
        request.setAttribute("totalLostItems", 0);

        request.setAttribute("totalFoundItems", 0);

        request.getRequestDispatcher("adminDashboard.jsp")
               .forward(request, response);
    }
}
