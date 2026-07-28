<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Login</title>

<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap" rel="stylesheet">

<link rel="stylesheet" href="css/login.css">

</head>
<body>

<header>

    <div class="logo">
        FINDIFY
    </div>

    <a href="index.html" class="back-btn">
        ← Back to Home
    </a>

</header>

<section class="login-section">

<div class="login-card">

<div class="paper-tape"></div>

<h4>MEMBER LOGIN</h4>

<h1>Welcome Back</h1>

<p>
Log in to report lost items, browse found items, and manage your account.
</p>
<%
String error = (String) request.getAttribute("error");

if (error != null) {
%>

<p style="color:red; text-align:center;">
    <%= error %>
</p>

<%
}
%>



<form action="LoginServlet" method="post">
<div class="input-group">

<label>Email Address<span style="color:#c0392b;">*</span></label>

<input
    type="email"
    id="email"
    name="email"
    placeholder="Enter your email"
    required></div>

<div class="input-group">

<label>Password<span style="color:#c0392b;">*</span></label>

<div class="password-box">
<input
    type="password"
    id="password"
    name="password"
    placeholder="Enter your password"
    required>
<span class="toggle-password" id="togglePassword">👁</span>

</div>
</div>

<button type="submit" class="login-btn">
Login
</button>

</form>

<div class="links">

<a href="#">Forgot Password?</a>

</div>

<div class="register">

Don't have an account?

<a href="register.html">

Create Account

</a>

</div>

</div>

</section>

<script src="js/login.js"></script>
</body>
</html>