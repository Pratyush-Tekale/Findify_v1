<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Findify | Verify OTP</title>

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

        <h4>VERIFY OTP</h4>

        <h1>Check Your Email</h1>

        <p>
            Enter the 6-digit OTP sent to your email address.
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


        <form action="VerifyOTPServlet" method="post">

            <div class="input-group">

                <label>
                    Enter OTP<span style="color:#c0392b;">*</span>
                </label>

                <input
                    type="text"
                    name="otp"
                    placeholder="Enter 6-digit OTP"
                    maxlength="6"
                    required>

            </div>


            <button type="submit" class="login-btn">
                Verify OTP
            </button>

        </form>

    </div>

</section>

</body>
</html>