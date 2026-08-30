<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ page import="com.findify.model.User" %>

<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");

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

    <title>Findify | Change Name</title>

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap"
          rel="stylesheet">

    <link rel="stylesheet"
          href="css/settings.css">


    <style>

        /* =====================================================
           PAGE-SPECIFIC FORM STYLING
           Same Findify theme, improved Change Name layout
        ===================================================== */

        .change-form {
            margin-top: 28px;
        }


        .form-group {
            margin-bottom: 22px;
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


        .form-group input {
            width: 100%;

            padding: 13px 14px;

            border: 2px solid #221E1A;

            border-radius: 4px;

            background: #FFFDF8;

            color: #221E1A;

            font-family: 'Inter', sans-serif;

            font-size: 15px;

            outline: none;

            transition: .2s ease;
        }


        .form-group input:focus {
            border-color: #B33A2D;

            box-shadow: 3px 3px 0 rgba(179,58,45,.15);
        }


        .form-group input:hover {
            border-color: #6B6255;
        }


        .current-value {
            margin-bottom: 24px;

            padding: 14px 16px;

            background: rgba(216,205,180,.28);

            border: 1px dashed #C8B99D;

            border-radius: 4px;
        }


        .current-value .current-label {
            display: block;

            margin-bottom: 5px;

            font-family: 'JetBrains Mono', monospace;

            font-size: 10px;

            font-weight: 700;

            letter-spacing: 1px;

            text-transform: uppercase;

            color: #6B6255;
        }


        .current-value .current-name {
            font-family: 'Special Elite', monospace;

            font-size: 19px;

            color: #221E1A;
        }


        .form-actions {
            display: flex;

            gap: 12px;

            margin-top: 26px;
        }


        .save-btn {
            flex: 1;

            display: inline-flex;

            justify-content: center;

            align-items: center;

            padding: 12px 18px;

            border: 2px solid #22303F;

            border-radius: 3px;

            background: #22303F;

            color: #FFFFFF;

            font-family: 'Inter', sans-serif;

            font-size: 13px;

            font-weight: 700;

            text-decoration: none;

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


        <!-- ICON -->

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

                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"></path>

                <circle cx="12" cy="7" r="4"></circle>

            </svg>

        </div>


        <h4>ACCOUNT SETTINGS</h4>

        <h1>Change Name</h1>

        <p class="settings-subtitle">
            Update the name associated with your Findify account.
        </p>


        <% if ("invalid".equals(error)) { %>

            <div class="error-message">
                Please enter a valid name.
            </div>

        <% } %>


        <% if ("failed".equals(error)) { %>

            <div class="error-message">
                Unable to update your name. Please try again.
            </div>

        <% } %>


        <!-- CURRENT NAME -->

        <div class="current-value">

            <span class="current-label">
                Current Name
            </span>

            <span class="current-name">
                <%= loggedInUser.getFullName() %>
            </span>

        </div>


        <!-- FORM -->

        <form action="ChangeNameServlet"
              method="post"
              class="change-form">


            <div class="form-group">

                <label for="fullName">
                    New Full Name
                </label>

                <input
                    type="text"
                    id="fullName"
                    name="fullName"
                    value="<%= loggedInUser.getFullName() %>"
                    placeholder="Enter your full name"
                    maxlength="100"
                    autocomplete="name"
                    required>

            </div>


            <div class="form-actions">

                <button type="submit"
                        class="save-btn">

                    Save Changes

                </button>


                <a href="settings.jsp"
                   class="cancel-btn">

                    Cancel

                </a>

            </div>

        </form>

    </div>

</section>


</body>

</html>
