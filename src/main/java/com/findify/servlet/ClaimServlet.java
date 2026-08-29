package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.model.Claim;
import com.findify.model.FoundItem;
import com.findify.model.User;
import com.findify.util.GeminiMatcher;

import jakarta.servlet.http.HttpSession;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ClaimServlet")
public class ClaimServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // Claims Gemini scores at or above this confidence (and marks as a
    // match) are approved automatically instead of sitting in the admin
    // queue. Everything below it still goes to a human for review.
    private static final int AUTO_APPROVE_CONFIDENCE = 80;

    public ClaimServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int foundId = Integer.parseInt(request.getParameter("foundId"));
        String submittedDescription = request.getParameter("description");

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

        if (submittedDescription == null || submittedDescription.trim().isEmpty()) {
            response.sendRedirect("verify.jsp?foundId=" + foundId + "&error=empty");
            return;
        }

        FoundItem foundItem = new FoundItemDAO().getFoundItemById(foundId);

        if (foundItem == null) {
            response.sendRedirect("verify.jsp?foundId=" + foundId + "&error=notfound");
            return;
        }

        submittedDescription = submittedDescription.trim();

        // Compares the claimant's description against the finder's private
        // original (found_items.description — never shown on the public
        // pages) and returns a match flag, a 0-100 confidence, and a short
        // human-readable reason for the admin to review.
        GeminiMatcher.Result verdict =
                GeminiMatcher.compare(foundItem.getDescription(), submittedDescription);

        Claim claim = new Claim();

        claim.setFoundId(foundId);
        claim.setClaimantId(loggedInUser.getUserId());
        claim.setSubmittedDescription(submittedDescription);
        claim.setAiMatch(verdict.match);
        claim.setAiConfidence(verdict.confidence);
        claim.setAiReasoning(verdict.reasoning);

        boolean autoApprove = verdict.match && verdict.confidence >= AUTO_APPROVE_CONFIDENCE;
        claim.setStatus(autoApprove ? "APPROVED" : "PENDING");

        ClaimDAO dao = new ClaimDAO();

        boolean success = dao.addClaim(claim);

        if (success) {
            response.sendRedirect("claim.html");
        } else {
            response.sendRedirect("verify.jsp?foundId=" + foundId);
        }
    }
}