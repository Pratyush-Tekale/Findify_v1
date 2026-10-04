package com.findify.servlet;

import java.io.IOException;
import java.util.List;

import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.dao.NotificationDAO;
import com.findify.model.Claim;
import com.findify.model.FoundItem;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Admin actions on a claim.
 *
 * approve  PENDING   -> APPROVED   (item becomes CLAIMED, rivals auto-rejected)
 * reject   PENDING   -> REJECTED   (with a reason)
 * collect  APPROVED  -> COLLECTED  (the physical handover, closes the loop)
 * revert   APPROVED  -> PENDING    (undo, item released again)
 */
@WebServlet("/ManageClaimsServlet")
public class ManageClaimsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        // =====================================================
        // ADMIN ONLY
        // =====================================================

        HttpSession session = request.getSession(false);

        User admin = (session == null)
                ? null
                : (User) session.getAttribute("loggedInUser");

        if (admin == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!"ADMIN".equalsIgnoreCase(admin.getRole())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN,
                    "Admin access required.");
            return;
        }

        // =====================================================
        // INPUT
        // =====================================================

        String claimIdParameter = request.getParameter("claimId");
        String action = request.getParameter("action");
        String reason = request.getParameter("reason");

        if (claimIdParameter == null || action == null) {
            response.sendRedirect("AdminDashboardServlet");
            return;
        }

        int claimId;

        try {
            claimId = Integer.parseInt(claimIdParameter);
        } catch (NumberFormatException e) {
            response.sendRedirect("AdminDashboardServlet");
            return;
        }

        ClaimDAO claimDAO = new ClaimDAO();
        NotificationDAO notificationDAO = new NotificationDAO();

        Claim claim = claimDAO.getClaimById(claimId);

        if (claim == null) {
            redirect(response, "notfound");
            return;
        }

        int claimantId = claim.getClaimantId();
        int foundId = claim.getFoundId();

        FoundItemDAO foundItemDAO = new FoundItemDAO();
        FoundItem foundItem = foundItemDAO.getFoundItemById(foundId);

        String itemName = (foundItem != null && foundItem.getItemName() != null)
                ? foundItem.getItemName()
                : "item";

        // =====================================================
        // APPROVE
        // =====================================================

        if ("approve".equalsIgnoreCase(action)) {

            if (!"PENDING".equalsIgnoreCase(claim.getStatus())) {
                redirect(response, "stale");
                return;
            }

            // Capture the rivals BEFORE they are auto-rejected,
            // so we still know who to notify.
            List<Claim> rivals = claimDAO.getPendingSiblings(foundId, claimId);

            boolean success = claimDAO.approveClaim(claimId, foundId, admin.getUserId());

            if (!success) {
                redirect(response, "failed");
                return;
            }

            notificationDAO.addNotification(
                    claimantId,
                    "Your claim (#" + claimId + ") for \"" + itemName
                    + "\" has been APPROVED. Please visit the Lost & Found Office "
                    + "with your College ID to collect the item. "
                    + "Quote reference CLM-" + claimId + " at the desk."
            );

            for (Claim rival : rivals) {

                notificationDAO.addNotification(
                        rival.getClaimantId(),
                        "Your claim (#" + rival.getClaimId() + ") for \"" + itemName
                        + "\" was closed because another claim for the same item "
                        + "was approved after admin verification."
                );
            }

            if (foundItem != null && foundItem.getUserId() != claimantId) {

                notificationDAO.addNotification(
                        foundItem.getUserId(),
                        "The item \"" + itemName + "\" you reported has been matched "
                        + "with its owner. Thank you for using Findify."
                );
            }

            redirect(response, "approved");
            return;
        }

        // =====================================================
        // REJECT
        // =====================================================

        if ("reject".equalsIgnoreCase(action)) {

            if (!"PENDING".equalsIgnoreCase(claim.getStatus())) {
                redirect(response, "stale");
                return;
            }

            boolean success = claimDAO.rejectClaim(claimId, admin.getUserId(), reason);

            if (!success) {
                redirect(response, "failed");
                return;
            }

            String shown = (reason == null || reason.trim().isEmpty())
                    ? "The details provided did not sufficiently prove ownership."
                    : reason.trim();

            notificationDAO.addNotification(
                    claimantId,
                    "Your claim (#" + claimId + ") for \"" + itemName
                    + "\" has been REJECTED after admin verification. Reason: " + shown
            );

            redirect(response, "rejected");
            return;
        }

        // =====================================================
        // COLLECT - THE ACTUAL HANDOVER
        // =====================================================

        if ("collect".equalsIgnoreCase(action)) {

            if (!"APPROVED".equalsIgnoreCase(claim.getStatus())) {
                redirect(response, "notapproved");
                return;
            }

            boolean success = claimDAO.markCollected(claimId, admin.getUserId());

            if (!success) {
                redirect(response, "failed");
                return;
            }

            notificationDAO.addNotification(
                    claimantId,
                    "Handover complete. \"" + itemName + "\" has been collected "
                    + "and claim #" + claimId + " is now closed. "
                    + "If this was not you, contact the Lost & Found Office immediately."
            );

            if (foundItem != null && foundItem.getUserId() != claimantId) {

                notificationDAO.addNotification(
                        foundItem.getUserId(),
                        "\"" + itemName + "\" that you handed in has been "
                        + "returned to its owner. Thank you."
                );
            }

            redirect(response, "collected");
            return;
        }

        // =====================================================
        // REVERT AN APPROVAL
        // =====================================================

        if ("revert".equalsIgnoreCase(action)) {

            if (!"APPROVED".equalsIgnoreCase(claim.getStatus())) {
                redirect(response, "notapproved");
                return;
            }

            boolean success = claimDAO.revertApproval(claimId, foundId);

            if (success) {

                notificationDAO.addNotification(
                        claimantId,
                        "Your approved claim (#" + claimId + ") for \"" + itemName
                        + "\" has been put back under review by the admin."
                );
            }

            redirect(response, success ? "reverted" : "failed");
            return;
        }

        response.sendRedirect("AdminDashboardServlet");
    }

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        // Claim actions must not be triggered by a plain link.
        response.sendRedirect("AdminDashboardServlet");
    }

    private void redirect(HttpServletResponse response, String msg)
            throws IOException {

        response.sendRedirect("AdminDashboardServlet?msg=" + msg);
    }
}
