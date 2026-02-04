<%@page import="de.hwg_lu.bwi520.messages.RegMessage"%>
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
<jsp:useBean id="myRegMsg" class="de.hwg_lu.bwi520.messages.RegMessage" scope="session" />
<%
	String vorname = request.getParameter("vorname");
	String nachname = request.getParameter("nachname");
	String email = request.getParameter("email");
	String passwort = request.getParameter("passwort");
	String action = request.getParameter("action");
	
	if(action == null)
		action = "";
	
	if(action.equals("Registrieren")){
		if(myAccount.saveAccount(email, vorname, nachname, passwort)){
			myRegMsg.setRegSuccess();
			response.sendRedirect("./RegView.jsp");
		}
		else{
			myRegMsg.setAccountExists(email);
			response.sendRedirect("./RegView.jsp");
		}
	}
	else if(action.equals("zumLogin")){
		response.sendRedirect("./LoginView.jsp");
	}
	else{
		response.sendRedirect("./HomepageView.jsp");
	}
%>
</body>
</html>