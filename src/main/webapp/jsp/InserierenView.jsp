<%@page import="de.hwg_lu.bwi520.beans.CategoryBean"%>
<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Inserieren</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link type="text/css" rel="stylesheet" href="../css/Inserieren.css" />
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/Inserieren.js"></script>
</head>

<body>
	<jsp:useBean id="myWeiter"
		class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<jsp:useBean id="listingBean"
		class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
	<jsp:useBean id="categoryBean"
		class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session" />
		<%
categoryBean.setListingBean(listingBean);
%>

	<jsp:getProperty property="navbarHtml" name="myAccount" />

	<div class="form-container">
		<h2 class="mb-4">Anzeige inserieren</h2>

		<form action="./InserierenAppl.jsp" method="get">
			<%
			if (listingBean.isEditMode()) {
			%>
			<input type="hidden" name="listingid"
				value="<%=listingBean.getAktListingId()%>">
			<%
			}
			%>

			<div class="row-gap">
				Titel <input type="text" class="form-control" name="title"
					placeholder="Was bietest du an?"
					value="<%=listingBean.getEditTitle()%>" required>
			</div>

			<div class='row row-gap' style='margin-top: 20px;'>
				<div class='col-md-12'>
					Bilder hochladen <input type='file' class='form-control'
						name='listingImage' id='bildUpload' accept='image/*' multiple>
				</div>

				<div id='vorschauContainer' class='d-flex flex-wrap gap-2 mt-3'></div>
			</div>

			<div class="row-gap">
				Beschreibung
				<textarea class="form-control" name="descr" style="height: 150px;"
					placeholder="Details zu deinem Inserat..." required><%=listingBean.getEditDescr()%></textarea>
			</div>

			<div class="row-gap">
				<div class="custom-select-wrapper">
					<jsp:getProperty name="categoryBean"
						property="categoriesDropdownHtml" />
				</div>
			</div>

			<jsp:getProperty name="categoryBean" property="lernmaterialHtml" />
			<jsp:getProperty name="categoryBean" property="nachhilfeHtml" />
			<jsp:getProperty name="categoryBean" property="wohnenHtml" />
			<jsp:getProperty name="categoryBean" property="jobsHtml" />
			<jsp:getProperty name="categoryBean" property="technikHtml" />
			<jsp:getProperty name="categoryBean" property="tauschenHtml" />
			<jsp:getProperty name="categoryBean" property="dienstleistungenHtml" />
			<jsp:getProperty name="categoryBean" property="eventsHtml" />

			<div id="testID" class="row row-gap">
				<div class="col-md-8">
					Ort <input type="text" class="form-control" name="city"
						placeholder="Musterstadt" value="<%=listingBean.getEditCity()%>">
				</div>
				<div class="col-md-4">
					PLZ <input type="text" class="form-control" name="zip"
						placeholder="12345" value="<%=listingBean.getEditZip()%>">
				</div>
			</div>

			<div class="mt-4">
				<input class="btn btn-primary btn-lg w-100" type="submit"
					name="action"
					value="<%=listingBean.isEditMode() ? "Anzeige aktualisieren" : "Anzeige erstellen"%>">
			</div>
		</form>
	</div>

</body>
</html>
