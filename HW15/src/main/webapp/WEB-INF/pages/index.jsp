<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Blog</title>
	</head>
	 <i>${message}</i> 
	
	<c:choose>
			<c:when test="${empty user_nick}">
				<p>Current user not logged in</p>
				<p>Existing user? Log in<a href="prepare"> here.</a></p>
				<p>New user? Register <a href="register">here.</a></p>
			</c:when>
			<c:otherwise>
				<p>Current user: <b><c:out value="${user_fn}"/> <c:out value="${user_ln}"/></b> <a href="logout">Logout</a></p>
				<p> Edit your blog <a href="author/${user_nick}">here</a></p>
			</c:otherwise>
		</c:choose>
	
	<body>
		<h1>List of existing authors</h1>
		<c:choose>
			<c:when test="${authors.isEmpty()}">
				<p>There are currently no authors.</p>
			</c:when>
			<c:otherwise>
			<ol>
			<c:forEach var="zapis" items="${authors}">
			<li>
			  <c:out value="${zapis.firstName}"/> 
			  <c:out value="${zapis.lastName}"/>
			  (<c:out value="${zapis.email}"/>)
			  <a href="author/${zapis.nick}">View <c:out value="${zapis.nick}"/>'s blog</a>
			</li>
			</c:forEach>
			</ol>
			</c:otherwise>
		</c:choose>
		
		
		
		
	</body>
</html>