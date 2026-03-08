package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import org.json.JSONArray;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Category;

public class CategoryBean {
	Vector<Category> allCategories;
    Connection dbConn;
    long selectedCategoryId = -1;
    ListingBean listingBean;

    public CategoryBean() throws ClassNotFoundException, SQLException {
        this.allCategories = new Vector<Category>();
        this.dbConn = new PostgreSQLAccess().getConnection();

        this.readAllCategoriesFromDB();
}
    
    private String getEditValue(String key) {
        if (this.listingBean == null) return "";
        return this.listingBean.getEditDetailValue(key);
    }
    
    private boolean isChecked(String key, String value) {

        if (this.listingBean == null) return false;

        try {
            JSONArray arr = this.listingBean.getEditDetails().optJSONArray(key);

            if (arr == null) return false;

            for (int i = 0; i < arr.length(); i++) {
                if (arr.getString(i).equals(value)) {
                    return true;
                }
            }

        } catch (Exception e) {
            return false;
        }

        return false;
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

            html += "<option value='" + cat.getId() + "'";

            if (cat.getId() == this.selectedCategoryId) {
                html += " selected";
            }

            html += ">" + cat.getName() + "</option>";
        }

        html += "</select>";

        return html;
    }

    public String getLernmaterialHtml() {

        String html = "<div id='catID1' hidden=''>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-6'>"
            + "    Studiengang "
            + "    <input type='text' class='form-control' "
            + "    name='studiengang' "
            + "    value='" + getEditValue("studiengang") + "' "
            + "    placeholder='z.B. Informatik'>"
            + "  </div>"

            + "  <div class='col-md-6'>"
            + "    Modulname "
            + "    <input type='text' class='form-control' "
            + "    name='modul' "
            + "    value='" + getEditValue("modul") + "' "
            + "    placeholder='z.B. Analysis 1'>"
            + "  </div>"

            + "</div>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-4'>"
            + "    Hochschule "
            + "    <input type='text' class='form-control' "
            + "    name='hochschule' "
            + "    value='" + getEditValue("hochschule") + "' "
            + "    placeholder='HWG LU'>"
            + "  </div>"

            + "  <div class='col-md-4'>"
            + "    Semester "
            + "    <input type='text' class='form-control' "
            + "    name='semester' "
            + "    value='" + getEditValue("semester") + "'>"
            + "  </div>"

            + "  <div class='col-md-4'>"
            + "    Format "
            + "    <input type='text' class='form-control' "
            + "    name='format' "
            + "    value='" + getEditValue("format") + "' "
            + "    placeholder='Digital, Gedruckt...'>"
            + "  </div>"

            + "</div>"

            + "</div>";

        return html;
    }
    
    public String getNachhilfeHtml() {

        String html = "<div id='catID2' hidden=''>"

            + "<div class='row row-gap'>"
            + "  <div class='col-md-6'>"
            + "    Fach"
            + "    <input type='text' class='form-control' name='fach' "
            + "    value='" + getEditValue("fach") + "'>"
            + "  </div>"

            + "  <div class='col-md-6'>"
            + "    Typ"
            + "    <select class='form-select' name='nachhilfeTyp' id='nachhilfeTypSelect' onchange='checkNachhilfePreis()'>"
            + "      <option value=''>Wählen...</option>"

            + "      <option value='Suche Nachhilfe' "
            + (getEditValue("nachhilfeTyp").equals("Suche Nachhilfe") ? "selected" : "")
            + ">Suche Nachhilfe</option>"

            + "      <option value='Biete Nachhilfe' "
            + (getEditValue("nachhilfeTyp").equals("Biete Nachhilfe") ? "selected" : "")
            + ">Biete Nachhilfe</option>"

            + "      <option value='Suche Lerngruppe' "
            + (getEditValue("nachhilfeTyp").equals("Suche Lerngruppe") ? "selected" : "")
            + ">Suche Lerngruppe (Kostenlos)</option>"

            + "    </select>"
            + "  </div>"
            + "</div>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-4'>"
            + "    Ort"
            + "    <select class='form-select' name='nachhilfeOrt'>"
            + "      <option value='Online' "
            + (getEditValue("nachhilfeOrt").equals("Online") ? "selected" : "")
            + ">Online</option>"

            + "      <option value='Präsenz' "
            + (getEditValue("nachhilfeOrt").equals("Präsenz") ? "selected" : "")
            + ">Präsenz</option>"

            + "      <option value='Campus' "
            + (getEditValue("nachhilfeOrt").equals("Campus") ? "selected" : "")
            + ">Campus</option>"
            + "    </select>"
            + "  </div>"

            + "  <div class='col-md-4'>"
            + "    Niveau"
            + "    <select class='form-select' name='nachhilfeNiveau'>"
            + "      <option value='Bachelor' "
            + (getEditValue("nachhilfeNiveau").equals("Bachelor") ? "selected" : "")
            + ">Bachelor</option>"

            + "      <option value='Master' "
            + (getEditValue("nachhilfeNiveau").equals("Master") ? "selected" : "")
            + ">Master</option>"

            + "      <option value='Staatsexamen' "
            + (getEditValue("nachhilfeNiveau").equals("Staatsexamen") ? "selected" : "")
            + ">Staatsexamen</option>"
            + "    </select>"
            + "  </div>"

            + "  <div class='col-md-4' id='nachhilfePreisBlock' hidden=''>"
            + "    Preis pro Stunde (€)"
            + "    <input type='number' class='form-control' name='preis_pro_stunde' "
            + "    id='nachhilfePreisInput' "
            + "    value='" + getEditValue("preisProStunde") + "' min='0'>"
            + "  </div>"

            + "</div>"
            + "</div>";

        return html;
    }

    public String getWohnenHtml() {

        String html = "<div id='catID3' hidden=''>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-4'>"
            + "    Zimmergröße (m²)"
            + "    <input type='number' class='form-control' name='zimmergroesse' "
            + "    value='" + getEditValue("zimmergroesse") + "' min='1'>"
            + "  </div>"

            + "  <div class='col-md-4'>"
            + "    Gesamtmiete (€)"
            + "    <input type='number' class='form-control' name='gesamtmiete' "
            + "    value='" + getEditValue("gesamtmiete") + "' min='0'>"
            + "  </div>"

            + "  <div class='col-md-4'>"
            + "    Einzugsdatum"
            + "    <input type='date' class='form-control' name='einzugsdatum' "
            + "    value='" + getEditValue("einzugsdatum") + "'>"
            + "  </div>"

            + "</div>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-6'>"
            + "    Befristung"
            + "    <select class='form-select' name='befristung'>"

            + "      <option value=''>Wählen...</option>"

            + "      <option value='Unbefristet' "
            + (getEditValue("befristung").equals("Unbefristet") ? "selected" : "")
            + ">Unbefristet</option>"

            + "      <option value='Zwischenmiete' "
            + (getEditValue("befristung").equals("Zwischenmiete") ? "selected" : "")
            + ">Zwischenmiete (Details in Beschreibung)</option>"

            + "    </select>"
            + "  </div>"

            + "  <div class='col-md-6'>"
            + "    WG-Details"
            + "    <div style='margin-top: 8px;'>"

            + "      <input type='checkbox' name='wg_details' value='Zweck-WG' "
            + (isChecked("wgDetails", "Zweck-WG") ? "checked" : "")
            + "> Zweck-WG &nbsp;"

            + "      <input type='checkbox' name='wg_details' value='Vegan/Vegetarisch' "
            + (isChecked("wgDetails", "Vegan/Vegetarisch") ? "checked" : "")
            + "> Vegan/Veggie &nbsp;"

            + "      <input type='checkbox' name='wg_details' value='Nur FLINTA*' "
            + (isChecked("wgDetails", "Nur FLINTA*") ? "checked" : "")
            + "> Nur FLINTA"

            + "    </div>"
            + "  </div>"

            + "</div>"
            + "</div>";

        return html;
    }

    public String getJobsHtml() {

        String html = "<div id='catID4' hidden=''>"

            + "<div class='row row-gap'>"
            + "  <div class='col-md-12'>"
            + "    Anzeigentyp"
            + "    <select class='form-select' name='jobTypSelect' "
            + "    id='jobTypSelect' onchange='checkJobVerguetung()'>"

            + "      <option value=''>Wählen...</option>"

            + "      <option value='Biete' "
            + (getEditValue("jobTypSelect").equals("Biete") ? "selected" : "")
            + ">Ich biete einen Job / ein Praktikum an</option>"

            + "      <option value='Suche' "
            + (getEditValue("jobTypSelect").equals("Suche") ? "selected" : "")
            + ">Ich suche einen Job / ein Praktikum</option>"

            + "    </select>"
            + "  </div>"
            + "</div>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-6'>"
            + "    Anstellungsart"
            + "    <select class='form-select' name='anstellungsart'>"

            + "      <option value='Werkstudierende' "
            + (getEditValue("anstellungsart").equals("Werkstudierende") ? "selected" : "")
            + ">Werkstudierende</option>"

            + "      <option value='Minijob' "
            + (getEditValue("anstellungsart").equals("Minijob") ? "selected" : "")
            + ">Minijob</option>"

            + "      <option value='Praktikum' "
            + (getEditValue("anstellungsart").equals("Praktikum") ? "selected" : "")
            + ">Praktikum</option>"

            + "      <option value='Abschlussarbeit' "
            + (getEditValue("anstellungsart").equals("Abschlussarbeit") ? "selected" : "")
            + ">Abschlussarbeit</option>"

            + "    </select>"
            + "  </div>"

            + "  <div class='col-md-6'>"
            + "    Wochenstunden"
            + "    <input type='number' class='form-control' name='wochenstunden' "
            + "    value='" + getEditValue("wochenstunden") + "' min='1'>"
            + "  </div>"

            + "</div>"

            + "<div class='row row-gap'>"

            + "  <div class='col-md-6'>"
            + "    Remote-Anteil"
            + "    <select class='form-select' name='remote'>"

            + "      <option value=''>Wählen...</option>"

            + "      <option value='Vor Ort' "
            + (getEditValue("remote").equals("Vor Ort") ? "selected" : "")
            + ">Vor Ort</option>"

            + "      <option value='Hybrid' "
            + (getEditValue("remote").equals("Hybrid") ? "selected" : "")
            + ">Hybrid</option>"

            + "      <option value='Full Remote' "
            + (getEditValue("remote").equals("Full Remote") ? "selected" : "")
            + ">Full Remote</option>"

            + "    </select>"
            + "  </div>"

            + "  <div class='col-md-6' id='jobVerguetungBlock'>"
            + "    Vergütung"
            + "    <input type='number' class='form-control' name='verguetung' "
            + "    id='jobVerguetungInput' "
            + "    value='" + getEditValue("verguetung") + "'>"
            + "  </div>"

            + "</div>"
            + "</div>";

        return html;
    }

	public String getTechnikHtml() {

	    String html = "<div id='catID5' hidden=''>"
	        + "<div class='row row-gap'>"
	        + "  <div class='col-md-6'>"
	        + "    Gerätetyp "
	        + "    <select class='form-select' name='geraetetyp'>"
	        + "      <option value='Laptop' "
	        + (getEditValue("geraetetyp").equals("Laptop") ? "selected" : "")
	        + ">Laptop</option>"
	        + "      <option value='Tablet' "
	        + (getEditValue("geraetetyp").equals("Tablet") ? "selected" : "")
	        + ">Tablet</option>"
	        + "      <option value='Monitor' "
	        + (getEditValue("geraetetyp").equals("Monitor") ? "selected" : "")
	        + ">Monitor</option>"
	        + "      <option value='Sonstiges' "
	        + (getEditValue("geraetetyp").equals("Sonstiges") ? "selected" : "")
	        + ">Sonstiges</option>"
	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-6'>"
	        + "    Marke"
	        + "    <input type='text' class='form-control' name='marke' "
	        + "    value='" + getEditValue("marke") + "'>"
	        + "  </div>"
	        + "</div>"

	        + "<div class='row row-gap'>"
	        + "  <div class='col-md-4'>"
	        + "    Zustand"
	        + "    <select class='form-select' name='zustandTechnik'>"
	        + "      <option value=''>Wählen...</option>"
	        + "      <option value='Neuwertig' "
	        + (getEditValue("zustandTechnik").equals("Neuwertig") ? "selected" : "")
	        + ">Neuwertig</option>"
	        + "      <option value='Gebraucht' "
	        + (getEditValue("zustandTechnik").equals("Gebraucht") ? "selected" : "")
	        + ">Gebraucht</option>"
	        + "      <option value='Defekt' "
	        + (getEditValue("zustandTechnik").equals("Defekt") ? "selected" : "")
	        + ">Defekt</option>"
	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-4'>"
	        + "    Restgarantie"
	        + "    <input type='text' class='form-control' name='garantie' "
	        + "    value='" + getEditValue("garantie") + "'>"
	        + "  </div>"

	        + "  <div class='col-md-4'>"
	        + "    Preis (€)"
	        + "    <input type='number' class='form-control' name='technik_preis' "
	        + "    value='" + getEditValue("technikPreis") + "'>"
	        + "  </div>"
	        + "</div>"
	        + "</div>";

	    return html;
	}

	public String getEventsHtml() {

	    String html = "<div id='catID6' hidden=''>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-6'>"
	        + "    Event-Datum"
	        + "    <input type='datetime-local' class='form-control' name='event_datum' "
	        + "    value='" + getEditValue("eventDatum") + "'>"
	        + "  </div>"

	        + "  <div class='col-md-6'>"
	        + "    Veranstalter"
	        + "    <input type='text' class='form-control' name='veranstalter' "
	        + "    value='" + getEditValue("veranstalter") + "'>"
	        + "  </div>"

	        + "</div>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-6'>"
	        + "    Eintritt"
	        + "    <select class='form-select' name='eintritt' "
	        + "    id='eventEintritt' onchange='checkEventPreis()'>"

	        + "      <option value=''>Wählen...</option>"

	        + "      <option value='Kostenlos' "
	        + (getEditValue("eintritt").equals("Kostenlos") ? "selected" : "")
	        + ">Kostenlos</option>"

	        + "      <option value='Kostenpflichtig' "
	        + (getEditValue("eintritt").equals("Kostenpflichtig") ? "selected" : "")
	        + ">Kostenpflichtig</option>"

	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-6'>"
	        + "    Anmeldung erforderlich?"
	        + "    <select class='form-select' name='anmeldung'>"

	        + "      <option value=''>Wählen...</option>"

	        + "      <option value='Ja' "
	        + (getEditValue("anmeldung").equals("Ja") ? "selected" : "")
	        + ">Ja</option>"

	        + "      <option value='Nein' "
	        + (getEditValue("anmeldung").equals("Nein") ? "selected" : "")
	        + ">Nein</option>"

	        + "    </select>"
	        + "  </div>"

	        + "</div>"

	        + "<div class='row row-gap' id='eventPreisBlock' hidden=''>"
	        + "  <div class='col-md-4'>"
	        + "    Ticketpreis (€)"
	        + "    <input type='number' class='form-control' "
	        + "    name='event_preis' id='eventPreisInput' "
	        + "    value='" + getEditValue("eventPreis") + "' min='0'>"
	        + "  </div>"
	        + "</div>"

	        + "</div>";

	    return html;
	}

	public String getTauschenHtml() {

	    String html = "<div id='catID7' hidden=''>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-12'>"
	        + "    Tausch gegen"
	        + "    <input type='text' class='form-control' "
	        + "    name='tauschGegen' "
	        + "    value='" + getEditValue("tauschGegen") + "'>"
	        + "  </div>"

	        + "</div>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-6'>"
	        + "    Zustand"
	        + "    <select class='form-select' name='zustandTauschen'>"

	        + "      <option value=''>Wählen...</option>"
	        
			+ "      <option value='Neuwertig' "
			+ (getEditValue("zustandTauschen").equals("Neuwertig") ? "selected" : "")
			+ ">Neuwertig</option>"

	        + "      <option value='Gebraucht' "
	        + (getEditValue("zustandTauschen").equals("Gebraucht") ? "selected" : "")
	        + ">Gebraucht</option>"

	        + "      <option value='Zu verschenken (Defekt)' "
	        + (getEditValue("zustandTauschen").equals("Zu verschenken (Defekt)") ? "selected" : "")
	        + ">Zu verschenken (Defekt)</option>"

	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-6'>"
	        + "    Abholung"
	        + "    <select class='form-select' name='abholung'>"

	        + "      <option value=''>Wählen...</option>"

	        + "      <option value='Nur Abholung' "
	        + (getEditValue("abholung").equals("Nur Abholung") ? "selected" : "")
	        + ">Nur Abholung</option>"

	        + "      <option value='Versand möglich' "
	        + (getEditValue("abholung").equals("Versand möglich") ? "selected" : "")
	        + ">Versand möglich</option>"

	        + "    </select>"
	        + "  </div>"

	        + "</div>"

	        + "</div>";

	    return html;
	}

	public String getDienstleistungenHtml() {

	    String html = "<div id='catID8' hidden=''>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-4'>"
	        + "    Kategorie"
	        + "    <select class='form-select' name='dienstleistungKat'>"

	        + "      <option value=''>Wählen...</option>"

	        + "      <option value='Korrekturlesen' "
	        + (getEditValue("dienstleistungKat").equals("Korrekturlesen") ? "selected" : "")
	        + ">Korrekturlesen</option>"

	        + "      <option value='Umzugshilfe' "
	        + (getEditValue("dienstleistungKat").equals("Umzugshilfe") ? "selected" : "")
	        + ">Umzugshilfe</option>"

	        + "      <option value='Programmierung' "
	        + (getEditValue("dienstleistungKat").equals("Programmierung") ? "selected" : "")
	        + ">Programmierung</option>"

	        + "      <option value='Design' "
	        + (getEditValue("dienstleistungKat").equals("Design") ? "selected" : "")
	        + ">Design</option>"

	        + "      <option value='Reparatur' "
	        + (getEditValue("dienstleistungKat").equals("Reparatur") ? "selected" : "")
	        + ">Reparatur</option>"

	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-4'>"
	        + "    Preismodell"
	        + "    <select class='form-select' name='preismodell'>"

	        + "      <option value=''>Wählen...</option>"

	        + "      <option value='Festpreis' "
	        + (getEditValue("preismodell").equals("Festpreis") ? "selected" : "")
	        + ">Festpreis</option>"

	        + "      <option value='Auf Verhandlungsbasis' "
	        + (getEditValue("preismodell").equals("Auf Verhandlungsbasis") ? "selected" : "")
	        + ">Auf Verhandlungsbasis (VB)</option>"

	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-4'>"
	        + "    Preis (€)"
	        + "    <input type='number' class='form-control' "
	        + "    name='dienstleistungPreis' "
	        + "    value='" + getEditValue("dienstleistungPreis") + "' min='0'>"
	        + "  </div>"

	        + "</div>"

	        + "<div class='row row-gap'>"

	        + "  <div class='col-md-12'>"
	        + "    Referenzen / Erfahrungen"
	        + "    <textarea class='form-control' name='referenzen' rows='2'>"
	        + getEditValue("referenzen")
	        + "</textarea>"
	        + "  </div>"

	        + "</div>"

	        + "</div>";

	    return html;
	}
    
	public String getSonstigesHtml() {

	    String html = "<div id='catID9' hidden=''>"
	        
	        + "<div class='row row-gap'>"
	        
	        + "  <div class='col-md-6'>"
	        + "    Preis-Option"
	        + "    <select class='form-select' name='sonstigesTyp' id='sonstigesPreisSelect' onchange='checkSonstigesPreis()'>"
	        + "      <option value=''>Wählen...</option>"
	        + "      <option value='Gratis'>Gratis</option>"
	        + "      <option value='Auf Anfrage'>Auf Anfrage</option>"
	        + "      <option value='Preis'>Festpreis / VB</option>"
	        + "    </select>"
	        + "  </div>"

	        + "  <div class='col-md-6' id='sonstigesPreisBlock' hidden=''>"
	        + "    Preis (€)"
	        + "    <input type='number' class='form-control' name='sonstigesPreis' "
	        + "    id='sonstigesPreisInput' value='' min='0'>"
	        + "  </div>"

	        + "</div>" 
	        
	        + "</div>"; 

	    return html;
	}
    
 // Abschnitt Getter und Setter
    
    public Vector<Category> getAllCategories() {
        return this.allCategories;
    }
    public void setSelectedCategoryId(long id) {
        this.selectedCategoryId = id;
    }

    public long getSelectedCategoryId() {
        return this.selectedCategoryId;
    }
    
    public void setListingBean(ListingBean listingBean) {
        this.listingBean = listingBean;
    }
    
    
    
}

