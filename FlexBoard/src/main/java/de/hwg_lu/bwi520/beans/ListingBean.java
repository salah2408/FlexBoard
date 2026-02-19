package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class ListingBean {

    Connection dbConn;

    public ListingBean() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();
    }

    // Hilfsmethode: neue listingid erzeugen (einfacher Übungsstil: max+1)
    public int getNextListingId() throws SQLException {
        String sql = "select max(listingid) as maxid from listing";
        PreparedStatement prep = this.dbConn.prepareStatement(sql);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
            int max = rs.getInt("maxid");
            return max + 1;
        }
        return 1;
    }

    // Anzeige speichern (Inserieren)
    public boolean saveListing(String userid, int catid, String title, String descr,
                               int price, String zip, String city) throws SQLException {

        // Mini-Validierung (prüfungsnah)
        if (userid == null || userid.trim().equals("")) return false;
        if (title == null || title.trim().equals("")) return false;
        if (catid <= 0) return false;
        if (price < 0) return false;

        try {
            int listingid = this.getNextListingId();

            String sql = "insert into listing "
                    + "(listingid, userid, catid, title, descr, price, zip, city, status, createdat, updatedat) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement prep = this.dbConn.prepareStatement(sql);
            prep.setInt(1, listingid);
            prep.setString(2, userid);      // bei euch: account.email
            prep.setInt(3, catid);
            prep.setString(4, title);
            prep.setString(5, descr);
            prep.setInt(6, price);
            prep.setString(7, zip);
            prep.setString(8, city);
            prep.setString(9, "A");         // A=active (oder D=draft)
            prep.setString(10, "");         // createdat (optional später)
            prep.setString(11, "");         // updatedat
            

            prep.executeUpdate();
            System.out.println("Listing erfolgreich eingefuegt: " + listingid);
            return true;
        } catch (Exception e) {
            System.out.println("Fehler beim Inserieren der Anzeige");
            e.printStackTrace();
            return false;
        }
    }
}
