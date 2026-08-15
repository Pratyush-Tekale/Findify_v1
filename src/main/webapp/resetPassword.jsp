<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    Boolean otpVerified =
            (Boolean) session.getAttribute("otpVerified");

    if (otpVerified == null || !otpVerified) {
        response.sendRedirect("forgotPassword.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Findify | Reset Password</title>

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

        <h4>RESET PASSWORD</h4>

        <h1>Create New Password</h1>

        <p>
            Enter a new password for your Findify account.
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

        <form action="ResetPasswordServlet" method="post">

            <div class="input-group">

                <label>
                    New Password<span style="color:#c0392b;">*</span>
                </label>

                <input
                    type="password"
                    name="password"
                    placeholder="Enter new password"
                    required>

            </div>

            <div class="input-group">

                <label>
                    Confirm Password<span style="color:#c0392b;">*</span>
                </label>

                <input
                    type="password"
                    name="confirmPassword"
                    placeholder="Confirm new password"
                    required>

            </div>

            <button type="submit" class="login-btn">
                Reset Password
            </button>

        </form>

    </div>

</section>

</body>
</html>