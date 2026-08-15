<%@ page import="java.util.List" %>
<%@ page import="com.findify.model.LostItem" %>
	
	
	<!DOCTYPE html>
	<html lang="en">
	
	<head>
	
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	
	<title>Findify | Browse Lost Items</title>
	
	
	<!-- Fonts -->
	
	<link rel="preconnect" href="https://fonts.googleapis.com">
	
	<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&family=JetBrains+Mono:wght@400;600&family=Special+Elite&display=swap" rel="stylesheet">
	
	
	<!-- CSS -->
	
	<link rel="stylesheet" href="css/browselost.css">
	
	
	</head>
	
	
	<body>
	
	
	<!-- ===========================
	          HEADER
	=========================== -->
	
	
	<header>
	
	<div class="container">
	
	
	<a href="index.jsp" class="logo">
	FINDIFY
	</a>
	
	
	<nav>
	
	<a href="index.jsp">
	Home
	</a>
	
	<a href="contact.html">
	Contact Us
	</a>
	
	</nav>
	
	
	</div>
	
	</header>
	
	
	
	
	<!-- ===========================
	             HERO
	=========================== -->
	
	
	<section class="hero">
	
	
	<div class="container">
	
	
	<div class="tag">
	LOST ITEMS DATABASE
	</div>
	
	
	<h1>
	Browse Lost Items
	</h1>
	
	
	<p class="hero-text">
	
	Something missing?
	Search through reported lost items and reconnect with what belongs to you.
	
	</p>
	
	
	</div>
	
	
	</section>
	
	
	
	
	
	<!-- ===========================
	        SEARCH SECTION
	=========================== -->
	
	
	<section class="search-section">
	
	
	<div class="container">
	
	
	<div class="search-area">
	
	
	<input 
	type="text" 
	id="searchInput"
	placeholder="Search lost item...">
	
	
	<select id="categoryFilter">
	
	<option value="all">
	All Categories
	</option>
	
	<option value="1">
	Electronics
	</option>
	
	<option value="2">
	Books
	</option>
	
	<option value="3">
	Wallet
	</option>
	
	<option value="4">
	ID Card
	</option>
	
	<option value="5">
	Keys
	</option>
	
	<option value="6">
	Bag
	</option>
	
	<option value="7">
	Clothing
	</option>
	
	<option value="8">
	Mobile
	</option>
	
	<option value="9">
	Jewellery
	</option>
	
	<option value="10">
	Accessories
	</option>
	
	<option value="11">
	Others
	</option>
	
	</select>
	
	
	</div>
	
	
	</div>
	
	
	</section>
	
	
	
	
	
	
	<!-- ===========================
	          LOST ITEMS
	=========================== -->
	
	
	<section>
	
	
	<div class="container">
	
	
	<div class="item-grid" id="itemGrid">
	
<%
List<LostItem> lostItems =
(List<LostItem>) request.getAttribute("lostItems");

if(lostItems != null && !lostItems.isEmpty()){

    for(LostItem item : lostItems){
%>
	
	<div class="item-card" data-category="<%= item.getCategoryId() %>">
	
	    <div class="content">
	
	        <span class="item-id">
	            #LST-<%= item.getLostId() %>
	        </span>
	
	        <h2 class="item-name">
	            <%= item.getItemName() %>
	        </h2>
	
	        <div class="status">
	            <%= item.getStatus() %>
	        </div>
	
	        <p>
	            Location : <%= item.getLocationLost() %>
	        </p>
	
	        <p>
	            Date : <%= item.getDateLost() %>
	        </p>
	
	    </div>
	
	</div>
	
<%
    }
}
else{
%>
	
	<h2 style="text-align:center;width:100%;padding:60px;color:#555;">
	No Lost Items Available
	</h2>
	
<%
}
%>
	
	</div>
	
	
	
	</div>
	
	
	</section>
	
	
	
	
	
	<!-- ===========================
	            FOOTER
	=========================== -->
	
	
	<footer>
	
	
	<p>
	&copy; 2026 Findify | Lost it? Log it. Found it? Tag it.
	</p>
	
	
	</footer>
	
	
	
	<script src="js/browselost.js"></script>
	
	
	</body>
	
	</html>