<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>User</title>
		
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
		<c:when test="${form.id.isEmpty()}">
		New user
		</c:when>
		<c:otherwise>
		Edit user
		</c:otherwise>
		</c:choose>
		</h1>

		<form action="save" method="post">
		
		
		<p>
		First name <input type="text" name="firstName" value='<c:out value="${form.firstName}"/>' size="30"><br>
		<c:if test="${form.hasError('firstName')}">
		<div class="greska"><c:out value="${form.getError('firstName')}"/></div>
		</c:if>
		</p>

		<p>
		Last name <input type="text" name="lastName" value='<c:out value="${form.lastName}"/>' size="50"><br>
		<c:if test="${form.hasError('lastName')}">
		<div class="greska"><c:out value="${form.getError('lastName')}"/></div>
		</c:if>
		</p>
		
		<p>
		Nick<input type="text" name="nick" value='<c:out value="${form.nick}"/>' size="20"><br>
		<c:if test="${form.hasError('nick')}">
		<div class="greska"><c:out value="${form.getError('nick')}"/></div>
		</c:if>
		</p>
		
		<p>
		Password<input type="password" name="password" value='' size="30"><br>
		<c:if test="${form.hasError('password')}">
		<div class="greska"><c:out value="${form.getError('password')}"/></div>
		</c:if>
		</p>
		
		<p>
		EMail <input type="text" name="email" value='<c:out value="${form.email}"/>' size="100"><br>
		<c:if test="${form.hasError('email')}">
		<div class="greska"><c:out value="${form.getError('email')}"/></div>
		</c:if>
		</p>
		
		<input type="submit" name="method" value="Save">
		<input type="submit" name="method" value="Cancel">
		
		</form>


	</body>
</html>