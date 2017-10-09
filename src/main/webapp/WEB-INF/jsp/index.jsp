<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<script src="https://apis.google.com/js/platform.js" async defer></script>
<script src="/resources/js/skillsheets.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Skill Sheets</title>

<script type="text/javascript">
	function onLoad() {
		skillsheets.signIn().then(function() {
			document.getElementById("id").innerHTML = skillsheets.profile().getId();
			document.getElementById("name").innerHTML = skillsheets.profile().getName();
			skillsheets.redirectWithToken("http://localhost:8080/home/");
		});
	}
</script>

</head>

<body onload="onLoad()">

<h1>index.jsp</h1>

<p>ID: <label id="id"/></p>
<p>FullName: <label id="name"/></p>

</body>

</html>