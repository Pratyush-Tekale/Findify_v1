package com.findify.servlet;

import java.io.IOException;
import java.util.ArrayList;

import com.findify.dao.FoundItemDAO;
import com.findify.model.FoundItem;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ViewFoundServlet")
public class ViewFoundServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        FoundItemDAO dao = new FoundItemDAO();

        ArrayList<FoundItem> list =
                dao.getAllFoundItems();

        request.setAttribute("foundItems", list);

        request.getRequestDispatcher("found.jsp")
               .forward(request, response);

    }

}