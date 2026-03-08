<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
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
<link rel="stylesheet" href="../css/HomepageView.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/Search.js"></script>
<script src="../js/Navigation.js"></script>
<script src="../js/favorite.js"></script>

</head>

<body>
	<% %>
    <jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
    <jsp:useBean id="mySearch" class="de.hwg_lu.bwi520.beans.SearchBean" scope="session" />
    <jsp:useBean id="myCategory" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="request" />
    <jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session"></jsp:useBean>
    
    <%
mySearch.setAccount(myAccount);
mySearch.setListingBean(listingBean);
%>

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
	<div class="toast-container position-fixed bottom-0 end-0 p-3">

    <div id="favoriteToast" class="toast align-items-center text-bg-dark border-0" role="alert">

        <div class="d-flex">

            <div class="toast-body" id="favoriteToastText">
                Favorit gespeichert
            </div>

            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>

        </div>

    </div>

</div>
</body>
</html>