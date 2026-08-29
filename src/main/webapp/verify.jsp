<!DOCTYPE html>
<html lang="en">
<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Verify Claim</title>

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">

<link rel="stylesheet" href="css/verify.css">

</head>

<body>

<header>

<div class="logo">
FINDIFY
</div>

<nav>

<a href="index.jsp">Home</a>
<a href="contact.html">Contact Us</a>

</nav>

</header>

<section class="verify-section">

<div class="verify-card">

<div class="paper-tape"></div>

<h4>VERIFY CLAIM</h4>

<h1>Claim Found Item</h1>

<p>
Provide proof that this item belongs to you. Our AI compares your description
against the finder's private notes, and our admin reviews the result before
approving it.
</p>

<%@ page import="com.findify.dao.FoundItemDAO" %>
<%@ page import="com.findify.model.FoundItem" %>

<%
String foundId = request.getParameter("foundId");

FoundItem foundItem = null;
if (foundId != null) {
    foundItem = new FoundItemDAO().getFoundItemById(Integer.parseInt(foundId));
}
%>

<% if ("empty".equals(request.getParameter("error"))) { %>
<p style="color:#c0392b;">
Please describe the item before submitting.
</p>
<% } %>

<% if ("notfound".equals(request.getParameter("error"))) { %>
<p style="color:#c0392b;">
This item could not be found.
</p>
<% } %>

<% if (foundItem == null) { %>

<p style="color:#c0392b;">
This item is not available for claiming right now.
</p>

<% } else { %>

<form action="ClaimServlet" method="post">

<input type="hidden"
       name="foundId"
       value="<%= foundId %>">

    <div class="input-group">

        <label>Describe this item in your own words
        <span style="color:#c0392b;">*</span>
        </label>

        <p style="font-size:0.85rem;color:#666;margin-top:-6px;">
        Include details only the real owner would know — brand, color, marks,
        scratches, what's inside, etc.
        </p>

        <textarea
            name="description"
            rows="6"
            placeholder="e.g. Black leather wallet, small tear on the left corner, has a blue metro card and a photo of a dog inside..."
            required></textarea>

    </div>

    <button type="submit" class="verify-btn">

        Submit Claim

    </button>

</form>

<% } %>

<div class="back-link">

<a href="index.jsp">

← Back to Home

</a>

</div>

</div>

</section>


</body>
</html>
