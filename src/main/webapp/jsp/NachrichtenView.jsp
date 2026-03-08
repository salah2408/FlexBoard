<%@ include file="./AuthRequired.jsp" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Posteingang</title>
<link href="../css/Nachrichten.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>	
</head>
<body class="d-flex flex-column min-vh-100">
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
	<jsp:getProperty property="navbarHtml" name="myAccount"/>
	<main class="flex-fill">
		<jsp:getProperty property="nachrichtenHtml" name="myAccount"/>
	</main>
	<jsp:getProperty name="myAccount" property="footerHtml" />
</body>
</html>