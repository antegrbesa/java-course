
<%@page import="java.util.concurrent.TimeUnit"%>
<%@page import="java.time.Instant"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
	
	String color = (String) session.getAttribute("color");
	
	long startTime = (long) application.getAttribute("startTime");
	long upTime = Instant.now().getEpochSecond() - startTime;
	int day = (int)TimeUnit.SECONDS.toDays(upTime);        
	long hours = TimeUnit.SECONDS.toHours(upTime) - (day *24);
	long minute = TimeUnit.SECONDS.toMinutes(upTime) - (TimeUnit.SECONDS.toHours(upTime)* 60);
	long second = TimeUnit.SECONDS.toSeconds(upTime) - (TimeUnit.SECONDS.toMinutes(upTime) *60);
	String totalTime =  day + " days " + hours + " hours " + minute +  " minutes " + second +  " seconds "; 
%>

<html>
	<body  bgcolor="<%=pickedColor%>">
		<h1>Application run time</h1>
		<p>Run time is:</p>
		<p><%=totalTime%></p>
		<p><a href="/webapp2/index.jsp">Return to home page</a></p>
	</body>

</html>
