<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">

<head>
<meta charset="UTF-8">
<title>Flexboard - Hauptseite</title>
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<link rel="stylesheet" href="../css/HomepageView.css">
</head>

<body class="d-flex flex-column min-vh-100">
	<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean"
		scope="session" />
	<!-- Navbar -->
	<jsp:getProperty property="navbarHtml" name="myAccount" />

	<% myAccount.setAktuelleSeite("home");%>

	<main class="flex-fill">
		<div class="container">
			<section class="py-5">
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
						<div class="col-lg-6 text-center">
							<div class="bg-light rounded-4 p-5 shadow-sm">
								<span class="text-muted"> Platzhalter für Illustration /
									Screenshot </span>
							</div>
						</div>
					</div>
				</div>
			</section>
		</div>
		
		<jsp:getProperty name="myAccount" property="suchfeldHtml"/>

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
					<!-- Card 1 -->
					<div class="col-md-4">
						<div class="card h-100 shadow-sm border-0">
							<div class="card-body text-center">
								<h5 class="card-title fw-bold">Inserate erstellen</h5>
								<p class="card-text text-muted">Erstelle in wenigen
									Schritten ein Inserat und erreiche andere Nutzer.</p>
								<a href="#" class="btn btn-outline-primary"> Erstellen </a>
							</div>
						</div>
					</div>
					<!-- Card 2 -->
					<div class="col-md-4">
						<div class="card h-100 shadow-sm border-0">
							<div class="card-body text-center">
								<h5 class="card-title fw-bold">Inserate finden</h5>
								<p class="card-text text-muted">Durchsuche Angebote nach
									Kategorien, Ort oder Stichworten.</p>
								<a href="#" class="btn btn-outline-primary"> Suchen </a>
							</div>
						</div>
					</div>
					<!-- Card 3 -->
					<div class="col-md-4">
						<div class="card h-100 shadow-sm border-0">
							<div class="card-body text-center">
								<h5 class="card-title fw-bold">Inserate verwalten</h5>
								<p class="card-text text-muted">Behalte deine eigenen
									Inserate im Blick und bearbeite sie jederzeit.</p>
								<a href="#" class="btn btn-outline-primary"> Verwalten </a>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>

		<%-- Das ist für das Mein Profil, wenn man eingeloggt ist --%>

		<jsp:getProperty name="myAccount" property="profilHtml" />

	</main>

	<footer class="bg-dark text-light mt-auto">
		<div class="container py-4">
			<div class="row">

				<div class="col-md-6">
					<h6 class="fw-bold mb-2">FlexBoard</h6>
					<p class="small text-muted mb-0">Eine einfache Plattform zum
						Erstellen und Finden von Inseraten.</p>
				</div>

				<div class="col-md-6 text-md-end mt-3 mt-md-0">
					<small class="text-muted"> © 2026 FlexBoard · Praktikum
						Anwendungssysteme </small>
				</div>

			</div>
		</div>
	</footer>

</body>
</html>