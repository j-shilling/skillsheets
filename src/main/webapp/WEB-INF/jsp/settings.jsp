<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage = "error.jsp" %>
    
<%@ page import="com.shilling.skillsheets.StringConstants" %>
<%@ page import="com.shilling.skillsheets.model.User" %>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<% User user = (User) request.getAttribute(StringConstants.USER); %>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>

<script src="https://apis.google.com/js/platform.js"></script>
<script src="/resources/js/skillsheets.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<title>Skill Sheets - <%=user.getName() %></title>
</head>

<body class="w3-light-gray">

<jsp:include page="header.jsp" />
<h1>settings.jsp</h1>
</body>
</html>