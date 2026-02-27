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
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<jsp:useBean id="myWeiter"
		class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
	<jsp:useBean id="listingBean"
		class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
	<jsp:useBean id="categoryBean"
		class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session"></jsp:useBean>
	<%
	String action = request.getParameter("action");
	String title = request.getParameter("title");
	String descr = request.getParameter("descr");
	String zip = request.getParameter("zip");
	String city = request.getParameter("city");
	String userid = myAccount.getEmail();

	int catid = 0;
	int price = 0;

	if (action == null)
		action = "";

	if (action.equals("saveListing")) {

		catid = Integer.parseInt(request.getParameter("catid"));
		price = Integer.parseInt(request.getParameter("price"));
		return;
	} else if (action.equals("zurHomepage"))
		response.sendRedirect("./HomepageView.jsp");
	else if (action.equals("zumLogin")) {
		response.sendRedirect("./LoginView.jsp");
	} else if (action.equals("zurReg")) {
		response.sendRedirect("./RegView.jsp");
	} else if (action.equals("zumInserieren")) {
		CategoryBean cBean = (CategoryBean) session.getAttribute("categoryBean");
		if (cBean != null) {
			cBean.setSelectedCategoryId(-1);
		}
		listingBean.resetEditMode();
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
		if (myAccount.getLogedIn())
			response.sendRedirect("./NachrichtenView.jsp");
		else {
			myWeiter.setLink("./NachrichtenView.jsp");
			response.sendRedirect("./LoginView.jsp");
		}
	} else if (action.equals("abmelden")) {
		myAccount.abmelden();
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
	} else if (action.equals("deaktiviereListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);
		listingBean.setAccount(myAccount);

		listingBean.deaktiviereListing();

		response.sendRedirect("./MeineInserateView.jsp");

	} else if (action.equals("aktiviereListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);
		listingBean.setAccount(myAccount);

		listingBean.aktiviereListing();

		response.sendRedirect("./MeineInserateView.jsp");
	} else if (action.equals("bearbeiteListing")) {

		String idParam = request.getParameter("id");

		if (idParam != null && myAccount.getLogedIn()) {

			int listingId = Integer.parseInt(idParam);

			listingBean.setAccount(myAccount);

			boolean loaded = listingBean.loadListingForEdit(listingId, myAccount.getEmail());

			if (loaded) {
		// Kategorie an CategoryBean übergeben
		CategoryBean cBean = (CategoryBean) session.getAttribute("categoryBean");

		if (cBean != null) {
			cBean.setSelectedCategoryId(listingBean.getEditCatId());
		}

		response.sendRedirect("./InserierenView.jsp");
		return;
			}
		}

		response.sendRedirect("./HomepageView.jsp");
		return;

	} else
		response.sendRedirect("./HomepageView.jsp");
	%>
</body>
</html>