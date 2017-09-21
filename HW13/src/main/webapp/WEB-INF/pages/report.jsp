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
		<h1>OS usage</h1>
		<p>Here are the results of OS usage in a survey that we completed.</p>	
		 <img src="/webapp2/reportImage">
	</body>

</html>