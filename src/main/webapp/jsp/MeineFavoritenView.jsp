<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Meine Favoriten</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" rel="stylesheet">

<link rel="stylesheet" href="../css/base.css">
<link rel="stylesheet" href="../css/HomepageView.css">

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/ui.js"></script>
<script src="../js/Navigation.js"></script>
<script src="../js/favorite.js"></script>

</head>
<body class="d-flex flex-column min-vh-100">

<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session"/>
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session"/>

<%
listingBean.setAccount(myAccount);
%>

<!-- Navbar -->
<jsp:getProperty property="navbarHtml" name="myAccount"/>
<main class="flex-grow-1 pt-4">

<!-- Favoriten Liste -->
<jsp:getProperty name="listingBean" property="meineInserateNavigationHtml"/>
<jsp:getProperty property="meineFavoritenHtml" name="listingBean"/>
</main>

<!-- Footer -->

<jsp:getProperty property="footerHtml" name="myAccount"/>
</body>
</html>