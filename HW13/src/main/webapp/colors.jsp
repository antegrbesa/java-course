<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
%>

<html>
	<body  bgcolor="<%=pickedColor%>">
	
	<p> <a href="/webapp2/setcolor?color=white">WHITE</a></p>
	<p> <a href="/webapp2/setcolor?color=red">RED</a></p>
	<p> <a href="/webapp2/setcolor?color=green">GREEN</a></p>
	<p> <a href="/webapp2/setcolor?color=cyan">CYAN</a></p>
	
	</body>

</html>