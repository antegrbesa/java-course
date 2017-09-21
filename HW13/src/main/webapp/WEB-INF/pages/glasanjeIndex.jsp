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
		<h1>Vote for your favorite band</h1>
		<p>From given bands, which one do you like the most? Vote for them!</p>
		<ol>
			<c:forEach var="entry" items="${values}">
  				<li><a href="glasanje-glasaj?id=${entry.key}">${entry.value}</a></li>
			</c:forEach>
		</ol>
		
		<p><a href="/webapp2/index.jsp">Return to home page</a></p>
	</body>

</html>