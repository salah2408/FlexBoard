<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<%
	String vorname = request.getParameter("");
	String nachname = request.getParameter("");
	String email = request.getParameter("");
	String passwort = request.getParameter("");
	String action = request.getParameter("");
	
	if(action == null)
		action = "";
	
	if(action.equals("")){
		
	}
	else{
		response.sendRedirect("./LoginView.jsp");
	}
%>
</body>
</html>