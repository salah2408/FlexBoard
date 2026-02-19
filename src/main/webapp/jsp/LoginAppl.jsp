<%@page import="de.hwg_lu.bwi520.messages.RegMessage"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<jsp:useBean id="myWeiter"
		class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<jsp:useBean id="myRegMsg" class="de.hwg_lu.bwi520.messages.RegMessage"
		scope="session" />
	<%
	String email = request.getParameter("email");
	String passwort = request.getParameter("passwort");
	String action = request.getParameter("action");

	if (action == null)
		action = "";

	if (action.equals("Einloggen")) {
		if (myAccount.login(email, passwort)) {
			myAccount.readAlleNachrichtenFromDB();
			myAccount.setLoginSuccess(true);
			response.sendRedirect(myWeiter.getLink());
		} else {
			response.sendRedirect("./LoginView.jsp");
		}
	} else if (action.equals("zurReg")) {
		myRegMsg.setRegMessage();
		response.sendRedirect("./RegView.jsp");
	} else {
		response.sendRedirect("./HomepageView.jsp");
	}
	%>
</body>
</html>