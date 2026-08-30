package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.dao.NotificationDAO;
import com.findify.model.Claim;
import com.findify.model.FoundItem;

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

        // =====================================================
        // GET CLAIM ID AND ACTION
        // =====================================================

        String claimIdParameter =
                request.getParameter("claimId");

        String action =
                request.getParameter("action");


        if (claimIdParameter == null ||
            action == null) {

            response.sendRedirect("AdminDashboardServlet");
            return;
        }


        int claimId;

        try {

            claimId =
                    Integer.parseInt(claimIdParameter);

        } catch (NumberFormatException e) {

            response.sendRedirect("AdminDashboardServlet");
            return;
        }


        // =====================================================
        // CREATE DAOS
        // =====================================================

        ClaimDAO claimDAO =
                new ClaimDAO();

        NotificationDAO notificationDAO =
                new NotificationDAO();


        // =====================================================
        // GET CLAIM
        // =====================================================

        Claim claim =
                claimDAO.getClaimById(claimId);


        if (claim == null) {

            response.sendRedirect(
                    "AdminDashboardServlet"
            );

            return;
        }


        int claimantId =
                claim.getClaimantId();


        // =====================================================
        // GET FOUND ITEM
        // =====================================================

        FoundItemDAO foundItemDAO =
                new FoundItemDAO();

        FoundItem foundItem =
                foundItemDAO.getFoundItemById(
                        claim.getFoundId()
                );


        String itemName = "item";

        if (foundItem != null &&
            foundItem.getItemName() != null) {

            itemName =
                    foundItem.getItemName();
        }


        // =====================================================
        // APPROVE CLAIM
        // =====================================================

        if ("approve".equalsIgnoreCase(action)) {

            boolean success =
                    claimDAO.approveClaim(claimId);


            if (success) {

                notificationDAO.addNotification(

                        claimantId,

                        "Your claim for \"" +
                        itemName +
                        "\" has been approved. Please visit the Lost & Found Office with your College ID to collect the item."
                );
            }
        }


        // =====================================================
        // REJECT CLAIM
        // =====================================================

        else if ("reject".equalsIgnoreCase(action)) {

            boolean success =
                    claimDAO.rejectClaim(claimId);


            if (success) {

                notificationDAO.addNotification(

                        claimantId,

                        "Your claim for \"" +
                        itemName +
                        "\" has been rejected after admin verification."
                );
            }
        }


        // =====================================================
        // RETURN TO ADMIN DASHBOARD
        // =====================================================

        response.sendRedirect(
                "AdminDashboardServlet"
        );
    }
}