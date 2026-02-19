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
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
	<jsp:useBean id="accountBean"
		class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
	<jsp:useBean id="listingBean"
		class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
	<jsp:useBean id="categoryBean" 
             class="de.hwg_lu.bwi520.beans.CategoryBean" 
             scope="session" />
		

	<%=accountBean.getNavbarHtml()%>

	<div style="max-width: 700px; margin: 20px auto;">
		<h2>Anzeige inserieren</h2>

		<form action="./NavbarAppl.jsp" method="post">
			<input type="hidden" name="action" value="saveListing" />

			<div class="mb-3">
				<label class="form-label">Titel</label> <input type="text"
					class="form-control" name="title" required>
			</div>

			<div style="margin-top: 10px;">
				<label>Beschreibung</label><br>
				<textarea name="descr" style="width: 100%; height: 120px;" required></textarea>
			</div>

			<div style="margin-top: 10px;">
				<jsp:getProperty name="categoryBean" property="categoriesDropdownHtml" />

			</div>

			<div style="margin-top: 10px;">
				<label>Preis</label><br> <input type="number" name="price"
					style="width: 100%;" value="0" min="0" required>
			</div>

			<div style="margin-top: 10px;">
				<label>PLZ</label><br> <input type="text" name="zip"
					style="width: 100%;">
			</div>

			<div style="margin-top: 10px;">
				<label>Ort</label><br> <input type="text" name="city"
					style="width: 100%;">
			</div>

			<div style="margin-top: 15px;">
				<button type="submit" class="btn btn-success">Inserieren</button>
			</div>
		</form>
	</div>

</body>
</html>
