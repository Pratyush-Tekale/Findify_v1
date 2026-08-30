<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.findify.model.User"%>
<%@ page import="com.findify.model.Dashboard"%>

<%
if(session.getAttribute("loggedInUser")==null){

    response.sendRedirect("login.jsp");
    return;
}

User user=(User)request.getAttribute("user");
Dashboard dashboard=(Dashboard)request.getAttribute("dashboard");
%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">

<title>Findify | User Dashboard</title>

<link rel="stylesheet" href="<%= request.getContextPath() %>/css/userdashboard.css">

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

</head>

<body>

<div class="container">

<!-- ================= SIDEBAR ================= -->

<div class="sidebar">

<h2>FINDIFY</h2>

<ul>

<li><a href="profile.jsp">👤 My Profile</a></li>

<li><a href="#">📋 My Claims</a></li>

<li><a href="index.jsp">Home</a></li>

<li><a href="LogoutServlet">Logout</a></li>

</ul>

</div>

<!-- ================= MAIN CONTENT ================= -->

<div class="main">

<div class="header">

    <div class="welcome-text">

        <h1>
            Welcome,
            <%= user.getFullName() %> 👋
        </h1>

        <p>
            Manage your Findify account from here.
        </p>

    </div>


    <!-- NOTIFICATION BELL -->

    <a href="NotificationServlet"
       class="notification-bell">

        🔔

        <%
            Integer unreadCount =
                (Integer) request.getAttribute("unreadCount");

            if (unreadCount != null && unreadCount > 0) {
        %>

            <span class="notification-count">
                <%= unreadCount %>
            </span>

        <%
            }
        %>

    </a>

</div>

<!-- ================= PROFILE CARD ================= -->

<div class="profile-card">

<h2>Your Info</h2>

<table>

<tr>

<td><strong>Full Name</strong></td>

<td><%= user.getFullName() %></td>

</tr>

<tr>

<td><strong>Email</strong></td>

<td><%= user.getEmail() %></td>

</tr>

<tr>

<td><strong>Phone</strong></td>

<td><%= user.getPhone() %></td>

</tr>

<tr>

<td><strong>Role</strong></td>

<td><%= user.getRole() %></td>

</tr>

</table>

</div>

<!-- ================= DASHBOARD CARDS ================= -->

<div class="cards">

<div class="card">

<h3>Lost Items You Reported</h3>

<h1><%= dashboard.getLostItems() %></h1>

</div>

<div class="card">

<h3>Found Items You Reported</h3>

<h1><%= dashboard.getFoundItems() %></h1>

</div>

<div class="card">

<h3>Pending Claims</h3>

<h1><%= dashboard.getPendingClaims() %></h1>

</div>

<div class="card">

<h3>Approved Claims</h3>

<h1><%= dashboard.getApprovedClaims() %></h1>

</div>

</div>

<!-- ================= QUICK ACTIONS ================= -->

<div class="actions">

    <h2>Quick Actions</h2>
    
    
	<!-- MY CLAIMS -->
    <button type="button"
        onclick="location.href='MyReportsServlet'">

        <span class="action-badge">MY ACTIVITY</span>

<span class="action-title">
    <span class="action-icon">📋</span>
    View My Reports & Claims
</span>

<span class="action-description">
    View your lost reports, found reports, and claimed items.
</span>

        <span class="action-open">
            Open →
        </span>

    </button>
    
    
    <!-- REPORT LOST -->
    <button type="button"
            onclick="location.href='report.html'">

        <span class="action-badge">REPORT</span>

        <span class="action-title">
            <span class="action-icon">📢</span>
            Report Lost Item
        </span>

        <span class="action-description">
            Lost something on campus? Submit a report so others can help find it.
        </span>

        <span class="action-open">
            Open →
        </span>

    </button>


    <!-- REPORT FOUND -->
    <button type="button"
            onclick="location.href='reportfound.html'">

        <span class="action-badge">REPORT</span>

        <span class="action-title">
            <span class="action-icon">📦</span>
            Report Found Item
        </span>

        <span class="action-description">
            Found an item? Report it and help reunite it with its owner.
        </span>

        <span class="action-open">
            Open →
        </span>

    </button>


    <!-- BROWSE LOST -->
    <button type="button"
            onclick="location.href='BrowseLostItemsServlet'">

        <span class="action-badge">BROWSE</span>

        <span class="action-title">
            <span class="action-icon">🔎</span>
            Browse Lost Items
        </span>

        <span class="action-description">
            View recently reported lost items across the campus.
        </span>

        <span class="action-open">
            Browse →
        </span>

    </button>


    <!-- BROWSE FOUND -->
    <button type="button"
            onclick="location.href='ViewFoundServlet'">

        <span class="action-badge">BROWSE</span>

        <span class="action-title">
            <span class="action-icon">✅</span>
            Browse Found Items
        </span>

        <span class="action-description">
            See all found items waiting to be claimed by their owners.
        </span>

        <span class="action-open">
            Browse →
        </span>

    </button>

</div>

</div>

</div>

<script src="js/userDashboard.js"></script>

</body>

</html>