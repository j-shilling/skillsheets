<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage = "error.jsp" %>
    
<%@ page import="com.shilling.skillsheets.StringConstants" %>
<%@ page import="com.shilling.skillsheets.model.User" %>
<%@ page import="com.shilling.skillsheets.model.UserViewOptions" %>
<%@ page import="com.shilling.skillsheets.dao.UserNotifications" %>
<%@ page import="com.shilling.skillsheets.dao.UserSettings" %>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<% User user = (User) request.getAttribute(StringConstants.USER); %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Skill Sheets - <%=user.getName() %></title>
</head>

<body class="w3-light-gray">

<jsp:include page="header.jsp" />

<h1>Home JSP</h1>

<p>Hello, <b><%=user.getName() %></b>! Your Google Id is <b><%=user.getId() %></b> and 
your image url is <b><%=user.getImageUrl() %></b></p>

</body>

</html>