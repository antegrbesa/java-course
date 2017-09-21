<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
%>

<html>
	<body bgcolor="<%=pickedColor%>">
	<p><a href="colors.jsp">Background color chooser</a></p>
	<p><a href="/webapp2/trigonometric?a=0&b=90">Trigonometric values between 0-90</a></p>
	
	<form action="trigonometric" method="GET">
 		 Start value:<br><input type="number" name="a" min="0" max="360" step="1" value="0"><br>
 		 End value:<br><input type="number" name="b" min="0" max="360" step="1" value="360"><br>
 		 <input type="submit" value="Create table"><input type="reset" value="Reset">
	</form>
	
	<p><a href="/webapp2/funny">A really funny story</a></p>
	<p><a href="/webapp2/report.jsp">OS statistics</a></p>
	<p><a href="/webapp2/appinfo.jsp">Run Time</a></p>
	<p><a href="/webapp2/powers?a=1&b=100&n=3">Excel table of powers from 1-100</a></p>
	<p><a href="/webapp2/glasanje">Vote for your favorite band</a></p>
	</body>

</html>