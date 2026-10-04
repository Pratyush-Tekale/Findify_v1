<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.findify.model.User" %>

<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Your Profile</title>

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">

<link rel="stylesheet" href="css/profile.css">

</head>

<body>

<header>

    <div class="logo">
        FINDIFY
    </div>

    <a href="index.jsp" class="back-btn">
        &lt;- Back to Home
    </a>

</header>


<section class="profile-section">

    <div class="profile-card">

        <div class="paper-tape"></div>


        <div class="profile-icon">

            <svg xmlns="http://www.w3.org/2000/svg"
                 width="55"
                 height="55"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="1.8"
                 stroke-linecap="round"
                 stroke-linejoin="round">

                <path d="M20 21a8 8 0 0 0-16 0"></path>

                <circle cx="12" cy="7" r="4"></circle>

            </svg>

        </div>


        <h4>MEMBER PROFILE</h4>

        <h1><%= loggedInUser.getFullName() %></h1>

        <p class="profile-subtitle">
            Your Findify account information
        </p>


        <div class="profile-info">


            <div class="info-row">

                <div class="info-label">
                    Full Name
                </div>

                <div class="info-value">
                    <%= loggedInUser.getFullName() %>
                </div>

            </div>


            <div class="info-row">

                <div class="info-label">
                    Email Address
                </div>

                <div class="info-value">
                    <%= loggedInUser.getEmail() %>
                </div>

            </div>


            <div class="info-row">

                <div class="info-label">
                    Phone Number
                </div>

                <div class="info-value">
                    <%= loggedInUser.getPhone() %>
                </div>

            </div>


            <div class="info-row">

                <div class="info-label">
                    Account Type
                </div>

                <div class="info-value">
                    <%= loggedInUser.getRole() %>
                </div>

            </div>


            <div class="info-row">

                <div class="info-label">
                    Member Since
                </div>

                <div class="info-value">
                    <%= loggedInUser.getCreatedAt() %>
                </div>

            </div>


        </div>


        <div class="profile-actions">


            <a href="LogoutServlet" class="logout-btn">
                Sign Out
            </a>

        </div>

    </div>

</section>

</body>
</html>