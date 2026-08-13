package com.findify.servlet;
import com.findify.model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpSession;


import com.findify.dao.LostItemDAO;
import com.findify.model.LostItem;
import java.sql.Date;

@SuppressWarnings("serial")
@WebServlet("/ReportServlet")
@MultipartConfig
public class ReportServlet extends HttpServlet {


protected void doPost(HttpServletRequest request,
        HttpServletResponse response)
        throws ServletException, IOException {
	System.out.println("ReportServlet Called");

    try {


        String itemName =
        request.getParameter("itemName");


        String category =
        request.getParameter("category");


        Date dateLost = 
        Date.valueOf(request.getParameter("dateLost"));


        String locationLost =
        request.getParameter("location");


        String description =
        request.getParameter("description");



        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User loggedInUser =
                (User) session.getAttribute("loggedInUser");

        int userId = loggedInUser.getUserId();



        // Convert category name to ID

        int categoryId =
        getCategoryId(category);



        // Image upload

        Part imagePart =
        request.getPart("image");


        String imageName = null;


        if(imagePart != null &&
           imagePart.getSize() > 0)
        {


            imageName =
            Paths.get(
            imagePart.getSubmittedFileName())
            .getFileName()
            .toString();



            String uploadPath =
            getServletContext()
            .getRealPath("")
            +
            "uploads";


            File uploadDir =
            new File(uploadPath);


            if(!uploadDir.exists())
            {
                uploadDir.mkdir();
            }


            imagePart.write(
            uploadPath + File.separator + imageName);

        }




        LostItem item =
        new LostItem(
                userId,
                categoryId,
                itemName,
                description,
                locationLost,
                dateLost,
                imageName
        );



        LostItemDAO dao =
        new LostItemDAO();


        boolean result =
        dao.addLostItem(item);



        if(result)
        {
        	response.sendRedirect("report-success.html");
        }
        else
        {
            response.sendRedirect("report.html?error=true");
        }


    }
    catch(Exception e)
    {

        e.printStackTrace();

        response.sendRedirect("report.html");

    }


}




// category name -> category id

private int getCategoryId(String category)
{


    switch(category)
    {

    case "Electronics":
        return 1;

    case "Books":
        return 2;

    case "Wallet":
        return 3;

    case "ID Card":
        return 4;

    case "Keys":
        return 5;

    case "Bag":
        return 6;

    case "Clothing":
        return 7;

    case "Mobile":
        return 8;

    case "Jewellery":
        return 9;

    case "Accessories":
        return 10;


    default:
        return 11;

    }

}


}