<%@ page contentType="text/html;charset=UTF-8" %>
<%
com.findify.model.User user =
(com.findify.model.User)session.getAttribute("loggedInUser");

if(user == null){
    response.sendRedirect("login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>

    <link rel="stylesheet" href="css/dashboard.css">
</head>
<body>

<div class="container">

    <h1>Admin Dashboard</h1>

    <h2>Welcome <%= user.getFullName() %></h2>

    <p><strong>Email:</strong> <%= user.getEmail() %></p>

    <p><strong>Role:</strong> <%= user.getRole() %></p>

    <hr>

    <h3>Modules</h3>

    <ul>
        <li>Manage Claims (Coming Soon)</li>
        <li>View Lost Items (Coming Soon)</li>
        <li>View Found Items (Coming Soon)</li>
    </ul>

    <a href="index.html">Home</a>

</div>

</body>
</html>