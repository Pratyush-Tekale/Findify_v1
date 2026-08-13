<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="com.findify.model.User"%>
<%@ page import="com.findify.model.Dashboard"%>

<%
if(session.getAttribute("user")==null){

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

<link rel="stylesheet" href="css/userDashboard.css">

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">

</head>

<body>

<div class="container">

<!-- ================= SIDEBAR ================= -->

<div class="sidebar">

<h2>FINDIFY</h2>

<ul>

<li><a href="UserDashboardServlet">🏠 Dashboard</a></li>

<li><a href="#">👤 My Profile</a></li>

<li><a href="#">📦 Report Lost Item</a></li>

<li><a href="#">🎁 Report Found Item</a></li>

<li><a href="#">📋 My Claims</a></li>

<li><a href="LogoutServlet">🚪 Logout</a></li>

</ul>

</div>

<!-- ================= MAIN CONTENT ================= -->

<div class="main">

<div class="header">

<h1>Welcome,
<%= user.getFullName() %> 👋
</h1>

<p>
Manage your Findify account from here.
</p>

</div>

<!-- ================= PROFILE CARD ================= -->

<div class="profile-card">

<h2>My Profile</h2>

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

<h3>Lost Items</h3>

<h1><%= dashboard.getLostItems() %></h1>

</div>

<div class="card">

<h3>Found Items</h3>

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

<button onclick="location.href='lostItem.jsp'">

Report Lost Item

</button>

<button onclick="location.href='foundItem.jsp'">

Report Found Item

</button>

<button onclick="location.href='myClaims.jsp'">

View My Claims

</button>

<button onclick="location.href='profile.jsp'">

Edit Profile

</button>

</div>

</div>

</div>

<script src="js/userDashboard.js"></script>

</body>

</html>