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

<form action="ClaimServlet" method="post">

<%
String foundId = request.getParameter("foundId");
%>

<input type="hidden"
       name="foundId"
       value="<%= foundId %>">
       
    <!-- Hidden Item ID -->

    <div class="input-group">

        <label>Proof of Ownership
        <span style="color:#c0392b;">*</span>
        </label>

        <textarea
            id="proof"
            name="proof"
            rows="6"
            placeholder="Describe something that proves this item belongs to you. Example: color, serial number, unique marks, contents, ID card inside, etc."
            required>
        </textarea>

    </div>

    <div class="input-group">

        <label>Supporting Image (Optional)</label>

        <input
            type="file"
            id="proofFile"
            name="proofFile"
            accept=".jpg,.jpeg,.png">

        <small>
            (Optional for now. File upload will be connected later.)
        </small>

    </div>

    <button type="submit" class="verify-btn">

        Submit Claim

    </button>

</form>

<div class="back-link">

<a href="index.html">

<-† Back to Home

</a>

</div>

</div>

</section>

<script src="js/verifyy.js"></script>

</body>
</html>