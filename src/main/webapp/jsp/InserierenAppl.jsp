<%@ include file="./AuthRequired.jsp" %>
<%@page import="de.hwg_lu.bwi520.beans.ListingBean"%>
<%@page import="de.hwg_lu.bwi520.beans.WeiterleitungsBean"%>
<%@page import="de.hwg_lu.bwi520.beans.AccountBean"%>
<%@ page import="org.json.JSONObject" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="./AuthRequired.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title></title>
</head>
<body>

<jsp:useBean id="myAccount" class="de.hwg_lu.bwi520.beans.AccountBean" scope="session" />
<jsp:useBean id="myWeiter" class="de.hwg_lu.bwi520.beans.WeiterleitungsBean" scope="session" />
<jsp:useBean id="myListing" class="de.hwg_lu.bwi520.beans.ListingBean" scope="session" />

<%
// Allgemein (steht immer dabei)
String action = request.getParameter("action");
String title = request.getParameter("title");
String descr = request.getParameter("descr");
String categoryID = request.getParameter("categoryID");
String city = request.getParameter("city");
String zip = request.getParameter("zip");
String imageBase64 = request.getParameter("imageBase64");
String listingIdParam = request.getParameter("listingid");


// Lernmaterial catid 1
String studiengang = request.getParameter("studiengang");
String modul = request.getParameter("modul");
String hochschule = request.getParameter("hochschule");
String semester = request.getParameter("semester");
String format = request.getParameter("format");

// Nachhilfe catid 2
String fach = request.getParameter("fach");
String nachhilfeTyp = request.getParameter("nachhilfeTyp");
String preisProStunde = request.getParameter("preis_pro_stunde");
String nachhilfeOrt = request.getParameter("nachhilfeOrt");
String nachhilfeNiveau = request.getParameter("nachhilfeNiveau");

// Wohnen catid 3
String zimmergroesse = request.getParameter("zimmergroesse");
String gesamtmiete = request.getParameter("gesamtmiete");
String einzugsdatum = request.getParameter("einzugsdatum");
String befristung = request.getParameter("befristung");
String[] wgDetails = request.getParameterValues("wg_details");

// Jobs catid 4
String jobTypSelect = request.getParameter("jobTypSelect");
String anstellungsart = request.getParameter("anstellungsart");
String wochenstunden = request.getParameter("wochenstunden");
String verguetung = request.getParameter("verguetung");

// Technik catid 5
String geraetetyp = request.getParameter("geraetetyp");
String marke = request.getParameter("marke");
String zustandTechnik = request.getParameter("zustandTechnik");
String garantie = request.getParameter("garantie");
String technikPreis = request.getParameter("technik_preis");

// CampusEvents catid 6
String eventDatum = request.getParameter("event_datum");
String veranstalter = request.getParameter("veranstalter");
String eintritt = request.getParameter("eintritt");
String anmeldung = request.getParameter("anmeldung");
String eventPreis = request.getParameter("event_preis");

// Tauschen catid 7
String tauschGegen = request.getParameter("tauschGegen");
String zustandTauschen = request.getParameter("zustandTauschen");
String abholung = request.getParameter("abholung");

// Dienstleistungen catid 8
String dienstleistungKat = request.getParameter("dienstleistungKat");
String preismodell = request.getParameter("preismodell");
String dienstleistungPreis = request.getParameter("dienstleistungPreis");
String referenzen = request.getParameter("referenzen");

// Sonstiges catid 9
String sonstigesTyp = request.getParameter("sonstigesTyp");
String sonstigesPreis = request.getParameter("sonstigesPreis");




if(action == null)
	action = "";
if(categoryID == null)
	categoryID = "";

