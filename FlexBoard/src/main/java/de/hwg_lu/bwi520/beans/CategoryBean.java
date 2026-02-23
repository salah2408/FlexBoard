package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Category;

public class CategoryBean {
	Vector<Category> allCategories;
    Connection dbConn;

    public CategoryBean() throws ClassNotFoundException, SQLException {
        this.allCategories = new Vector<Category>();
        this.dbConn = new PostgreSQLAccess().getConnection();

        this.readAllCategoriesFromDB();
}
    
    
    
	// Abschnitt Kategorien
    
    
    
    public void readAllCategoriesFromDB() throws SQLException {
        this.allCategories.clear();

        String sql = "SELECT id, name, description FROM category ORDER BY name";
        PreparedStatement prep = this.dbConn.prepareStatement(sql);

        ResultSet dbRes = prep.executeQuery();
        while (dbRes.next()) {

            long id = dbRes.getLong("id");
            String name = dbRes.getString("name");
            String description = dbRes.getString("description");

            this.allCategories.add(
                new Category(id, name, description)
            );
        }
    }
    
 // Abschnitt getHtml
    
    public String getCategoriesDropdownHtml() {

        String html = "";

        html += "Kategorie";
        html += "<select id='categorySelect' name='categoryID' class='form-select' required onchange='zeigeZusatzFelder()'>";
        html += "<option value=''>Bitte auswählen</option>";

        for (Category cat : this.allCategories) {
            html += "<option value='" + cat.getId() + "'>"
                    + cat.getName()
                    + "</option>";
        }

        html += "</select>";

        return html;
    }

    public String getLernmaterialHtml() {
    	String html = "<div id='catID1' hidden=''>"
    			+ "<div class='row row-gap'>"
				+ "<div class='col-md-6'>"
				+	"Studiengang "
				+ "<input type='text' class='form-control'name='studiengang' placeholder='z.B. Informatik'>"
				+"</div>"
				+"<div class='col-md-6'>"
				+"Modulname "
				+ "<input type='text' class='form-control'name='modul' placeholder='z.B. Analysis 1'>"
				+"</div>"
			+"</div>"

			+"<div class='row row-gap'>"
			+"	<div class='col-md-4'>"
			+		"Hochschule "
			+ "<input type='text' class='form-control'name='hochschule' placeholder='HWG LU'>"
			+	"</div>"
			+	"<div class='col-md-4'>"
			+		"Semester "
			+ "<input type='text' class='form-control' name='semester' placeholder=''>"
			+	"</div>"
			+	"<div class='col-md-4'>"
			+		"Format "
			+ "<input type='text' class='form-control'name='format' placeholder='Digital, Gedruckt...'>"
			+	"</div>"
			+"</div>"
			+"</div>";
    	
    	return html;
    }
    
