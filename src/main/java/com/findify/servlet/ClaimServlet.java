package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.dao.NotificationDAO;
import com.findify.model.Claim;
import com.findify.model.FoundItem;
import com.findify.model.User;
import com.findify.util.GeminiMatcher;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ClaimServlet")
public class ClaimServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        String foundIdParameter = request.getParameter("foundId");
        String submittedDescription = request.getParameter("description");

        if (foundIdParameter == null || foundIdParameter.trim().isEmpty()) {
            response.sendRedirect("ViewFoundServlet");
            return;
        }

        int foundId;

        try {
            foundId = Integer.parseInt(foundIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("ViewFoundServlet");
            return;
        }

        // =====================================================
        // SESSION
        // =====================================================

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

        // =====================================================
        // VALIDATE DESCRIPTION
        // =====================================================

        if (submittedDescription == null || submittedDescription.trim().isEmpty()) {
            redirectWithError(response, foundId, "empty");
            return;
        }

        submittedDescription = submittedDescription.trim();

        if (submittedDescription.length() < 20) {
            redirectWithError(response, foundId, "short");
            return;
        }

        // =====================================================
        // FOUND ITEM
        // =====================================================

        FoundItemDAO foundDAO = new FoundItemDAO();
        FoundItem foundItem = foundDAO.getFoundItemById(foundId);

        if (foundItem == null) {
            redirectWithError(response, foundId, "notfound");
            return;
        }

        ClaimDAO claimDAO = new ClaimDAO();

        // GUARD 1 - cannot claim an item you reported yourself
        if (foundItem.getUserId() == loggedInUser.getUserId()) {
            redirectWithError(response, foundId, "self");
            return;
        }

        // GUARD 2 - item must still be available
        if ("CLAIMED".equalsIgnoreCase(foundItem.getStatus())
                || claimDAO.isItemAlreadyAwarded(foundId)) {
            redirectWithError(response, foundId, "closed");
            return;
        }

        // GUARD 3 - no duplicate live claim by the same user
        if (claimDAO.hasActiveClaim(foundId, loggedInUser.getUserId())) {
            redirectWithError(response, foundId, "duplicate");
            return;
        }

        // =====================================================
        // GEMINI AI VERIFICATION (advisory only)
        // =====================================================

        GeminiMatcher.Result verdict =
                GeminiMatcher.compare(
                        foundItem.getDescription(),
                        submittedDescription
                );

        // =====================================================
        // CREATE CLAIM - ALWAYS PENDING, ADMIN DECIDES
        // =====================================================

        Claim claim = new Claim();

        claim.setFoundId(foundId);
        claim.setClaimantId(loggedInUser.getUserId());
        claim.setSubmittedDescription(submittedDescription);
        claim.setAiMatch(verdict.match);
        claim.setAiConfidence(verdict.confidence);
        claim.setAiReasoning(verdict.reasoning);
        claim.setStatus("PENDING");

        int claimId = claimDAO.addClaim(claim);

        if (claimId <= 0) {
            redirectWithError(response, foundId, "failed");
            return;
        }

        // =====================================================
        // NOTIFICATIONS
        // =====================================================

        NotificationDAO notificationDAO = new NotificationDAO();

        notificationDAO.addNotification(
                loggedInUser.getUserId(),
                "Your claim (#" + claimId + ") for \"" + foundItem.getItemName()
                + "\" has been submitted and is pending admin verification."
        );

        int finderUserId = foundItem.getUserId();

        if (finderUserId != loggedInUser.getUserId()) {

            notificationDAO.addNotification(
                    finderUserId,
                    "A claim has been submitted for the found item \""
                    + foundItem.getItemName() + "\" that you reported."
            );
        }

        // =====================================================
        // SUCCESS PAGE - REAL CLAIM ID, REAL ITEM NAME
        // =====================================================

        request.setAttribute("claimId", claimId);
        request.setAttribute("itemName", foundItem.getItemName());
        request.setAttribute("foundId", foundId);

        request.getRequestDispatcher("claimSuccess.jsp")
               .forward(request, response);
    }

    private void redirectWithError(HttpServletResponse response,
                                    int foundId,
                                    String code) throws IOException {

        response.sendRedirect("verify.jsp?foundId=" + foundId + "&error=" + code);
    }
}
