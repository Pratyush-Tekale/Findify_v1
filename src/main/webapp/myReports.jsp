<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List"%>
<%@ page import="com.findify.model.User"%>
<%@ page import="com.findify.model.LostItem"%>
<%@ page import="com.findify.model.FoundItem"%>
<%@ page import="com.findify.model.Claim"%>

<%
    User loggedInUser =
        (User) session.getAttribute("loggedInUser");

    if (loggedInUser == null) {
        response.sendRedirect("login.jsp");
        return;
    }

    List<LostItem> lostItems =
        (List<LostItem>) request.getAttribute("lostItems");

    List<FoundItem> foundItems =
        (List<FoundItem>) request.getAttribute("foundItems");

    List<Claim> claims =
        (List<Claim>) request.getAttribute("claims");

    String success =
        request.getParameter("success");

    String error =
        request.getParameter("error");
%>

<!DOCTYPE html>

<html lang="en">

<head>

    <meta charset="UTF-8">

    <meta name="viewport"
          content="width=device-width, initial-scale=1.0">

    <title>Findify | My Reports</title>

    <!-- Findify Fonts -->

    <link rel="preconnect"
          href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600;700&display=swap"
          rel="stylesheet">


    <style>

        /* =====================================================
           RESET
        ===================================================== */

        * {
            box-sizing: border-box;
        }


        /* =====================================================
           BODY
        ===================================================== */

        body {

            margin: 0;

            background: #B99167;

            color: #221E1A;

            font-family: 'Inter', sans-serif;
        }


        /* =====================================================
           HEADER
        ===================================================== */

        header {

            height: 95px;

            background: #F3EEE1;

            border-bottom: 2px solid rgba(34, 30, 26, .15);

            display: flex;

            align-items: center;

            justify-content: space-between;

            padding: 0 58px;
        }


        .logo {

            font-family: 'Special Elite', monospace;

            font-size: 32px;

            letter-spacing: 1px;

            color: #221E1A;
        }


        .back-btn {

            display: inline-block;

            padding: 11px 20px;

            background: #22303F;

            color: #FFFFFF;

            text-decoration: none;

            border: 2px solid #22303F;

            border-radius: 4px;

            font-size: 14px;

            font-weight: 700;

            transition: .2s ease;
        }


        .back-btn:hover {

            background: #B33A2D;

            border-color: #B33A2D;

            transform: translate(-2px, -2px);

            box-shadow: 4px 4px 0 #221E1A;
        }


        /* =====================================================
           MAIN PAGE
        ===================================================== */

        .page {

            max-width: 1100px;

            margin: 0 auto;

            padding: 55px 20px 70px;
        }


        .page-label {

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;

            letter-spacing: 2px;

            color: #B33A2D;

            text-transform: uppercase;

            margin-bottom: 7px;
        }


        h1 {

            font-family: 'Special Elite', monospace;

            font-size: 40px;

            font-weight: 400;

            margin: 0 0 10px;

            color: #221E1A;
        }


        .intro {

            color: #5F564B;

            margin-bottom: 35px;

            font-size: 15px;

            line-height: 1.6;
        }


        /* =====================================================
           REPORT SECTION
        ===================================================== */

        .report-section {

            position: relative;

            background: #FBF7EE;

            border: 2px solid #221E1A;

            border-radius: 5px;

            padding: 30px;

            margin-bottom: 30px;

            box-shadow: 6px 7px 0 rgba(0,0,0,.12);
        }


        /* Paper Tape */

        .report-section::before {

            content: "";

            position: absolute;

            top: -8px;

            left: 30px;

            width: 55px;

            height: 14px;

            background: rgba(227,163,67,.72);

            border: 1px solid rgba(0,0,0,.12);

            transform: rotate(-2deg);
        }


        /* Section heading */

        .section-title {

            font-family: 'Special Elite', monospace;

            font-size: 27px;

            font-weight: 400;

            margin: 0 0 20px;

            color: #221E1A;
        }


        /* =====================================================
           ITEM ROW
        ===================================================== */

        .report-item {

            border-top: 1px dashed #C9B99F;

            padding: 20px 0;

            display: flex;

            justify-content: space-between;

            align-items: center;

            gap: 20px;
        }


        .report-item:first-of-type {

            border-top: none;
        }


        .item-id {

            font-family: 'JetBrains Mono', monospace;

            font-size: 11px;

            font-weight: 700;

            color: #B33A2D;

            margin-bottom: 5px;
        }


        .item-name {

            margin: 0 0 7px;

            font-size: 20px;

            font-weight: 700;

            color: #221E1A;
        }


        .details {

            color: #6B6255;

            font-size: 13px;

            line-height: 1.6;
        }


        /* =====================================================
           STATUS BADGES
        ===================================================== */

        .status {

            display: inline-block;

            margin-top: 8px;

            padding: 6px 10px;

            border-radius: 4px;

            font-family: 'JetBrains Mono', monospace;

            font-size: 10px;

            font-weight: 700;

            text-transform: uppercase;

            letter-spacing: .5px;
        }


        /* PENDING = AMBER */

        .status-pending {

            background: #FFF3CD;

            color: #664D03;

            border: 1px solid #E3A343;
        }


        /* APPROVED = GREEN */

        .status-approved {

            background: #D1E7DD;

            color: #0F5132;

            border: 1px solid #198754;
        }


        /* REJECTED = RED */

        .status-rejected {

            background: #F8D7DA;

            color: #842029;

            border: 1px solid #B33A2D;
        }


        /* OTHER STATUS */

        .status-other {

            background: #E8E3D8;

            color: #5F564B;

            border: 1px solid #A79B89;
        }


        /* =====================================================
           REMOVE REPORT
        ===================================================== */

        .remove-form {

            flex-shrink: 0;
        }


        .remove-btn {

            padding: 9px 14px;

            border: 2px solid #B33A2D;

            background: transparent;

            color: #B33A2D;

            border-radius: 3px;

            font-size: 12px;

            font-weight: 700;

            cursor: pointer;

            transition: .2s ease;
        }


        .remove-btn:hover {

            background: #B33A2D;

            color: #FFFFFF;

            transform: translate(-2px, -2px);

            box-shadow: 3px 3px 0 #221E1A;
        }


        /* =====================================================
           CLAIM
        ===================================================== */

        .claim-item {

            border-top: 1px dashed #C9B99F;

            padding: 20px 0;
        }


        .claim-item:first-of-type {

            border-top: none;
        }


        .claim-grid {

            display: grid;

            grid-template-columns: 1fr 1fr;

            gap: 18px 25px;

            margin-top: 12px;
        }


        .claim-label {

            font-family: 'JetBrains Mono', monospace;

            font-size: 10px;

            font-weight: 700;

            text-transform: uppercase;

            color: #6B6255;

            margin-bottom: 5px;
        }


        .claim-value {

            font-size: 14px;

            font-weight: 600;

            color: #221E1A;
        }


        /* =====================================================
           EMPTY
        ===================================================== */

        .empty {

            padding: 18px 0;

            color: #6B6255;

            font-size: 14px;
        }


        /* =====================================================
           SUCCESS / ERROR ALERT AREA
        ===================================================== */

        .notice {

            margin-bottom: 25px;

            padding: 12px 15px;

            border-radius: 3px;

            font-size: 13px;

            font-weight: 600;
        }


        .notice-success {

            background: #D1E7DD;

            color: #0F5132;

            border-left: 4px solid #198754;
        }


        .notice-error {

            background: #F8D7DA;

            color: #842029;

            border-left: 4px solid #B33A2D;
        }


        /* =====================================================
           RESPONSIVE
        ===================================================== */

        @media (max-width: 700px) {

            header {

                padding: 0 20px;

                height: 80px;
            }


            .logo {

                font-size: 26px;
            }


            .back-btn {

                padding: 9px 12px;

                font-size: 12px;
            }


            .page {

                padding: 40px 15px 55px;
            }


            h1 {

                font-size: 32px;
            }


            .report-section {

                padding: 25px 20px;
            }


            .report-item {

                flex-direction: column;

                align-items: flex-start;
            }


            .remove-form {

                width: 100%;
            }


            .remove-btn {

                width: 100%;
            }


            .claim-grid {

                grid-template-columns: 1fr;
            }

        }

    </style>

