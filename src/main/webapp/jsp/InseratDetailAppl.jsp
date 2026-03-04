<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<%
	String action = request.getParameter("action");
	String listingid   = request.getParameter("listingid");
	String empfaengerid = request.getParameter("empfaengerid");
	String nachrichtText   = request.getParameter("nachrichtText");
	
	if(action == null)
		action = "";
	
	if(action.equals("Senden")){
		myAccount.setAktAnzeigeID(Integer.parseInt(listingid));
		myAccount.setAktChatPartner(Integer.parseInt(listingid));
		myAccount.sendMessage(nachrichtText);
		myAccount.readAlleNachrichtenFromDB();
		response.sendRedirect("./NachrichtenView.jsp");
	}
%>
</body>
</html>