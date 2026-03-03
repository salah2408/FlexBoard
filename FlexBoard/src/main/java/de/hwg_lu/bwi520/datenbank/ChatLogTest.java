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
//      this.insertTestChatLogs2();
//      this.insertTestChatLogs3();
    }

    public void createTableChatLog() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS chatlog ("
                + "id SERIAL PRIMARY KEY, "
                + "listingid INT NOT NULL, "
                + "sender_email VARCHAR(64) NOT NULL, "
                + "empfaenger_email VARCHAR(64) NOT NULL, "
                + "datum DATE NOT NULL, "
                + "uhrzeit TIME NOT NULL, "
                + "nachricht TEXT NOT NULL, "
                + "CONSTRAINT fk_listingid FOREIGN KEY (listingid) REFERENCES listing(listingid) ON DELETE CASCADE, "
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
    public void insertTestChatLogs2() throws SQLException {
				String sql = "INSERT INTO chatlog " + "(sender_email, empfaenger_email, datum, uhrzeit, nachricht) VALUES "
				// Neue Konversation: Kaan verkauft Fahrrad an Tarik
				+ "('tarik@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-04', '16:10:00', 'Hallo, ist das Fahrrad noch da?'),"
				+ "('kaan@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-04', '16:12:00', 'Ja, ist noch verfügbar.'),"
				+ "('tarik@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-04', '16:13:00', 'Was letzter Preis?'),"
				+ "('kaan@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-04', '16:15:00', 'VB 150€, ich könnte auf 140€ gehen.'),"
				+ "('tarik@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-04', '16:18:00', '120€ und ich hole heute ab.'),"
				+ "('kaan@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-04', '16:20:00', '130€ und es ist deins.'),"
				+ "('tarik@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-04', '16:21:00', 'Okay 130€, schick Adresse.'),"

				// Neue Konversation: Enes verkauft Laptop an Kaan
				+ "('kaan@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-05', '11:05:00', 'Hi, ist der Laptop noch zu haben?'),"
				+ "('enes@hwg-lu.de',  'kaan@hwg-lu.de',  '2025-10-05', '11:07:00', 'Ja, ist noch da.'),"
				+ "('kaan@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-05', '11:08:00', 'Wie alt ist der?'),"
				+ "('enes@hwg-lu.de',  'kaan@hwg-lu.de',  '2025-10-05', '11:10:00', 'Ca. 2 Jahre, läuft einwandfrei.'),"
				+ "('kaan@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-05', '11:12:00', '350€ wäre mein Angebot.'),"
				+ "('enes@hwg-lu.de',  'kaan@hwg-lu.de',  '2025-10-05', '11:14:00', '400€ wäre fair.'),"
				+ "('kaan@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-05', '11:16:00', 'Okay, ich überlege es mir.'),"

				// Neue Konversation: Tarik verkauft PS5 an Enes
				+ "('enes@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-06', '19:00:00', 'Ist die PS5 noch verfügbar?'),"
				+ "('tarik@hwg-lu.de', 'enes@hwg-lu.de',  '2025-10-06', '19:02:00', 'Ja, solange die Anzeige online ist.'),"
				+ "('enes@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-06', '19:03:00', 'Mit Controller und Spielen?'),"
				+ "('tarik@hwg-lu.de', 'enes@hwg-lu.de',  '2025-10-06', '19:05:00', '1 Controller dabei, Spiele extra.'),"
				+ "('enes@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-06', '19:06:00', '400€ komplett?'),"
				+ "('tarik@hwg-lu.de', 'enes@hwg-lu.de',  '2025-10-06', '19:08:00', '450€ komplett.'),"
				+ "('enes@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-06', '19:10:00', 'Ich melde mich.')";
				
				PreparedStatement prep = dbConn.prepareStatement(sql);
				prep.executeUpdate();

				System.out.println("Test-Chatlogs wurden erfolgreich eingefügt");
	}
    
    public void insertTestChatLogs3() throws SQLException {

        String sql = "INSERT INTO chatlog "
            + "(sender_email, empfaenger_email, datum, uhrzeit, nachricht) VALUES "

            // Kaan ↔ Salah
            + "('kaan@hwg-lu.de',  'salah@hwg-lu.de', '2025-10-07', '10:00:00', 'Hallo, ist die Jacke noch da?'),"
            + "('salah@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-07', '10:02:00', 'Ja, noch verfügbar.'),"
            + "('kaan@hwg-lu.de',  'salah@hwg-lu.de', '2025-10-07', '10:03:00', '80€ würde ich geben.'),"
            + "('salah@hwg-lu.de', 'kaan@hwg-lu.de',  '2025-10-07', '10:05:00', '90€ und sie gehört dir.'),"

            // Tarik ↔ Akin
            + "('tarik@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-07', '12:15:00', 'Ist das iPhone noch zu haben?'),"
            + "('akin@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-07', '12:17:00', 'Ja, 256GB Version.'),"
            + "('tarik@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-07', '12:18:00', 'Was letzter Preis?'),"
            + "('akin@hwg-lu.de',  'tarik@hwg-lu.de', '2025-10-07', '12:20:00', 'Kein letzter Preis, bitte realistisch bleiben.'),"
            + "('tarik@hwg-lu.de', 'akin@hwg-lu.de',  '2025-10-07', '12:22:00', 'Okay 500€?'),"

            // Enes ↔ Plame
            + "('enes@hwg-lu.de',  'plame@hwg-lu.de', '2025-10-08', '09:30:00', 'Hi, ist der Schreibtisch noch verfügbar?'),"
            + "('plame@hwg-lu.de', 'enes@hwg-lu.de',  '2025-10-08', '09:32:00', 'Ja, nur Abholung.'),"
            + "('enes@hwg-lu.de',  'plame@hwg-lu.de', '2025-10-08', '09:33:00', 'Passt, wann kann ich kommen?'),"
            + "('plame@hwg-lu.de', 'enes@hwg-lu.de',  '2025-10-08', '09:35:00', 'Heute ab 17 Uhr möglich.'),"

            // Kaan ↔ Akin
            + "('kaan@hwg-lu.de',  'akin@hwg-lu.de',  '2025-10-08', '14:10:00', 'Noch da?'),"
            + "('akin@hwg-lu.de',  'kaan@hwg-lu.de',  '2025-10-08', '14:12:00', 'Ja.'),"
            + "('kaan@hwg-lu.de',  'akin@hwg-lu.de',  '2025-10-08', '14:13:00', 'Komme jetzt vorbei.'),"

            // Tarik ↔ Salah
            + "('tarik@hwg-lu.de', 'salah@hwg-lu.de', '2025-10-09', '18:40:00', 'Versand möglich?'),"
            + "('salah@hwg-lu.de', 'tarik@hwg-lu.de', '2025-10-09', '18:42:00', 'Nur gegen Aufpreis.'),"
            + "('tarik@hwg-lu.de', 'salah@hwg-lu.de', '2025-10-09', '18:43:00', 'Mit PayPal Freunde?'),"
            + "('salah@hwg-lu.de', 'tarik@hwg-lu.de', '2025-10-09', '18:45:00', 'Nur Überweisung.'),"

            // Enes ↔ Akin
            + "('enes@hwg-lu.de',  'akin@hwg-lu.de',  '2025-10-10', '20:00:00', 'Noch verfügbar?'),"
            + "('akin@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-10', '20:01:00', 'Solange online, ja.'),"
            + "('enes@hwg-lu.de',  'akin@hwg-lu.de',  '2025-10-10', '20:02:00', 'Reservierst du bis morgen?'),"
            + "('akin@hwg-lu.de',  'enes@hwg-lu.de',  '2025-10-10', '20:04:00', 'Nein, wer zuerst kommt.');";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();

        System.out.println("Gemischte Test-Chatlogs wurden eingefügt");
    }

}