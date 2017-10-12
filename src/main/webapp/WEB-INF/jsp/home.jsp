<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.shilling.skillsheets.model.User" %>

<% User user = (User) request.getAttribute("user"); %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Skill Sheets - <%=user.getName() %></title>
</head>

<body class="w3-light-gray">

<div class="w3-bar w3-white w3-padding" style="height:56px">

	<img class="w3-bar-item w3-left" style="height:40px" src="/resources/img/logo.png">
	
	<input class="w3-bar-item w3-input w3-light-gray" />
	
	<div class="w3-dropdown-hover w3-right">
		<img class="w3-circle w3-right w3-hover-grayscale" style="width:40px;height:40px;"
			src="<%=user.getImageUrl() %>">
			
		<div class="w3-dropdown-content w3-bar-block" style="position:fixed;top:56px;right:0">
			<span class="w3-bar-item w3-border w3-small">
				<b>Google Account</b><br>
				<%=user.getName() %><br>
				<%=user.getEmail() %>
			</span>
			<span class="w3-bar-item w3-button">Settings</span>
			<span class="w3-bar-item w3-button">Logout</span>
		</div>
	</div>

</div>

<h1>Home JSP</h1>

<p>Hello, <b><%=user.getName() %></b>! Your Google Id is <b><%=user.getId() %></b> and 
your image url is <b><%=user.getImageUrl() %></b></p>

</body>

</html>