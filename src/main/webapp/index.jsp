<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.findify.model.User" %>

<%
    User loggedInUser = (User) session.getAttribute("loggedInUser");
%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Findify &mdash; Lost it? Log it. Found it? Tag it.</title>
<link rel="preconnect" href="https://fonts.googleapis.com">
<link href="https://fonts.googleapis.com/css2?family=Special+Elite&family=Big+Shoulders+Condensed:wght@600;700;800&family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;500;700&display=swap" rel="stylesheet">
<link rel="stylesheet" href="css/style.css">
<style>
.user-menu {
    position: relative;
    display: inline-block;
}

.user-btn {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 10px 16px;
    background: #22303F;
    color: white;
    border: none;
    border-radius: 4px;
    font-size: 15px;
    font-weight: 600;
    cursor: pointer;
}

.user-btn:hover {
    background: #B33A2D;
}

.user-dropdown {
    display: none;
    position: absolute;
    right: 0;
    top: 100%;
    margin-top: 6px;
    width: 190px;
    background: #FBF7EE;
    border: 2px solid #221E1A;
    border-radius: 5px;
    box-shadow: 5px 6px 0 rgba(0,0,0,.25);
    z-index: 9999;
    overflow: hidden;
}

.user-dropdown a {
    display: block;
    padding: 12px 15px;
    color: #221E1A;
    text-decoration: none;
    font-size: 14px;
    border-bottom: 1px solid #d8cdb4;
}

.user-dropdown a:last-child {
    border-bottom: none;
}

.user-dropdown a:hover {
    background: #B33A2D;
    color: white;
}
</style>
</head>
<body>

<header>
  <div class="wrap">
    <nav>
      <a href="#top" class="logo">
        <svg class="tag-ico" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M2 12L12 2H20V10L10 20C9 21 7 21 6 20L2 16C1 15 1 13 2 12Z" stroke="#221E1A" stroke-width="2" stroke-linejoin="round"/>
          <circle cx="15" cy="7" r="1.6" fill="#221E1A"/>
        </svg>
        FINDIFY
      </a>
      <ul class="links"> 
        <li><a href="#how">How it Works</a></li> 
        <li><a href="contact.html">Contact us</a></li>
      </ul>
<%
if (loggedInUser != null) {
%>

<div class="user-menu">

    <button type="button" class="user-btn" onclick="toggleUserMenu()">

        <svg xmlns="http://www.w3.org/2000/svg"
             width="18"
             height="18"
             viewBox="0 0 24 24"
             fill="none"
             stroke="currentColor"
             stroke-width="2"
             stroke-linecap="round"
             stroke-linejoin="round">

            <path d="M20 21a8 8 0 0 0-16 0"></path>
            <circle cx="12" cy="7" r="4"></circle>

        </svg>

        User

    </button>


    <div class="user-dropdown" id="userDropdown">

        <a href="#">
            Your Profile
        </a>

        <a href="#">
            My Dashboard
        </a>

        <a href="#">
            Settings
        </a>

        <a href="LogoutServlet" style="color: red;">
            Log Out
        </a>

    </div>

</div>

<%
} else {
%>

<a href="login.jsp" class="nav-cta">

    <svg xmlns="http://www.w3.org/2000/svg"
         width="18"
         height="18"
         viewBox="0 0 24 24"
         fill="none"
         stroke="currentColor"
         stroke-width="2"
         stroke-linecap="round"
         stroke-linejoin="round">

        <path d="M20 21a8 8 0 0 0-16 0"></path>
        <circle cx="12" cy="7" r="4"></circle>

    </svg>

    Log in

</a>

<%
}
%>
    </nav>
  </div>
</header>
<section class="hero" id="top">
  <div class="wrap hero-inner">
    <div>
      <div class="eyebrow-strip">Campus Lost &amp; Found 	· Live Board</div>
      <h1 class="hero-title">
        <span class="stampline">If it's lost,</span><br>
        it's <span class="redword">logged.</span>
      </h1>
      <p class="lead">Findify keeps a running log of everything found around campus &mdash; books, IDs, earbuds, bottles, etc &mdash; so you can search it, claim it, and get back to class. Report what you lost in under a minute.</p>
      <div class="hero-ctas">

    <a href="report.html" class="btn btn-primary">
        Report Lost Item
    </a>

    <a href="BrowseLostItemsServlet" class="btn btn-secondary">
        Browse Lost Items
    </a>

    <a href="reportfound.html" class="btn btn-primary">
        Report Found Item
    </a>

    <a href="ViewFoundServlet" class="btn btn-secondary">
        Browse Found Items
    </a>

</div>
    </div>
    <div class="tag-hang-wrap">
      <div class="pin"></div>
      <div class="string"></div>
      <div class="claim-tag">
        <div class="tag-id">Campus Promise</div>
        <div class="tag-foot">See it?</div>&nbsp;&nbsp;&nbsp;&nbsp;
        <div class="stamp-flip">&nbsp;Report it&nbsp;</div>
        <div class="tag-foot">Small actions make campus better for everyone.</div>
      </div>
    </div>
  </div>
