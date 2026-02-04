package de.hwg_lu.bwi520.datenbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class ChatLogTest {

    Connection dbConn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        ChatLogTest myObj = new ChatLogTest();
        myObj.doSomething();
    }

    public void doSomething() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();
        this.createTableChatLog();
        this.showAllChatLogs();
//      this.deleteAllChatLogs();
//      this.insertTestChatLogs();
    }

    public void createTableChatLog() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS chatlog ("
                + "id SERIAL PRIMARY KEY, "
                + "sender_email VARCHAR(64) NOT NULL, "
                + "empfaenger_email VARCHAR(64) NOT NULL, "
                + "datum DATE NOT NULL, "
                + "uhrzeit TIME NOT NULL, "
                + "nachricht TEXT NOT NULL, "
                + "CONSTRAINT fk_sender FOREIGN KEY (sender_email) REFERENCES account(email) ON DELETE CASCADE, "
                + "CONSTRAINT fk_empfaenger FOREIGN KEY (empfaenger_email) REFERENCES account(email) ON DELETE CASCADE"
                + ");";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();

        System.out.println("Tabelle ChatLog müsste jetzt existieren");
    }

    public void showAllChatLogs() throws SQLException {
        String sql = "SELECT sender_email, empfaenger_email, datum, uhrzeit, nachricht FROM chatlog";
        PreparedStatement prep = dbConn.prepareStatement(sql);

        ResultSet dbRes = prep.executeQuery();
        while (dbRes.next()) {
            String sender = dbRes.getString("sender_email");
            String empfaenger = dbRes.getString("empfaenger_email");
            String datum = dbRes.getDate("datum").toString();
            String uhrzeit = dbRes.getTime("uhrzeit").toString();
            String nachricht = dbRes.getString("nachricht");

            System.out.println(sender + " -> " + empfaenger + " || " + datum + " " + uhrzeit + " || " + nachricht);
        }
    }

    public void deleteAllChatLogs() throws SQLException {
        String sql = "DELETE FROM chatlog";
        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();

        System.out.println("Alle ChatLogs müssten geloescht sein");
    }
    
    public void insertTestChatLogs() throws SQLException {
        String sql = "INSERT INTO chatlog "
            + "(sender_email, empfaenger_email, datum, uhrzeit, nachricht) VALUES "
            + "('salah@hwg-lu.de', 'akin@hwg-lu.de',  '2025-09-30', '10:00:00', 'Hallo, ist der Artikel noch verfügbar?'),"
            + "('akin@hwg-lu.de',  'salah@hwg-lu.de', '2025-09-30', '10:02:00', 'Ja, ist noch da.'),"
            + "('salah@hwg-lu.de', 'akin@hwg-lu.de',  '2025-09-30', '10:03:00', 'Was ist dein letzter Preis?'),"
            + "('akin@hwg-lu.de',  'salah@hwg-lu.de', '2025-09-30', '10:05:00', 'Ich dachte an 120€.'),"
            + "('salah@hwg-lu.de', 'akin@hwg-lu.de',  '2025-09-30', '10:06:00', 'Würdest du 100€ akzeptieren?'),"

            + "('plame@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-01', '09:10:00', 'Hi, ich hätte auch Interesse am Artikel.'),"
            + "('akin@hwg-lu.de',  'plame@hwg-lu.de', '2025-10-01', '09:12:00', 'Alles klar, aktuell ist noch nichts fest.'),"
            + "('plame@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-01', '09:15:00', 'Mein Angebot wären 110€.'),"

            + "('akin@hwg-lu.de',  'salah@hwg-lu.de', '2025-10-01', '12:00:00', 'Ich kann auf 110€ runtergehen.'),"
            + "('salah@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-01', '12:02:00', 'Deal, ich nehme ihn 👍');";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();

        System.out.println("Test-Chatlogs für Verhandlung wurden eingefügt");
    }
}