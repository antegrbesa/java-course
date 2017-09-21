<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
%>

<p>Results:</p>

<body  bgcolor="<%=pickedColor%>">

	<table border="1"> 
		<tr><th>x</th><th>sin(x)</th><th>cos(x)</th></tr>
		<c:forEach  items="${results}" var="value">
			<tr>
				<td>${value.angle}</td>
				<td>${value.sin}</td>
				<td>${value.cos}</td>
			</tr>
		</c:forEach>
	</table>
	
	<p><a href="/webapp2/index.jsp">Return to home page</a></p>
</body>