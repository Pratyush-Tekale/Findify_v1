package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.FoundItemDAO;
import com.findify.dao.LostItemDAO;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RemoveReportServlet")
public class RemoveReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request,
                           HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        User user =
                (User) session.getAttribute("loggedInUser");

        if (user == null) {

            response.sendRedirect("login.jsp");
            return;
        }


        String type =
                request.getParameter("type");

        String idParam =
                request.getParameter("id");


        if (type == null || idParam == null) {

            response.sendRedirect("MyReportsServlet");
            return;
        }


        int id;

        try {

            id = Integer.parseInt(idParam);

        } catch (NumberFormatException e) {

            response.sendRedirect("MyReportsServlet");
            return;
        }


        boolean success = false;


        // ==============================
        // REMOVE LOST REPORT
        // ==============================

        if ("lost".equalsIgnoreCase(type)) {

            LostItemDAO dao =
                    new LostItemDAO();

            success =
                    dao.deleteLostItem(
                            id,
                            user.getUserId()
                    );
        }


        // ==============================
        // REMOVE FOUND REPORT
        // ==============================

        else if ("found".equalsIgnoreCase(type)) {

            FoundItemDAO dao =
                    new FoundItemDAO();

            success =
                    dao.deleteFoundItem(
                            id,
                            user.getUserId()
                    );
        }


        if (success) {

            response.sendRedirect(
                    "MyReportsServlet?success=removed"
            );

        } else {

            response.sendRedirect(
                    "MyReportsServlet?error=removefailed"
            );
        }
    }
}