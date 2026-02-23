package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.json.JSONObject;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class ListingBean {

	Connection dbConn;
	private int aktListingId;
	public AccountBean account;
	public String anbieterEmail;

	public ListingBean() throws ClassNotFoundException, SQLException {
		this.dbConn = new PostgreSQLAccess().getConnection();
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

	// gethtml
	public String getInseratDetailHtml() {

		String html = "";
		String sql = "SELECT l.title, l.descr, l.city, l.zip, l.date, l.details, "
				+ "c.name AS category_name, l.userid " + "FROM listing l " + "JOIN category c ON l.catid = c.id "
				+ "WHERE l.listingid = ?";

		try {
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setInt(1, this.aktListingId);
			ResultSet rs = prep.executeQuery();

			if (rs.next()) {

				String title = rs.getString("title");
				String descr = rs.getString("descr");
				String category = rs.getString("category_name");
				String city = rs.getString("city");
				String zip = rs.getString("zip");
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
				String userid = rs.getString("userid");
				this.anbieterEmail = userid;

				html += "<div class='container py-5'>";
				html += "<div class='row justify-content-center'>";
				html += "<div class='col-lg-8'>";

				html += "<div class='card shadow-sm border-0 rounded-4'>";
				html += "<div class='card-body p-5'>";

				html += "<a href='./NavbarAppl.jsp?action=zurHomepage' "
						+ "class='btn btn-outline-secondary btn-sm mb-4'>" + "<i class='bi bi-arrow-left'></i> Zurück"
						+ "</a>";

				html += "<h2 class='fw-bold mb-3'>" + title + "</h2>";
				html += "<div class='mb-3'>";
				html += "<span class='badge bg-primary fs-6 px-3 py-2'>" + category + "</span>";
				html += "</div>";

				html += "<p class='text-muted mb-2'>";
				html += (zip != null ? zip : "") + " " + (city != null ? city : "");
				html += "</p>";

				html += "<hr class='my-4'>";

				html += "<p class='mb-4'>" + descr + "</p>";

				if (price > 0) {
					html += "<div class='my-4'>";
					html += "<span class='display-6 fw-bold text-primary'>" + price + " €</span>";
					html += "</div>";
				} else {
					html += "<div class='my-4'>";
					html += "<span class='display-6 fw-bold text-secondary'>" + "Preis auf Anfrage</span>";
					html += "</div>";
				}
				html += "<div class='border-top pt-4 mt-4'>";
				html += "<div class='d-flex align-items-center gap-2 text-muted'>";
				html += "<i class='bi bi-person-circle fs-5'></i>";
				html += "<span>Anbieter: <strong>" + userid + "</strong></span>";
				html += "</div>";
				html += "</div>";
				html += "</div></div></div></div></div>";

			} else {
				html = "<div class='container py-5 text-center text-muted'>" + "Inserat nicht gefunden." + "</div>";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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

		String sql = "SELECT listingid, title, city, date, status, details " + "FROM listing " + "WHERE userid = ? "
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
				} else {
					html += "<a href='./NavbarAppl.jsp?action=aktiviereListing&id=" + listingid
							+ "' class='btn btn-sm btn-outline-success d-block'>Reaktivieren</a>";
				}
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
}
