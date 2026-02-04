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
	
	if(action.equals("zurHomepage"))
		response.sendRedirect("./HomepageView.jsp");
	else if(action.equals("zumLogin"))
		response.sendRedirect("./LoginView.jsp");
	else if(action.equals("zurReg"))
		response.sendRedirect("./RegView.jsp");
	else if(action.equals("zumInserieren"))
		response.sendRedirect("./InserierenView.jsp");
	else if(action.equals("zurSuche"))
		response.sendRedirect("./SucheView.jsp");
	else
		response.sendRedirect("./HomepageView.jsp");

%>
</body>
</html>