<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<%
	String action = request.getParameter("action");
	String user   = request.getParameter("user"); 
	String text   = request.getParameter("text");
	
	if(action == null)
		action = "";
	
	if(action.equals("Senden")){
		myAccount.sendMessage(text);
		myAccount.readAlleNachrichtenFromDB();
		response.sendRedirect("./NachrichtenView.jsp");
	}
	else if(action.equals("switch")){
		myAccount.setAktChatPartner(user);
		response.sendRedirect("./NachrichtenView.jsp");
	}
	else{
		response.sendRedirect("./HomepageView.jsp");
	}
%>
</body>
</html>