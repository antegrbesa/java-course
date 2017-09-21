<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
	<head>
		<title>Login</title>
		
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
		Login form
		</h1>

		<form action="login" method="post">
			

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
		
		
		<input type="submit" name="method" value="Login">
		<input type="submit" name="method" value="Cancel">
		
		</form>


	</body>
</html>