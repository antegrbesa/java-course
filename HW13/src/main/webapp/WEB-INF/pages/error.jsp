<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
	
	String color = (String) session.getAttribute("color");
%>

<html>
	<body  bgcolor="<%=pickedColor%>">
		<h1>Error</h1>
		<p>An error occurred.</p>	
		<p><a href="/webapp2/index.jsp">Return to home page</a></p>
	</body>

</html>