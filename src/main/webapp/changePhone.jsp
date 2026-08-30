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

    <title>Findify | Change Phone Number</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet" href="css/settings.css">

</head>

<body>

<header>

    <div class="logo">
        FINDIFY
    </div>

    <a href="settings.jsp" class="back-btn">
        &lt;- Back to Settings
    </a>

</header>


<section class="settings-section">

    <div class="settings-card">

        <div class="paper-tape"></div>


        <div class="settings-icon">

            <svg xmlns="http://www.w3.org/2000/svg"
                 width="40"
                 height="40"
                 viewBox="0 0 24 24"
                 fill="none"
                 stroke="currentColor"
                 stroke-width="1.8"
                 stroke-linecap="round"
                 stroke-linejoin="round">

                <rect x="5" y="2" width="14" height="20" rx="2"></rect>

                <line x1="9" y1="18" x2="15" y2="18"></line>

            </svg>

        </div>


        <h4>ACCOUNT SETTINGS</h4>

        <h1>Change Phone Number</h1>

        <p class="settings-subtitle">
            Update your registered phone number.
        </p>


        <form action="ChangePhoneServlet" method="post">

            <div class="setting-item">

                <div style="width:100%;">

                    <h3>Current Phone Number</h3>

                    <p>
                        <%= loggedInUser.getPhone() %>
                    </p>

                </div>

            </div>


            <div class="setting-item">

                <div style="width:100%;">

                    <h3>New Phone Number</h3>

                    <input
                        type="tel"
                        name="phone"
                        placeholder="Enter new phone number"
                        maxlength="10"
                        pattern="[0-9]{10}"
                        required
                        style="width:100%; padding:12px; margin-top:8px; box-sizing:border-box;"
                    >

                </div>

            </div>


            <div class="bottom-buttons">

                <button type="submit" class="profile-btn">
                    Save Changes
                </button>

                <a href="settings.jsp" class="logout-btn">
                    Cancel
                </a>

            </div>

        </form>

    </div>

</section>

</body>

</html>