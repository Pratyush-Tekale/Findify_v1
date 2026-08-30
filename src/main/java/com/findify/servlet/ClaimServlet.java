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

    public ClaimServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // GET FORM DATA
        // =====================================================

        String foundIdParameter =
                request.getParameter("foundId");

        String submittedDescription =
                request.getParameter("description");

        // Check found ID
        if (foundIdParameter == null ||
            foundIdParameter.trim().isEmpty()) {

            response.sendRedirect("ViewFoundServlet");
            return;
        }

        int foundId;

        try {

            foundId =
                    Integer.parseInt(foundIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect("ViewFoundServlet");
            return;
        }


        // =====================================================
        // CHECK SESSION
        // =====================================================

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        if (loggedInUser == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        // =====================================================
        // VALIDATE DESCRIPTION
        // =====================================================

        if (submittedDescription == null ||
            submittedDescription.trim().isEmpty()) {

            response.sendRedirect(
                    "verify.jsp?foundId=" +
                    foundId +
                    "&error=empty"
            );

            return;
        }


        // =====================================================
        // GET FOUND ITEM
        // =====================================================

        FoundItemDAO foundDAO =
                new FoundItemDAO();

        FoundItem foundItem =
                foundDAO.getFoundItemById(foundId);

        if (foundItem == null) {

            response.sendRedirect(
                    "verify.jsp?foundId=" +
                    foundId +
                    "&error=notfound"
            );

            return;
        }


        submittedDescription =
                submittedDescription.trim();


        // =====================================================
        // GEMINI AI VERIFICATION
        // =====================================================

        GeminiMatcher.Result verdict =
                GeminiMatcher.compare(
                        foundItem.getDescription(),
                        submittedDescription
                );


        // =====================================================
        // CREATE CLAIM
        // =====================================================

        Claim claim = new Claim();

        claim.setFoundId(foundId);

        claim.setClaimantId(
                loggedInUser.getUserId()
        );

        claim.setSubmittedDescription(
                submittedDescription
        );

        // Store AI result
        claim.setAiMatch(
                verdict.match
        );

        claim.setAiConfidence(
                verdict.confidence
        );

        claim.setAiReasoning(
                verdict.reasoning
        );


        // =====================================================
        // EVERY CLAIM MUST REMAIN PENDING
        // ADMIN MAKES THE FINAL DECISION
        // =====================================================

        claim.setStatus("PENDING");


        // =====================================================
        // SAVE CLAIM
        // =====================================================

        ClaimDAO claimDAO =
                new ClaimDAO();

        boolean success =
                claimDAO.addClaim(claim);


        if (success) {

            NotificationDAO notificationDAO =
                    new NotificationDAO();


            // =================================================
            // NOTIFY CLAIMANT
            // =================================================

            notificationDAO.addNotification(

                    loggedInUser.getUserId(),

                    "Your claim for \"" +
                    foundItem.getItemName() +
                    "\" has been submitted and is pending admin verification."
            );


            // =================================================
            // NOTIFY FINDER
            // =================================================

            int finderUserId =
                    foundItem.getUserId();


            // Don't send another notification if
            // claimant and finder are the same user.
            if (finderUserId != loggedInUser.getUserId()) {

                notificationDAO.addNotification(

                        finderUserId,

                        "A claim has been submitted for the found item \"" +
                        foundItem.getItemName() +
                        "\" that you reported."
                );
            }


            // =================================================
            // GO TO CLAIM SUCCESS PAGE
            // PASS THE FOUND ITEM ID
            // =================================================

            response.sendRedirect(
                    "claim.html?foundId=" + foundId
            );


        } else {

            response.sendRedirect(
                    "verify.jsp?foundId=" +
                    foundId +
                    "&error=failed"
            );
        }
    }
}