<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@page import="de.hwg_lu.bwi520.messages.RegMessage"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="de">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1" />
<title>FlexBoard - Registrieren</title>

<link rel="stylesheet" href="../css/TermsModal.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="../js/TermsContent.js" defer></script>
<script src="../js/TermsModal.js" defer></script>
</head>

<body class="d-flex flex-column min-vh-100">
    <jsp:useBean id="myRegMsg" class="de.hwg_lu.bwi520.messages.RegMessage" scope="session" />
    <jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />

    <jsp:getProperty property="navbarHtml" name="myAccount" />

    <main class="container mt-5 flex-fill">
        <div class="row justify-content-center">
            <div class="col-md-6">
                <div class="card shadow-sm p-4">
                    <h2 class="mb-4"><jsp:getProperty name="myRegMsg" property="msg" /></h2>
                    <h4 class="mb-4"><jsp:getProperty name="myRegMsg" property="action" /></h4>

                    <form action="./RegAppl.jsp" method="post">
                        <div class="mb-3">
                            <label for="regFirstName" class="form-label">Vorname</label>
                            <input type="text" name="vorname" class="form-control" id="regFirstName" required>
                        </div>

                        <div class="mb-3">
                            <label for="regLastName" class="form-label">Nachname</label>
                            <input type="text" name="nachname" class="form-control" id="regLastName" required>
                        </div>

                        <div class="mb-3">
                            <label for="regEmail" class="form-label">Email address</label>
                            <input type="email" name="email" class="form-control" id="regEmail" required>
                        </div>

                        <div class="mb-3">
                            <label for="regPassword" class="form-label">Password</label>
                            <input type="password" name="passwort" class="form-control" id="regPassword" required>
                        </div>

                        <div class="form-check mb-3">
                            <input class="form-check-input" type="checkbox" id="agbAccepted" name="agbAccepted" value="true" required>
                            <label class="form-check-label" for="agbAccepted">
                                Ich akzeptiere die
                                <a href="#" id="openTermsLink">Nutzungsbedingungen</a>
                            </label>
                            <div class="form-text text-muted" id="termsHint">
                                Bitte erst lesen und bis zum Ende scrollen.
                            </div>
                        </div>

                        <div class="d-grid">
                            <input class="btn btn-primary" type="submit" name="action" value="Registrieren" />
                        </div>

                        <br>
                        <p class="mt-3">
                            Schon registriert? Melden Sie sich <a href="./RegAppl.jsp?action=zumLogin">hier</a> an
                        </p>
                    </form>
                </div>
            </div>
        </div>
    </main>

    <jsp:getProperty name="myAccount" property="footerHtml" />

    <!-- Nur EIN Modal -->
    <div class="modal fade" id="termsModal" tabindex="-1" aria-labelledby="termsModalLabel" aria-hidden="true"
         data-bs-backdrop="static" data-bs-keyboard="false">
        <div class="modal-dialog modal-lg modal-dialog-scrollable">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="termsModalLabel">Nutzungsbedingungen - FlexBoard</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Schliessen"></button>
                </div>

                <div class="modal-body terms-scroll-box" id="termsScrollBox">
                    <div id="termsContent"></div>
                </div>

                <div class="modal-footer d-flex justify-content-between">
                    <small id="termsProgress" class="text-muted">Noch nicht bis zum Ende gescrollt.</small>
                    <button type="button" class="btn btn-primary" id="termsAcceptBtn" disabled>
                        Gelesen und akzeptieren
                    </button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>