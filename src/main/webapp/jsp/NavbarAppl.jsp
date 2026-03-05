<%@page import="de.hwg_lu.bwi520.beans.CategoryBean"%>
<%@page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
<jsp:useBean id="categoryBean" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session"/>
	<%
	String action = request.getParameter("action");
	
	String currSite = request.getParameter("currSite");
	
	if(currSite != null){
		myWeiter.setLink(currSite);
	}

	if (action == null)
		action = "";

	
	if (action.equals("zurHomepage")){
		response.sendRedirect("./HomepageView.jsp");
	} else if (action.equals("zumLogin")) {
		response.sendRedirect("./LoginView.jsp");
	} else if (action.equals("zurReg")) {
		response.sendRedirect("./RegView.jsp");
	} else if (action.equals("zumInserieren")) {
		if (myAccount.getLogedIn())
			response.sendRedirect("./InserierenView.jsp");
		else {
			myWeiter.setLink("./InserierenView.jsp");
			response.sendRedirect("./LoginView.jsp");
		}
	} else if (action.equals("zurSuche")) {
		String q = request.getParameter("q");

		if (q == null) {
			q = "";
		}

		response.sendRedirect("./SucheView.jsp?q=" + java.net.URLEncoder.encode(q, "UTF-8"));
	} else if (action.equals("zurPost")) {
		if (myAccount.getLogedIn()){
			myAccount.readAlleNachrichtenFromDB();
			response.sendRedirect("./NachrichtenView.jsp");
		}
		else {
			myWeiter.setLink("./NachrichtenView.jsp");
			myAccount.readAlleNachrichtenFromDB();
			response.sendRedirect("./LoginView.jsp");
		}
	} else if (action.equals("abmelden")) {
		myAccount.abmelden();
		myWeiter.setLink("./HomepageView.jsp");
		response.sendRedirect("./HomepageView.jsp");

	} else if (action.equals("zurMeineInserate")) {
		if (myAccount.getLogedIn())
			response.sendRedirect("./MeineInserateView.jsp");
		else {
			myWeiter.setLink("./MeineInserateView.jsp");
			response.sendRedirect("./LoginView.jsp");
		}
	} else if (action.equals("zumListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);

		response.sendRedirect("./InseratDetailView.jsp");
			}
	else
		response.sendRedirect("./HomepageView.jsp");
	%>
</body>
</html>