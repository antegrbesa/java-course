<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<body>
		<h1>${poll.message}</h1>
		<ol>
			<c:forEach var="entry" items="${data}">
  				<li><a href="glasanje-glasaj?id=${entry.id}">${entry.optionTitle}</a></li>
			</c:forEach>
		</ol>
		
		<p><a href="index.html">Return to home page</a></p>
	</body>

</html>