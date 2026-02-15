<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>FlexBoard - Hauptseite</title>

<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
	rel="stylesheet">

<link
	href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap"
	rel="stylesheet">

<link rel="stylesheet" href="../css/HomepageView.css">
</head>

<body class="d-flex flex-column min-vh-100">
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<jsp:useBean id="home" class="de.hwg_lu.bwi520.beans.HomeBean"
		scope="request" />
	<!-- Navbar -->
	<jsp:getProperty property="navbarHtml" name="myAccount" />

	<%
	myAccount.setAktuelleSeite("home");
	%>

	<main class="flex-fill">

		<section class="hero-section">
			<div class="container">
				<div class="row align-items-center">
					<!-- LEFT: TEXT -->
					<div class="col-lg-6 mb-4 mb-lg-0">
						<h1 class="display-5 fw-bold mb-3">
							Inserate einfach <br> <span class="text-primary">erstellen
								und finden</span>
						</h1>
						<p class="lead text-muted mb-4">FlexBoard ist deine zentrale
							Plattform, um Angebote zu erstellen, Inserate zu entdecken und
							Kontakte schnell herzustellen.</p>

						<div class="d-flex gap-3">
							<a href="#" class="btn btn-primary btn-lg px-4"> Inserat
								erstellen </a> <a href="#"
								class="btn btn-outline-secondary btn-lg px-4"> Inserate
								ansehen </a>
						</div>
					</div>

					<!-- RIGHT: VISUAL PLACEHOLDER -->
					<div class="col-lg-6">
						<div class="hero-mockup shadow-lg rounded-4 p-4 bg-white">
							<div class="card border-0 shadow-sm mb-3">
								<div class="card-body">
									<h6 class="fw-bold mb-1">MacBook Air M1</h6>
									<p class="text-muted small mb-1">Ludwigshafen</p>
									<span class="fw-bold text-primary">650 €</span>
								</div>
							</div>

							<div class="card border-0 shadow-sm">
								<div class="card-body">
									<h6 class="fw-bold mb-1">IKEA Schreibtisch</h6>
									<p class="text-muted small mb-1">Mannheim</p>
									<span class="fw-bold text-primary">40 €</span>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="row justify-content-center mt-5">
					<div class="col-lg-8">
						<jsp:getProperty name="home" property="searchCardHtml" />
					</div>
				</div>
			</div>
		</section>




		<section class="py-5 bg-light">
			<div class="container">
				<!-- Section Headline -->
				<div class="text-center mb-5">
					<h2 class="fw-bold">Was kannst du mit FlexBoard tun?</h2>
					<p class="text-muted">Alles, was du brauchst, um schnell und
						unkompliziert Inserate zu nutzen.</p>
				</div>

				<!-- Cards -->
				<div class="row g-4">
					<jsp:getProperty name="home" property="featureCardsHtml" />
				</div>
			</div>
		</section>

		<%-- Das ist für das Mein Profil, wenn man eingeloggt ist --%>

		<jsp:getProperty name="myAccount" property="profilHtml" />

	</main>

	<footer class="custom-footer mt-auto">
		<div class="container py-4">
			<div class="row">

				<div class="col-md-6">
					<h6 class="fw-bold mb-2">FlexBoard</h6>
					<p class="small text-muted mb-0">Eine einfache Plattform zum
						Erstellen und Finden von Inseraten. ✓ Schnell erstellt &nbsp; ✓
						Kostenlos &nbsp; ✓ Lokal vernetzt</p>
				</div>

				<div class="col-md-6 text-md-end mt-3 mt-md-0">
					<small> © 2026 FlexBoard · Praktikum Anwendungssysteme </small>
				</div>

			</div>
		</div>
	</footer>
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>