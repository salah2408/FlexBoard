package de.hwg_lu.bwi520.datenbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class CategoryTest {

    Connection dbConn;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        CategoryTest myObj = new CategoryTest();
        myObj.doSomething();
    }

    public void doSomething() throws ClassNotFoundException, SQLException {
        this.dbConn = new PostgreSQLAccess().getConnection();
        this.createTableCategory();
        this.insertDefaultCategories();
        this.showAllCategories();
        // this.deleteAllCategories();
    }

    public void createTableCategory() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS category ("
                + "id BIGSERIAL PRIMARY KEY, "
                + "name VARCHAR(100) NOT NULL UNIQUE, "
                + "description TEXT"
                + ");";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Tabelle Category müsste jetzt existieren");
    }

    public void insertDefaultCategories() throws SQLException {
        String sql = "INSERT INTO category (name, description) VALUES "
                + "('Lernmaterial & Skripte', 'Studienbezogene Unterlagen wie Skripte, Bücher oder Zusammenfassungen'), "
                + "('Nachhilfe & Lerngruppen', 'Unterstützung beim Lernen, Nachhilfeangebote oder Projektpartner'), "
                + "('WG & Wohnen', 'WG-Zimmer, Zwischenmiete oder studentisches Wohnen'), "
                + "('Jobs & Praktika', 'Werkstudentenjobs, Praktika oder Nebenjobs für Studierende'), "
                + "('Technik fürs Studium', 'Laptops, Tablets, Drucker und weitere studienrelevante Technik'), "
                + "('Campus-Events & Hochschulgruppen', 'Veranstaltungen, Hochschulgruppen oder Campus-Aktivitäten'), "
                + "('Tauschen & Verschenken', 'Kostenlose Angebote oder Tauschmöglichkeiten innerhalb der Community'), "
                + "('Dienstleistungen von Studierenden', 'Services wie Nachhilfe, Design, IT-Support oder Umzugshilfe') "
                + "ON CONFLICT (name) DO NOTHING;";

        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Hochschul-Kategorien eingefügt.");
    }
    
    public void showAllCategories() throws SQLException {
        String sql = "SELECT id, name, description FROM category";
        PreparedStatement prep = dbConn.prepareStatement(sql);

        ResultSet dbRes = prep.executeQuery();
        while (dbRes.next()) {
            long id = dbRes.getLong("id");
            String name = dbRes.getString("name");
            String description = dbRes.getString("description");

            System.out.println(id + " || " + name + " || " + description);
        }
    }

    public void deleteAllCategories() throws SQLException {
        String sql = "DELETE FROM category";
        PreparedStatement prep = dbConn.prepareStatement(sql);
        prep.executeUpdate();
        System.out.println("Alle Kategorien müssten gelöscht sein");
    }
}
