<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
<head>
<meta charset="UTF-8">
<title>Flexboard - Hauptseite</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>	
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />

	<jsp:getProperty property="navbarHtml" name="myAccount"/>
	
	

	Email: <jsp:getProperty name="myAccount" property="email" /> <br>
	vorname: <jsp:getProperty name="myAccount" property="vorname" /> <br>
	nachname: <jsp:getProperty name="myAccount" property="nachname" /> <br>
	active: <jsp:getProperty name="myAccount" property="active" /> <br>
	admin: <jsp:getProperty name="myAccount" property="admin" /> <br>
	LogedIn: <jsp:getProperty name="myAccount" property="logedIn" /> <br>
</body>
</html>