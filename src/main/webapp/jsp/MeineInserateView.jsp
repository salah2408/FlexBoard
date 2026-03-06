<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@ include file="./AuthRequired.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>FlexBoard - Meine Inserate</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

</head>
<body class="d-flex flex-column min-vh-100">

<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%
    listingBean.setAccount(myAccount);

    if (!myAccount.getLogedIn()) {
        response.sendRedirect("./LoginView.jsp");
        return;
    }
%>

<jsp:getProperty property="navbarHtml" name="myAccount" />

<main class="flex-fill">
	<jsp:getProperty name="listingBean" property="profilHtml" />

    <jsp:getProperty  property="meineInserateHtml" name="listingBean" />

</main>

<jsp:getProperty name="myAccount" property="footerHtml" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>