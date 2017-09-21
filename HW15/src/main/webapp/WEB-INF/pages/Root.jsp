<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
	</head>
	
	<c:choose>
			<c:when test="${empty user_nick}">
				<p>Current user not logged in</p>
			</c:when>
			<c:otherwise>
				<p>Current user: <c:out value="${user_fn}"/> <c:out value="${user_ln}"/> <a href="logout">Logout</a></p>
			</c:otherwise>
		</c:choose>
	
	<body>
		<h1>List of <c:out value="${nick}"/>'s blogs </h1>
		<c:choose>
			<c:when test="${entries.isEmpty()}">
				<p>There are currently no blogs.</p>
			</c:when>
			<c:otherwise>
			<ol>
			<c:forEach var="zapis" items="${entries}">
			<li>
			  <a href="${zapis.creator.nick}/${zapis.id}"><c:out value="${zapis.title}"/></a>
			</li>
			</c:forEach>
			</ol>
			</c:otherwise>
		</c:choose>
		
		<c:choose>
			<c:when test="${user_nick eq nick}">
				<a href="${user_nick}/new">Create new blog</a>
			</c:when>
		</c:choose>
		
		
	</body>
</html>