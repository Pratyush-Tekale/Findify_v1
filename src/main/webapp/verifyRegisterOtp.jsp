```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Findify | Verify Email</title>

    <!-- Findify Fonts -->
    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&display=swap"
          rel="stylesheet">

    <style>

        /* =========================
           RESET
        ========================= */

        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }


        /* =========================
           BODY
        ========================= */

        body {
            min-height: 100vh;

            background: #B99167;

            color: #221E1A;

            font-family: 'Inter', sans-serif;

            display: flex;
            flex-direction: column;
        }


        /* =========================
           HEADER
        ========================= */

        header {
            height: 95px;

            background: #F3EEE1;

            border-bottom: 2px solid rgba(34, 30, 26, 0.15);

            display: flex;

            align-items: center;

            justify-content: space-between;

            padding: 0 58px;
        }


        /* FINDIFY LOGO */

        .logo {
            font-family: 'Special Elite', monospace;

            font-size: 32px;

            letter-spacing: 1px;

            color: #221E1A;
        }


        /* BACK BUTTON */

        .back-btn {
            display: inline-block;

            padding: 11px 20px;

            background: #22303F;

            color: #ffffff;

            text-decoration: none;

            border: 2px solid #22303F;

            border-radius: 4px;

            font-size: 14px;

            font-weight: 700;

            transition: 0.2s ease;
        }

        .back-btn:hover {
            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px, -2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        /* =========================
           MAIN SECTION
        ========================= */

        .verify-section {

            flex: 1;

            display: flex;

            justify-content: center;

            align-items: center;

            padding: 60px 20px;
        }


        /* =========================
           VERIFY CARD
        ========================= */

        .verify-card {

            width: 430px;

            max-width: 95%;

            padding: 40px;

            background: #FBF7EE;

            border: 2px solid #221E1A;

            border-radius: 6px;

            box-shadow: 7px 8px 0 rgba(0, 0, 0, 0.18);

            position: relative;
        }


        /* =========================
           PAPER TAPE
        ========================= */

        .verify-card::before {

            content: "";

            position: absolute;

            top: -9px;

            left: 35px;

            width: 55px;

            height: 15px;

            background: rgba(227, 163, 67, 0.75);

            border: 1px solid rgba(0, 0, 0, 0.15);

            transform: rotate(-2deg);
        }


        /* =========================
           CARD LABEL
        ========================= */

        .label {

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;

            letter-spacing: 1.5px;

            text-transform: uppercase;

            color: #B33A2D;

            margin-bottom: 8px;
        }


        /* =========================
           HEADING
        ========================= */

        h1 {

            font-family: 'Special Elite', monospace;

            font-size: 30px;

            font-weight: 400;

            line-height: 1.2;

            margin-bottom: 12px;

            color: #221E1A;
        }


        /* =========================
           DESCRIPTION
        ========================= */

        .description {

            color: #6B6255;

            font-size: 14px;

            line-height: 1.6;

            margin-bottom: 25px;
        }


        /* =========================
           ERROR MESSAGE
        ========================= */

        .error {

            margin-bottom: 18px;

            padding: 11px 12px;

            background: #F8D7DA;

            border-left: 4px solid #B33A2D;

            color: #842029;

            font-size: 13px;

            line-height: 1.4;
        }


        /* =========================
           OTP INPUT
        ========================= */

        .otp-input {

            width: 100%;

            padding: 15px;

            border: 2px solid #221E1A;

            border-radius: 4px;

            background: #FFFDF8;

            color: #221E1A;

            font-family: 'JetBrains Mono', monospace;

            font-size: 24px;

            font-weight: 700;

            letter-spacing: 8px;

            text-align: center;

            outline: none;

            transition: 0.2s ease;
        }


        .otp-input::placeholder {

            color: #9A9185;

            opacity: 1;
        }


        .otp-input:focus {

            border-color: #B33A2D;

            box-shadow: 3px 3px 0 rgba(179, 58, 45, 0.20);
        }


        /* =========================
           VERIFY BUTTON
        ========================= */

        .verify-btn {

            width: 100%;

            margin-top: 20px;

            padding: 14px;

            border: 2px solid #22303F;

            border-radius: 3px;

            background: #22303F;

            color: white;

            font-family: 'Inter', sans-serif;

            font-size: 14px;

            font-weight: 700;

            cursor: pointer;

            transition: 0.2s ease;
        }


        .verify-btn:hover {

            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px, -2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        /* =========================
           INFO TEXT
        ========================= */

        .info {

            margin-top: 20px;

            text-align: center;

            color: #6B6255;

            font-size: 12px;

            line-height: 1.5;
        }


        /* =========================
           RESPONSIVE
        ========================= */

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

            .verify-section {

                padding: 35px 15px;
            }

            .verify-card {

                padding: 30px 25px;
            }

            h1 {

                font-size: 26px;
            }
        }

    </style>

</head>


<body>


    <!-- =========================
         FINDIFY HEADER
    ========================== -->

    <header>

        <div class="logo">
            FINDIFY
        </div>

        <a href="register.html" class="back-btn">
            ← Back to Register
        </a>

    </header>


    <!-- =========================
         VERIFY SECTION
    ========================== -->

    <section class="verify-section">

        <div class="verify-card">


            <!-- Label -->

            <div class="label">
                Verify OTP
            </div>


            <!-- Heading -->

            <h1>
                Check Your Email
            </h1>


            <!-- Description -->

            <p class="description">

                We've sent a 6-digit verification code
                to the email address you entered during
                registration.

            </p>


            <!-- Error Message -->

            <%
                String error = (String) request.getAttribute("error");

                if (error != null) {
            %>

                <div class="error">
                    <%= error %>
                </div>

            <%
                }
            %>


            <!-- OTP FORM -->

            <form action="VerifyRegisterOtpServlet" method="post">


                <input
                    type="text"
                    name="otp"
                    class="otp-input"
                    placeholder="000000"
                    maxlength="6"
                    pattern="[0-9]{6}"
                    inputmode="numeric"
                    autocomplete="one-time-code"
                    required
                >


                <button
                    type="submit"
                    class="verify-btn">

                    Verify Email

                </button>


            </form>


            <!-- OTP Information -->

            <div class="info">

                OTP is valid for 5 minutes.

            </div>


        </div>

    </section>


</body>

</html>
```
