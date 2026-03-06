<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="de.hwg_lu.bwi520.beans.SearchBean" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="mySearch" class="de.hwg_lu.bwi520.beans.SearchBean" scope="session" />
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<%
mySearch.setAccount(myAccount);

String action = request.getParameter("action");

if(action == null)
	action = "";

if (action.equals("Finden")) {

	String suchbegriff = request.getParameter("q");
	String kategorie = request.getParameter("kategorie");
	String plzOrt = request.getParameter("plzOrt");
	String minPreisStr = request.getParameter("minPreis");
	String maxPreisStr = request.getParameter("maxPreis");

	// Parameter setzen
	mySearch.setSuchbegriff(suchbegriff);
	mySearch.setKategorie(kategorie);
	mySearch.setPlzOrt(plzOrt);

	// Preis-Filter (optional)
	try {
		if (minPreisStr != null && !minPreisStr.isEmpty()) {
	mySearch.setMinPreis(Integer.parseInt(minPreisStr));
		} else {
	mySearch.setMinPreis(null);
		}
	} catch (NumberFormatException e) {
		mySearch.setMinPreis(null);
	}

	try {
		if (maxPreisStr != null && !maxPreisStr.isEmpty()) {
	mySearch.setMaxPreis(Integer.parseInt(maxPreisStr));
		} else {
	mySearch.setMaxPreis(null);
		}
	} catch (NumberFormatException e) {
		mySearch.setMaxPreis(null);
	}

	// Suche ausführen
	mySearch.suche();

	// Zurück zur View
	response.sendRedirect("./SucheView.jsp");
}

// =============================
// ACTION: Filter Kategorie
// =============================
else if (action.equals("filterKategorie")) {

	String kategorie = request.getParameter("kategorie");
	mySearch.setKategorie(kategorie);

	// Suche neu ausführen
	mySearch.suche();

	response.sendRedirect("./SucheView.jsp");
}


// =============================
// ACTION: Filter zurücksetzen
// =============================
else if (action.equals("reset")) {

	mySearch.setSuchbegriff("");
	mySearch.setKategorie("");
	mySearch.setPlzOrt("");
	mySearch.setMinPreis(null);
	mySearch.setMaxPreis(null);

	// Alle Anzeigen laden
	mySearch.suche();

	response.sendRedirect("./SucheView.jsp");
}
%>
</body>
</html>