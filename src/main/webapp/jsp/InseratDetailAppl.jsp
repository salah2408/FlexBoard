<%@ page language="java" contentType="text/html; charset=ISO-8859-1"  pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="myListing" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%	
	myListing.setAccount(myAccount);
	
	String action = request.getParameter("action");
	String listingid   = request.getParameter("listingid");
	String empfaengerid = request.getParameter("empfaengerid");
	String nachrichtText   = request.getParameter("nachrichtText");
	String reportText = request.getParameter("reportText");
	
	String link = request.getParameter("link");
	
	if(link != null)
		myWeiter.setLink(link);
	
	if(action == null)
		action = "";
	
	if(action.equals("Senden")){
		myAccount.setAktAnzeigeID(Integer.parseInt(listingid));
		myAccount.setAktChatPartner(empfaengerid);
		myAccount.sendMessage(nachrichtText);
		myAccount.readAlleNachrichtenFromDB();
		response.sendRedirect("./NachrichtenView.jsp");
	}
	else if(action.equals("anmelden")){
		response.sendRedirect("./LoginView.jsp");
	}
	else if(action.equals("Melden")){
		int listingidNum = listingid != null ? Integer.parseInt(listingid) : 0;
		if(listingidNum > 0){
		myListing.reportListing(listingidNum, reportText);
		response.sendRedirect("./InseratDetailView.jsp");
		}
		
	}
	else
		response.sendRedirect("./HomepageView.jsp");
	
%>
</body>
</html>