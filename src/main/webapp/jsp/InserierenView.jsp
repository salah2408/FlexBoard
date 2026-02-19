<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Inserieren</title>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="../css/Inserieren.css" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/Inserieren.js"></script>
</head>
<body>
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
	<jsp:useBean id="listingBean" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />
	<jsp:useBean id="categoryBean" class="de.hwg_lu.bwi520.beans.CategoryBean" scope="session" />
		
    <jsp:getProperty property="navbarHtml" name="myAccount" />

	<div class="form-container">
		<h2 class="mb-4">Anzeige inserieren</h2>

		<form action="./NavbarAppl.jsp" method="post">
			<input type="hidden" name="action" value="saveListing" />

			<div class="row-gap">
				<label>Titel</label> <input type="text" class="form-control"
					name="title" placeholder="Was bietest du an?" required>
			</div>

			<div class="row-gap">
				<label>Beschreibung</label>
				<textarea class="form-control" name="descr" style="height: 150px;"
					placeholder="Details zu deinem Inserat..." required></textarea>
			</div>

			<div class="row-gap">
				<div class="custom-select-wrapper">
					<jsp:getProperty name="categoryBean"
						property="categoriesDropdownHtml" />
				</div>
			</div>

			<div id="categoryInput">
			
			</div>

			<div class="row row-gap">
				<div class="col-md-8">
					<label>Ort</label> <input type="text" class="form-control"
						name="city" placeholder="Musterstadt">
				</div>
				<div class="col-md-4">
					<label>PLZ</label> <input type="text" class="form-control"
						name="zip" placeholder="12345">
				</div>
			</div>

			<div class="row row-gap">
				<div class="col-md-4">
					<label>Preis (€)</label> <input type="number" class="form-control"
						name="price" value="0" min="0" required>
				</div>
			</div>

			<div class="mt-4">
				<input class="btn btn-primary btn-lg w-100" type="submit" name="action" value="Anzeige erstellen">
			</div>
		</form>
	</div>

</body>
</html>
