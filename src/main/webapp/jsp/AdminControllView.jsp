<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin Controll</title>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" ></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

</head>
<body class="d-flex flex-column min-vh-100">
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<% if(!myAccount.getLogedIn() || !myAccount.getAdmin()) response.sendRedirect("./HomepageView.jsp");%>
<% listingBean.setAccount(myAccount); %>

<jsp:getProperty name="myAccount" property="navbarHtml"/>

<main class='flex-fill'>
	<jsp:getProperty name="listingBean" property="adminReportsHtml"/>
</main>

<jsp:getProperty name="myAccount" property="footerHtml" />

</body>
</html>