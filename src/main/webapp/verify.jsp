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
Provide proof that this item belongs to you. Our admin will review your request before approving it.
</p>

<%@ page import="java.util.List" %>
<%@ page import="com.findify.dao.VerificationQuestionDAO" %>
<%@ page import="com.findify.model.VerificationQuestion" %>

<%
String foundId = request.getParameter("foundId");

List<VerificationQuestion> questions = null;
if (foundId != null) {
    questions = new VerificationQuestionDAO()
            .getQuestionsByFoundId(Integer.parseInt(foundId));
}
%>

<% if ("noquestions".equals(request.getParameter("error"))) { %>
<p style="color:#c0392b;">
This item has no verification questions on file yet — please contact the admin.
</p>
<% } %>

<% if (questions == null || questions.isEmpty()) { %>

<p style="color:#c0392b;">
No verification questions are available for this item.
</p>

<% } else { %>

<form action="ClaimServlet" method="post">

<input type="hidden"
       name="foundId"
       value="<%= foundId %>">

    <!-- Hidden Item ID -->

    <% for (VerificationQuestion q : questions) { %>

    <div class="input-group">

        <label><%= q.getQuestionText() %>
        <span style="color:#c0392b;">*</span>
        </label>

        <input
            type="text"
            name="answer_<%= q.getQuestionId() %>"
            placeholder="Your answer"
            required>

    </div>

    <% } %>

    <button type="submit" class="verify-btn">

        Submit Claim

    </button>

</form>

<% } %>

<div class="back-link">

<a href="index.html">

← Back to Home

</a>

</div>

</div>

</section>

<script src="js/verifyy.js"></script>

</body>
</html>