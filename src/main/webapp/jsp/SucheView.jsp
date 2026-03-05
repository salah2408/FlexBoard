<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="de.hwg_lu.bwi520.beans.CategoryBean" %>
<%@ page import="de.hwg_lu.bwi520.classes.Category" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Suche</title>
<link href="../css/Search.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/Search.js"></script>
<script src="../js/Navigation.js"></script>

</head>

<body>
	<% %>
    <jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
    <jsp:useBean id="mySearch" class="de.hwg_lu.bwi520.beans.SearchBean" scope="session" />
    <jsp:useBean id="myCategory" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="request" />

    <jsp:getProperty property="navbarHtml" name="myAccount" />
	<main class="flex-fill">
    <form action="./SucheAppl.jsp" method="get">
        <jsp:getProperty name="mySearch" property="suchleisteHtml"/>
    


    <div class="container-fluid">
        <div class="row"> 
			<jsp:getProperty name="mySearch" property="sidebarHtml"/>
        	<jsp:getProperty name="mySearch" property="ergebnisseHtml"/>
        </div>
    </div>
    </form>
    </main>
	<jsp:getProperty name="myAccount" property="footerHtml" />
</body>
</html>