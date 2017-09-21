<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>

  <style type="text/css">
		.greska {
		   font-family: fantasy;
		   font-weight: bold;
		   font-size: 0.9em;
		   color: #FF0000;
		}
		</style>
 
  <body>
  
  <c:choose>
    <c:when test="${blogEntry==null}">
      Nema unosa!
    </c:when>
    <c:otherwise>
      <h1><c:out value="${blogEntry.title}"/></h1>
      <p><c:out value="${blogEntry.text}"/></p>
      <c:if test="${!blogEntry.comments.isEmpty()}">
      <strong>Comments</strong>
      <ul>
      <c:forEach var="e" items="${blogEntry.comments}">
        <li><div style="font-weight: bold">[Korisnik=<c:out value="${e.usersEMail}"/>] <c:out value="${e.postedOn}"/></div><div style="padding-left: 10px;"><c:out value="${e.message}"/></div></li>
      </c:forEach>
      </ul>
      </c:if>
    </c:otherwise>
  </c:choose>
  
  <c:choose>
   <c:when test="${user_nick==nick}">
   	  <a href="edit">Edit blog</a>
   	  </c:when>
   	  </c:choose>
   	  
   	  
   	 <p><b>Add comment</b></p>
   	  
   	  <form action="/blog/servleti/comment" method="post">
		<c:choose>
		 <c:when test="${user_nick==null}">
		
		<p>
		Email <input type="text" name="usersEMail" value='<c:out value="${form.usersEMail}"/>' size="30"><br>
		<c:if test="${form.hasError('usersEMail')}">
		<div class="greska"><c:out value="${form.getError('usersEMail')}"/></div>
		</c:if>
		</p>

		</c:when>
		</c:choose>

		<p>
		Message: <textarea name="message"  rows="10"  cols="20"><c:out value="${form.message}"/></textarea>
		<c:if test="${form.hasError('message')}">
		<div class="greska"><c:out value="${form.getError('message')}"/></div>
		</c:if>
		</p>
		
	
		
		<input type="submit" name="method" value="Save">
		<input type="submit" name="method" value="Cancel">
		
		</form>

  </body>
</html>