</section>
<div class="stats-strip">
  <div class="marquee-track">
    <div class="cat">
    <div class="cat-circle">&#x1F9F4;</div>
	<div class="cat-label">Bottles</div>
    </div>
    <div class="cat">
      <div class="cat-circle">🆔</div>
      <div class="cat-label">ID Cards</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F3A7;</div>
      <div class="cat-label">Earbuds</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4D6;</div>
      <div class="cat-label">Books</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F511;</div>
      <div class="cat-label">Keys</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4BE;</div>
      <div class="cat-label">Pendrive</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F45B;</div>
      <div class="cat-label">Wallets</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4F1;</div>
      <div class="cat-label">Phones</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F50C;</div>
      <div class="cat-label">Chargers</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x2602;&#xFE0F;</div>
      <div class="cat-label">Umbrellas</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x231A;</div>
      <div class="cat-label">Watches</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F392;</div>
      <div class="cat-label">Bags</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F9F4;</div>
      <div class="cat-label">Bottles</div>
    </div>
    <div class="cat">
      <div class="cat-circle">🆔</div>
      <div class="cat-label">ID Cards</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F3A7;</div>
      <div class="cat-label">Earbuds</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4D6;</div>
      <div class="cat-label">Books</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F511;</div>
      <div class="cat-label">Keys</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4BE;</div>
      <div class="cat-label">Pendrive</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F45B;</div>
      <div class="cat-label">Wallets</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F4F1;</div>
      <div class="cat-label">Phones</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F50C;</div>
      <div class="cat-label">Chargers</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x2602;&#xFE0F;</div>
      <div class="cat-label">Umbrellas</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x231A;</div>
      <div class="cat-label">Watches</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F392;</div>
      <div class="cat-label">Bags</div>
    </div>
    <div class="cat">
      <div class="cat-circle">&#x1F9F4;</div>
      <div class="cat-label">Bottles</div>
    </div>
    
  </div>
</div>
<section class="section quick-access">

<div class="wrap">

<div class="section-head">

<div class="section-eyebrow">
Quick Access
</div>

<h2 class="section-title">
What would you like to do?
</h2>

<p class="section-sub">
Choose any option to get started with Findify.
</p>

</div>


<div class="how-grid">


<div class="how-card">

<span class="how-tab">
REPORT
</span>

<h3>
&#x1F4E2; Report Lost Item
</h3>

<p>
Lost something on campus? Submit a report so others can help find it.
</p>

<a href="report.html" class="ticket-cta">Open -></a>

</div>


<div class="how-card">

<span class="how-tab">
BROWSE
</span>

<h3>
&#x1F50D; Browse Lost Items
</h3>

<p>
View recently reported lost items across the campus.
</p>

<a href="BrowseLostItemsServlet" class="ticket-cta">Browse -></a>

</div>


<div class="how-card">

<span class="how-tab">
REPORT
</span>

<h3>
&#x1F4E6; Report Found Item
</h3>

<p>
Found an item? Report it and help reunite it with its owner.
</p>

<a href="reportfound.html" class="ticket-cta">Open -></a>

</div>  



<div class="how-card">

<span class="how-tab">
BROWSE
</span>

<h3>
&#x2705; Browse Found Items
</h3>

<p>
See all found items waiting to be claimed by their owners.
</p>

<a href="ViewFoundServlet" class="ticket-cta">Browse -></a>

</div>

</div>

</div>

</section>
<section class="section" id="how">
  <div class="wrap">
    <div class="section-head">
      <div class="section-eyebrow">Process</div>
      <h2 class="section-title">Three steps, one board.</h2>
      <p class="section-sub">No forms buried in email. Report an item, let the board do the matching, and pick it up with a ticket.</p>
    </div>
    <div class="how-grid">
      <div class="how-card">
        <span class="how-tab">STEP 1 · FILE</span>
        <h3>Log it</h3>
        <p>Lost something or found something lying around? File a two-minute report with a photo, category, and where it turned up.</p>
      </div>
      <div class="how-card">
        <span class="how-tab">STEP 2 · MATCH</span>
        <h3>Get matched</h3>
        <p>Findify checks your report against the board by category, location, and date, and flags likely matches for you to review.</p>
      </div>
      <div class="how-card">
        <span class="how-tab">STEP 3 · CLAIM</span>
        <h3>Pick it up</h3>
        <p>Confirm it's yours, get a claim ticket, and collect your item at the nearest drop point no chasing announcements.</p>
      </div>
    </div>
  </div>
</section>

<section class="cta-band" id="report">
  <div class="wrap">
    <h2>Lost something today?</h2>
    <p>Filing a report takes less time than walking back to check.</p>
    <a href="report.html" class="btn btn-primary">Report a Lost Item</a>
  </div>
</section>
<footer>
  <div class="wrap">
    <div class="footer-grid">
      <div>
        <div class="footer-logo">FINDIFY</div>
        <p>A campus lost &amp; found board that actually gets things back to people. Built as a student project to make reporting and claiming items simple.</p>
      </div>
      <div class="footer-col">
        <h4>Findify</h4>
        <ul>
          <li><a href="ViewFoundServlet">Browse Found</a></li>
          <li><a href="report.html">Report Lost</a></li>
          <li><a href="BrowseLostItemsServlet">Browse Lost</a></li>
          <li><a href="reportfound.html">Report found</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>Campus</h4>
        <ul>
          <li><a href="rules.html">Rules &amp; FAQ</a></li>
          <li><a href="contact.html">Contact Admin</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>Project</h4>
        <ul>
          <li><a href="about.html">About This Build</a></li>
          <li><a href="#how">How it Works</a></li>
          <li><a href="https://github.com/Pratyush-Tekale/Findify_v1">GitHub Repo</a></li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <span id="copyright">Â© 2026 FINDIFY â COLLEGE PROJECT</span>
      <span> ISSUED CAMPUS-WIDE</span>
    </div>
  </div>
</footer>
<script src="js/main.js"></script>

<script>
function toggleUserMenu() {

    const menu = document.getElementById("userDropdown");

    if (menu.style.display === "block") {
        menu.style.display = "none";
    } else {
        menu.style.display = "block";
    }

}

</script>
</body>
</html>