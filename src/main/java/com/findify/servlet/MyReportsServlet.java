package com.findify.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.findify.dao.ClaimDAO;
import com.findify.dao.FoundItemDAO;
import com.findify.dao.LostItemDAO;
import com.findify.model.Claim;
import com.findify.model.FoundItem;
import com.findify.model.LostItem;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/MyReportsServlet")
public class MyReportsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        // Check session
        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        // Get logged-in user
        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        int userId = user.getUserId();


        // ==============================
        // LOST ITEMS
        // ==============================

        LostItemDAO lostDAO =
                new LostItemDAO();

        List<LostItem> lostItems =
                lostDAO.getLostItemsByUser(userId);


        // ==============================
        // FOUND ITEMS
        // ==============================

        FoundItemDAO foundDAO =
                new FoundItemDAO();

        ArrayList<FoundItem> foundItems =
                foundDAO.getFoundItemsByUser(userId);


        // ==============================
        // CLAIMS
        // ==============================

        ClaimDAO claimDAO =
                new ClaimDAO();

        List<Claim> claims =
                claimDAO.getClaimsByUser(userId);


        // ==============================
        // SEND TO JSP
        // ==============================

        request.setAttribute(
                "lostItems",
                lostItems
        );

        request.setAttribute(
                "foundItems",
                foundItems
        );

        request.setAttribute(
                "claims",
                claims
        );


        // ==============================
        // OPEN PAGE
        // ==============================

        request.getRequestDispatcher(
                "myReports.jsp"
        ).forward(request, response);
    }
}