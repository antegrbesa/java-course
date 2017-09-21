<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
%>

<html>
	<head>
		<style type="text/css">table.rez td {text-align: center;}
		</style>
	</head>
	<body bgcolor="<%=pickedColor%>">
		<h1>Voting results</h1>
		<p>Results are as follows:</p>
		<table border ="1" class="rez">
			<thead><tr><th>Band</th><th>Number of votes</th></tr></thead>
			<tbody>
				<c:forEach var="entry" items="${votes}">
  					<tr><td>${entry.key}</td><td>${entry.value}</td></tr>
				</c:forEach>		
			</tbody>
		</table>
		
		<h2>Graphical view of results</h2>
		 <img src="/webapp2/glasanje-grafika">
		 
		<h2>Results in XLS (excel) format</h2>
		<p>Results in XLS (excel) format are available <a href="glasanje-xls">here</a></p>
		
		<h2>Other</h2>
		<p>Songs from winner band/s</p>
		<ul>
			<c:forEach var="entry" items="${links}">
  					<li><a href="${entry.value}" target="blank">${entry.key}</a></li>
			</c:forEach>
		</ul>
		
		<p><a href="/webapp2/index.jsp">Return to home page</a></p>
	</body>

</html>