package de.hwg_lu.bwi520.beans;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONObject;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;



public class HomeBean {
	
	public AccountBean account;
	private String suchbegriff = "";
	Connection dbConn;

    public HomeBean() throws ClassNotFoundException, SQLException {
    	
    	this.dbConn = new PostgreSQLAccess().getConnection();
    	
    	
    	
    	
    }
    
 // Abschnitt getHtml
    public String getHeroHtml() {

        String html = "";

        html += "<section class='hero-section'>";
        html += "<div class='container'>";
        html += "<div class='row justify-content-center text-center'>";
        html += "<div class='col-lg-8'>";

        html += "<h1 class='display-4 fw-bold mb-4'>";
        html += "Der digitale Marktplatz für deine ";
        html += "<span class='text-primary fw-semibold'>Campus-Community</span>";
        html += "</h1>";

        html += "<p class='lead text-muted mb-4'>";
        html += "Finde Nachhilfe, WG-Zimmer, Jobs oder Technik – schnell, ";
        html += "kostenlos und direkt mit Kommiliton:innen.";
        html += "</p>";

        html += "<div class='d-flex justify-content-center gap-3 flex-wrap mt-4'>";

        // WICHTIG: account darf nicht null sein
        if (account != null) {
            html += "<a href='" + account.getInserierenLink() + "' ";
            html += "class='btn btn-primary btn-lg px-5 shadow'>";
            html += "Inserat erstellen";
            html += "</a>";
        }

        html += "<a href='./SucheView.jsp' ";
        html += "class='btn btn-outline-secondary btn-lg px-5'>";
        html += "Inserate entdecken";
        html += "</a>";

        html += "</div>";

        html += "<p class='text-muted mt-4 small'>";
        html += "Kostenlos. Schnell. Direkt aus deiner Community.";
        html += "</p>";

        html += "</div></div></div>";

        // Suchkarte
        html += "<div class='row justify-content-center mt-5'>";
        html += "<div class='col-lg-8'>";
        html += this.getSearchCardHtml();  // bestehende Methode
        html += "</div>";
        html += "</div>";

        html += "</section>";

        return html;
    }
    
    public String getFeatureSectionHtml() {

        String html = "";

        html += "<section class='py-5 bg-light'>";
        html += "<div class='container'>";

        // Headline
        html += "<div class='text-center mb-5'>";
        html += "<h2 class='fw-bold'>Was kannst du mit FlexBoard tun?</h2>";
        html += "<p class='text-muted'>";
        html += "Alles, was du brauchst, um schnell und unkompliziert Inserate zu nutzen.";
        html += "</p>";
        html += "</div>";

        // Cards (bestehende Methode wiederverwenden!)
        html += "<div class='row g-4'>";
        html += this.getFeatureCardsHtml();
        html += "</div>";

        html += "</div>";
        html += "</section>";

        return html;
    }
    
    // Hero + Suchbereich der Startseite
    public String getSearchCardHtml() {

        String html = "";

        html += "<div class='bg-white rounded-4 shadow-lg p-4 text-center'>";

        html += "<div class='d-flex justify-content-center align-items-center mb-3'>";
        html += "<img src='../img/flexboard-logo.jpg' height='36' class='me-2'>";
        html += "<h3 class='fw-semibold m-0'>Eine Suche, viele Treffer</h3>";
        html += "</div>";

        html += "<form action='./NavbarAppl.jsp' method='get'>";
        html += "<input type='hidden' name='action' value='zurSuche'>";

        html += "<div class='input-group input-group-lg mt-3'>";
        html += "<input type='text' "
              + "class='form-control rounded-start-pill' "
              + "name='q' "
              + "value='" + (this.suchbegriff == null ? "" : this.suchbegriff) + "' "
              + "placeholder='Wonach suchst du? (z.B. Fahrrad, Laptop, Buch)'>";

        html += "<button class='btn btn-primary rounded-end-pill px-4' "
              + "type='submit'>Suchen</button>";

        html += "</div>";
        html += "</form>";

        html += "</div>";

        return html;
    }
    
