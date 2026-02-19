<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html lang="de">
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>FlexBoard - Login</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />

	<jsp:getProperty property="navbarHtml" name="myAccount" />

	<main class="container mt-5">
		<div class="row justify-content-center">
			<div class="col-md-6">
				<h1>Anmelden</h1>
				<form action="./LoginAppl.jsp" method="post">
					<div class="mb-3">
						
						<label for="exampleInputEmail1" class="form-label">Email</label> <input
							type="email" name="email" class="form-control"
							id="exampleInputEmail1" required>
					</div>
					<div class="mb-3">
						<label for="exampleInputPassword1" class="form-label">Password</label>
						<input type="password" name="passwort" class="form-control"
							id="exampleInputPassword1" required>
					</div>
					<input class="btn btn-primary" type="submit" name="action"
						value="Einloggen" />
				</form>
				<br>
				<p class="mt-3">
					Noch kein Konto? <a href="./LoginAppl.jsp?action=zurReg">Registrieren</a>
				</p>
			</div>
		</div>
	</main>

</body>
</html>