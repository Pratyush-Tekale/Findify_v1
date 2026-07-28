<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.findify.model.User" %>

<%
User user = (User) session.getAttribute("loggedInUser");
%>

<!DOCTYPE html>
<html>
<head>
    <title>Login Success</title>
</head>
<body>

<h1>🎉 Login Successful!</h1>

<h2>Welcome <%= user.getFullName() %></h2>

<p>Email: <%= user.getEmail() %></p>

<p>Role: <%= user.getRole() %></p>

<p>User ID: <%= user.getUserId() %></p>

</body>
</html>