<%@page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean"%>
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
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<%
	String action = request.getParameter("action");
	if(action == null)
		action = "";
	
	if(action.equals("zurHomepage"))
		response.sendRedirect("./HomepageView.jsp");
	else if(action.equals("zumLogin")){
		response.sendRedirect("./LoginView.jsp");
	}
	else if(action.equals("zurReg")){
		response.sendRedirect("./RegView.jsp");
	}
	else if(action.equals("zumInserieren")){
		if(myAccount.getLogedIn())
			response.sendRedirect("./InserierenView.jsp");
		else{
			myWeiter.setLink("./InserierenView.jsp");
			response.sendRedirect("./LoginView.jsp");
		}
	}
	else if(action.equals("zurSuche")){
		response.sendRedirect("./SucheView.jsp");
	}
	else if(action.equals("zurPost")){
		if(myAccount.getLogedIn())
			response.sendRedirect("./NachrichtenView.jsp");
		else{
			myWeiter.setLink("./NachrichtenView.jsp");
			response.sendRedirect("./LoginView.jsp");
		}
	}
	else
		response.sendRedirect("./HomepageView.jsp");

%>
</body>
</html>