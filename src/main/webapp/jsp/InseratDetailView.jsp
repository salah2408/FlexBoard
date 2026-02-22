<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Inserat Details - FlexBoard</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">

<link rel="stylesheet" href="../css/HomepageView.css">
</head>
<body class="d-flex flex-column min-vh-100">

	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session"></jsp:useBean>
	<jsp:useBean id="listingBean"
		class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

	<%
	listingBean.setAccount(myAccount);
	%>

	<jsp:getProperty name="myAccount" property="navbarHtml" />

	<main class="flex-fill">

		<jsp:getProperty name="listingBean" property="inseratDetailHtml" />

		<jsp:getProperty name="listingBean" property="kontaktButtonHtml" />
	</main>

	<footer class="custom-footer mt-auto">
		<div class="container py-4 text-center">
			<small>© 2026 FlexBoard · Praktikum Anwendungssysteme</small>
		</div>
	</footer>

	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


</body>
</html>