package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class SearchBean {

    Connection dbConn;
    
    // Filter-Parameter
    private String suchbegriff;
    private String kategorie;
    private String plzOrt;
    private Integer minPreis;
    private Integer maxPreis;
    private String anbieterTyp; // "Privat" oder "Gewerblich"
    
    // Suchergebnisse
    private List<SearchResult> ergebnisse;
    
    public SearchBean() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();
        this.ergebnisse = new ArrayList<>();
    }
    
    // =============================
    // HAUPT-SUCHMETHODE
    // =============================
    public void suche() {
        this.ergebnisse.clear();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT l.listingid, l.userid, l.catid, l.title, l.descr, ");
        sql.append("l.city, l.zip, l.date, l.details, c.name as catname ");
        sql.append("FROM listing l ");
        sql.append("JOIN category c ON l.catid = c.id ");
        sql.append("WHERE l.status = 'A' ");
        
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
            
            // Parameter setzen
            for (int i = 0; i < params.size(); i++) {
                prep.setObject(i + 1, params.get(i));
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
        } else if (details.has("gesamtmiete")) {  // ✅ FÜR WG & WOHNEN
            return details.optInt("gesamtmiete", 0);
        }
        return null;
    }
    
    // =============================
    // HTML GENERIEREN
    // =============================
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
            html += "<div class='card h-100 shadow-sm border-0 hover-card'>";
            html += "<div class='card-body'>";
            html += "<div class='row'>";
            
            // Bild (Platzhalter)
            html += "<div class='col-md-3'>";
            html += "<img src='../img/flexboard-logo.jpg' class='img-fluid rounded' alt='Bild'>";
            html += "</div>";
            
            // Inhalt
            html += "<div class='col-md-9'>";
            html += "<h5 class='card-title fw-bold'>" + result.title + "</h5>";
            html += "<span class='badge bg-primary mb-2'>" + result.catname + "</span>";
            
            // ✅ PREIS ANZEIGEN - verbessert
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
                if (result.catid == 2) {
                    html += "<p class='text-muted'>Preis auf Anfrage</p>";
                } else if (result.catid == 7) {
                    html += "<p class='text-success fw-bold'>Kostenlos / Tausch</p>";
                } else {
                    html += "<p class='text-muted'>Preis auf Anfrage</p>";
                }
            }
            
            // Beschreibung (gekürzt)
            String shortDescr = result.descr;
            if (shortDescr != null && shortDescr.length() > 150) {
                shortDescr = shortDescr.substring(0, 150) + "...";
            }
            html += "<p class='card-text text-muted'>" + (shortDescr != null ? shortDescr : "") + "</p>";
            
            // Standort und Datum
            html += "<p class='text-muted small mb-2'>";
            html += "<i class='bi bi-geo-alt'></i> " + result.zip + " " + result.city;
            html += " &nbsp; | &nbsp; ";
            html += "<i class='bi bi-calendar'></i> " + result.date;
            html += "</p>";
            
            // Details-Button
            html += "<a href='./NavbarAppl.jsp?action=zumListing&id=" + result.listingid + "' ";
            html += "class='btn btn-outline-primary btn-sm'>";
            html += "Details ansehen <i class='bi bi-arrow-right'></i>";
            html += "</a>";
            
            html += "</div>"; // col-md-9
            html += "</div>"; // row
            html += "</div>"; // card-body
            html += "</div>"; // card
            html += "</div>"; // col-12
        }
        
        html += "</div>"; // row
        
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
    
    public void setAnbieterTyp(String anbieterTyp) {
        this.anbieterTyp = anbieterTyp;
    }
    
    public String getAnbieterTyp() {
        return anbieterTyp;
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