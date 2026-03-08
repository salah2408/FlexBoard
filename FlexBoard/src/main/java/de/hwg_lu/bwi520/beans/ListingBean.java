package de.hwg_lu.bwi520.beans;

import java.sql.Statement;
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
	int latestListingId;
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
		this.anzeigen.clear();
		String sql = "SELECT listingid, userid, catid, title, descr, city, zip, status, date, details FROM listing";

		try {
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			ResultSet dbRes = prep.executeQuery();

			while (dbRes.next()) {
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
		} catch (Exception e) {
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

			PreparedStatement prep = this.dbConn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

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
			
			ResultSet dbRes = prep.getGeneratedKeys();
			if (dbRes.next()) {
			    int neueListingId = dbRes.getInt(1);
			    this.latestListingId = neueListingId;
			}

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
	public boolean updateListing(String userid, String title, String descr, int catid, String city, String zip,
			JSONObject detailsJson) {

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
	// =============================
		// GET DETAILS DIRECTLY FROM DB (Fuer Update)
		// =============================
		public JSONObject getDetailsForListing(int listingId) {
			try {
				String sql = "SELECT details FROM listing WHERE listingid = ?";
				PreparedStatement prep = this.dbConn.prepareStatement(sql);
				prep.setInt(1, listingId);
				
				ResultSet rs = prep.executeQuery();
				if (rs.next()) {
					String detailsStr = rs.getString("details");
					if (detailsStr != null && !detailsStr.isEmpty()) {
						return new JSONObject(detailsStr);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
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

				boolean isOwner = this.account.getEmail().equals(userid);

				html += "<div class='container py-5'>";
				html += "<div class='row g-4'>";

				if (isOwner) {
				    html += "<div class='col-lg-8 offset-lg-2'>";
				} else {
				    html += "<div class='col-lg-8'>";
				}

				html += "<div class='card shadow-sm border-0 rounded-4 mb-4'>";
				html += "<div class='card-body p-4'>";
				html += "<h2 class='fw-bold mb-2'>" + title + "</h2>";
				html += "<span class='badge bg-primary fs-6 mb-3'>" + catName + "</span>";
				html += "<p class='text-muted'>";
				html += "<i class='bi bi-geo-alt me-1'></i>" + zip + " " + city;
				html += "</p>";
				html += "<hr>";

				if (this.isPrice(detailsJson))
					html += "<div class='display-6 fw-bold text-primary'>" + this.getPreisHtml(detailsJson) + "</div>";
				else
				    html += "<div class='display-6 fw-bold text-primary'>" + this.getGratisHtml(catid, detailsJson) + "</div>";
				html += "</div>";
				html += "</div>";

				String imageBase64 = detailsJson.optString("imageBase64", null);

				html += "<div class='mb-4'>";

				if (imageBase64 != null && !imageBase64.isEmpty()) {

					html += "<img src='" + imageBase64 + "' "
							+ "class='img-fluid rounded-4 shadow-sm w-100 listing-main-image' "
							+ "data-bs-toggle='modal' data-bs-target='#imageGalleryModal' " + "alt='Hauptbild'>";

				} else {

					html += "<img src='../img/flexboard-logo.jpg' "
							+ "class='img-fluid rounded-4 shadow-sm w-100 listing-main-image' "
							+ "data-bs-toggle='modal' data-bs-target='#imageGalleryModal' " + "alt='Placeholder'>";
				}
				html += "<div class='text-end mt-2'>";
				html += "<small class='text-muted'><i class='bi bi-images'></i> Bild</small>";
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
				
				// Nachricht schreiben soll nur erscheinen wenn der Inhaber der Anzeige nicht der User selbst ist
				if(!isOwner) {
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
					}if (this.account.getLogedIn()) {

						if (this.isFavorite()) {

						    html += "<button ";
						    html += "class='btn btn-danger w-100 py-2 mb-2 favorite-toggle' ";
						    html += "data-id='" + this.aktListingId + "' ";
						    html += "data-action='remove'>";
						    html += "<i class='bi bi-heart-fill'></i> Favorit entfernen";
						    html += "</button>";

						} else {

						    html += "<button ";
						    html += "class='btn btn-outline-danger w-100 py-2 mb-2 favorite-toggle' ";
						    html += "data-id='" + this.aktListingId + "' ";
						    html += "data-action='add'>";
						    html += "<i class='bi bi-heart'></i> Zu Favoriten hinzufügen";
						    html += "</button>";

						}

					} else {
					    html += "<a href='./InseratDetailAppl.jsp?action=anmelden&link=./InseratDetailView.jsp' class='btn btn-primary w-100 py-2 mb-2 mt-3'>";
					    html += "<i class='bi bi-box-arrow-in-right me-2'></i> Zum Schreiben einloggen";
					    html += "</a>";
					}
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

				if (imageBase64 != null && !imageBase64.isEmpty()) {

				    html += "<div class='carousel-item active'>";
				    html += "<img src='" + imageBase64 + "' class='d-block w-100 carousel-image-custom' alt='Bild'>";
				    html += "</div>";

				} else {

				    html += "<div class='carousel-item active'>";
				    html += "<img src='../img/flexboard-logo.jpg' class='d-block w-100 carousel-image-custom' alt='Placeholder'>";
				    html += "</div>";

				}

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
				html += "<form action='./InseratDetailAppl.jsp' method='post'>"; 

				html += "<input type='hidden' name='empfaengerid' value='" + userid + "'>";
				html += "<input type='hidden' name='listingid' value='" + this.aktListingId + "'>";

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

		    return html;
		}
	// Hilfsmethoden für die DetailAnzeigenHtml

	// Hilfsmethode um für die aktuellen Details der Anzeige zu bekommen (von
	// Kategorie abhängig)
	public String getDetailsKategorie(int catid, JSONObject detailsJson) {
	    String html = "";
	    if(catid == 9)
	    	return html;
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
	        int preisProStunde = detailsJson.optInt("preisProStunde");
	        String nachhilfeOrt = detailsJson.optString("nachhilfeOrt");
	        String nachhilfeNiveau = detailsJson.optString("nachhilfeNiveau");

	        html += "<li class='mb-2'><strong>Fach:</strong> " + fach + "</li>";
	        html += "<li class='mb-2'><strong>Typ:</strong> " + nachhilfeTyp + "</li>";
	        html += "<li class='mb-2'><strong>Preis pro Stunde:</strong> " + preisProStunde + "</li>";
	        html += "<li class='mb-2'><strong>Ort:</strong> " + nachhilfeOrt + "</li>";
	        html += "<li class='mb-2'><strong>Niveau:</strong> " + nachhilfeNiveau + "</li>";

	    } else if (catid == 3) {
	        String zimmergroesse = detailsJson.optString("zimmergroesse");
	        int gesamtmiete = detailsJson.optInt("gesamtmiete");
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
	        int verguetung = detailsJson.optInt("verguetung");

	        html += "<li class='mb-2'><strong>Job-Typ:</strong> " + jobTypSelect + "</li>";
	        html += "<li class='mb-2'><strong>Anstellungsart:</strong> " + anstellungsart + "</li>";
	        html += "<li class='mb-2'><strong>Wochenstunden:</strong> " + wochenstunden + "</li>";
	        html += "<li class='mb-2'><strong>Vergütung:</strong> " + verguetung + "</li>";

	    } else if (catid == 5) {
	        String geraetetyp = detailsJson.optString("geraetetyp");
	        String marke = detailsJson.optString("marke");
	        String zustandTechnik = detailsJson.optString("zustandTechnik");
	        String garantie = detailsJson.optString("garantie");
	        int technikPreis = detailsJson.optInt("technikPreis");

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
	        int eventPreis = detailsJson.optInt("eventPreis");

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
	        int dienstleistungPreis = detailsJson.optInt("dienstleistungPreis");
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
	public boolean isPrice(JSONObject detailsJson) throws JSONException {
		if(detailsJson.has("dienstleistungPreis"))
			return detailsJson.getInt("dienstleistungPreis") > 0;
		else if(detailsJson.has("eventPreis"))
			return detailsJson.getInt("eventPreis") > 0;
		else if(detailsJson.has("technikPreis"))
			return detailsJson.getInt("technikPreis") > 0;
		else if(detailsJson.has("preisProStunde"))
			return detailsJson.getInt("preisProStunde") > 0;
		else if(detailsJson.has("gesamtmiete"))
			return detailsJson.getInt("gesamtmiete") > 0;
		else if(detailsJson.has("verguetung"))
			return detailsJson.getInt("verguetung") > 0;
		else if(detailsJson.has("sonstigesPreis")) {
			return detailsJson.getInt("sonstigesPreis") > 0;
		}
			
		return false;
	}

	// Hilfsmethode um den richtigen Preis zu Printen
	public String getPreisHtml(JSONObject detailsJson) throws JSONException {
		String html = "";
		
		if(detailsJson.has("dienstleistungPreis"))
			return detailsJson.getInt("dienstleistungPreis")  + "€";
		else if(detailsJson.has("eventPreis"))
			return detailsJson.getInt("eventPreis")  + "€";
		else if(detailsJson.has("technikPreis"))
			return detailsJson.getInt("technikPreis")  + "€";
		else if(detailsJson.has("preisProStunde"))
			return detailsJson.getInt("preisProStunde") + "€/h";
		else if(detailsJson.has("gesamtmiete"))
			return detailsJson.getInt("gesamtmiete")  + "€";
		else if(detailsJson.has("verguetung"))
			return detailsJson.getInt("verguetung")  + "€";
		else if(detailsJson.has("sonstigesPreis"))
			return detailsJson.getInt("sonstigesPreis") + "€";
		
		return html;
	}
	
	// Hilfsmethode um zu prüfen ob eine Anzeige Gratis ist oder auf Anfrage
	public String getGratisHtml(int catid, JSONObject detailsJson) throws JSONException {
		String html = "";
		
		 if (catid == 1) {
			 	html = "Gratis";
		    } else if (catid == 2) {
		    	if(detailsJson.getString("nachhilfeTyp").equals("Suche Nachhilfe"))
		    		html = "Preis auf Anfrage";
		    	else if(detailsJson.getString("nachhilfeTyp").equals("Suche Lerngruppe (Kostenlos)"))
		    		html = "Gratis";
		    	else if(detailsJson.getString("nachhilfeTyp").equals("Biete Nachhilfe"))
		    		html = "Preis auf Anfrage";
		    } else if (catid == 3) {
		    	html = "Preis auf Anfrage";
		    } else if (catid == 4) {
		    	html = "Preis auf Anfrage";
		    } else if (catid == 5) {
		    	html = "Preis auf Anfrage";
		    } else if (catid == 6) {
		    	if(detailsJson.getString("eintritt").equals("Kostenlos"))
		    		html = "Gratis";
		    	else if(detailsJson.getString("eintritt").equals("Kostenpflichtig"))
		    		html = "Preis auf Anfrage";
		    } else if (catid == 7) {
		    	html = "Tausch";
		    } else if (catid == 8) {
		    	html = "Preis auf Anfrage";
		    } else if(catid == 9) {
		    	if(detailsJson.getString("sonstigesTyp").equals("Gratis"))
		    		html = "Gratis";
		    	else if(detailsJson.getString("sonstigesTyp").equals("Auf Anfrage"))
		    		html = "Preis auf Anfrage";
		    }
		    else
		    	System.out.println("ungültige catid !!!");

		
		return html;
	}
	
	
	// Hilfsmethode: PRÜFEN OB FAVORIT EXISTIERT
	
	public boolean isFavorite() {

	    if (account == null || !account.getLogedIn())
	        return false;

	    try {

	        String sql = "SELECT * FROM favorite WHERE userid=? AND listingid=?";
	        PreparedStatement prep = dbConn.prepareStatement(sql);

	        prep.setString(1, account.getEmail());
	        prep.setInt(2, aktListingId);

	        ResultSet rs = prep.executeQuery();

	        return rs.next();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public boolean isFavorite(int listingId) {

	    if (account == null || !account.getLogedIn())
	        return false;

	    try {

	        String sql = "SELECT * FROM favorite WHERE userid=? AND listingid=?";
	        PreparedStatement prep = dbConn.prepareStatement(sql);

	        prep.setString(1, account.getEmail());
	        prep.setInt(2, listingId);

	        ResultSet rs = prep.executeQuery();

	        return rs.next();

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	
	//getHtml
	
	public String getPreisBadgeHtml(int catid, JSONObject detailsJson) {

	    try {

	        if (this.isPrice(detailsJson)) {
	            return "<span class='badge bg-primary fs-6 px-3 py-2 mt-3'>"
	                    + this.getPreisHtml(detailsJson)
	                    + "</span>";
	        } else {
	            String gratisText = this.getGratisHtml(catid, detailsJson);

	            if (gratisText.equals("Gratis")) {
	                return "<span class='badge bg-success fs-6 px-3 py-2 mt-3'>Gratis</span>";
	            } else {
	                return "<span class='badge bg-dark bg-opacity-75 fs-6 px-3 py-2 mt-3'>"
	                        + gratisText
	                        + "</span>";
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	        return "<span class='badge bg-dark bg-opacity-75 fs-6 px-3 py-2 mt-3'>Preis auf Anfrage</span>";
	    }
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

		String sql = "SELECT listingid, title, city, date, status, details " + "FROM listing "
				+ "WHERE userid = ? AND status <> 'X' " + "ORDER BY listingid DESC";

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
				} else if (detailsJson.has("sonstigesPreis")) {
					price = detailsJson.optInt("sonstigesPreis", 0);
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
				html += "<a href='./MeineInserateAppl.jsp?action=zumListing&id=" + listingid
						+ "' class='btn btn-sm btn-outline-secondary mb-2 d-block'>Details anzeigen</a>";
				html += "<a href='./MeineInserateAppl.jsp?action=bearbeiteListing&id=" + listingid
						+ "' class='btn btn-sm btn-outline-primary mb-2 d-block'>Bearbeiten</a>";
				if (status.equals("A")) {
					html += "<a href='./MeineInserateAppl.jsp?action=deaktiviereListing&id=" + listingid
							+ "' class='btn btn-sm btn-outline-danger d-block'>Deaktivieren</a>";
				} else {
					html += "<a href='./MeineInserateAppl.jsp?action=aktiviereListing&id=" + listingid
							+ "' class='btn btn-sm btn-outline-success d-block'>Reaktivieren</a>";
				}
				html += "<a href='./MeineInserateAppl.jsp?action=loescheListing&id=" + listingid
						+ "' class='btn btn-sm btn-danger d-block mt-2' "
						+ "onclick=\"return confirm('Inserat wirklich endgültig löschen?');\">" + "Löschen</a>";
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
	
	public String getMeineFavoritenHtml() {

	    if (account == null)
	        return "";

	    if (!account.getLogedIn())
	        return "";

	    String html = "";

	    String sql =
	        "SELECT l.listingid, l.title, l.city, l.date, l.details " +
	        "FROM favorite f " +
	        "JOIN listing l ON l.listingid = f.listingid " +
	        "WHERE f.userid = ? AND l.status = 'A' " +
	        "ORDER BY f.createdat DESC";

	    try {

	        PreparedStatement prep = dbConn.prepareStatement(sql);
	        prep.setString(1, account.getEmail());

	        ResultSet rs = prep.executeQuery();

	        html += "<section class='py-5 bg-light'>";
	        html += "<div class='container' id='favoritesContainer'>";
	        html += "<h2 class='fw-bold mb-4'>Meine Favoriten</h2>";

	        boolean hasResults = false;

	        while (rs.next()) {

	            hasResults = true;

	            int listingid = rs.getInt("listingid");
	            String title = rs.getString("title");
	            String city = rs.getString("city");
	            java.sql.Date date = rs.getDate("date");

	            html += "<div class='card shadow-sm mb-4 border-0 favorite-card' ";
	            html += "data-id='" + listingid + "'>";
	            html += "<div class='card-body'>";

	            html += "<div class='d-flex justify-content-between align-items-start'>";

	            html += "<div>";
	            html += "<h5 class='fw-bold'>" + title + "</h5>";

	            if (city != null)
	                html += "<p class='text-muted'>" + city + "</p>";

	            if (date != null)
	                html += "<p class='text-muted small'>Erstellt am: " + date + "</p>";

	            html += "</div>";

	            html += "<div class='text-end'>";

	            html += "<a href='./NavbarAppl.jsp?action=zumListing&id=" + listingid + "' ";
	            html += "class='btn btn-sm btn-outline-secondary mb-2 d-block'>";
	            html += "Details anzeigen</a>";

	            html += "<button ";
	            html += "class='btn btn-sm btn-danger d-block favorite-toggle' ";
	            html += "data-id='" + listingid + "' ";
	            html += "data-action='remove'>";
	            html += "Favorit entfernen</button>";

	            html += "</div>";

	            html += "</div>";
	            html += "</div>";
	            html += "</div>";

	        }

	        if (!hasResults) {
	            html += "<div class='text-muted'>Du hast noch keine Favoriten gespeichert.</div>";
	        }

	        html += "</div></section>";

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return html;
	}
	
	public String getMeineInserateNavigationHtml() {

	    String html = "";

	    html += "<div class='container pt-4'>";

	    html += "<div class='d-flex gap-3 mb-4'>";

	    html += "<a href='./NavbarAppl.jsp?action=zurMeineInserate' ";
	    html += "class='btn btn-outline-primary'>";
	    html += "Meine Inserate";
	    html += "</a>";

	    html += "<a href='./NavbarAppl.jsp?action=zuMeineFavoriten' ";
	    html += "class='btn btn-outline-danger'>";
	    html += "<i class='bi bi-heart-fill'></i> Meine Favoriten";
	    html += "</a>";

	    html += "</div>";
	    html += "</div>";

	    return html;
	}
	
	public String getProfilHtml() {
	    if (!this.account.getLogedIn()) {
	        return "";
	    }

	    String html = "<section class='py-5 bg-light'>"
	            + "<div class='container'>"
	            + "<div class='row justify-content-center'>"
	            + "<div class='col-md-6'>"
	            + "<div class='card shadow-sm border-0'>"
	            + "<div class='card-body'>"
	            + "<h5 class='card-title fw-bold mb-3'>Mein Profil</h5>"
	            + "<ul class='list-group list-group-flush'>"
	            + "<li class='list-group-item'><strong>Name:</strong> "
	            + this.account.getVorname() + " " + this.account.getNachname() + "</li>"
	            + "<li class='list-group-item'><strong>E-Mail:</strong> "
	            + this.account.getEmail() + "</li>"
	            + "<li class='list-group-item'><strong>Status:</strong> "
	            + "<span class='badge bg-success'>Aktiv</span></li>"
	            + "</ul>"
	            + "<div class='mt-3 text-end'>"
	            + "<a href='./NavbarAppl.jsp?action=profilBearbeiten' class='btn btn-sm btn-outline-secondary'>"
	            + "Profil bearbeiten</a>"
	            + "</div>"
	            + "</div></div></div></div></div></section>";

	    return html;
	}

	public boolean aktiviereListing() {
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
	public String getEditImageHtml() {
	    if (!this.editMode || this.editDetails == null)
	        return "";

	    String image = this.editDetails.optString("imageBase64", "");

	    if (image.isEmpty())
	        return "";

	    return "<img src='" + image + "' " +
	           "style='height:100px;width:100px;object-fit:cover;" +
	           "border-radius:8px;border:1px solid #dee2e6;'>";
	}
	// =============================
	// FAVORIT SPEICHERN
	// =============================
	public boolean addFavorite() {

	    if (account == null || !account.getLogedIn())
	        return false;

	    try {

	        String sqlCheck = "SELECT * FROM favorite WHERE userid=? AND listingid=?";
	        PreparedStatement check = dbConn.prepareStatement(sqlCheck);
	        check.setString(1, account.getEmail());
	        check.setInt(2, aktListingId);

	        ResultSet rs = check.executeQuery();

	        if (rs.next()) {
	            return false; // existiert bereits
	        }

	        String sql = "INSERT INTO favorite (userid, listingid, createdat) VALUES (?, ?, CURRENT_DATE)";
	        PreparedStatement prep = dbConn.prepareStatement(sql);

	        prep.setString(1, account.getEmail());
	        prep.setInt(2, aktListingId);

	        prep.executeUpdate();

	        return true;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	// =============================
	// FAVORIT ENTFERNEN
	// =============================
	public boolean removeFavorite() {

	    if (account == null || !account.getLogedIn())
	        return false;

	    try {

	        String sql = "DELETE FROM favorite WHERE userid=? AND listingid=?";
	        PreparedStatement prep = dbConn.prepareStatement(sql);

	        prep.setString(1, account.getEmail());
	        prep.setInt(2, aktListingId);

	        int rows = prep.executeUpdate();
	        
	        if(rows == 1){
	            this.readAlleAnzeigenFromDB();
	        }

	        return rows == 1;

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	// Getter und Setter (Inserieren)
	
	public int getLatestListingId() {
		return this.latestListingId;
	}
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
		if (this.editDetails == null)
			return "";
		return this.editDetails.optString(key, "");
	}
	public String getEditImageBase64() {
	    if (this.editDetails == null)
	        return "";

	    return this.editDetails.optString("imageBase64", "");
	}
}