    public String getNachhilfeHtml() {
		String html = "<div id='catID2' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Fach"
				+ "<input type='text' class='form-control' name='fach' placeholder='z.B. Statistik'>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Typ <select class='form-select' name='nachhilfeTyp' id='nachhilfeTypSelect' onchange='checkNachhilfePreis()'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Suche Nachhilfe'>Suche Nachhilfe</option>"
				+ "			<option value='Biete Nachhilfe'>Biete Nachhilfe</option>"
				+ "			<option value='Suche Lerngruppe'>Suche Lerngruppe (Kostenlos)</option>"
				+ "		</select>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-4'>"
				+ "		Ort <select class='form-select' name='nachhilfeOrt'>"
				+ "			<option value='Online'>Online</option>"
				+ "			<option value='Präsenz'>Präsenz</option>"
				+ "			<option value='Campus'>Campus</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Niveau <select class='form-select' name='nachhilfeNiveau'>"
				+ "			<option value='Bachelor'>Bachelor</option>"
				+ "			<option value='Master'>Master</option>"
				+ "			<option value='Staatsexamen'>Staatsexamen</option>"
				+ "		</select>"
				+ "	</div>"
				
				+ "	<div class='col-md-4' id='nachhilfePreisBlock' hidden=''>"
				+ "		Preis pro Stunde (€)"
				+ "		<input type='number' class='form-control' name='preis_pro_stunde' id='nachhilfePreisInput' min='0'>"
				+ "	</div>"
				
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getWohnenHtml() {
		String html = "<div id='catID3' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-4'>"
				+ "		Zimmergröße (m²)"
				+ "		<input type='number' class='form-control' name='zimmergroesse' placeholder='' min='1'>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Gesamtmiete (€)"
				+ "		<input type='number' class='form-control' name='gesamtmiete' placeholder='' min='0'>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Einzugsdatum"
				+ "		<input type='date' class='form-control' name='einzugsdatum'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Befristung <select class='form-select' name='befristung'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Unbefristet'>Unbefristet</option>"
				+ "			<option value='Zwischenmiete'>Zwischenmiete (Details in Beschreibung)</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		WG-Details"
				+ "		<div style='margin-top: 8px;'>"
				+ "			<input type='checkbox' name='wg_details' value='Zweck-WG' checked> Zweck-WG &nbsp;"
				+ "			<input type='checkbox' name='wg_details' value='Vegan/Vegetarisch'> Vegan/Veggie &nbsp;"
				+ "			<input type='checkbox' name='wg_details' value='Nur FLINTA*'> Nur FLINTA"
				+ "		</div>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getJobsHtml() {
		String html = "<div id='catID4' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-12'>"
				+ "		Anzeigentyp <select class='form-select' name='job_typ' id='jobTypSelect' onchange='checkJobVerguetung()'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Biete'>Ich biete einen Job / ein Praktikum an</option>"
				+ "			<option value='Suche'>Ich suche einen Job / ein Praktikum</option>"
				+ "		</select>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Anstellungsart <select class='form-select' name='anstellungsart'>"
				+ "			<option value='Werkstudierende'>Werkstudierende</option>"
				+ "			<option value='Minijob'>Minijob</option>"
				+ "			<option value='Praktikum'>Praktikum</option>"
				+ "			<option value='Abschlussarbeit'>Abschlussarbeit</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Wochenstunden <input type='number' class='form-control' name='wochenstunden' placeholder='z.B. 20' min='1'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Remote-Anteil <select class='form-select' name='remote'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Vor Ort'>Vor Ort</option>"
				+ "			<option value='Hybrid'>Hybrid</option>"
				+ "			<option value='Full Remote'>Full Remote</option>"
				+ "		</select>"
				+ "	</div>"
				
				+ "	<div class='col-md-6' id='jobVerguetungBlock'>"
				+ "		Vergütung"
				+ "		<input type='text' class='form-control' name='verguetung' id='jobVerguetungInput' placeholder='z.B. 15€/h oder 500€ Fix'>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getTechnikHtml() {
		String html = "<div id='catID5' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Gerätetyp <select class='form-select' name='geraetetyp'>"
				+ "			<option value='Laptop'>Laptop</option>"
				+ "			<option value='Tablet'>Tablet</option>"
				+ "			<option value='Monitor'>Monitor</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Marke"
				+ "		<input type='text' class='form-control' name='marke' placeholder='z.B. Apple, Lenovo'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-4'>"
				+ "		Zustand <select class='form-select' name='zustandTechnik'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Neuwertig'>Neuwertig</option>"
				+ "			<option value='Gebraucht'>Gebraucht (gut)</option>"
				+ "			<option value='Defekt'>Defekt/Ersatzteil</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Restgarantie "
				+ "		<input type='text' class='form-control' name='garantie' placeholder='z.B. bis 12/2026 oder Nein'>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Preis (€)"
				+ "		<input type='number' class='form-control' name='technik_preis' min='0'>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getEventsHtml() {
		String html = "<div id='catID6' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Event-Datum"
				+ "		<input type='datetime-local' class='form-control' name='event_datum'>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Veranstalter"
				+ "		<input type='text' class='form-control' name='veranstalter' placeholder='z.B. Fachschaft WiWi'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Eintritt <select class='form-select' name='eintritt' id='eventEintritt' onchange='checkEventPreis()'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Kostenlos'>Kostenlos</option>"
				+ "			<option value='Kostenpflichtig'>Kostenpflichtig</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Anmeldung erforderlich? <select class='form-select' name='anmeldung'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Ja'>Ja</option>"
				+ "			<option value='Nein'>Nein</option>"
				+ "		</select>"
				+ "	</div>"
				+ "</div>"
				
				+ "<div class='row row-gap' id='eventPreisBlock' hidden=''>"
				+ "	<div class='col-md-4'>"
				+ "		Ticketpreis (€)"
				+ "		<input type='number' class='form-control' name='event_preis' id='eventPreisInput' min='0'>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getTauschenHtml() {
		String html = "<div id='catID7' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-12'>"
				+ "		Tausch gegen"
				+ "		<input type='text' class='form-control' name='tausch_gegen' placeholder='Was hättest du gerne im Gegenzug? (z.B. Kiste Spezi)'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-6'>"
				+ "		Zustand <select class='form-select' name='zustandTauschen'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Gebraucht'>Gebraucht</option>"
				+ "			<option value='Zu verschenken (Defekt)'>Zu verschenken (Defekt)</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-6'>"
				+ "		Abholung <select class='form-select' name='abholung'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Nur Abholung'>Nur Abholung</option>"
				+ "			<option value='Versand möglich'>Versand möglich</option>"
				+ "		</select>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}

	public String getDienstleistungenHtml() {
		String html = "<div id='catID8' hidden=''>"
				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-4'>"
				+ "		Kategorie <select class='form-select' name='dienstleistung_kat'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Korrekturlesen'>Korrekturlesen</option>"
				+ "			<option value='Umzugshilfe'>Umzugshilfe</option>"
				+ "			<option value='Programmierung'>Programmierung</option>"
				+ "			<option value='Design'>Design</option>"
				+ "			<option value='Reparatur'>Reparatur</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Preismodell <select class='form-select' name='preismodell'>"
				+ "			<option value=''>Wählen...</option>"
				+ "			<option value='Festpreis'>Festpreis</option>"
				+ "			<option value='Auf Verhandlungsbasis'>Auf Verhandlungsbasis (VB)</option>"
				+ "		</select>"
				+ "	</div>"
				+ "	<div class='col-md-4'>"
				+ "		Preis (€)"
				+ "		<input type='number' class='form-control' name='dienstleistung_preis' min='0'>"
				+ "	</div>"
				+ "</div>"

				+ "<div class='row row-gap'>"
				+ "	<div class='col-md-12'>"
				+ "		Referenzen / Erfahrungen"
				+ "		<textarea class='form-control' name='referenzen' rows='2' placeholder='Kurze Info zu bisherigen Erfahrungen...'></textarea>"
				+ "	</div>"
				+ "</div>"
				+ "</div>";

		return html;
	}
    
    
 // Abschnitt Getter und Setter
    
    public Vector<Category> getAllCategories() {
        return this.allCategories;
    }
    
    
    
    
}

