<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@ page import="com.shilling.skillsheets.StringConstants" %>
<%@ page import="com.shilling.skillsheets.model.User" %>
<%@ page import="com.shilling.skillsheets.model.UserViewOptions" %>
<%@ page import="com.shilling.skillsheets.dao.UserNotifications" %>
<%@ page import="com.shilling.skillsheets.dao.UserSettings" %>

<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<% User user = (User) request.getAttribute(StringConstants.USER); %>
<% UserNotifications userNotifications = 
	(UserNotifications) request.getAttribute (StringConstants.USER_NOTIFICATIONS); %>
<% UserSettings userSettings = 
	(UserSettings) request.getAttribute (StringConstants.USER_SETTINGS); %>
    
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
	
	<input class="w3-bar-item w3-input w3-light-gray" type="text" value="Search">
	
	<div class="w3-dropdown-hover w3-right" style="margin-left:20px">
		<img class="w3-bar-item w3-circle w3-right w3-hover-grayscale" style="padding:0;width:40px;height:40px;"
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
	
	<c:choose>
		<c:when test="${userNotifications.getPendingNotificationCount(user) <= 0}">
			<span class="w3-bar-item w3-circle w3-gray w3-large w3-center w3-text-white w3-right"
					style="width:40px;height:40px;padding:0;line-height:40px;margin-left:50px">
				<b>0</b>
			</span>
		</c:when>
		
		<c:otherwise>
			<div class="w3-dropdown-hover w3-right">
				<span class="w3-bar-item w3-circle w3-red w3-large w3-center w3-hover-grayscale"
						style="width:40px;height:40px;padding:0;line-height:40px;margin-left:50px">
					<b><%=userNotifications.getPendingNotificationCount(user) %></b>
				</span>
				<div class="w3-dropdown-content w3-bar-block"
						style="position:fixed;top:56px">
					<span class="w3-bar-item w3-button">Message Here</span>
				</div>
			</div>
		</c:otherwise>
	</c:choose>
	
	<select class="w3-bar-item w3-select w3-boarder w3-right w3-large w3-light-gray"
			style="height:40px">
		<c:forEach items="${UserViewOptions.values()}" var="opt">
			<option class="w3-large" value="${opt.toString()}" 
				<c:if test="${userSettings.getUserViewOption(user) == opt}">selected</c:if>
			>
				<c:out value="${opt.toString().substring(0, 1).toUpperCase().concat(opt.toString().substring(1).toLowerCase())}"/> Resources
			</option>
		</c:forEach>
	</select>
	
	<label class="w3-right w3-large w3-light-gray"
			style="height:40px;line-height:40px;text-alien:right;padding-left:8px">
		Viewing:
	</label>

</div>

<h1>Home JSP</h1>

<p>Hello, <b><%=user.getName() %></b>! Your Google Id is <b><%=user.getId() %></b> and 
your image url is <b><%=user.getImageUrl() %></b></p>

</body>

</html>