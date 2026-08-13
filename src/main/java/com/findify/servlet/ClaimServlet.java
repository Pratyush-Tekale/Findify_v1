package com.findify.servlet;



import com.findify.dao.FoundItemDAO;
import com.findify.model.FoundItem;
import com.findify.util.TrustScoreUtil;
import java.io.IOException;

import com.findify.dao.ClaimDAO;
import com.findify.model.Claim;
import com.findify.model.User;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ClaimServlet")
public class ClaimServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public ClaimServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int foundId = Integer.parseInt(request.getParameter("foundId"));
        String proof = request.getParameter("proof");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        
        FoundItemDAO foundDao = new FoundItemDAO();

        FoundItem foundItem = foundDao.getFoundItemById(foundId);

        if (foundItem == null) {
            response.sendRedirect("verify.jsp?foundId=" + foundId + "&error=itemnotfound");
            return;
        }

        String description = foundItem.getDescription();

        int trustScore =
                TrustScoreUtil.calculateTrustScore(description, proof);
        
        Claim claim = new Claim();

        claim.setFoundId(foundId);
        claim.setClaimantId(loggedInUser.getUserId());
        claim.setProof(proof);
        claim.setStatus("PENDING");
        claim.setTrustScore(trustScore);
        
        ClaimDAO dao = new ClaimDAO();

        boolean success = dao.addClaim(claim);

        if (success) {
            response.sendRedirect("claim.html");
        } else {
            response.sendRedirect("verify.jsp?foundId=" + foundId);
        }
    }
}