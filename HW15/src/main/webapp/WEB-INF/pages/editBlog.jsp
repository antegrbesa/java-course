<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Edit blog</title>
		
		<style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		}
		</style>
	</head>

	<body>
		<h1>
		<c:choose>
		<c:when test="${empty eid}">
		New blog
		</c:when>
		<c:otherwise>
		Edit blog
		</c:otherwise>
		</c:choose>
		</h1>

		<form action="update" method="post">
		
		
		<p>
		Title: <input type="text" name="title" value='<c:out value="${form.title}"/>'	 size="30"><br>
		<c:if test="${form.hasError('title')}">
		<div class="greska"><c:out value="${form.getError('title')}"/></div>
		</c:if>
		</p>
	
		<p>
		Text: <textarea name="text"  rows="20"  cols="60"><c:out value="${form.text}"/></textarea>
		</p>
		
		
		<input type="submit" name="method" value="Save">
		<input type="submit" name="method" value="Cancel">
		
		</form>


	</body>
</html>