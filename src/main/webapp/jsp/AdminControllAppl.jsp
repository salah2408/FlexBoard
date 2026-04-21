<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title></title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myListing" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%
	String action = request.getParameter("action");
	String listingid = request.getParameter("listingid");
	String userid = request.getParameter("userid");
	String reportid = request.getParameter("reportid");
	String adminNotiz = request.getParameter("adminNotiz");

	if(action == null)
		action = "";
	
	if(action.equals("zumListing")){
		myListing.setAktListingId(Integer.parseInt(listingid));
		System.out.println(myListing.getAktListingId());
		response.sendRedirect("./InseratDetailView.jsp");
	}
	else if(action.equals("deaktiviereListing")){
		myListing.setAktListingId(Integer.parseInt(listingid));
		myListing.deaktiviereListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("aktiviereListing")){
		myListing.setAktListingId(Integer.parseInt(listingid));
		myListing.aktiviereListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("loescheListing")){
		myListing.setAktListingId(Integer.parseInt(listingid));
		myListing.loescheListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("sperreUser")){
		myListing.bannUser(userid);
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("reportSchliessen")){
		myListing.closeReport(Integer.parseInt(reportid), adminNotiz);
		response.sendRedirect("./AdminControllView.jsp");
	}
	else{
		response.sendRedirect("./AdminControllView.jsp");
	}

%>
</body>
</html>