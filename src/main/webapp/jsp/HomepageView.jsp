<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">

<head>

<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>FlexBoard - Hauptseite</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">

<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet">

<link rel="stylesheet" href="../css/HomepageView.css">
</head>

<body class="d-flex flex-column min-vh-100">
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<jsp:useBean id="home" class="de.hwg_lu.bwi520.beans.HomeBean"
		scope="request" />
	<%
home.setAccount(myAccount);
%>

	<jsp:getProperty property="navbarHtml" name="myAccount" />

	<main class="flex-fill">

		<jsp:getProperty name="home" property="heroHtml" />

		<%-- FeatureSection mit den Karten --%>
		<jsp:getProperty name="home" property="featureSectionHtml" />

		<%-- Das ist für das Mein Profil, wenn man eingeloggt ist --%>

		<jsp:getProperty name="home" property="homepageListingsHtml" />

		<jsp:getProperty name="myAccount" property="profilHtml" />

	</main>
	<jsp:getProperty name="myAccount" property="footerHtml" />
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="../js/ui.js"></script>
	<%
	if (myAccount.getLoginSuccess()) {
	%>
	<script>
		showToast("Erfolgreich eingeloggt.");
	</script>
	<%
	myAccount.setLoginSuccess(false);
	}
	%>

</body>
</html>