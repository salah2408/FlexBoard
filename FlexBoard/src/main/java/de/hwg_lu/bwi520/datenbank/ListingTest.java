package de.hwg_lu.bwi520.datenbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class ListingTest {

    Connection dbConn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ListingTest t = new ListingTest();
        t.doSomething();
    }

    public void doSomething() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();

     
        this.createTableListing();
//        this.createTableListingImage();
//        this.createTableFavorite();
//        this.createTableMessage();

        System.out.println("Alle Tabellen fuer Inserieren/Chat/Favoriten sollten jetzt existieren.");
    }

    
    public void createTableListing() throws SQLException {
        String sql = "CREATE TABLE listing ("
            + "    listingid SERIAL PRIMARY KEY,"
            + "    userid VARCHAR(50) NOT NULL,"
            + "    catid INT NOT NULL,"
            + "    title VARCHAR(150) NOT NULL,"
            + "    descr VARCHAR(3000) NOT NULL,"
            + "    zip VARCHAR(20) NOT NULL,"
            + "    city VARCHAR(100) NOT NULL,"
            + "    status VARCHAR(3) NOT NULL,"
            + "    date DATE NOT NULL,"
            + "    details TEXT NOT NULL,"
            + "    FOREIGN KEY (userid) REFERENCES account(email),"
            + "    FOREIGN KEY (catid) REFERENCES category(id)"
            + ")";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Tabelle listing existiert jetzt (FK korrekt gesetzt)");
    }


    // 3) Bilder
    public void createTableListingImage() throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS listing_image(" +
            "imageid INT PRIMARY KEY, " +
            "listingid INT NOT NULL, " +
            "imgurl VARCHAR(500) NOT NULL, " +
            "sortno INT NOT NULL DEFAULT 0, " +
            "CONSTRAINT fk_img_listing FOREIGN KEY(listingid) REFERENCES listing(listingid)" +
            ");";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Tabelle listing_image müsste jetzt existieren");
    }

    // 4) Favoriten (Merkliste) - n:m
    // Composite PK verhindert doppelte Favoriten
    public void createTableFavorite() throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS favorite(" +
            "userid VARCHAR(64) NOT NULL, " +              // account.email
            "listingid INT NOT NULL, " +
            "createdat VARCHAR(25) NOT NULL DEFAULT '', " +
            "CONSTRAINT pk_favorite PRIMARY KEY(userid, listingid), " +
            "CONSTRAINT fk_fav_user FOREIGN KEY(userid) REFERENCES account(email), " +
            "CONSTRAINT fk_fav_listing FOREIGN KEY(listingid) REFERENCES listing(listingid)" +
            ");";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Tabelle favorite müsste jetzt existieren");
    }

    // 5) Nachrichten
    public void createTableMessage() throws SQLException {
        String sql =
            "CREATE TABLE IF NOT EXISTS message(" +
            "msgid INT PRIMARY KEY, " +
            "listingid INT NOT NULL, " +
            "from_userid VARCHAR(64) NOT NULL, " +          // account.email
            "to_userid VARCHAR(64) NOT NULL, " +            // account.email
            "text VARCHAR(2000) NOT NULL DEFAULT '', " +
            "sentat VARCHAR(25) NOT NULL DEFAULT '', " +
            "readflag VARCHAR(3) NOT NULL DEFAULT 'no', " +
            "CONSTRAINT fk_msg_listing FOREIGN KEY(listingid) REFERENCES listing(listingid), " +
            "CONSTRAINT fk_msg_from FOREIGN KEY(from_userid) REFERENCES account(email), " +
            "CONSTRAINT fk_msg_to FOREIGN KEY(to_userid) REFERENCES account(email)" +
            ");";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Tabelle message müsste jetzt existieren");
    }
}
