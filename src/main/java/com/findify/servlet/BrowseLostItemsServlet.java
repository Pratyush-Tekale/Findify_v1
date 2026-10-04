package com.findify.servlet;


import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import com.findify.dao.LostItemDAO;
import com.findify.model.LostItem;


@SuppressWarnings("serial")
@WebServlet("/BrowseLostItemsServlet")
public class BrowseLostItemsServlet extends HttpServlet {


protected void doGet(HttpServletRequest request,
HttpServletResponse response)
throws ServletException, IOException {

	
    LostItemDAO dao = new LostItemDAO();


    List<LostItem> lostItems =
            dao.getAllLostItems();


    request.setAttribute(
            "lostItems",
            lostItems
    );


    request.getRequestDispatcher(
            "browselost.jsp"
    ).forward(request,response);

}

}