<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Forgot Password</title>

<link rel="stylesheet" href="css/login.css">

</head>
<body>

<header>

    <div class="logo">
        FINDIFY
    </div>

    <a href="login.jsp" class="back-btn">
        <- Back to Login
    </a>

</header>

<section class="login-section">

<div class="login-card">

<div class="paper-tape"></div>

<h4>ACCOUNT RECOVERY</h4>

<h1>Forgot Password?</h1>

<p>
Enter your registered email address and we will send you a verification OTP.
</p>

<%
String error = (String) request.getAttribute("error");
String message = (String) request.getAttribute("message");

if (error != null) {
%>

<p style="color:red; text-align:center;">
    <%= error %>
</p>

<%
}

if (message != null) {
%>

<p style="color:green; text-align:center;">
    <%= message %>
</p>

<%
}
%>

<form action="ForgotPasswordServlet" method="post">

<div class="input-group">

<label>Email Address<span style="color:#c0392b;">*</span></label>

<input
    type="email"
    name="email"
    placeholder="Enter your registered email"
    required>

</div>

<button type="submit" class="login-btn">
Send OTP
</button>

</form>

</div>

</section>

</body>
</html>