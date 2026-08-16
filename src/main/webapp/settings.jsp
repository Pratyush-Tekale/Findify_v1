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

<title>Findify | Settings</title>

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">

<link rel="stylesheet" href="css/settings.css">

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

                <circle cx="12" cy="12" r="3"></circle>

                <path d="M19.4 15a1.7 1.7 0 0 0 .34 1.88l.06.06-1.5 1.5-.06-.06a1.7 1.7 0 0 0-1.88-.34 1.7 1.7 0 0 0-1.03 1.56V20h-2.12v-.09a1.7 1.7 0 0 0-1.03-1.56 1.7 1.7 0 0 0-1.88.34l-.06.06-1.5-1.5.06-.06A1.7 1.7 0 0 0 9.16 15a1.7 1.7 0 0 0-1.56-1.03H7v-2.12h.09A1.7 1.7 0 0 0 8.65 10a1.7 1.7 0 0 0-.34-1.88l-.06-.06 1.5-1.5.06.06A1.7 1.7 0 0 0 11.69 7a1.7 1.7 0 0 0 1.03-1.56V5h2.12v.09A1.7 1.7 0 0 0 15.87 7a1.7 1.7 0 0 0 1.88-.34l.06-.06 1.5 1.5-.06.06A1.7 1.7 0 0 0 18.91 10a1.7 1.7 0 0 0 1.56 1.03h.09v2.12h-.09A1.7 1.7 0 0 0 19.4 15z">
                </path>

            </svg>

        </div>


        <h4>ACCOUNT SETTINGS</h4>

        <h1>Settings</h1>

        <p class="settings-subtitle">
            Manage your Findify account and preferences.
        </p>


        <!-- ACCOUNT -->

        <div class="settings-group">

            <div class="group-title">
                ACCOUNT
            </div>

            <div class="setting-item">

                <div>
                    <h3>Change Name</h3>
                    <p>Update your full name.</p>
                </div>

                <button type="button" onclick="showMessage('Change Name feature coming soon.')">
                    Edit
                </button>

            </div>


            <div class="setting-item">

                <div>
                    <h3>Change Phone Number</h3>
                    <p>Update your registered phone number.</p>
                </div>

                <button type="button" onclick="showMessage('Change Phone feature coming soon.')">
                    Edit
                </button>

            </div>


            <div class="setting-item">

                <div>
                    <h3>Change Email</h3>
                    <p>Update your registered email address.</p>
                </div>

                <button type="button" onclick="showMessage('Change Email feature coming soon.')">
                    Edit
                </button>

            </div>

        </div>


        <!-- SECURITY -->

        <div class="settings-group">

            <div class="group-title">
                SECURITY
            </div>

            <div class="setting-item">

                <div>
                    <h3>Change Password</h3>
                    <p>Update your account password.</p>
                </div>

                <button type="button" onclick="showMessage('Change Password feature coming soon.')">
                    Change
                </button>

            </div>

        </div>


        <!-- NOTIFICATIONS -->

        <div class="settings-group">

            <div class="group-title">
                NOTIFICATIONS
            </div>

            <div class="setting-item">

                <div>
                    <h3>Email Notifications</h3>
                    <p>Receive important Findify updates by email.</p>
                </div>

                <label class="switch">

                    <input type="checkbox" checked>

                    <span class="slider"></span>

                </label>

            </div>


            <div class="setting-item">

                <div>
                    <h3>Item Match Notifications</h3>
                    <p>Get notified when a possible match is found.</p>
                </div>

                <label class="switch">

                    <input type="checkbox" checked>

                    <span class="slider"></span>

                </label>

            </div>


            <div class="setting-item">

                <div>
                    <h3>Claim Updates</h3>
                    <p>Receive updates about your requests and claims.</p>
                </div>

                <label class="switch">

                    <input type="checkbox" checked>

                    <span class="slider"></span>

                </label>

            </div>

        </div>


        
        <!-- DANGER ZONE -->

        <div class="danger-zone">

            <div>

                <h3>Delete Account</h3>

                <p>
                    Permanently delete your Findify account.
                </p>

            </div>

            <button type="button" class="delete-btn"
                    onclick="confirmDelete()">

                Delete Account

            </button>

        </div>


        <div class="bottom-buttons">

            <a href="profile.jsp" class="profile-btn">
                Your Profile
            </a>

            <a href="LogoutServlet" class="logout-btn">
                Sign Out
            </a>

        </div>

    </div>

</section>


<script>

function showMessage(message) {

    alert(message);

}


function confirmDelete() {

    const result = confirm(
        "Are you sure you want to delete your account?"
    );

    if (result) {

        alert(
            "Account deletion will be implemented next."
        );

    }

}

</script>

</body>

</html>
```
