<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.findify.model.User"%>
<%@ page import="com.findify.model.Notification"%>

<%
    User loggedInUser =
        (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<Notification> notifications =
        (List<Notification>) request.getAttribute("notifications");

    Integer unreadCountObj =
        (Integer) request.getAttribute("unreadCount");

    int unreadCount =
        (unreadCountObj != null) ? unreadCountObj : 0;
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Findify | Notifications</title>

    <!-- Findify Fonts -->

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600;700&display=swap"
          rel="stylesheet">


    <style>

        /* =====================================================
           RESET
        ===================================================== */

        * {
            box-sizing: border-box;
        }


        /* =====================================================
           BODY
        ===================================================== */

        body {

            margin: 0;

            min-height: 100vh;

            background: #B99167;

            color: #221E1A;

            font-family: 'Inter', sans-serif;
        }


        /* =====================================================
           HEADER
        ===================================================== */

        header {

            height: 95px;

            background: #F3EEE1;

            border-bottom: 2px solid rgba(34, 30, 26, .15);

            display: flex;

            align-items: center;

            justify-content: space-between;

            padding: 0 58px;
        }


        .logo {

            font-family: 'Special Elite', monospace;

            font-size: 32px;

            letter-spacing: 1px;
        }


        .back-btn {

            padding: 11px 20px;

            background: #22303F;

            color: #FFFFFF;

            text-decoration: none;

            border: 2px solid #22303F;

            border-radius: 4px;

            font-size: 14px;

            font-weight: 700;

            transition: .2s ease;
        }


        .back-btn:hover {

            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px, -2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        /* =====================================================
           MAIN
        ===================================================== */

        .page {

            max-width: 900px;

            margin: 0 auto;

            padding: 55px 20px 70px;
        }


        .page-label {

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;

            letter-spacing: 2px;

            color: #B33A2D;

            text-transform: uppercase;

            margin-bottom: 7px;
        }


        h1 {

            margin: 0 0 10px;

            font-family: 'Special Elite', monospace;

            font-size: 40px;

            font-weight: 400;
        }


        .intro {

            color: #5F564B;

            font-size: 15px;

            line-height: 1.6;

            margin-bottom: 30px;
        }


        /* =====================================================
           NOTIFICATION CARD
        ===================================================== */

        .notifications-card {

            position: relative;

            background: #FBF7EE;

            border: 2px solid #221E1A;

            border-radius: 5px;

            box-shadow: 6px 7px 0 rgba(0,0,0,.12);

            overflow: hidden;
        }


        .notifications-card::before {

            content: "";

            position: absolute;

            top: -8px;

            left: 30px;

            width: 55px;

            height: 14px;

            background: rgba(227,163,67,.72);

            border: 1px solid rgba(0,0,0,.12);

            transform: rotate(-2deg);

            z-index: 2;
        }


        /* =====================================================
           CARD HEADER
        ===================================================== */

        .notifications-header {

            display: flex;

            align-items: center;

            justify-content: space-between;

            padding: 22px 25px;

            border-bottom: 2px dashed #C9B99F;
        }


        .notifications-header h2 {

            margin: 0;

            font-family: 'Special Elite', monospace;

            font-size: 24px;

            font-weight: 400;
        }


        .unread-badge {

            display: inline-flex;

            align-items: center;

            justify-content: center;

            min-width: 30px;

            height: 26px;

            padding: 0 8px;

            background: #B33A2D;

            color: #FFFFFF;

            border-radius: 4px;

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;
        }


        /* =====================================================
           NOTIFICATION ITEM
        ===================================================== */

        .notification-item {

            padding: 20px 25px;

            border-bottom: 1px dashed #C9B99F;

            display: flex;

            align-items: flex-start;

            gap: 15px;

            transition: background .2s ease;
        }


        .notification-item:last-child {

            border-bottom: none;
        }


        .notification-item:hover {

            background: rgba(216,205,180,.16);
        }


        /* UNREAD */

        .notification-item.unread {

            background: rgba(227,163,67,.10);
        }


        .notification-item.unread:hover {

            background: rgba(227,163,67,.17);
        }


        /* =====================================================
           ICON
        ===================================================== */

        .notification-icon {

            width: 38px;

            height: 38px;

            flex-shrink: 0;

            display: flex;

            align-items: center;

            justify-content: center;

            border: 2px solid #22303F;

            border-radius: 50%;

            color: #22303F;

            font-size: 16px;
        }


        .notification-item.unread .notification-icon {

            background: #22303F;

            color: #FFFFFF;
        }


        /* =====================================================
           CONTENT
        ===================================================== */

        .notification-content {

            flex: 1;
        }


        .notification-message {

            margin: 0 0 7px;

            font-size: 14px;

            line-height: 1.55;

            color: #221E1A;
        }


        .notification-item.unread .notification-message {

            font-weight: 700;
        }


        .notification-date {

            font-family: 'JetBrains Mono', monospace;

            font-size: 10px;

            color: #6B6255;
        }


        .unread-label {

            display: inline-block;

            margin-left: 8px;

            padding: 3px 6px;

            border-radius: 3px;

            background: #FFF3CD;

            color: #664D03;

            border: 1px solid #E3A343;

            font-family: 'JetBrains Mono', monospace;

            font-size: 9px;

            font-weight: 700;

            text-transform: uppercase;
        }


        /* =====================================================
           EMPTY STATE
        ===================================================== */

        .empty {

            padding: 55px 25px;

            text-align: center;

            color: #6B6255;
        }


        .empty-icon {

            font-size: 38px;

            margin-bottom: 10px;
        }


        .empty h3 {

            margin: 0 0 7px;

            font-family: 'Special Elite', monospace;

            font-size: 22px;

            font-weight: 400;

            color: #221E1A;
        }


        .empty p {

            margin: 0;

            font-size: 13px;
        }


        /* =====================================================
           FOOTER ACTION
        ===================================================== */

        .page-actions {

            margin-top: 25px;

            display: flex;

            justify-content: flex-end;
        }


        .dashboard-btn {

            padding: 11px 18px;

            background: #22303F;

            color: #FFFFFF;

            border: 2px solid #22303F;

            border-radius: 3px;

            text-decoration: none;

            font-size: 13px;

            font-weight: 700;

            transition: .2s ease;
        }


        .dashboard-btn:hover {

            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px,-2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        /* =====================================================
           MOBILE
        ===================================================== */

        @media (max-width: 600px) {

            header {

                height: 80px;

                padding: 0 20px;
            }


            .logo {

                font-size: 26px;
            }


            .back-btn {

                padding: 9px 12px;

                font-size: 12px;
            }


            .page {

                padding: 40px 15px 55px;
            }


            h1 {

                font-size: 32px;
            }


            .notifications-header {

                padding: 18px 20px;
            }


            .notification-item {

                padding: 18px 20px;
            }
            
            .mark-all-btn {
    padding: 11px 18px;

    background: #22303F;

    color: #FFFFFF;

    border: 2px solid #22303F;

    border-radius: 3px;

    font-family: 'Inter', sans-serif;

    font-size: 13px;

    font-weight: 700;

    cursor: pointer;

    transition: .2s ease;
}

.mark-all-btn:hover {
    background: #B33A2D;

    border-color: #B33A2D;

    transform: translate(-2px, -2px);

    box-shadow: 4px 4px 0 #221E1A;
}

        }

    </style>

</head>


<body>


<!-- =====================================================
     HEADER
===================================================== -->

<header>

    <div class="logo">
        FINDIFY
    </div>


    <a href="UserDashboardServlet"
       class="back-btn">

        &lt;- Back to Dashboard

    </a>
	
	
</header>



<!-- =====================================================
     MAIN
===================================================== -->

<div class="page">


    <div class="page-label">
        FINDIFY UPDATES
    </div>


    <h1>
        Notifications
    </h1>


    <p class="intro">
        Stay updated about your reports, claims,
        and important Findify activity.
    </p>



    <div class="notifications-card">


        <!-- CARD HEADER -->

        <div class="notifications-header">

            <h2>
                Your Notifications
            </h2>


            <% if (unreadCount > 0) { %>

                <span class="unread-badge">
                    <%= unreadCount %>
                </span>

            <% } %>

        </div>



        <!-- NOTIFICATIONS -->

        <%
            if (notifications != null &&
                !notifications.isEmpty()) {

                for (Notification notification : notifications) {

                    String itemClass =
                        notification.isRead()
                            ? "notification-item"
                            : "notification-item unread";
        %>


        <div class="<%= itemClass %>">


            <!-- ICON -->

            <div class="notification-icon">

                <%
                    if (notification.isRead()) {
                %>

                    ✓

                <%
                    } else {
                %>

                    !

                <%
                    }
                %>

            </div>


            <!-- CONTENT -->

            <div class="notification-content">

                <p class="notification-message">

                    <%= notification.getMessage() %>

                </p>


                <span class="notification-date">

                    <%= notification.getCreatedAt() %>

                </span>


                <% if (!notification.isRead()) { %>

                    <span class="unread-label">
                        New
                    </span>

                <% } %>

            </div>


        </div>


        <%
                }

            } else {
        %>


        <!-- EMPTY -->

        <div class="empty">

            <div class="empty-icon">
                🔔
            </div>

            <h3>
                No Notifications Yet
            </h3>

            <p>
                You're all caught up. New Findify
                updates will appear here.
            </p>

        </div>


        <%
            }
        %>


    </div>



    <div class="page-actions">

    <% if (unreadCount > 0) { %>

        <form action="NotificationServlet" method="post">

            <button type="submit"
                    name="action"
                    value="markAll"
                    class="mark-all-btn">

                Mark All as Read

            </button>

        </form>

    <% } %>

</div>


</div>


</body>

</html>