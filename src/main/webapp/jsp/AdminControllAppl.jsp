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
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%
	String action = request.getParameter("action");
	String listingid = request.getParameter("listingid");
	String userid = request.getParameter("userid");
	String reportid = request.getParameter("reportid");
	String adminNotiz = request.getParameter("adminNotiz");

	if(action == null)
		action = "";
	
	if(action.equals("zumListing")){
		listingBean.setAktListingId(Integer.parseInt(listingid));
		System.out.println(listingBean.getAktListingId());
		response.sendRedirect("./InseratDetailView.jsp");
	}
	else if(action.equals("deaktiviereListing")){
		listingBean.setAktListingId(Integer.parseInt(listingid));
		listingBean.deaktiviereListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("aktiviereListing")){
		listingBean.setAktListingId(Integer.parseInt(listingid));
		listingBean.aktiviereListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("loescheListing")){
		listingBean.setAktListingId(Integer.parseInt(listingid));
		listingBean.loescheListing();
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("sperreUser")){
		listingBean.bannUser(userid);
		response.sendRedirect("./AdminControllView.jsp");
	}
	else if(action.equals("reportSchliessen")){
		listingBean.closeReport(Integer.parseInt(reportid), adminNotiz);
		response.sendRedirect("./AdminControllView.jsp");
	}
	else{
		response.sendRedirect("./AdminControllView.jsp");
	}

%>
</body>
</html>