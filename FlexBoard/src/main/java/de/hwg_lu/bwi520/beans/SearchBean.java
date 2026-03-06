package de.hwg_lu.bwi520.beans;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Category;

public class SearchBean {
	
	Connection dbConn;
	AccountBean account;
    String suchbegriff;
    String kategorie;
    String plzOrt;
    Integer minPreis;
    Integer maxPreis;
    
    List<SearchResult> ergebnisse;
    
    CategoryBean categoryBean;
    
    public SearchBean() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();
        this.ergebnisse = new ArrayList<>();
        this.categoryBean = new CategoryBean();
    }
    
    
    public void suche() {
        this.ergebnisse.clear();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.listingid, l.userid, l.catid, l.title, l.descr, ");
        sql.append("l.city, l.zip, l.date, l.details, c.name as catname ");
        sql.append("FROM listing l ");
        sql.append("JOIN category c ON l.catid = c.id ");
        sql.append("WHERE l.status = 'A' ");
        if (account != null && account.getLogedIn()) {
            sql.append("AND l.userid <> ? ");
        }
        List<Object> params = new ArrayList<>();
        // Suchbegriff-Filter
        if (suchbegriff != null && !suchbegriff.trim().isEmpty()) {
            sql.append("AND (LOWER(l.title) LIKE ? OR LOWER(l.descr) LIKE ?) ");
            String searchPattern = "%" + suchbegriff.toLowerCase() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        // Kategorie-Filter
        if (kategorie != null && !kategorie.isEmpty() && !kategorie.equals("Kategorie wählen")) {
            sql.append("AND c.name = ? ");
            params.add(kategorie);
        }
        
        // PLZ/Ort-Filter
        if (plzOrt != null && !plzOrt.trim().isEmpty()) {
            sql.append("AND (l.zip LIKE ? OR LOWER(l.city) LIKE ?) ");
            String ortPattern = "%" + plzOrt.toLowerCase() + "%";
            params.add(plzOrt + "%");
            params.add(ortPattern);
        }
        
        sql.append("ORDER BY l.date DESC, l.listingid DESC");
        
        try {
            PreparedStatement prep = this.dbConn.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (account != null && account.getLogedIn()) {
                prep.setString(paramIndex++, account.getEmail());
            }
            // Parameter setzen
            for (int i = 0; i < params.size(); i++) {
                prep.setObject(paramIndex++, params.get(i));
            }
            
            ResultSet rs = prep.executeQuery();
            
            while (rs.next()) {
                int listingid = rs.getInt("listingid");
                String userid = rs.getString("userid");
                int catid = rs.getInt("catid");
                String title = rs.getString("title");
                String descr = rs.getString("descr");
                String city = rs.getString("city");
                String zip = rs.getString("zip");
                java.sql.Date date = rs.getDate("date");
                String catname = rs.getString("catname");
                
                String detailsStr = rs.getString("details");
                JSONObject details = new JSONObject(detailsStr);
                
                // Preis extrahieren
                Integer preis = this.extractPreis(details);
                
                // Preis-Filter anwenden
                if (minPreis != null && preis != null && preis < minPreis) {
                    continue;
                }
                if (maxPreis != null && preis != null && preis > maxPreis) {
                    continue;
                }
                
                // Anbieter-Filter (hier vereinfacht - kann erweitert werden)
                // Wenn du ein Feld "anbieterTyp" in der DB hast, nutze das
                
                SearchResult result = new SearchResult(
                    listingid, userid, catid, catname, title, descr, 
                    city, zip, date, preis, details
                );
                
                this.ergebnisse.add(result);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // =============================
    // PREIS EXTRAHIEREN
    // =============================
    private Integer extractPreis(JSONObject details) {
        if (details.has("dienstleistungPreis")) {
            return details.optInt("dienstleistungPreis", 0);
        } else if (details.has("eventPreis")) {
            return details.optInt("eventPreis", 0);
        } else if (details.has("technikPreis")) {
            return details.optInt("technikPreis", 0);
        } else if (details.has("preisProStunde")) {
            return details.optInt("preisProStunde", 0);
        } else if (details.has("gesamtmiete")) { 
            return details.optInt("gesamtmiete", 0);
        } else if (details.has("sonstigesPreis")) {
        	return details.optInt("sonstigesPreis", 0);
        }
        return null;
    }
    
    // =============================
    // HTML GENERIEREN
    // =============================
    

    public String getSuchleisteHtml() {
    	String html = "";
    	html += "<div class='container-fluid search-bar'>";
    	html += "<div class='container'>";
    	html += "<div class='row g-2 align-items-center'>";

    	html += "<div class='col-md-4'>";
    	html += "<input type='text' class='form-control' name='q' value='" + this.getSuchbegriff() + "' placeholder='Was suchst du?'>";
    	html += "</div>";

    	html += "<div class='col-md-3'>";
    	html += "<select class='form-select' name='kategorie'>";
    	html += "<option value=''>Alle Kategorien</option>";
    	html += this.getKategorienHtml();
    	html += "</select>";
    	html += "</div>";

    	html += "<div class='col-md-3'>";
    	html += "<input type='text' class='form-control' name='plzOrt' value='" + this.getPlzOrt() + "' placeholder='PLZ oder Ort'>";
    	html += "</div>";

    	html += "<div class='col-md-2 d-grid'>";
    	html += "<button class='btn btn-light' type='submit' name='action' value='Finden'>";
    	html += "<i class='bi bi-search'></i> Finden";
    	html += "</button>";
    	html += "</div>";

    	html += "</div>";
    	html += "</div>";
    	html += "</div>";
    	
    	return html;
    }
    
   public String getSidebarHtml() throws UnsupportedEncodingException {
	   String html = "";
	   html += "<div class='col-md-3 col-lg-2 sidebar'>";

	   html += "<div class='d-flex justify-content-between align-items-center mb-3'>";
	   html += "<h5 class='mb-0'>Filter</h5>";
	   html += "<a href='./SucheAppl.jsp?action=reset' class='btn btn-sm btn-outline-secondary'>";
	   html += "Zurücksetzen";
	   html += "</a>";
	   html += "</div>";

	   html += this.getKategorienSidebarHtml();

	   html += "<h6>Preis</h6>";

	   html += "<div class='mb-2'>";
	   html += "<input id='minPreis' type='number' class='form-control mb-2' name='minPreis' placeholder='Min €' min='0' value='" + (this.getMinPreis() != null ? this.getMinPreis() : "") + "' onchange='checkMinMaxPreis()'>";
	   html += "<input id='maxPreis' type='number' class='form-control mb-2' name='maxPreis' placeholder='Max €' min='0' value='" + (this.getMaxPreis() != null ? this.getMaxPreis() : "") + "' onchange='checkMinMaxPreis()'>";

	   html += "<button type='submit' name='action' value='Finden' class='btn btn-sm btn-primary w-100'>";
	   html += "Anwenden";
	   html += "</button>";
	   html += "</div>";

	   html += "</div>";
	   return html;
   }
   
   public String getErgebnisseHtml() {
	   String html = "";
	   html += "<div class='col-md-9 col-lg-10 p-4'>";

	   html += "<div class='d-flex justify-content-between align-items-center mb-4'>";

	   // Singular/Plural für "Ergebnis(se)" direkt im String zusammenbauen
	   html += "<h4>" + this.getAnzahlErgebnisse() + " " + (this.getAnzahlErgebnisse() == 1 ? "Ergebnis" : "Ergebnisse") + "</h4>";

	   // Prüfen, ob Suchbegriff oder Kategorie ausgefüllt sind
	   if (!this.getSuchbegriff().isEmpty() || !this.getKategorie().isEmpty()) {
	       html += "<span class='text-muted'>";
	       
	       if (!this.getSuchbegriff().isEmpty()) {
	           html += " für &quot;<strong>" + this.getSuchbegriff() + "</strong>&quot;";
	       }
	       
	       if (!this.getKategorie().isEmpty()) {
	           html += " in <strong>" + this.getKategorie() + "</strong>";
	       }
	       
	       html += "</span>";
	   }

	   html += "</div>";

	   // Suchergebnisse HTML einfügen (den HTML-Kommentar habe ich hier weggelassen)
	   html += this.getSuchergebnisseHtml();

	   html += "</div>";
	   return html;
   }
    
   public String getSuchergebnisseHtml() {
	    String html = "";
	    
	    if (ergebnisse.isEmpty()) {
	        html += "<div class='text-center py-5'>";
	        html += "<i class='bi bi-search' style='font-size: 4rem; color: #ccc;'></i>";
	        html += "<h4 class='mt-3 text-muted'>Keine Ergebnisse gefunden</h4>";
	        html += "<p class='text-muted'>Versuche andere Suchbegriffe oder Filter</p>";
	        html += "</div>";
	        return html;
	    }
	    
	    html += "<div class='row g-3'>";
	    
	    for (SearchResult result : ergebnisse) {
	        html += "<div class='col-12'>";
	        
	        html += "<div class='card h-100 shadow-sm border-0 hover-card position-relative'>";
	        html += "<div class='card-body'>";
	        html += "<div class='row'>";
	        
	        // Bild ()
	        html += "<div class='col-md-3'>";

	        String imageBase64 = result.details.optString("imageBase64", null);

	        if (imageBase64 != null && !imageBase64.isEmpty()) {
	            html += "<img src='" + imageBase64 + "' class='img-fluid rounded listing-preview-img'>";
	        } else {
	            html += "<img src='../img/flexboard-logo.jpg' class='img-fluid rounded'>";
	        }

	        html += "</div>";
	        
	        // Inhalt
	        html += "<div class='col-md-9'>";
	        
	        html += "<a href='./NavbarAppl.jsp?action=zumListing&id=" + result.listingid + "' class='stretched-link'></a>";
	        
	        // Titel und Badge
	        html += "<h5 class='card-title fw-bold'>" + result.title + "</h5>";
	        html += "<span class='badge bg-primary mb-2'>" + result.catname + "</span>";
	        
	        if (result.preis != null && result.preis > 0) {
	            // Preis pro Stunde für Nachhilfe (catid == 2)
	            if (result.catid == 2 && result.details.has("preisProStunde")) {
	                html += "<p class='text-primary fw-bold fs-5'>" + result.preis + " €/h</p>";
	            } 
	            // Miete für WG & Wohnen (catid == 3)
	            else if (result.catid == 3 && result.details.has("gesamtmiete")) {
	                html += "<p class='text-primary fw-bold fs-5'>" + result.preis + " € Miete</p>";
	            } 
	            // Standard Preis
	            else {
	                html += "<p class='text-primary fw-bold fs-5'>" + result.preis + " €</p>";
	            }
	        } else {
	            // Kategorie-spezifische Texte
	            if (result.catid == 1) {
	                html += "<p class='text-muted'>Gratis</p>";
	            } else if (result.catid == 6 && "Kostenlos".equals(result.details.optString("eintritt"))) {
	            	html += "<p class='text-muted'>Gratis</p>";
	            } else if (result.catid == 7) {
	                html += "<p class='text-success fw-bold'>Tausch</p>";
	            } else if (result.catid == 9 && "Gratis".equals(result.details.optString("sonstigesTyp"))){
	            	html += "<p class='text-success fw-bold'>Kostenlos</p>";
	            }  
	            else {
	                html += "<p class='text-muted'>Preis auf Anfrage</p>";
	            }
	        }
	        
	        String shortDescr = result.descr;
	        if (shortDescr != null && shortDescr.length() > 150) {
	            shortDescr = shortDescr.substring(0, 150) + "...";
	        }
	        html += "<p class='card-text text-muted'>" + (shortDescr != null ? shortDescr : "") + "</p>";
	        
	        html += "<p class='text-muted small mb-2'>";
	        html += "<i class='bi bi-geo-alt'></i> " + result.zip + " " + result.city;
	        html += " &nbsp; | &nbsp; ";
	        html += "<i class='bi bi-calendar'></i> " + result.date;
	        html += "</p>";
	        
	        html += "<span class='btn btn-outline-primary btn-sm position-relative' style='z-index: 2;'>";
	        html += "Details ansehen <i class='bi bi-arrow-right'></i>";
	        html += "</span>";
	        
	        html += "</div>"; 
	        html += "</div>"; 
	        html += "</div>"; 
	        html += "</div>"; 
	        html += "</div>"; 
	    }
	    
	    html += "</div>";
	    
	    return html;
	}
    
    // Methode um die Kategorien als Dropdown für die option select zu bekommen
    public String getKategorienHtml() {
    	String html = "";
    	 for (Category cat : this.categoryBean.getAllCategories()) {
             String selected = cat.getName().equals(this.getKategorie()) ? "selected" : "";

             html += "<option value='" + cat.getName() + "'" + selected + ">"  + cat.getName() + "</option>";
     
         }
    
    	
    	return html;
    }
    
    public String getKategorienSidebarHtml() throws UnsupportedEncodingException {
    	String html = "";
    	html += "<h6>Kategorien</h6>";

    	String[] icons = {
    	    "bi-book",           // Lernmaterial
    	    "bi-people",         // Nachhilfe
    	    "bi-house",          // Wohnen
    	    "bi-briefcase",      // Jobs
    	    "bi-laptop",         // Technik
    	    "bi-calendar-event", // Events
    	    "bi-arrow-left-right", // Tauschen
    	    "bi-tools"           // Dienstleistungen
    	};

    	int iconIndex = 0;
    	for (Category cat : this.categoryBean.getAllCategories()) {
    	    // Welches Icon und ob die Kategorie aktiv ist, ermitteln
    	    String icon = iconIndex < icons.length ? icons[iconIndex] : "bi-circle";
    	    String activeClass = cat.getName().equals(this.getKategorie()) ? "active" : "";
    	    
    	    // Den Kategorienamen für die URL sicher codieren (z.B. aus Leerzeichen ein %20 machen)
    	    String encodedName = java.net.URLEncoder.encode(cat.getName(), "UTF-8");

    	    // HTML zusammensetzen
    	    html += "<div class='filter-item'>";
    	    html += "<a class='sidebar-link " + activeClass + "' href='./SucheAppl.jsp?action=filterKategorie&kategorie=" + encodedName + "'>";
    	    html += "<i class='bi " + icon + " me-2'></i>" + cat.getName();
    	    html += "</a>";
    	    html += "</div>";

    	    iconIndex++;
    	}
    	return html;
    }
    
    public int getAnzahlErgebnisse() {
        return ergebnisse.size();
    }
    
    // =============================
    // GETTER & SETTER
    // =============================
    
    public void setSuchbegriff(String suchbegriff) {
        this.suchbegriff = suchbegriff;
    }
    
    public String getSuchbegriff() {
        return suchbegriff != null ? suchbegriff : "";
    }
    
    public void setKategorie(String kategorie) {
        this.kategorie = kategorie;
    }
    
    public String getKategorie() {
        return kategorie != null ? kategorie : "";
    }
    
    public void setPlzOrt(String plzOrt) {
        this.plzOrt = plzOrt;
    }
    
    public String getPlzOrt() {
        return plzOrt != null ? plzOrt : "";
    }
    
    public void setMinPreis(Integer minPreis) {
        this.minPreis = minPreis;
    }
    
    public Integer getMinPreis() {
        return minPreis;
    }
    
    public void setMaxPreis(Integer maxPreis) {
        this.maxPreis = maxPreis;
    }
    
    public Integer getMaxPreis() {
        return maxPreis;
    }
    
    public AccountBean getAccount() {
		return account;
	}
	public void setAccount(AccountBean account) {
		this.account = account;
	}
    
    
    // =============================
    // INNER CLASS: SearchResult
    // =============================
    public static class SearchResult {
        int listingid;
        String userid;
        int catid;
        String catname;
        String title;
        String descr;
        String city;
        String zip;
        java.sql.Date date;
        Integer preis;
        JSONObject details;
        
        public SearchResult(int listingid, String userid, int catid, String catname,
                          String title, String descr, String city, String zip,
                          java.sql.Date date, Integer preis, JSONObject details) {
            this.listingid = listingid;
            this.userid = userid;
            this.catid = catid;
            this.catname = catname;
            this.title = title;
            this.descr = descr;
            this.city = city;
            this.zip = zip;
            this.date = date;
            this.preis = preis;
            this.details = details;
        }
    }
}