package de.hwg_lu.bwi520.beans;

public class HomeBean {
	private String suchbegriff = "";

    public HomeBean() {
    }

    // Hero + Suchbereich der Startseite
    public String getSearchCardHtml() {

        String html = "";

        html += "<div class='bg-white rounded-4 shadow-lg p-4 text-center'>";

        html += "<div class='d-flex justify-content-center align-items-center mb-3'>";
        html += "<img src='../img/flexboard-logo.jpg' height='36' class='me-2'>";
        html += "<h3 class='fw-semibold m-0'>Eine Suche, viele Treffer</h3>";
        html += "</div>";

        html += "<form action='./NavbarAppl.jsp' method='get'>";
        html += "<input type='hidden' name='action' value='zurSuche'>";

        html += "<div class='input-group input-group-lg mt-3'>";
        html += "<input type='text' "
              + "class='form-control rounded-start-pill' "
              + "name='q' "
              + "value='" + (this.suchbegriff == null ? "" : this.suchbegriff) + "' "
              + "placeholder='Wonach suchst du? (z.B. Fahrrad, Laptop, Buch)'>";

        html += "<button class='btn btn-primary rounded-end-pill px-4' "
              + "type='submit'>Suchen</button>";

        html += "</div>";
        html += "</form>";

        html += "</div>";

        return html;
    }
    
    public String getFeatureCardsHtml() {
    	System.out.println("FeatureCardsHtml wird aufgerufen");
    	String[][] features = {
    		    {"bi-megaphone-fill", "Inserate erstellen",
    		     "Erstelle in wenigen Schritten ein Inserat und erreiche andere Nutzer.",
    		     "Erstellen", "NavbarAppl.jsp?action=zumInserieren"},

    		    {"bi-search", "Inserate finden",
    		     "Durchsuche Angebote nach Kategorien, Ort oder Stichworten.",
    		     "Suchen", "NavbarAppl.jsp?action=zurSuche"},

    		    {"bi-pencil-square", "Inserate verwalten",
    		     "Behalte deine eigenen Inserate im Blick und bearbeite sie jederzeit.",
    		     "Verwalten", "NavbarAppl.jsp?action=zurMeineInserate"}
    		};

        String html = "";

        for (int i = 0; i < features.length; i++) {

            html += "<div class='col-md-4'>";
            html += "<div class='card h-100 shadow-sm border-0'>";
            html += "<div class='card-body text-center'>";

            html += "<div class='mb-3'><i class='bi "
                  + features[i][0] + " feature-icon'></i></div>";

            html += "<h5 class='card-title fw-bold'>"
                  + features[i][1] + "</h5>";

            html += "<p class='card-text text-muted'>"
                  + features[i][2] + "</p>";

            html += "<a href='" + features[i][4] + "' "
            	      + "class='btn btn-outline-primary'>"
            	      + features[i][3] + "</a>";

            html += "</div></div></div>";
        }

        return html;
    }
    
    
	public String getSuchbegriff() {
		return suchbegriff;
	}

	public void setSuchbegriff(String suchbegriff) {
		this.suchbegriff = suchbegriff;
	}
}