    public String getFeatureCardsHtml() {
    	String[][] features = {
    		    {"bi-megaphone-fill", "Inserate erstellen",
    		     "Erstelle in wenigen Schritten ein Inserat und erreiche andere Nutzer.",
    		     "Erstellen", "NavbarAppl.jsp?action=zumInserieren"},

    		    {"bi-search", "Inserate finden",
    		     "Durchsuche Angebote nach Kategorien, Ort oder Stichworten.",
    		     "Suchen", "NavbarAppl.jsp?action=zurSuche"},

    		    {"bi-pencil-square", "Inserate verwalten",
    		     "Behalte deine eigenen Inserate im Blick und bearbeite sie jederzeit.",
    		     "Verwalten", "NavbarAppl.jsp?action=zurMeineInserate"}
    		};

        String html = "";

        for (int i = 0; i < features.length; i++) {

            html += "<div class='col-md-4'>";
            html += "<div class='card h-100 shadow-sm border-0'>";
            html += "<div class='card-body text-center'>";

            html += "<div class='mb-3'><i class='bi "
                  + features[i][0] + " feature-icon'></i></div>";

            html += "<h5 class='card-title fw-bold'>"
                  + features[i][1] + "</h5>";

            html += "<p class='card-text text-muted'>"
                  + features[i][2] + "</p>";

            html += "<a href='" + features[i][4] + "' "
            	      + "class='btn btn-outline-primary'>"
            	      + features[i][3] + "</a>";

            html += "</div></div></div>";
        }

        return html;
    }
    public String getHomepageListingsHtml() {

        String html = "";
        String sql = "SELECT l.listingid, l.title, l.city, l.details, l.catid, c.name AS category_name " +
                     "FROM listing l " +
                     "JOIN category c ON l.catid = c.id " +
                     "WHERE l.status = 'A' ";

        if (account != null && account.getLogedIn()) {
            sql += "AND l.userid <> ? ";
        }

        sql += "ORDER BY l.date DESC, RANDOM() LIMIT 6";

        try {

           
            PreparedStatement prep = this.dbConn.prepareStatement(sql);

            if (account != null && account.getLogedIn()) {
                prep.setString(1, account.getEmail());
            }

            ResultSet rs = prep.executeQuery();

            html += "<section class='py-5 bg-white'>";
            html += "<div class='container'>";
            html += "<div class='text-center mb-4'>";
            html += "<h2 class='fw-bold'>Jetzt Inserate entdecken</h2>";
            html += "<p class='text-muted'>Aktuelle & zufällige Angebote aus deiner Community</p>";
            html += "</div>";

            html += "<div id='homepageCarousel' class='carousel slide' data-bs-ride='carousel'>";
            html += "<div class='carousel-inner'>";

            int counter = 0;
            int slideCount = 0;
            boolean slideOpen = false;

            while (rs.next()) {

                if (counter % 3 == 0) {

                    if (slideOpen) {
                        html += "</div></div>";
                    }

                    html += "<div class='carousel-item " + (slideCount == 0 ? "active" : "") + "'>";
                    html += "<div class='row justify-content-center g-4'>";

                    slideCount++;
                    slideOpen = true;
                }

                int listingId = rs.getInt("listingid");
                int catid = rs.getInt("catid");
                String title = rs.getString("title");
                String category = rs.getString("category_name");
                String city = rs.getString("city");
                JSONObject detailsJson = new JSONObject(rs.getString("details"));
                String imageBase64 = detailsJson.optString("imageBase64", null);

                html += "<div class='col-md-4'>";
                html += "<div class='card h-100 shadow-sm border-0 rounded-4 home-listing-card'>";
                html += "<div class='card-body p-4 d-flex flex-column'>";
                html += "<a href='./NavbarAppl.jsp?action=zumListing&id="
                        + listingId + "' class='stretched-link'></a>";

                if (imageBase64 != null && !imageBase64.isEmpty()) {
                    html += "<img src='" + imageBase64 + "' class='card-img-top rounded-top' style='height:200px; object-fit:cover;'>";
                } else {
                    html += "<div class='mb-4 text-center fs-2'>";
                    html += "<i class='bi bi-box-seam text-primary opacity-75'></i>";
                    html += "</div>";
                }

                html += "<div class='listing-content'>";

                html += "<h5 class='fw-bold fs-4 mb-2 listing-title'>" + title + "</h5>";

                html += "<span class='badge bg-primary bg-opacity-10 text-primary fw-semibold mb-2 listing-category'>"
                        + category + "</span>";

                if (city != null && !city.isEmpty()) {
                    html += "<p class='text-muted small listing-city'>" + city + "</p>";
                }

                html += "</div>";

                html += this.getPreisBadgeHtml(catid, detailsJson);
                html += "</div></div></div>";

                counter++;
            }

            if (slideOpen) {
                html += "</div></div>";
            }

            html += "</div>";

            if (slideCount > 1) {
                html += "<button class='carousel-control-prev' type='button' data-bs-target='#homepageCarousel' data-bs-slide='prev'>";
                html += "<span class='carousel-control-prev-icon'></span></button>";

                html += "<button class='carousel-control-next' type='button' data-bs-target='#homepageCarousel' data-bs-slide='next'>";
                html += "<span class='carousel-control-next-icon'></span></button>";
            }

            html += "</div>";

            if (slideCount >= 1) {
                html += "<div class='text-center mt-3'>";
                html += "<span id='carouselPageIndicator' class='text-muted small'></span>";
                html += "</div>";
            }

            html += "</div></section>";

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }
    public String getPreisBadgeHtml(int catid, JSONObject detailsJson) {

        int price = 0;
        String priceText = "Preis auf Anfrage";
        String badgeClass = "bg-dark";   // Default = neutral

        if (detailsJson.has("dienstleistungPreis"))
            price = detailsJson.optInt("dienstleistungPreis", 0);
        else if (detailsJson.has("eventPreis"))
            price = detailsJson.optInt("eventPreis", 0);
        else if (detailsJson.has("technikPreis"))
            price = detailsJson.optInt("technikPreis", 0);
        else if (detailsJson.has("preisProStunde"))
            price = detailsJson.optInt("preisProStunde", 0);
        else if (detailsJson.has("gesamtmiete"))
            price = detailsJson.optInt("gesamtmiete", 0);
        else if (detailsJson.has("verguetung"))
            price = detailsJson.optInt("verguetung", 0);

        if (price > 0) {
            priceText = price + " €";
            badgeClass = "bg-primary";   // Blau für echte Preise
        } else {
            // Gratis-Fälle prüfen (wie vorher in ListingBean)
            if (catid == 1) {
                priceText = "Gratis";
                badgeClass = "bg-success";
            }
            else if (catid == 6 && "Kostenlos".equals(detailsJson.optString("eintritt"))) {
                priceText = "Gratis";
                badgeClass = "bg-success";
            }
        }

        return "<span class='badge " + badgeClass + " fs-6 px-3 py-2 mt-3'>"
                + priceText + "</span>";
    }
    
    
    //getter und setter
    
    public void setAccount(AccountBean account) {
        this.account = account;
    }
	public String getSuchbegriff() {
		return suchbegriff;
	}

	public void setSuchbegriff(String suchbegriff) {
		this.suchbegriff = suchbegriff;
	}
}