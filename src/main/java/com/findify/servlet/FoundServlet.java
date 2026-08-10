package com.findify.servlet;

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

import com.findify.dao.FoundItemDAO;
import com.findify.model.FoundItem;

@WebServlet("/FoundServlet")
@MultipartConfig
public class FoundServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String itemName =
            request.getParameter("itemName");

            String category =
            request.getParameter("category");

            String dateFound =
            request.getParameter("dateFound");

            String locationFound =
            request.getParameter("location");

            String description =
            request.getParameter("description");



            // Temporary user ID
            // Replace with session after login module

            int userId = 1;



            // Category name -> Category ID

            int categoryId =
            getCategoryId(category);



            // Image Upload

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



            FoundItem item =
            new FoundItem(
                    userId,
                    categoryId,
                    itemName,
                    description,
                    locationFound,
                    dateFound,
                    imageName
            );



            FoundItemDAO dao =
            new FoundItemDAO();

            boolean result =
            dao.addFoundItem(item);



            if(result)
            {
                response.sendRedirect("found-success.html");
            }
            else
            {
                response.sendRedirect("reportfound.html?error=true");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();

            response.sendRedirect("reportfound.html");
        }

    }



    // Category Name -> Category ID

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