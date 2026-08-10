<%@ page import="java.util.ArrayList" %>
<%@ page import="com.findify.model.FoundItem" %>
<!DOCTYPE html>
<html lang="en">

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>Findify | Found Items</title>

<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">

<link rel="preconnect" href="https://fonts.googleapis.com">

<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=Special+Elite&family=JetBrains+Mono:wght@400;600&family=Big+Shoulders+Condensed:wght@700&display=swap" rel="stylesheet">

<link rel="stylesheet" href="css/found.css">

</head>

<body>

<!-- ================= HEADER ================= -->

<header>

<div class="container">

<div class="logo">
 
FINDIFY

</div>

<nav>

<a href="index.html ">Home</a>

<a href="contact.html">Contact us</a>


</nav>

</div>

</header>

<!-- ================= HERO ================= -->

<section class="hero">

<div class="container">

<p class="tag">

Campus Lost & Found 

</p>

<h1>

Recently Found Items

</h1>

<p class="hero-text">

Search for your belongings and claim them after verification.

</p>

</div>

</section>

<!-- ================= SEARCH ================= -->

<section class="search-section">

<div class="container">

<div class="search-area">

<input
type="text"
id="searchInput"
placeholder="Search found item...">

<select id="categoryFilter">

<option value="all">
All Categories
</option>

<option value="Electronics">
Electronics
</option>

<option value="Books">
Books
</option>

<option value="Wallet">
Wallet
</option>

<option value="ID Card">
ID Card
</option>

<option value="Keys">
Keys
</option>

<option value="Bag">
Bag
</option>

<option value="Clothing">
Clothing
</option>

<option value="Mobile">
Mobile
</option>

<option value="Jewellery">
Jewellery
</option>

<option value="Accessories">
Accessories
</option>

<option value="Others">
Others
</option>

</select>

</div>

</div>

</section>

<!-- ================= FOUND ITEMS ================= -->

<section class="items">

<div class="container">

<div class="item-grid">

<%
ArrayList<FoundItem> foundItems =
(ArrayList<FoundItem>) request.getAttribute("foundItems");

if(foundItems != null && !foundItems.isEmpty()){

    for(FoundItem item : foundItems){
%>

<div class="item-card" data-category="<%= item.getCategoryName() %>">

    <div class="icon">
    <i class="fa-solid fa-tag"></i>
</div>

    <div class="content">

        <span class="item-id">
            #FND-<%= item.getFoundId() %>
        </span>

        <h2 class="item-name">
            <%= item.getItemName() %>
        </h2>

        <div class="status">
            <%= item.getStatus() %>
        </div>

        <p>
            Location : <%= item.getLocationFound() %>
        </p>

        <p>
            Date : <%= item.getDateFound() %>
        </p>

       <a href="verify.jsp?foundId=<%= item.getFoundId() %>" class="claim-btn">
    Claim Item
</a>

    </div>

</div>

<%
    }
}
else{
%>

<h2 style="text-align:center;width:100%;padding:60px;color:#555;">
No Found Items Available
</h2>

<%
}
%>

</div>


</div>

<!-- CARD -->

<div class="item-card" data-category="Watch">



</div>

</section>

<!-- ================= FOOTER ================= -->

<footer>

<div class="container">

<p>
&copy; 2026 FINDIFY | Campus Lost & Found

</p>

</div>

</footer>

<script src="js/found.js"></script>

</body>

</html>