<%@page import="de.hwg_lu.bwi520.beans.MessageBean"%>
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
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session"/>
<jsp:useBean id="myMessage" class="de.hwg_lu.bwi520.beans.MessageBean" scope="session"/>
	<h2><jsp:getProperty property="msg" name="myMessage"/></h2>
	<h4><jsp:getProperty property="action" name="myMessage"/></h4>
	<p>Heute war ein toter tag kb mehr auf Web</p>
</body>
</html>