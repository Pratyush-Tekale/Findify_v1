package com.findify.servlet;

import com.findify.dao.UserDAO;
import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.dao.LostItemDAO;
import com.findify.model.Claim;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/AdminDashboardServlet")
public class AdminDashboardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ClaimDAO dao = new ClaimDAO();
        LostItemDAO lost=new LostItemDAO();
        FoundItemDAO found=new FoundItemDAO();

        String status = request.getParameter("status");
        String search = request.getParameter("search");

        List<Claim> claims;
        String viewLabel; // used to make the table header honest

        if (search != null && !search.isEmpty()) {
            claims = dao.searchClaims(search);
            viewLabel = "Search Results for \"" + search + "\"";
        } else if (status != null && !status.isEmpty()) {
            claims = dao.getClaimsByStatus(status);
            viewLabel = capitalize(status) + " Claims";
        } else {
            claims = dao.getAllClaims();
            viewLabel = "All Claims";
        }

        // Stat cards
        request.setAttribute("pendingClaims", claims);
        request.setAttribute("viewLabel", viewLabel);
        request.setAttribute("pendingClaimsCount", dao.getPendingClaimsCount());
        request.setAttribute("approvedClaimsCount", dao.getApprovedClaimsCount());
        request.setAttribute("rejectedClaimsCount", dao.getRejectedClaimsCount());

        UserDAO userDao = new UserDAO();
        request.setAttribute("totalUsers", userDao.getTotalUsers());

        // ⚠️ Wire these up once LostItemDAO/FoundItemDAO methods exist
        request.setAttribute("totalLostItems", lost.getTotalLostItems());
        request.setAttribute("totalFoundItems",found.getTotalFoundItems() );

        // Recent activity feed — last 6 claims regardless of filter/trust score
        List<Claim> recentActivity = dao.getRecentClaims(6);
        request.setAttribute("recentActivity", recentActivity);

        // Retain filter values so the form doesn't reset on submit
        request.setAttribute("searchValue", search == null ? "" : search);
        request.setAttribute("statusValue", status == null ? "" : status);

        request.getRequestDispatcher("adminDashboard.jsp").forward(request, response);
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.charAt(0) + s.substring(1).toLowerCase();
    }
}