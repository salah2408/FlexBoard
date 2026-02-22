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
<style>
    /* Der Hintergrund des Modals (abgedunkelt) */
    .modal-overlay {
        display: none; /* Standardmäßig versteckt */
        position: fixed; 
        z-index: 9999; /* Ganz weit vorne */
        padding-top: 50px; 
        left: 0;
        top: 0;
        width: 100%; 
        height: 100%; 
        overflow: auto; 
        background-color: rgba(0,0,0,0.85); /* Schwarzer Hintergrund mit Transparenz */
        justify-content: center;
        align-items: center;
    }

    /* Das große Bild im Modal */
    .modal-content-img {
        margin: auto;
        display: block;
        max-width: 90%;
        max-height: 85vh; /* Maximal 85% der Bildschirmhöhe */
        border-radius: 8px;
        box-shadow: 0 4px 8px rgba(255,255,255,0.1);
        animation: zoom 0.3s;
    }

    /* Schließen-Button (das X oben rechts) */
    .close-modal {
        position: absolute;
        top: 25px;
        right: 45px;
        color: #f1f1f1;
        font-size: 40px;
        font-weight: bold;
        transition: 0.3s;
        cursor: pointer;
    }

    .close-modal:hover,
    .close-modal:focus {
        color: #bbb;
        text-decoration: none;
        cursor: pointer;
    }

    /* Kleine Animation beim Öffnen */
    @keyframes zoom {
        from {transform:scale(0.7); opacity:0} 
        to {transform:scale(1); opacity:1}
    }
    
    /* Cursor für die Thumbnails anpassen */
    #vorschauContainer img {
        cursor: zoom-in;
        transition: transform 0.2s;
    }
    #vorschauContainer img:hover {
        transform: scale(1.05); /* Leichter Zoom beim Drüberfahren */
    }
</style>
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

			<div class='row row-gap' style='margin-top: 20px;'>
				<div class='col-md-12'>
					<label>Bilder hochladen</label> <input type='file'
						class='form-control' name='listingImage' id='bildUpload'
						accept='image/*' multiple>
				</div>

				<div id='vorschauContainer' class='d-flex flex-wrap gap-2 mt-3'></div>
			</div>

			<div class="row-gap">
				<label>Beschreibung</label>
				<textarea class="form-control" name="descr" style="height: 150px;"
					placeholder="Details zu deinem Inserat..." required></textarea>
			</div>

			<div class="row-gap">
				<div class="custom-select-wrapper">
					<jsp:getProperty name="categoryBean" property="categoriesDropdownHtml" />
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

			<div id="testID" class="row row-gap"  hidden="">
				<div class="col-md-8">
					<label>Ort</label> <input type="text" class="form-control"
						name="city" placeholder="Musterstadt">
				</div>
				<div class="col-md-4">
					<label>PLZ</label> <input type="text" class="form-control"
						name="zip" placeholder="12345">
				</div>
			</div>

			<div class="mt-4">
				<input class="btn btn-primary btn-lg w-100" type="submit" name="action" value="Anzeige erstellen">
			</div>
		</form>
	</div>

</body>
</html>
