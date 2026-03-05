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
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
<jsp:useBean id="categoryBean" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session"/>

	<%
		String action = request.getParameter("action");
		
		if(action == null)
			action = "";
			
		if (action.equals("zumListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);

		response.sendRedirect("./InseratDetailView.jsp");
			} else if (action.equals("deaktiviereListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);
		listingBean.setAccount(myAccount);

		listingBean.deaktiviereListing();

		response.sendRedirect("./MeineInserateView.jsp");
		return;
			} else if (action.equals("loescheListing")) {

		int listingid = Integer.parseInt(request.getParameter("id"));

		listingBean.setAktListingId(listingid);
		listingBean.setAccount(myAccount);

		listingBean.loescheListing();

		response.sendRedirect("./MeineInserateView.jsp");
		return;

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

				if (categoryBean != null) {
					categoryBean.setSelectedCategoryId(listingBean.getEditCatId());
				}

				response.sendRedirect("./InserierenView.jsp");
				return;
			}
		}

		response.sendRedirect("./HomepageView.jsp");
		return;

			}
	%>
</body>
</html>