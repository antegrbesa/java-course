<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
	String pickedColor = (String) session.getAttribute("pickedBgCol");
	if(pickedColor == null) {
		pickedColor = "white";
	}
	
	String color = (String) session.getAttribute("color");
%>

<html>
	<body  bgcolor="<%=pickedColor%>">
		<p>
			<font color="<%=color%>">
			Two ints and a Float walk into a bar. They spot an attractive Double on her own.	
			The first int walks up to her and says: “Hey baby, my VM or yours?”. She slaps him and he walks back rejected.
			</font></p>
		<p>
			<font color="<%=color%>">
			The second int walks over. “Hey cute-stuff, can I cook you ‘Beans’ for breakfast?” After a quick slapping, he too walks back.
			Then the Float ambles over casually: “Where those two primitive types bothering you?”, he remarks.
			</font></p>
		<p>
			<font color="<%=color%>">
			“Yes, I’m so glad you’re here”, she says. “They just had no Class!” 
		</font></p>
	
		<p><a href="/webapp2/index.jsp">Return to home page</a></p>
	</body>

</html>