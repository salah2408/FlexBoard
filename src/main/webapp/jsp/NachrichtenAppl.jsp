<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	String action = request.getParameter("action");
	
	if(action == null)
		action = "";
	
	if(action.equals("Senden")){
		response.sendRedirect("./NachrichtenView.jsp");
	}
	else{
		response.sendRedirect("./HomepageView.jsp");
	}
%>
</body>
</html>