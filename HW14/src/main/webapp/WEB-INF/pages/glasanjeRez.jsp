<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
	<head>
		<style type="text/css">table.rez td {text-align: center;}
		</style>
	</head>
	<body>
		<h1>Voting results</h1>
		<p>Results are as follows:</p>
		<table border ="1" class="rez">
			<thead><tr><th>Option</th><th>Number of votes</th></tr></thead>
			<tbody>
				<c:forEach var="entry" items="${data}">
  					<tr><td>${entry.optionTitle}</td><td>${entry.votesCount}</td></tr>
				</c:forEach>		
			</tbody>
		</table>
		
		<h2>Graphical view of results</h2>
		 <img src="glasanje-grafika">
		 
		<h2>Results in XLS (excel) format</h2>
		<p>Results in XLS (excel) format are available <a href="glasanje-xls">here</a></p>
		
		<h2>Other</h2>
		<p>Link/s to best-voted options:</p>
		<ul>
			<c:forEach var="entry" items="${links}">
  					<li><a href="${entry.optionLink}" target="blank">${entry.optionTitle}</a></li>
			</c:forEach>
		</ul>
		
		<p><a href="index.html">Return to home page</a></p>
	</body>

</html>