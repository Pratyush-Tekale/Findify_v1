package com.findify.servlet;
import jakarta.servlet.http.HttpSession;
import com.findify.model.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import com.findify.dao.FoundItemDAO;
import com.findify.model.FoundItem;

@SuppressWarnings("serial")
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



            HttpSession session = request.getSession(false);

            if(session == null || session.getAttribute("loggedInUser") == null){
                response.sendRedirect("login.jsp");
                return;
            }

            User loggedInUser =
                    (User) session.getAttribute("loggedInUser");

            int userId = loggedInUser.getUserId();


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

                String originalName =
                Paths.get(
                imagePart.getSubmittedFileName())
                .getFileName()
                .toString();

                // Prefix with a random UUID so two uploads with the same
                // original filename (e.g. IMG_0001.jpg from a phone) never
                // collide and overwrite each other on disk.
                imageName = UUID.randomUUID().toString() + "_" + originalName;

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
