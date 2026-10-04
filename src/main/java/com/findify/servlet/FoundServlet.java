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
import com.findify.util.UploadUtil;

@SuppressWarnings("serial")
@WebServlet("/FoundServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024,        // 1 MB in memory, then spool to disk
    maxFileSize       = 5L * 1024 * 1024,   // 5 MB per file
    maxRequestSize     = 10L * 1024 * 1024  // 10 MB per request
)
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



            // =================================================
            // IMAGE UPLOAD (validated - see UploadUtil)
            // =================================================

            String imageName = null;

            try {

                Part imagePart = request.getPart("image");

                String uploadPath =
                    UploadUtil.resolveUploadPath(
                        getServletContext().getRealPath("")
                    );

                imageName = UploadUtil.saveImage(imagePart, uploadPath);

            } catch (UploadUtil.InvalidUploadException e) {

                response.sendRedirect("reportfound.jsp?error=upload");
                return;
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

            int foundId =
            dao.addFoundItem(item);



            if(foundId > 0)
            {
                // CHANGED: no more per-question verification rows. The
                // description above IS the private verification baseline —
                // ClaimServlet/GeminiMatcher compares a claimant's submitted
                // description against this one.
                response.sendRedirect("found-success.html");
            }
            else
            {
                response.sendRedirect("reportfound.jsp?error=true");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();

            response.sendRedirect("reportfound.jsp");
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