if(action.equals("Anzeige erstellen") || action.equals("Anzeige aktualisieren")){
    JSONObject detailsJson = new JSONObject();

    if(categoryID.equals("1")){
        detailsJson.put("studiengang", studiengang);
        detailsJson.put("modul", modul);
        detailsJson.put("hochschule", hochschule);
        detailsJson.put("semester", semester);
        detailsJson.put("format", format);

    } else if (categoryID.equals("2")) {
        detailsJson.put("fach", fach);
        detailsJson.put("nachhilfeTyp", nachhilfeTyp);
        detailsJson.put("preisProStunde", (preisProStunde != null && !preisProStunde.equals("")) ? Integer.parseInt(preisProStunde) : 0);
        detailsJson.put("nachhilfeOrt", nachhilfeOrt);
        detailsJson.put("nachhilfeNiveau", nachhilfeNiveau);

    } else if (categoryID.equals("3")) {
        detailsJson.put("zimmergroesse", zimmergroesse);
        detailsJson.put("gesamtmiete", gesamtmiete != null ? Integer.parseInt(gesamtmiete) : 0);
        detailsJson.put("einzugsdatum", einzugsdatum);
        detailsJson.put("befristung", befristung);
        detailsJson.put("wgDetails", wgDetails != null ? wgDetails : "");

    } else if (categoryID.equals("4")) {
        detailsJson.put("jobTypSelect", jobTypSelect);
        detailsJson.put("anstellungsart", anstellungsart);
        detailsJson.put("wochenstunden", wochenstunden);
        detailsJson.put("verguetung", (verguetung != null && !verguetung.equals("")) ? Integer.parseInt(verguetung) : 0);

    } else if (categoryID.equals("5")) {
        detailsJson.put("geraetetyp", geraetetyp);
        detailsJson.put("marke", marke);
        detailsJson.put("zustandTechnik", zustandTechnik);
        detailsJson.put("garantie", garantie);
        detailsJson.put("technikPreis", technikPreis != null ? Integer.parseInt(technikPreis) : 0);

    } else if (categoryID.equals("6")) {
        detailsJson.put("eventDatum", eventDatum);
        detailsJson.put("veranstalter", veranstalter);
        detailsJson.put("eintritt", eintritt);
        detailsJson.put("anmeldung", anmeldung);
        detailsJson.put("eventPreis", (eventPreis != null && !eventPreis.equals(""))? Integer.parseInt(eventPreis) : 0);

    } else if (categoryID.equals("7")) {
        detailsJson.put("tauschGegen", tauschGegen);
        detailsJson.put("zustandTauschen", zustandTauschen);
        detailsJson.put("abholung", abholung);

    } else if (categoryID.equals("8")) {
        detailsJson.put("dienstleistungKat", dienstleistungKat);
        detailsJson.put("preismodell", preismodell);
        detailsJson.put("dienstleistungPreis", dienstleistungPreis != null ? Integer.parseInt(dienstleistungPreis) : 0);
        detailsJson.put("referenzen", referenzen);
    } else if(categoryID.equals("9")){
    	detailsJson.put("sonstigesTyp", sonstigesTyp);
    	detailsJson.put("sonstigesPreis", (sonstigesPreis != null && !sonstigesPreis.equals("")) ? Integer.parseInt(sonstigesPreis) : 0);
    }
    if (imageBase64 != null && !imageBase64.isEmpty()) {
        // Neues Bild wurde hochgeladen
        detailsJson.put("imageBase64", imageBase64);
    } 
    else if (listingIdParam != null && !listingIdParam.isEmpty()) {
        // Kein neues Bild → DB-basiert altes Bild direkt abfragen
        JSONObject oldDetails = myListing.getDetailsForListing(Integer.parseInt(listingIdParam));
        if (oldDetails != null && oldDetails.has("imageBase64")) {
            detailsJson.put("imageBase64", oldDetails.getString("imageBase64"));
        }
    }


    boolean success;

    if(listingIdParam != null && !listingIdParam.isEmpty()) {

        myListing.setAktListingId(Integer.parseInt(listingIdParam));

        success = myListing.updateListing(
            myAccount.getEmail(),
            title,
            descr,
            Integer.parseInt(categoryID),
            city,
            zip,
            detailsJson
        );

    } else {

        success = myListing.saveListing(
            myAccount.getEmail(),
            title,
            descr,
            Integer.parseInt(categoryID),
            city,
            zip,
            detailsJson
        );
    }
    if(success){
    	myListing.readAlleAnzeigenFromDB();
    	myListing.resetEditMode();
    	int latestListing = myListing.getLatestListingId();
    	response.sendRedirect("./NavbarAppl.jsp?action=zumListing&id=" + latestListing);
    }
    else{
    	System.out.println("Ein Fehler beim Inserieren oder bearbeiten ist geschehen bitte wenden sie sich an ihren IT-Admin");
    	response.sendRedirect("./HomepageView.jsp");
    }
    	
    
} else {
	response.sendRedirect("./HomepageView.jsp");
}
%>

</body>
</html>