</head>


<body>


<!-- =====================================================
     HEADER
===================================================== -->

<header>

    <div class="logo">
        FINDIFY
    </div>


    <a href="UserDashboardServlet"
       class="back-btn">

        &lt;- Back to Dashboard

    </a>

</header>



<!-- =====================================================
     PAGE
===================================================== -->

<div class="page">


    <div class="page-label">
        MY FINDIFY ACTIVITY
    </div>


    <h1>
        Your Reports & Claims
    </h1>


    <p class="intro">
        View the items you have reported and the items
        you have claimed.
    </p>



    <!-- =====================================================
         SUCCESS / ERROR MESSAGE
    ====================================================== -->

    <% if ("removed".equals(success)) { %>

        <div class="notice notice-success">
            ✓ Report removed successfully.
        </div>

    <% } %>


    <% if ("removefailed".equals(error)) { %>

        <div class="notice notice-error">
            Unable to remove the report. Please try again.
        </div>

    <% } %>



    <!-- =====================================================
         LOST ITEMS
    ====================================================== -->

    <div class="report-section">

        <h2 class="section-title">
            Lost Items You Reported
        </h2>


        <%

            if (lostItems != null &&
                !lostItems.isEmpty()) {

                for (LostItem item : lostItems) {

                    String lostStatus = item.getStatus();

                    String lostStatusClass = "status-other";

                    if ("PENDING".equalsIgnoreCase(lostStatus)) {

                        lostStatusClass = "status-pending";

                    } else if ("APPROVED".equalsIgnoreCase(lostStatus)) {

                        lostStatusClass = "status-approved";

                    } else if ("REJECTED".equalsIgnoreCase(lostStatus)) {

                        lostStatusClass = "status-rejected";
                    }

        %>


        <div class="report-item">


            <div>

                <div class="item-id">
                    #LST-<%= item.getLostId() %>
                </div>


                <h3 class="item-name">
                    <%= item.getItemName() %>
                </h3>


                <div class="details">

                    Location:
                    <%= item.getLocationLost() %>

                    <br>

                    Date:
                    <%= item.getDateLost() %>

                </div>


                <div class="status <%= lostStatusClass %>">

                    <%= lostStatus %>

                </div>

            </div>



            <!-- REMOVE LOST REPORT -->

            <form action="RemoveReportServlet"
                  method="post"
                  class="remove-form"
                  onsubmit="return confirm(
                      'Are you sure you want to remove this lost item report?'
                  );">


                <input
                    type="hidden"
                    name="type"
                    value="lost">


                <input
                    type="hidden"
                    name="id"
                    value="<%= item.getLostId() %>">


                <button
                    type="submit"
                    class="remove-btn">

                    Remove Report

                </button>

            </form>


        </div>


        <%

                }

            } else {

        %>


        <div class="empty">

            You have not reported any lost items.

        </div>


        <%

            }

        %>

    </div>



    <!-- =====================================================
         FOUND ITEMS
    ====================================================== -->

    <div class="report-section">

        <h2 class="section-title">
            Found Items You Reported
        </h2>


        <%

            if (foundItems != null &&
                !foundItems.isEmpty()) {

                for (FoundItem item : foundItems) {

                    String foundStatus = item.getStatus();

                    String foundStatusClass = "status-other";

                    if ("PENDING".equalsIgnoreCase(foundStatus)) {

                        foundStatusClass = "status-pending";

                    } else if ("APPROVED".equalsIgnoreCase(foundStatus)) {

                        foundStatusClass = "status-approved";

                    } else if ("REJECTED".equalsIgnoreCase(foundStatus)) {

                        foundStatusClass = "status-rejected";
                    }

        %>


        <div class="report-item">


            <div>

                <div class="item-id">
                    #FND-<%= item.getFoundId() %>
                </div>


                <h3 class="item-name">
                    <%= item.getItemName() %>
                </h3>


                <div class="details">

                    Location:
                    <%= item.getLocationFound() %>

                    <br>

                    Date:
                    <%= item.getDateFound() %>

                </div>


                <div class="status <%= foundStatusClass %>">

                    <%= foundStatus %>

                </div>

            </div>



            <!-- REMOVE FOUND REPORT -->

            <form action="RemoveReportServlet"
                  method="post"
                  class="remove-form"
                  onsubmit="return confirm(
                      'Are you sure you want to remove this found item report?'
                  );">


                <input
                    type="hidden"
                    name="type"
                    value="found">


                <input
                    type="hidden"
                    name="id"
                    value="<%= item.getFoundId() %>">


                <button
                    type="submit"
                    class="remove-btn">

                    Remove Report

                </button>

            </form>


        </div>


        <%

                }

            } else {

        %>


        <div class="empty">

            You have not reported any found items.

        </div>


        <%

            }

        %>

    </div>



    <!-- =====================================================
         YOUR CLAIMS
    ====================================================== -->

    <div class="report-section">

        <h2 class="section-title">
            Your Claims
        </h2>


        <%

            if (claims != null &&
                !claims.isEmpty()) {

                for (Claim claim : claims) {

                    String claimStatus = claim.getStatus();

                    String claimStatusClass = "status-other";

                    if ("PENDING".equalsIgnoreCase(claimStatus)) {

                        claimStatusClass = "status-pending";

                    } else if ("APPROVED".equalsIgnoreCase(claimStatus)) {

                        claimStatusClass = "status-approved";

                    } else if ("REJECTED".equalsIgnoreCase(claimStatus)) {

                        claimStatusClass = "status-rejected";
                    }

        %>


        <div class="claim-item">


            <div class="item-id">

                CLAIM #<%= claim.getClaimId() %>

            </div>



            <div class="claim-grid">


                <!-- ITEM -->

                <div>

                    <div class="claim-label">
                        Item
                    </div>

                    <div class="claim-value">

                        <%= claim.getItemName() %>

                    </div>

                </div>



                <!-- TICKET ID -->

                <div>

                    <div class="claim-label">
                        Ticket ID
                    </div>

                    <div class="claim-value">

                        #FND-<%= claim.getFoundId() %>

                    </div>

                </div>



                <!-- STATUS -->

                <div>

                    <div class="claim-label">
                        Status
                    </div>

                    <div class="claim-value">

                        <span class="status <%= claimStatusClass %>">

                            <%= claimStatus %>

                        </span>

                    </div>

                </div>



                <!-- CLAIM DATE -->

                <div>

                    <div class="claim-label">
                        Claimed On
                    </div>

                    <div class="claim-value">

                        <%= claim.getClaimDate() %>

                    </div>

                </div>


            </div>

        </div>


        <%

                }

            } else {

        %>


        <div class="empty">

            You have not claimed any items yet.

        </div>


        <%

            }

        %>


    </div>


</div>


</body>

</html>