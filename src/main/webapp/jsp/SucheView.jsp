<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Flexboard - Suche</title>
<link href="../css/Suche.css" rel="stylesheet">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>	
</head>

<body>
<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />

	<jsp:getProperty property="navbarHtml" name="myAccount" />
	<form action="./SucheAppl.jsp">
		<div class="container-fluid search-bar">
			<div class="container">
				<div class="row g-2 align-items-center">

					<div class="col-md-4">
						<input type="text" class="form-control" placeholder="Was suchst du?">
					</div>

					<div class="col-md-3">
						<select class="form-select" name="kategorie">
							<option>Kategorie wählen</option>
							<option>Elektronik</option>
							<option>Jobs</option>
							<option>Möbel</option>
						</select>
					</div>

					<div class="col-md-3">
						<input type="text" class="form-control" placeholder="PLZ oder Ort">
					</div>

					<div class="col-md-2 d-grid">
						<input class="btn btn-light" type="submit" name="action" value="Finden">
					</div>

				</div>
			</div>
		</div>
	</form>

	
	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3 col-lg-2 sidebar">

				<h5>Filter</h5>

				<h6>Kategorien</h6>
				<div class="form-check filter-item">
					<a class="sidebar-link" href="./SucheAppl.jsp?action=sucheKategorie&kategorie=Elektronik">Elektronik</a>
				</div>
				<div class="form-check filter-item">
					<a class="sidebar-link" href="./SucheAppl.jsp?action=sucheKategorie&kategorie=M&ouml;bel">Möbel</a>
				</div>
				<div class="form-check filter-item">
					<a class="sidebar-link" href="./SucheAppl.jsp?action=sucheKategorie&kategorie=Jobs">Jobs</a>
				</div>

				<h6>Preis</h6>
				<div class="mb-2">
					<input type="number" class="form-control mb-2" placeholder="Min €">
					<input type="number" class="form-control" placeholder="Max €">
				</div>

				<h6>Anbieter</h6>
				<div class="form-check filter-item">
					<a class="sidebar-link" href="./SucheAppl.jsp?action=sucheAnbieter&anbieter=Privat">Privat</a>
				</div>
				<div class="form-check filter-item">
					<a class="sidebar-link" href="./SucheAppl.jsp?action=sucheAnbieter&anbieter=Gewerblich">Gewerblich</a>
				</div>

			</div>
			<div class="col-md-9 col-lg-10 p-4">
				<h4>Suchergebnisse erscheinen hier...</h4>
			</div>
		</div>
	</div>

</body>
</html>