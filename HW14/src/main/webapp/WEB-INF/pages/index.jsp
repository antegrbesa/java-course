<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>
		<h1>Available polls:</h1>
			<c:forEach var="entry" items="${polls}">
  				<p><a href="/voting-app/servleti/glasanje?id=${entry.id}">${entry.title}</a></p>
			</c:forEach>		
	</body>

</html>