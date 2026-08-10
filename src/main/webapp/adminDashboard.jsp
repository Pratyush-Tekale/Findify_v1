<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<%
com.findify.model.User loggedInUser =
(com.findify.model.User)session.getAttribute("loggedInUser");

if(loggedInUser==null){
response.sendRedirect("login.jsp");
return;
}

if(!"ADMIN".equals(loggedInUser.getRole())){
response.sendRedirect("login.jsp");
return;
}
%>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">

<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Admin Dashboard</title>

<link rel="stylesheet" href="css/style.css">
<link rel="stylesheet" href="css/admiin.css">

</head>

<body>

<header class="site-header">

<div class="wrap">

    <nav>

        <a href="AdminDashboardServlet" class="logo">
            FINDIFY
            <span class="admin-badge">
                Admin
            </span>
        </a>

        <div class="links">
			<a href="index.html" >
			Home
			</a>
            <a href="AdminDashboardServlet" class="active">
                Dashboard
            </a>

            <a href="#pendingClaims">
                Manage Claims
            </a>

            <a href="found.html">
                Found Items
            </a>

            <a href="lost.html">
                Lost Items
            </a>

        </div>

        <a href="LogoutServlet" class="nav-cta ghost">
            Logout
        </a>

    </nav>

</div>

</header>

<section class="section">
<div class="wrap">

<div class="section-head">

<h2 class="section-title">

Welcome,

${loggedInUser.fullName}

</h2>

<p class="section-sub">

Manage campus claims and monitor all lost & found activities.

</p>

</div>
<div class="admin-stat-grid">

    <div class="admin-stat-card">

        <div class="a-num">${totalUsers}</div>

        <div class="a-label">
            Total Users
        </div>

    </div>

    <div class="admin-stat-card">

        <div class="a-num">${totalLostItems}</div>

        <div class="a-label">
            Lost Items
        </div>

    </div>

    <div class="admin-stat-card">

        <div class="a-num">${totalFoundItems}</div>

        <div class="a-label">
            Found Items
        </div>

    </div>

    <div class="admin-stat-card">

        <div class="a-num">${pendingClaimsCount}</div>

        <div class="a-label">
            Pending Claims
        </div>

    </div>

    <div class="admin-stat-card">

        <div class="a-num">${approvedClaimsCount}</div>

        <div class="a-label">
            Approved Claims
        </div>

    </div>

    <div class="admin-stat-card">

        <div class="a-num">${rejectedClaimsCount}</div>

        <div class="a-label">
            Rejected Claims
        </div>

    </div>

</div>
<form action="AdminDashboardServlet" method="get" class="filter-bar">

<input
type="text"
name="search"
placeholder="Search Item">

<select name="status">

<option value="">
All Status
</option>

<option value="PENDING">
Pending
</option>

<option value="APPROVED">
Approved
</option>

<option value="REJECTED">
Rejected
</option>

</select>

<button type="submit">

Search

</button>

</form>

<div class="section-head" id="pendingClaims">

<h2 class="section-title">

Pending Claims

</h2>

<p class="section-sub">

Review and verify all submitted claim requests.

</p>

</div>

<div class="table-wrap">

<table class="table-ticket">

<thead>

<tr>

<th>Claim ID</th>

<th>Item</th>

<th>Claimed By</th>

<th>Phone</th>

<th>Proof</th>

<th>Claim Date</th>

<th>Status</th>

<th>Actions</th>

</tr>

</thead>

<tbody>

<c:choose>

<c:when test="${empty pendingClaims}">

<tr>

<td colspan="8" style="text-align:center;">

No Claims Found

</td>

</tr>

</c:when>

<c:otherwise>

<c:forEach var="claim" items="${pendingClaims}">

<tr>

<td>${claim.claimId}</td>

<td>${claim.itemName}</td>

<td>${claim.claimantName}</td>

<td>${claim.claimantPhone}</td>

<td>
    <span class="proof-text" title="${claim.proof}">
        ${claim.proof}
    </span>
</td>

<td>
<fmt:formatDate value="${claim.claimDate}" pattern="dd MMM yyyy HH:mm"/>
</td>

<td>

<c:choose>

<c:when test="${claim.status=='PENDING'}">
<span class="badge badge-amber">
Pending
</span>
</c:when>

<c:when test="${claim.status=='APPROVED'}">
<span class="badge badge-green">
Approved
</span>
</c:when>

<c:otherwise>
<span class="badge badge-red">
Rejected
</span>
</c:otherwise>

</c:choose>

</td>

<td>

<c:choose>

<c:when test="${claim.status=='PENDING'}">

<form action="ManageClaimsServlet" method="post" style="display:inline;">

<input type="hidden" name="claimId" value="${claim.claimId}">

<input type="hidden" name="action" value="approve">

<button class="btn btn-approve">
Approve
</button>

</form>

<form action="ManageClaimsServlet" method="post" style="display:inline;">

<input type="hidden" name="claimId" value="${claim.claimId}">

<input type="hidden" name="action" value="reject">

<button class="btn btn-reject">
Reject
</button>

</form>

</c:when>

<c:otherwise>

<span class="completed">
Completed
</span>

</c:otherwise>

</c:choose>

</td>

</tr>

</c:forEach>

</c:otherwise>

</c:choose>

</tbody>

</table>
</div>
</div>
</section>

<footer class="site-footer">

<div class="wrap">

<div class="footer-bottom">

<span>

© 2026 FINDIFY

</span>

<span>

Campus Lost & Found Admin Panel

</span>

</div>

</div>

</footer>

</body>

</html>
