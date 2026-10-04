<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.findify.model.User" %>

<%
    User loggedInUser =
            (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    String error = request.getParameter("error");
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Findify | Change Password</title>

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="css/settings.css">


    <style>

        .password-form {
            margin-top: 28px;
        }


        .form-group {
            margin-bottom: 20px;
        }


        .form-group label {
            display: block;

            margin-bottom: 8px;

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;

            letter-spacing: 1px;

            text-transform: uppercase;

            color: #6B6255;
        }


        .password-box {
            position: relative;
        }


        .password-box input {
            width: 100%;

            padding: 13px 46px 13px 14px;

            border: 2px solid #221E1A;

            border-radius: 4px;

            background: #FFFDF8;

            color: #221E1A;

            font-family: 'Inter', sans-serif;

            font-size: 15px;

            outline: none;

            transition: .2s ease;
        }


        .password-box input:focus {
            border-color: #B33A2D;

            box-shadow: 3px 3px 0 rgba(179,58,45,.15);
        }


        .toggle-password {
            position: absolute;

            right: 13px;

            top: 50%;

            transform: translateY(-50%);

            cursor: pointer;

            color: #6B6255;

            font-size: 17px;

            user-select: none;
        }


        .toggle-password:hover {
            color: #B33A2D;
        }


        .form-note {
            margin-top: 6px;

            color: #6B6255;

            font-size: 12px;

            line-height: 1.5;
        }


        .form-actions {
            display: flex;

            gap: 12px;

            margin-top: 26px;
        }


        .save-btn {
            flex: 1;

            padding: 12px 18px;

            border: 2px solid #22303F;

            border-radius: 3px;

            background: #22303F;

            color: #FFFFFF;

            font-family: 'Inter', sans-serif;

            font-size: 13px;

            font-weight: 700;

            cursor: pointer;

            transition: .2s ease;
        }


        .save-btn:hover {
            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px,-2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        .cancel-btn {
            flex: 1;

            display: inline-flex;

            justify-content: center;

            align-items: center;

            padding: 12px 18px;

            border: 2px solid #221E1A;

            border-radius: 3px;

            background: #FBF7EE;

            color: #221E1A;

            font-family: 'Inter', sans-serif;

            font-size: 13px;

            font-weight: 700;

            text-decoration: none;

            transition: .2s ease;
        }


        .cancel-btn:hover {
            background: #221E1A;

            color: #FFFFFF;

            transform: translate(-2px,-2px);

            box-shadow: 4px 4px 0 #B33A2D;
        }


        .error-message {
            margin-bottom: 20px;

            padding: 11px 13px;

            background: #F8D7DA;

            border-left: 4px solid #B33A2D;

            color: #842029;

            font-size: 13px;

            line-height: 1.4;

            border-radius: 2px;
        }


        @media (max-width: 600px) {

            .form-actions {
                flex-direction: column;
            }

        }

    </style>

</head>


<body>


<header>

    <div class="logo">
        FINDIFY
    </div>

    <a href="settings.jsp" class="back-btn">
        ← Back to Settings
    </a>

</header>


<section class="settings-section">

    <div class="settings-card">

        <div class="paper-tape"></div>


        <!-- PASSWORD ICON -->

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

                <rect x="3"
                      y="11"
                      width="18"
                      height="10"
                      rx="2">
                </rect>

                <path d="M7 11V7a5 5 0 0 1 10 0v4">
                </path>

                <circle cx="12"
                        cy="16"
                        r="1">
                </circle>

            </svg>

        </div>


        <h4>SECURITY SETTINGS</h4>

        <h1>Change Password</h1>

        <p class="settings-subtitle">
            Update your Findify account password.
        </p>


        <% if ("wrong".equals(error)) { %>

            <div class="error-message">
                Current password is incorrect.
            </div>

        <% } %>


        <% if ("mismatch".equals(error)) { %>

            <div class="error-message">
                New password and confirm password do not match.
            </div>

        <% } %>


        <% if ("invalid".equals(error)) { %>

            <div class="error-message">
                Password must be at least 6 characters long.
            </div>

        <% } %>


        <% if ("failed".equals(error)) { %>

            <div class="error-message">
                Password could not be updated. Please try again.
            </div>

        <% } %>


        <form action="ChangePasswordServlet"
              method="post"
              class="password-form">


            <!-- CURRENT PASSWORD -->

            <div class="form-group">

                <label for="currentPassword">
                    Current Password
                </label>

                <div class="password-box">

                    <input
                        type="password"
                        id="currentPassword"
                        name="currentPassword"
                        placeholder="Enter current password"
                        required>

                    <span class="toggle-password"
                          onclick="togglePassword('currentPassword', this)">
                        👁
                    </span>

                </div>

            </div>


            <!-- NEW PASSWORD -->

            <div class="form-group">

                <label for="newPassword">
                    New Password
                </label>

                <div class="password-box">

                    <input
                        type="password"
                        id="newPassword"
                        name="newPassword"
                        placeholder="Enter new password"
                        minlength="6"
                        required>

                    <span class="toggle-password"
                          onclick="togglePassword('newPassword', this)">
                        👁
                    </span>

                </div>

                <p class="form-note">
                    Use at least 6 characters.
                </p>

            </div>


            <!-- CONFIRM PASSWORD -->

            <div class="form-group">

                <label for="confirmPassword">
                    Confirm New Password
                </label>

                <div class="password-box">

                    <input
                        type="password"
                        id="confirmPassword"
                        name="confirmPassword"
                        placeholder="Confirm new password"
                        minlength="6"
                        required>

                    <span class="toggle-password"
                          onclick="togglePassword('confirmPassword', this)">
                        👁
                    </span>

                </div>

            </div>


            <!-- BUTTONS -->

            <div class="form-actions">

                <button
                    type="submit"
                    class="save-btn">

                    Change Password

                </button>


                <a href="settings.jsp"
                   class="cancel-btn">

                    Cancel

                </a>

            </div>


        </form>


    </div>

</section>


<script>

function togglePassword(id, icon) {

    const input = document.getElementById(id);

    if (input.type === "password") {

        input.type = "text";

        icon.textContent = "🙈";

    } else {

        input.type = "password";

        icon.textContent = "👁";

    }
}

</script>


</body>

</html>
