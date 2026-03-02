package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Listing;

public class ListingBean {

	Connection dbConn;

	int aktListingId;
	AccountBean account;
	String anbieterEmail;
	ArrayList<Listing> anzeigen;
	
	String editTitle;
  	String editDescr;
  	String editCity;
  	String editZip;
  	int editCatId;
  	JSONObject editDetails;
	boolean editMode = false;


	public ListingBean() throws ClassNotFoundException, SQLException {
		this.dbConn = new PostgreSQLAccess().getConnection();
		this.anzeigen = new ArrayList<Listing>();
		this.readAlleAnzeigenFromDB();
	}
	
	// Anzeigen auslesen
	public void readAlleAnzeigenFromDB() {
		String sql = "SELECT listingid, userid, catid, title, descr, city, zip, status, date, details FROM listing";

		try {
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.aktListingId);
			ResultSet dbRes = prep.executeQuery();
			
			while(dbRes.next()) {
				int listingid = dbRes.getInt("listingid");
				String userid = dbRes.getString("userid");
				int catid = dbRes.getInt("catid");
				String title = dbRes.getString("title");
				String descr = dbRes.getString("descr");
				String zip = dbRes.getString("zip");
				String city = dbRes.getString("city");
				String status = dbRes.getString("status");
				Date date = dbRes.getDate("date");
				String details = dbRes.getString("details");
				JSONObject detailsJson = new JSONObject(details);
				
				this.anzeigen.add(new Listing(userid, catid, title, descr, city, status, date, detailsJson));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	// Anzeige speichern
	public boolean saveListing(String userid, String title, String descr, int catid, String city, String zip,
			JSONObject detailsJson) throws SQLException {

		LocalDate date = LocalDate.now();

		try {

			String sql = "INSERT INTO listing (userid, catid, title, descr, zip, city, status, date, details) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

			PreparedStatement prep = this.dbConn.prepareStatement(sql);

			prep.setString(1, userid);
			prep.setInt(2, catid);
			prep.setString(3, title);
			prep.setString(4, descr);
			prep.setString(5, zip);
			prep.setString(6, city);
			prep.setString(7, "A");
			prep.setDate(8, java.sql.Date.valueOf(date));
			prep.setString(9, detailsJson.toString());

			prep.executeUpdate();

			return true;

		} catch (Exception e) {
			System.out.println("Fehler beim Inserieren der Anzeige");
			e.printStackTrace();
			return false;
		}
	}
	
	// =============================
		// UPDATE
		// =============================
		public boolean updateListing(String userid, String title, String descr,
				int catid, String city, String zip, JSONObject detailsJson)  {

			try {

				String sql = "UPDATE listing SET catid=?, title=?, descr=?, zip=?, city=?, details=? "
						+ "WHERE listingid=? AND userid=?";

				PreparedStatement prep = this.dbConn.prepareStatement(sql);

				prep.setInt(1, catid);
				prep.setString(2, title);
				prep.setString(3, descr);
				prep.setString(4, zip);
				prep.setString(5, city);
				prep.setString(6, detailsJson.toString());
				prep.setInt(7, this.aktListingId);
				prep.setString(8, userid);

				int rows = prep.executeUpdate();

				this.editMode = false; // nach Update verlassen wir Edit-Mode
				return rows == 1;

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
		
		
		// =============================
		// LOAD FOR EDIT: Für das Laden zum Bearbeiten
		// =============================
		public boolean loadListingForEdit(int listingId, String userid) {

			try {

				String sql = "SELECT * FROM listing WHERE listingid=? AND userid=?";
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, listingId);
				prep.setString(2, userid);

				ResultSet rs = prep.executeQuery();

				if (rs.next()) {

					this.aktListingId = listingId;
					this.editTitle = rs.getString("title");
					this.editDescr = rs.getString("descr");
					this.editCity = rs.getString("city");
					this.editZip = rs.getString("zip");
					this.editCatId = rs.getInt("catid");
					this.editDetails = new JSONObject(rs.getString("details"));

					this.editMode = true;

					return true;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			return false;
		}
		// =============================
				// Um aus dem Bearbeitungsmodus rauszukommen
				// =============================
		public void resetEditMode() {
		    this.editMode = false;
		    this.editTitle = null;
		    this.editDescr = null;
		    this.editCity = null;
		    this.editZip = null;
		    this.editCatId = 0;
		    this.editDetails = null;
		}
		

	// gethtml
	public String getInseratDetailHtml() {

		String html = "";
		String sql = "SELECT c.name, l.userid, l.catid, l.title, l.descr, l.city, l.zip, l.status, l.date, l.details FROM listing l JOIN category c ON c.id = l.catid "
				+ "WHERE listingid = ?";

		try {
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.aktListingId);
			ResultSet dbRes = prep.executeQuery();

			if (dbRes.next()) {

				String catName = dbRes.getString("name");
				String userid = dbRes.getString("userid");
				int catid = dbRes.getInt("catid");
				String title = dbRes.getString("title");
				String descr = dbRes.getString("descr");
				String zip = dbRes.getString("zip");
				String city = dbRes.getString("city");
				String status = dbRes.getString("status");
				Date date = dbRes.getDate("date");
				String details = dbRes.getString("details");
				JSONObject detailsJson = new JSONObject(details);

				this.anbieterEmail = userid;

				html += "<div class='container py-5'>";
				html += "<div class='row g-4'>";

				html += "<div class='col-lg-8'>";

				html += "<div class='card shadow-sm border-0 rounded-4 mb-4'>";
				html += "<div class='card-body p-4'>";
				html += "<h2 class='fw-bold mb-2'>" + title + "</h2>";
				html += "<span class='badge bg-primary fs-6 mb-3'>" + catName + "</span>";
				html += "<p class='text-muted'>";
				html += "<i class='bi bi-geo-alt me-1'></i>" + zip + " " + city;
				html += "</p>";
				html += "<hr>";

				if(this.isPrice(detailsJson))
				    html += "<div class='display-6 fw-bold text-primary'>" + this.getPreisHtml(detailsJson) + "</div>";
				else
				    html += "<div class='display-6 fw-bold text-primary'>GRATIS / VB</div>";
				html += "</div>";
				html += "</div>";

				html += "<div class='mb-4'>";
				html += "<img src='../img/flexboard-logo.jpg' ";
				html += "class='img-fluid rounded-4 shadow-sm w-100 listing-main-image' ";
				html += "data-bs-toggle='modal' data-bs-target='#imageGalleryModal' ";
				html += "alt='Hauptbild'>";
				html += "<div class='text-end mt-2'>";
				html += "<small class='text-muted'><i class='bi bi-images'></i> 1 / 2</small>";
				html += "</div>";
				html += "</div>";

				html += "<div class='card shadow-sm border-0 rounded-4 mb-4'>";
				html += "<div class='card-body p-4'>";
				html += "<h5 class='fw-bold mb-3'>Beschreibung</h5>";
				html += "<p class='mb-0 description-text'>" + descr + "</p>";
				html += "</div>";
				html += "</div>";

				html += this.getDetailsKategorie(catid, detailsJson);

				html += "</div>";

				html += "<div class='col-lg-4'>";

				html += "<div class='card shadow-sm border-0 rounded-4 sticky-sidebar'>";
				html += "<div class='card-body p-4 text-center'>";
				html += "<div class='mb-3'>";
				html += "<i class='bi bi-person-circle text-secondary profile-icon-large'></i>";
				html += "</div>";
				html += "<h5 class='fw-bold mb-1'>" + userid + "</h5>";


				if (this.account.getLogedIn()) {
				    html += "<button class='btn btn-primary w-100 py-2 mb-2 mt-3' data-bs-toggle='offcanvas' data-bs-target='#chatOffcanvas'>";
				    html += "<i class='bi bi-envelope me-2'></i> Nachricht schreiben";
				    html += "</button>";
				} else {
				    html += "<a href='./LoginView.jsp' class='btn btn-primary w-100 py-2 mb-2 mt-3'>";
				    html += "<i class='bi bi-box-arrow-in-right me-2'></i> Zum Schreiben einloggen";
				    html += "</a>";
				}
				html += "</div>";
				html += "</div>";
				html += "</div>";
				html += "</div>";
				html += "</div>";

				html += "<div class='modal fade' id='imageGalleryModal' tabindex='-1' aria-hidden='true'>";
				html += "<div class='modal-dialog modal-xl modal-dialog-centered'>";
				html += "<div class='modal-content bg-transparent border-0 shadow-none'>";

				html += "<div class='modal-header border-0 pb-0'>";
				html += "<button type='button' class='btn-close btn-close-white ms-auto modal-close-custom' data-bs-dismiss='modal' aria-label='Close'></button>";
				html += "</div>";

				html += "<div class='modal-body p-0'>";
				html += "<div id='listingCarousel' class='carousel slide' data-bs-ride='false'>";

				html += "<div class='carousel-inner rounded-3 shadow'>";
				html += "<div class='carousel-item active'>";
				html += "<img src='../img/flexboard-logo.jpg' class='d-block w-100 carousel-image-custom' alt='Bild 1'>";
				html += "</div>";
				html += "<div class='carousel-item'>";
				html += "<img src='../img/nico_robin.jpg' class='d-block w-100 carousel-image-custom' alt='Bild 2'>";
				html += "</div>";
				html += "</div>";

				html += "<button class='carousel-control-prev' type='button' data-bs-target='#listingCarousel' data-bs-slide='prev'>";
				html += "<span class='carousel-control-prev-icon' aria-hidden='true'></span>";
				html += "<span class='visually-hidden'>Zurück</span>";
				html += "</button>";
				html += "<button class='carousel-control-next' type='button' data-bs-target='#listingCarousel' data-bs-slide='next'>";
				html += "<span class='carousel-control-next-icon' aria-hidden='true'></span>";
				html += "<span class='visually-hidden'>Weiter</span>";
				html += "</button>";

				html += "</div>";
				html += "</div>";

				html += "</div>";
				html += "</div>";
				html += "</div>";
				
				html += "<div class='offcanvas offcanvas-bottom shadow-lg rounded-top-4' tabindex='-1' id='chatOffcanvas' style='height: auto; max-height: 50vh;'>";
				html += "<div class='offcanvas-header border-bottom px-4 py-3'>";
				html += "<h5 class='offcanvas-title fw-bold'>";
				html += "<i class='bi bi-chat-dots text-primary me-2'></i>Nachricht an " + userid;
				html += "</h5>";
				html += "<button type='button' class='btn-close' data-bs-dismiss='offcanvas' aria-label='Close'></button>";
				html += "</div>";

				html += "<div class='offcanvas-body p-4'>";
				html += "<form action='./NachrichtSendenAppl.jsp' method='post'>"; 

				html += "<input type='hidden' name='empfaengerId' value='" + userid + "'>";
				html += "<input type='hidden' name='listingId' value='" + this.aktListingId + "'>";

				html += "<div class='mb-3'>";
				html += "<label class='form-label text-muted small'>Deine Nachricht:</label>";
				html += "<textarea class='form-control rounded-4 bg-light' name='nachrichtText' rows='4' placeholder='Hallo, ich habe Interesse an deinem Inserat...' required></textarea>";
				html += "</div>";

				html += "<div class='text-end'>";
				html += "<input type='submit' name='action' value='Senden' class='btn btn-primary px-4 py-2 rounded-5'>";

				html += "</button>";
				html += "</div>";

				html += "</form>";

				html += "</div>";
				html += "</div>";

			} else {
				html = "<div class='container py-5 text-center text-muted'>" + "Inserat nicht gefunden." + "</div>";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}
	// Hilfsmethoden für die DetailAnzeigenHtml
	
	// Hilfsmethode um für die aktuellen Details der Anzeige zu bekommen (von Kategorie abhängig)
	public String getDetailsKategorie(int catid, JSONObject detailsJson) {
	    String html = "";
	    html += "<div class='card shadow-sm border-0 rounded-4 mb-4'>";
	    html += "<div class='card-body p-4'>";
	    html += "<h5 class='fw-bold mb-3'>Details</h5>";
	    html += "<ul class='list-unstyled mb-0'>";

	    if (catid == 1) {
	        String studiengang = detailsJson.optString("studiengang");
	        String modul = detailsJson.optString("modul");
	        String hochschule = detailsJson.optString("hochschule");
	        String semester = detailsJson.optString("semester");
	        String format = detailsJson.optString("format");

	        html += "<li class='mb-2'><strong>Studiengang:</strong> " + studiengang + "</li>";
	        html += "<li class='mb-2'><strong>Modul:</strong> " + modul + "</li>";
	        html += "<li class='mb-2'><strong>Hochschule:</strong> " + hochschule + "</li>";
	        html += "<li class='mb-2'><strong>Semester:</strong> " + semester + "</li>";
	        html += "<li class='mb-2'><strong>Format:</strong> " + format + "</li>";

	    } else if (catid == 2) {
	        String fach = detailsJson.optString("fach");
	        String nachhilfeTyp = detailsJson.optString("nachhilfeTyp");
	        String preisProStunde = detailsJson.optString("preisProStunde");
	        String nachhilfeOrt = detailsJson.optString("nachhilfeOrt");
	        String nachhilfeNiveau = detailsJson.optString("nachhilfeNiveau");

	        html += "<li class='mb-2'><strong>Fach:</strong> " + fach + "</li>";
	        html += "<li class='mb-2'><strong>Typ:</strong> " + nachhilfeTyp + "</li>";
	        html += "<li class='mb-2'><strong>Preis pro Stunde:</strong> " + preisProStunde + "</li>";
	        html += "<li class='mb-2'><strong>Ort:</strong> " + nachhilfeOrt + "</li>";
	        html += "<li class='mb-2'><strong>Niveau:</strong> " + nachhilfeNiveau + "</li>";

	    } else if (catid == 3) {
	        String zimmergroesse = detailsJson.optString("zimmergroesse");
	        String gesamtmiete = detailsJson.optString("gesamtmiete");
	        String einzugsdatum = detailsJson.optString("einzugsdatum");
	        String befristung = detailsJson.optString("befristung");
	        String wgDetails = detailsJson.optString("wgDetails");

	        html += "<li class='mb-2'><strong>Zimmergröße:</strong> " + zimmergroesse + "</li>";
	        html += "<li class='mb-2'><strong>Gesamtmiete:</strong> " + gesamtmiete + "</li>";
	        html += "<li class='mb-2'><strong>Einzugsdatum:</strong> " + einzugsdatum + "</li>";
	        html += "<li class='mb-2'><strong>Befristung:</strong> " + befristung + "</li>";
	        html += "<li class='mb-2'><strong>WG-Details:</strong> " + wgDetails + "</li>";

	    } else if (catid == 4) {
	        String jobTypSelect = detailsJson.optString("jobTypSelect");
	        String anstellungsart = detailsJson.optString("anstellungsart");
	        String wochenstunden = detailsJson.optString("wochenstunden");
	        String verguetung = detailsJson.optString("verguetung");

	        html += "<li class='mb-2'><strong>Job-Typ:</strong> " + jobTypSelect + "</li>";
	        html += "<li class='mb-2'><strong>Anstellungsart:</strong> " + anstellungsart + "</li>";
	        html += "<li class='mb-2'><strong>Wochenstunden:</strong> " + wochenstunden + "</li>";
	        html += "<li class='mb-2'><strong>Vergütung:</strong> " + verguetung + "</li>";

	    } else if (catid == 5) {
	        String geraetetyp = detailsJson.optString("geraetetyp");
	        String marke = detailsJson.optString("marke");
	        String zustandTechnik = detailsJson.optString("zustandTechnik");
	        String garantie = detailsJson.optString("garantie");
	        String technikPreis = detailsJson.optString("technikPreis");

	        html += "<li class='mb-2'><strong>Gerätetyp:</strong> " + geraetetyp + "</li>";
	        html += "<li class='mb-2'><strong>Marke:</strong> " + marke + "</li>";
	        html += "<li class='mb-2'><strong>Zustand:</strong> " + zustandTechnik + "</li>";
	        html += "<li class='mb-2'><strong>Garantie:</strong> " + garantie + "</li>";
	        html += "<li class='mb-2'><strong>Preis:</strong> " + technikPreis + "</li>";

	    } else if (catid == 6) {
	        String eventDatum = detailsJson.optString("eventDatum");
	        String veranstalter = detailsJson.optString("veranstalter");
	        String eintritt = detailsJson.optString("eintritt");
	        String anmeldung = detailsJson.optString("anmeldung");
	        String eventPreis = detailsJson.optString("eventPreis");

	        html += "<li class='mb-2'><strong>Datum:</strong> " + eventDatum + "</li>";
	        html += "<li class='mb-2'><strong>Veranstalter:</strong> " + veranstalter + "</li>";
	        html += "<li class='mb-2'><strong>Eintritt:</strong> " + eintritt + "</li>";
	        html += "<li class='mb-2'><strong>Anmeldung:</strong> " + anmeldung + "</li>";
	        html += "<li class='mb-2'><strong>Preis:</strong> " + eventPreis + "</li>";

	    } else if (catid == 7) {
	        String tauschGegen = detailsJson.optString("tauschGegen");
	        String zustandTauschen = detailsJson.optString("zustandTauschen");
	        String abholung = detailsJson.optString("abholung");

	        html += "<li class='mb-2'><strong>Tausch gegen:</strong> " + tauschGegen + "</li>";
	        html += "<li class='mb-2'><strong>Zustand:</strong> " + zustandTauschen + "</li>";
	        html += "<li class='mb-2'><strong>Abholung:</strong> " + abholung + "</li>";

	    } else if (catid == 8) {
	        String dienstleistungKat = detailsJson.optString("dienstleistungKat");
	        String preismodell = detailsJson.optString("preismodell");
	        String dienstleistungPreis = detailsJson.optString("dienstleistungPreis");
	        String referenzen = detailsJson.optString("referenzen");

	        html += "<li class='mb-2'><strong>Kategorie:</strong> " + dienstleistungKat + "</li>";
	        html += "<li class='mb-2'><strong>Preismodell:</strong> " + preismodell + "</li>";
	        html += "<li class='mb-2'><strong>Preis:</strong> " + dienstleistungPreis + "</li>";
	        html += "<li class='mb-2'><strong>Referenzen:</strong> " + referenzen + "</li>";
	    }

	    html += "</ul>";
	    html += "</div>";
	    html += "</div>";

	    return html;
	}
	
	// Hilfsmethode um zu finden, ob in der JSON ein preis vorhanden ist
	public boolean isPrice(JSONObject detailsJson) {
		if(detailsJson.has("dienstleistungPreis"))
			return true;
		else if(detailsJson.has("eventPreis"))
			return true;
		else if(detailsJson.has("technikPreis"))
			return true;
		else if(detailsJson.has("preisProStunde"))
			return true;
		return false;
	}
	
	// Hilfsmethode um den richtigen Preis zu Printen
	public String getPreisHtml(JSONObject detailsJson) throws JSONException {
		String html = "";
		
		if(detailsJson.has("dienstleistungPreis"))
			return detailsJson.getString("dienstleistungPreis")  + "€";
		else if(detailsJson.has("eventPreis"))
			return detailsJson.getString("eventPreis")  + "€";
		else if(detailsJson.has("technikPreis"))
			return detailsJson.getString("technikPreis")  + "€";
		else if(detailsJson.has("preisProStunde"))
			return detailsJson.getString("preisProStunde") + "€/h";
		
		return html;
	}
	
	
	public String getKontaktButtonHtml() {

		if (account == null)
			return "";

		String html = "";

		if (!account.getLogedIn()) {

			html += "<div class='text-center mt-4'>";
			html += "<a href='./NavbarAppl.jsp?action=zumLogin' "
					+ "class='btn btn-primary btn-lg px-4 shadow rounded-3'>" + "Zum Login, um Kontakt aufzunehmen"
					+ "</a></div>";

		} else if (this.anbieterEmail != null && !account.getEmail().equals(this.anbieterEmail)) {

			html += "<div class='text-center mt-4'>";
			html += "<a href='./NachrichtenAppl.jsp?action=switch&user=" + this.anbieterEmail
					+ "' class='btn btn-success btn-lg px-4 shadow rounded-3'>"
					+ "<i class='bi bi-chat-dots'></i> Nachricht senden" + "</a></div>";

		} else {

			html += "<div class='text-center mt-4 text-muted'>";
			html += "Das ist dein eigenes Inserat.";
			html += "</div>";
		}

		return html;
	}

	public String getEigentuemerButtonsHtml() {

		if (account == null)
			return "";

		String html = "";

		if (account.getLogedIn() && this.anbieterEmail != null && account.getEmail().equals(this.anbieterEmail)) {

			html += "<div class='text-center mt-4 d-flex gap-3 justify-content-center'>";

			html += "<a href='./NavbarAppl.jsp?action=bearbeiteListing' "
					+ "class='btn btn-outline-primary px-4 rounded-3'>"
					+ "<i class='bi bi-pencil'></i> Inserat bearbeiten" + "</a>";

			html += "<a href='./NavbarAppl.jsp?action=deaktiviereListing' "
					+ "class='btn btn-outline-danger px-4 rounded-3'>"
					+ "<i class='bi bi-x-circle'></i> Inserat deaktivieren" + "</a>";

			html += "</div>";
		}

		return html;
	}

	public boolean deaktiviereListing() {

		if (account == null)
			return false;
		if (!account.getLogedIn())
			return false;

		try {

			String sqlCheck = "SELECT userid FROM listing WHERE listingid = ?";
			PreparedStatement prepCheck = this.dbConn.prepareStatement(sqlCheck);
			prepCheck.setInt(1, this.aktListingId);
			ResultSet rs = prepCheck.executeQuery();

			if (!rs.next())
				return false;

			String owner = rs.getString("userid");

			if (!account.getEmail().equals(owner))
				return false;

			String sqlUpdate = "UPDATE listing SET status = 'D' WHERE listingid = ?";
			PreparedStatement prepUpdate = this.dbConn.prepareStatement(sqlUpdate);
			prepUpdate.setInt(1, this.aktListingId);

			int rows = prepUpdate.executeUpdate();

			return rows == 1;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public String getMeineInserateHtml() {

		if (account == null)
			return "";
		if (!account.getLogedIn())
			return "";

		String html = "";

		String sql ="SELECT listingid, title, city, date, status, details "
		        + "FROM listing "
		        + "WHERE userid = ? AND status <> 'X' "
		        + "ORDER BY listingid DESC";

		try {

			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, account.getEmail());
			ResultSet rs = prep.executeQuery();

			html += "<section class='py-5 bg-light'>";
			html += "<div class='container'>";
			html += "<h2 class='fw-bold mb-4'>Meine Inserate</h2>";

			boolean hasResults = false;

			while (rs.next()) {

				hasResults = true;

				int listingid = rs.getInt("listingid");
				String title = rs.getString("title");
				String city = rs.getString("city");
				String status = rs.getString("status");
				java.sql.Date date = rs.getDate("date");

				String detailsJsonString = rs.getString("details");
				JSONObject detailsJson = new JSONObject(detailsJsonString);

				int price = 0;

				if (detailsJson.has("price")) {
					price = detailsJson.optInt("price", 0);
				} else if (detailsJson.has("technikPreis")) {
					price = detailsJson.optInt("technikPreis", 0);
				} else if (detailsJson.has("eventPreis")) {
					price = detailsJson.optInt("eventPreis", 0);
				} else if (detailsJson.has("dienstleistungPreis")) {
					price = detailsJson.optInt("dienstleistungPreis", 0);
				}
				html += "<div class='card shadow-sm mb-4 border-0'>";
				html += "<div class='card-body'>";
				html += "<div class='d-flex justify-content-between align-items-start'>";
				html += "<div>";
				html += "<h5 class='fw-bold'>" + title + "</h5>";
				html += "<p class='text-muted mb-1'>";
				html += "Status: <strong>" + (status.equals("A") ? "Aktiv" : "Deaktiviert") + "</strong>";
				html += "</p>";
				if (date != null) {
					html += "<p class='text-muted small'>Erstellt am: " + date.toString() + "</p>";
				}
				if (city != null && !city.isEmpty()) {
					html += "<p class='text-muted small'>" + city + "</p>";
				}

				if (price > 0) {
					html += "<p class='fw-bold text-primary'>" + price + " €</p>";
				} else {
					html += "<p class='text-muted'>Preis auf Anfrage</p>";
				}
				html += "</div>";
				html += "<div class='text-end'>";
				html += "<a href='./NavbarAppl.jsp?action=zumListing&id=" + listingid
						+ "' class='btn btn-sm btn-outline-secondary mb-2 d-block'>Details anzeigen</a>";
				html += "<a href='./NavbarAppl.jsp?action=bearbeiteListing&id=" + listingid
						+ "' class='btn btn-sm btn-outline-primary mb-2 d-block'>Bearbeiten</a>";
				if (status.equals("A")) {
					html += "<a href='./NavbarAppl.jsp?action=deaktiviereListing&id=" + listingid
							+ "' class='btn btn-sm btn-outline-danger d-block'>Deaktivieren</a>";
				}
				else {
					html += "<a href='./NavbarAppl.jsp?action=aktiviereListing&id=" + listingid
							+ "' class='btn btn-sm btn-outline-success d-block'>Reaktivieren</a>";
				}
				html += "<a href='./NavbarAppl.jsp?action=loescheListing&id=" + listingid
				        + "' class='btn btn-sm btn-danger d-block mt-2' "
				        + "onclick=\"return confirm('Inserat wirklich endgültig löschen?');\">"
				        + "Löschen</a>";
				html += "</div>";
				html += "</div>"; // flex
				html += "</div>"; // card-body
				html += "</div>"; // card
			}

			if (!hasResults) {
				html += "<div class='text-muted'>Du hast noch keine Inserate erstellt.</div>";
			}

			html += "</div></section>";

		} catch (Exception e) {
			e.printStackTrace();
		}

		return html;
	}
	
	public boolean aktiviereListing() {
	    if (account == null) return false;
	    if (!account.getLogedIn()) return false;

	    try {

	        String sqlCheck = "SELECT userid FROM listing WHERE listingid = ?";
	        PreparedStatement prepCheck = this.dbConn.prepareStatement(sqlCheck);
	        prepCheck.setInt(1, this.aktListingId);
	        ResultSet rs = prepCheck.executeQuery();

	        if (!rs.next()) return false;

	        String owner = rs.getString("userid");

	        if (!account.getEmail().equals(owner)) return false;

	        String sqlUpdate = "UPDATE listing SET status = 'A' WHERE listingid = ?";
	        PreparedStatement prepUpdate = this.dbConn.prepareStatement(sqlUpdate);
	        prepUpdate.setInt(1, this.aktListingId);

	        int rows = prepUpdate.executeUpdate();

	        return rows == 1;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public boolean loescheListing() {

	    if (account == null) return false;
	    if (!account.getLogedIn()) return false;

	    try {

	        String sqlCheck = "SELECT userid FROM listing WHERE listingid = ?";
	        PreparedStatement prepCheck = this.dbConn.prepareStatement(sqlCheck);
	        prepCheck.setInt(1, this.aktListingId);
	        ResultSet rs = prepCheck.executeQuery();

	        if (!rs.next()) return false;

	        String owner = rs.getString("userid");

	        if (!account.getEmail().equals(owner)) return false;

	        String sqlUpdate = "UPDATE listing SET status = 'X' WHERE listingid = ?";
	        PreparedStatement prepUpdate = this.dbConn.prepareStatement(sqlUpdate);
	        prepUpdate.setInt(1, this.aktListingId);

	        int rows = prepUpdate.executeUpdate();

	        return rows == 1;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}

	// Getter und Setter (Inserieren)

	public void setAktListingId(int id) {
		this.aktListingId = id;
	}

	public int getAktListingId() {
		return this.aktListingId;
	}

	public void setAccount(AccountBean account) {
		this.account = account;
	}
	public boolean isEditMode() {
		return editMode;
	}
	public String getEditTitle() {
		return editTitle != null ? editTitle : "";
	}
	public String getEditDescr() {
		return editDescr != null ? editDescr : "";
	}
	public String getEditCity() {
		return editCity != null ? editCity : "";
	}
	public String getEditZip() {
		return editZip != null ? editZip : "";
	}
	public int getEditCatId() {
		return editCatId;
	}
	public JSONObject getEditDetails() {
	    return editDetails;
	}
	public String getEditDetailValue(String key) {
	    if (this.editDetails == null) return "";
	    return this.editDetails.optString(key, "");
	}
	
}
