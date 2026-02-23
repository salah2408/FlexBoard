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
    public boolean saveListing(String userid, String title, String descr, String categoryID, String city, String zip, JSONObject detailsJson) throws SQLException {
    	LocalDate date = LocalDate.now();

        try {

            String sql = "INSERT INTO listing (userid, catid, title, descr, zip, city, status, date, details) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement prep = this.dbConn.prepareStatement(sql);
            prep.setString(1, userid);
            prep.setInt(2, Integer.parseInt(categoryID));     
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
    
    
    
    
    //gethtml 
    public String getInseratDetailHtml() {

        String html = "";
        String sql = "SELECT l.title, l.descr, l.price, l.city, l.zip, l.createdat, " +
                     "c.name AS category_name, l.userid " +
                     "FROM listing l " +
                     "JOIN category c ON l.catid = c.id " +
                     "WHERE l.listingid = ?";

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
                int price = rs.getInt("price");
                String userid = rs.getString("userid");
                this.anbieterEmail = userid;

                html += "<div class='container py-5'>";
                html += "<div class='row justify-content-center'>";
                html += "<div class='col-lg-8'>";

                html += "<div class='card shadow-sm border-0 rounded-4'>";
                html += "<div class='card-body p-5'>";
                
                html += "<a href='./NavbarAppl.jsp?action=zurHomepage' "
                	      + "class='btn btn-outline-secondary btn-sm mb-4'>"
                	      + "<i class='bi bi-arrow-left'></i> Zurück"
                	      + "</a>";

                html += "<h2 class='fw-bold mb-3'>" + title + "</h2>";

                html += "<span class='badge bg-primary bg-opacity-10 text-primary mb-3'>"
                      + category + "</span>";

                html += "<p class='text-muted mb-2'>";
                html += (zip != null ? zip : "") + " " + (city != null ? city : "");
                html += "</p>";

                html += "<hr class='my-4'>";

                html += "<p class='mb-4'>" + descr + "</p>";

                if (price > 0) {
                    html += "<h4 class='fw-bold text-primary'>" + price + " €</h4>";
                } else {
                    html += "<h4 class='fw-bold text-secondary'>Preis auf Anfrage</h4>";
                }

                html += "<p class='text-muted mt-3'>Anbieter: " + userid + "</p>";

                html += "</div></div></div></div></div>";

            } else {
                html = "<div class='container py-5 text-center text-muted'>"
                     + "Inserat nicht gefunden."
                     + "</div>";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return html;
    }
    
    public String getKontaktButtonHtml() {

        if (account == null) return "";

        String html = "";

        if (!account.getLogedIn()) {

            html += "<div class='text-center mt-4'>";
            html += "<a href='./NavbarAppl.jsp?action=zumLogin' "
                  + "class='btn btn-primary'>"
                  + "Zum Login, um Kontakt aufzunehmen"
                  + "</a></div>";

        } else if (this.anbieterEmail != null 
                && !account.getEmail().equals(this.anbieterEmail)) {

            html += "<div class='text-center mt-4'>";
            html += "<a href='./NachrichtenAppl.jsp?action=switch&user="
                  + this.anbieterEmail
                  + "' class='btn btn-success'>"
                  + "<i class='bi bi-chat-dots'></i> Nachricht senden"
                  + "</a></div>";

        } else {

            html += "<div class='text-center mt-4 text-muted'>";
            html += "Das ist dein eigenes Inserat.";
            html += "</div>";
        }

        return html;
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
