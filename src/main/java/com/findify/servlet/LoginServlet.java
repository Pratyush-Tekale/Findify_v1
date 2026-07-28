package com.findify.servlet;

import java.io.IOException;

import com.findify.dao.UserDAO;
import com.findify.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public LoginServlet() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Get form data
        
    	String email = request.getParameter("email");
    	String password = request.getParameter("password");

    	UserDAO dao = new UserDAO();

    	User user = dao.login(email, password);

    	if(user != null){

    	    HttpSession session = request.getSession();

    	    session.setAttribute("loggedInUser", user);

    	    if("ADMIN".equals(user.getRole())){

    	        request.getRequestDispatcher("adminDashboard.jsp")
    	               .forward(request,response);

    	    }else{

    	        request.getRequestDispatcher("loginSuccess.jsp")
    	               .forward(request,response);

    	    }

    	}else{

    	    request.setAttribute("error","Invalid Email or Password");

    	    request.getRequestDispatcher("login.jsp")
    	           .forward(request,response);
    	}
    }
}