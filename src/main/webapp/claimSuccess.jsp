<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    Object claimIdAttr  = request.getAttribute("claimId");
    Object itemNameAttr = request.getAttribute("itemName");

    // This page is only meaningful when reached through ClaimServlet.
    if (claimIdAttr == null) {
        response.sendRedirect("ViewFoundServlet");
        return;
    }

    int claimId = (Integer) claimIdAttr;

    String itemName = (itemNameAttr != null)
            ? itemNameAttr.toString()
            : "Found Item";

    // Basic escaping so an item name can never inject markup.
    String safeItemName = itemName
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;");
%>
<!DOCTYPE html>
<html lang="en">

<head>

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>Findify | Claim Submitted</title>

    <link rel="preconnect" href="https://fonts.googleapis.com">

    <link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;700&display=swap"
          rel="stylesheet">

    <link rel="stylesheet" href="css/claim.css">

</head>

<body>

<div class="container">

    <div class="card">

        <h1>Verification Submitted</h1>

        <p class="message">
            Your claim has been recorded and is now waiting for admin review.
        </p>


        <!-- ============================================
             CLAIM TICKET - REAL DATA
        ============================================= -->

        <div class="ticket">

            <h2>Claim Status</h2>

            <p>
                <strong>Status :</strong>
                Pending Admin Verification
            </p>

            <p>
                <strong>Item :</strong>
                <span id="claimedItem"><%= safeItemName %></span>
            </p>

            <p>
                <strong>Reference ID :</strong>
                <span id="ticketId">CLM-<%= claimId %></span>
            </p>

            <p>
                <strong>Response Time :</strong>
                Usually within 24 hours
            </p>

        </div>


        <!-- ============================================
             WHAT HAPPENS NEXT
        ============================================= -->

        <div class="info">

            <h3>What Happens Next?</h3>

            <ul>
                <li>
                    An admin reviews your description alongside the AI
                    verification result.
                </li>
                <li>
                    You will be notified here and in your notifications
                    once a decision is made.
                </li>
                <li>
                    If approved, bring your College ID and quote
                    <strong>CLM-<%= claimId %></strong> at the
                    Lost &amp; Found Office.
                </li>
                <li>
                    The desk marks the claim as collected once you have
                    the item in hand. That closes the claim.
                </li>
            </ul>

        </div>


        <a href="MyReportsServlet#claims" class="btn">
            Track My Claims
        </a>

        <a href="index.jsp" class="btn">
            Back to Home
        </a>

    </div>

</div>

</body>
</html>
