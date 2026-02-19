package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Vector;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Account;
import de.hwg_lu.bwi520.classes.Nachricht;

public class AccountBean {
	Vector<Account> allAccounts;
	Connection dbConn;
	public String aktuelleSeite = "";
	
	Account user;
	HashMap<String, Vector<Nachricht>> alleNachrichten;
	
	String[] aktChatReihenfolge;
	String aktChatPartner;
	public boolean loginSuccess = false;
	
	public AccountBean() throws ClassNotFoundException, SQLException {
		this.allAccounts = new Vector<Account>();
		this.dbConn = new PostgreSQLAccess().getConnection(); 
		
		this.user = new Account();
		this.alleNachrichten = new HashMap<String, Vector<Nachricht>>();
		
		this.readAllAccountsFromDB();
		this.aktChatReihenfolge = new String[0];
		this.aktChatPartner = "";
	}
	
	
	
	
	
	
	
	
	// Abschnitt Account
	
	
	
	
	
	public void readAllAccountsFromDB() throws SQLException {
		String sql = "SELECT email, vorname, nachname, passwort, active, admin FROM account";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		
		ResultSet dbRes = prep.executeQuery();
		while(dbRes.next()) {
			String email = dbRes.getString("email");
			String vorname = dbRes.getString("vorname");
			String nachname = dbRes.getString("nachname");
			String passwort = dbRes.getString("passwort");
			String active = dbRes.getString("active");
			String admin = dbRes.getString("admin");
			
			this.allAccounts.add(new Account(vorname, nachname, email, active, admin));
		}
	}
	
	public boolean saveAccount(String email, String vorname, String nachname, String passwort) throws SQLException {
		if(this.checkAccountExists(email)) {
			System.out.println("Account mit dieser Email existiert bereits");
			return false;
		}
		
		try {
			String sql ="insert into account (email, vorname, nachname, passwort, active, admin) "
					+ "values (?,?,?,?,?,?)";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, email);
			prep.setString(2, vorname);
			prep.setString(3, nachname);
			prep.setString(4, passwort);
			prep.setString(5, "yes");
			prep.setString(6, "no");
			prep.executeUpdate();
			System.out.println("Account erfolgreich eingefuegt");	
			
			return true;
		}
		catch(Exception e) {
			System.out.println("Ein unerwarteter Fehler ist aufgetreten. Bitte wenden sie sich an einen Admin");
			e.printStackTrace();
			return false;
		}
		
	}
	
	public boolean checkAccountExists(String email) throws SQLException {
		String sql = "select email from account where email = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, email);
		ResultSet dbRes = prep.executeQuery();
		return dbRes.next(); 
	}
	
	public boolean login(String email, String passwort) throws SQLException {
		String sql = "select email, vorname, nachname, passwort, active, admin "
				+ "from account where email = ?";
		System.out.println(sql);
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, email);
		ResultSet dbRes = prep.executeQuery();
		if(dbRes.next()) {		
			if(dbRes.getString("passwort").equals(passwort)) {
				user.setEmail(email);
				user.setVorname(dbRes.getString("vorname"));
				user.setNachname(dbRes.getString("nachname"));
				user.setActive(dbRes.getString("active"));
				user.setAdmin(dbRes.getString("admin"));
				user.setLogedIn(true);
				return true;
			}
			else
				return false;
		}
		else
			return false;
		
	}

	
	public void abmelden() {
		this.allAccounts = new Vector<Account>();	
		this.user = new Account();
		this.alleNachrichten = new HashMap<String, Vector<Nachricht>>();
		this.aktChatReihenfolge = new String[0];
		this.aktChatPartner = "";
		System.out.println("Erfolgreich abgemeldet");
	}
	
	public String getInserierenLink() {
	    return "./NavbarAppl.jsp?action=zumInserieren";
	}

	

	

	
	
	
	
	
	
	
	
	
	
	
	// Abschnitt Nachrichten / Posteingang
	
	
	
	
	// Methode um vom aktuell eingelogten User alle Chatverläufe zu lesen (in Reihenfolge: Alt -> neu) und in eine Hashmap zu speichern mit allen wichtigen informationen
	public void readAlleNachrichtenFromDB() throws SQLException {
		this.alleNachrichten.clear();
		String sql = "SELECT sender_email, empfaenger_email, datum, uhrzeit, nachricht FROM chatlog WHERE sender_email = ? OR empfaenger_email = ? "
				+ "ORDER BY datum ASC, uhrzeit ASC";
		PreparedStatement prep = this.dbConn.prepareStatement(sql);
		prep.setString(1, this.user.getEmail());
		prep.setString(2, this.user.getEmail());
		
		 ResultSet dbRes = prep.executeQuery();
	        while (dbRes.next()) {
	            String sender = dbRes.getString("sender_email");
	            String empfaenger = dbRes.getString("empfaenger_email");
	            String nachricht = dbRes.getString("nachricht");
	            LocalDate datum = dbRes.getDate("datum").toLocalDate();
	            LocalTime uhrzeit = dbRes.getTime("uhrzeit").toLocalTime();
	            LocalDateTime zeitpunkt = LocalDateTime.of(datum, uhrzeit);


	            
	            // Erstmal überprüfen ob der aktuelle user der sender oder der empfaenger der Nachricht ist
	            // User ist sender 
	            if(this.user.getEmail().equals(sender)) {
	            	// Überprüfen ob der andere User bereits in die HashMap gespeichert wurde falls nicht dann neuen Eintrag erstellen und neuen Vector
	            	if(this.alleNachrichten.containsKey(empfaenger)) {
	            		this.alleNachrichten.get(empfaenger).add(new Nachricht(nachricht, datum, uhrzeit, zeitpunkt, sender, empfaenger));
	            	}
	            	// ersten Eintrag vom anderen User erstellen
	            	else {
	            		Vector<Nachricht> nachrichten = new Vector<Nachricht>();
	            		nachrichten.add(new Nachricht(nachricht, datum, uhrzeit, zeitpunkt, sender, empfaenger));
	            		this.alleNachrichten.put(empfaenger, nachrichten);
	            	}
	            }
	            // User ist empfaenger
	            else {
	            	if(this.alleNachrichten.containsKey(sender)) {
	            		this.alleNachrichten.get(sender).add(new Nachricht(nachricht, datum, uhrzeit, zeitpunkt, sender, empfaenger));
	            	}
	            	// ersten Eintrag vom anderen User erstellen
	            	else {
	            		Vector<Nachricht> nachrichten = new Vector<Nachricht>();
	            		nachrichten.add(new Nachricht(nachricht, datum, uhrzeit, zeitpunkt, sender, empfaenger));
	            		this.alleNachrichten.put(sender, nachrichten);
	            	}
	            }
	        }
	       this.sortChats();
	}
	
	// Methode um die Reihenfolge der Chats festzulegen. Dadurch soll beim öffnen des Posteingagns die aktuellsten Nachrichten immer oben stehen.
	public void sortChats() {
		this.aktChatReihenfolge = new String[this.alleNachrichten.size()];
		int counter = 0;
		
		// Chats in das Array speichern
		for(String user: this.alleNachrichten.keySet()) {
			this.aktChatReihenfolge[counter] = user;
			counter++;
		}
		
		
		// Sortieren des Chats mit Bubble Sort
		int n = this.aktChatReihenfolge.length;
		boolean swapped;
		for (int i = 0; i < n - 1; i++) {
			swapped = false; // Track if any swap happens in this pass
			for (int j = 0; j < n - 1 - i; j++) {
				Vector<Nachricht> nachrichten1 = this.alleNachrichten.get(this.aktChatReihenfolge[j]);
				Vector<Nachricht> nachrichten2 = this.alleNachrichten.get(this.aktChatReihenfolge[j+1]);
				// Die aktuellsten Nachrichten werden raus genommen um verglichen zu werden
				LocalDateTime zeitUser1 = nachrichten1.get(nachrichten1.size()-1).getZeitpunkt();
				LocalDateTime zeitUser2 = nachrichten2.get(nachrichten2.size()-1).getZeitpunkt();
				
				System.out.println("user1: " + this.aktChatReihenfolge[j] + "||" + "user2: " + this.aktChatReihenfolge[j+1]);
				System.out.println("User1: " + zeitUser1 + "|| User2: " + zeitUser2);
				System.out.println("Zeitpunkt 2 größer 1 ? " + zeitUser2.isAfter(zeitUser1));
				
				if (zeitUser2.isAfter(zeitUser1)) { 
					String temp = this.aktChatReihenfolge[j];
					this.aktChatReihenfolge[j] = this.aktChatReihenfolge[j + 1];
					this.aktChatReihenfolge[j + 1] = temp; 
					swapped = true;
				}
			}
			if (!swapped)
				break;
		}
		
		
		
		for(int i = 0; i < this.aktChatReihenfolge.length; i++) {
			String user = this.aktChatReihenfolge[i];
			System.out.println(i+1 + ". User: " + user);
			Vector<Nachricht> nachrichten = this.alleNachrichten.get(user);
			for(Nachricht nachricht : nachrichten) {
				System.out.println(nachricht.getSender() + " -> " + nachricht.getEmpfaenger() + " || " + nachricht.getDatum() + " " + nachricht.getUhrzeit() + " || " + nachricht.getText());
			}
		}
		
		if(this.aktChatReihenfolge.length > 0) {
			this.aktChatPartner = this.aktChatReihenfolge[0];
		}
		
	}
	
	// Methode um eine neue Nachricht zu senden
	public void sendMessage(String text) {
		
		LocalDate date = LocalDate.now();
		LocalTime time = LocalTime.now();
		
		try {
			String sql ="insert into chatlog (sender_email, empfaenger_email, datum, uhrzeit, nachricht) "
					+ "values (?,?,?,?,?)";
			System.out.println(sql);
			PreparedStatement prep = this.dbConn.prepareStatement(sql);
			prep.setString(1, this.getEmail());
			prep.setString(2, this.aktChatPartner);
			prep.setDate(3, java.sql.Date.valueOf(date));
			prep.setTime(4, java.sql.Time.valueOf(time));
			prep.setString(5, text);
			prep.executeUpdate();
			System.out.println("Nachricht erfolgreich eingefuegt");	
			
		}
		catch(Exception e) {
			System.out.println("Ein unerwarteter Fehler ist aufgetreten. Bitte wenden sie sich an einen Admin");
			e.printStackTrace();
		}
	}
	

	
	

	
	
	
	// Abschnitt getHtml
	
	public String getProfilHtml() {
	    if (!this.getLogedIn()) {
	        return "";
	    }

	    String html = "<section class='py-5'>"
	            + "<div class='container'>"
	            + "<div class='row justify-content-center'>"
	            + "<div class='col-md-6'>"
	            + "<div class='card shadow-sm border-0'>"
	            + "<div class='card-body'>"
	            + "<h5 class='card-title fw-bold mb-3'>Mein Profil</h5>"
	            + "<ul class='list-group list-group-flush'>"
	            + "<li class='list-group-item'><strong>Name:</strong> "
	            + this.getVorname() + " " + this.getNachname() + "</li>"
	            + "<li class='list-group-item'><strong>E-Mail:</strong> "
	            + this.getEmail() + "</li>"
	            + "<li class='list-group-item'><strong>Status:</strong> "
	            + "<span class='badge bg-success'>Aktiv</span></li>"
	            + "</ul>"
	            + "<div class='mt-3 text-end'>"
	            + "<a href='./NavbarAppl.jsp?action=profilBearbeiten' class='btn btn-sm btn-outline-secondary'>"
	            + "Profil bearbeiten</a>"
	            + "</div>"
	            + "</div></div></div></div></div></section>";

	    return html;
	}
	
	public String getNavbarHtml() {

	    String html = "<nav class='navbar navbar-expand-lg navbar-dark bg-dark shadow-sm py-2'>"
	            + "<div class='container'>"
	            + "<a class='navbar-brand d-flex align-items-center fw-semibold fs-4' "
	            + "href='./NavbarAppl.jsp?action=zurHomepage'>"

	            + "<img src='../img/flexboard-logo.jpg' "
	            + "alt='FlexBoard Logo' "
	            + "height='48' "
	            + "class='me-3'>"

	            + "FlexBoard"
	            + "</a>"

	            + "<button class='navbar-toggler' type='button' data-bs-toggle='collapse' "
	            + "data-bs-target='#navbarNav'>"
	            + "<span class='navbar-toggler-icon'></span>"
	            + "</button>"

	            + "<div class='collapse navbar-collapse' id='navbarNav'>"

	            // Linke Navigation
	            + "<ul class='navbar-nav me-auto'>";

	    // NUR wenn eingeloggt → diese Links anzeigen
	    if (this.getLogedIn()) {
	        html += "<li class='nav-item'>"
	              + "<a class='nav-link "
	              + (this.aktuelleSeite.equals("inserieren") ? "active fw-bold" : "")
	              + "' href='./NavbarAppl.jsp?action=zumInserieren'>Inserieren</a>"
	              + "</li>"

	              + "<li class='nav-item'>"
	              + "<a class='nav-link "
	              + (this.aktuelleSeite.equals("suche") ? "active fw-bold" : "")
	              + "' href='./NavbarAppl.jsp?action=zurSuche'>Jetzt finden</a>"
	              + "</li>"

	              + "<li class='nav-item'>"
	              + "<a class='nav-link "
	              + (this.aktuelleSeite.equals("post") ? "active fw-bold" : "")
	              + "' href='./NavbarAppl.jsp?action=zurPost'>Posteingang</a>"
	              + "</li>";
	    }

	    html += "</ul>"

	            // Rechte Navigation
	            + "<ul class='navbar-nav ms-auto'>";

	    if (!this.getLogedIn()) {
	        html += "<li class='nav-item'>"
	              + "<a class='nav-link' href='./NavbarAppl.jsp?action=zumLogin'>Login</a>"
	              + "</li>"
	              + "<li class='nav-item'>"
	              + "<a class='nav-link' href='./NavbarAppl.jsp?action=zurReg'>Registrieren</a>"
	              + "</li>";
	    } else {
	        html += "<li class='nav-item dropdown'>"
	              + "<a class='nav-link dropdown-toggle' href='#' role='button' "
	              + "data-bs-toggle='dropdown'>"
	              + this.getVorname()
	              + "</a>"
	              + "<ul class='dropdown-menu dropdown-menu-end'>"
	              + "<li><a class='dropdown-item' href='./NavbarAppl.jsp?action=profil'>Profil</a></li>"
	              + "<li><hr class='dropdown-divider'></li>"
	              + "<li><a class='dropdown-item text-danger' "
	              + "href='./NavbarAppl.jsp?action=abmelden'>Abmelden</a></li>"
	              + "</ul>"
	              + "</li>";
	    }

	    html += "</ul></div></div></nav>";

	    return html;
	}
	
	public String getNachrichtenHtml() throws SQLException {
		String html = "<div class='container-fluid chat-container'>"
			    + "<div class='row h-100'>"
			    + "<div class='col-12 col-md-4 col-lg-3 chat-list p-0'>"
			    + "<div class='list-group list-group-flush'>";
			    
		
			    
			   html += this.getChatSeitenanzeige();	
			    
			   html += "</div>"
			    + "</div>";
			    		
			    
			   html += "<div class='col-12 col-md-8 col-lg-9 d-flex flex-column p-0'>"
			    + "<div class='border-bottom p-3 fw-bold'>" + this.getNameFromUser(this.aktChatPartner) +"</div>"
			    + "<div class='chat-messages flex-grow-1'>";
			   
			   
			   
			   html += this.getChatverlauf();
			    
			   html += "</div>"
			    + "<div class='border-top p-3'>"
			    + "<div class='input-group'>"
			    + "<form action='./NachrichtenAppl.jsp' method='get'>"
			    + "<input class='form-control' type='text' name='text' value='' placeholder='Nachricht schreiben...'>"
			    + "<input class='btn btn-primary' type='submit' name='action' value='Senden'>"
			    + "</form>"
			    + "</div>"
			    + "</div>"
			    + "</div>"
			    + "</div>"
			    + "</div>";
		
		
		return html;
	}
	
	
	
	
	// Abschnitt Hilfsmethoden für getHtml
	
	// Methode um den Vor- und Nachnamen der Chatpartner zu bekommen
	public String getNameFromUser(String user) {
		String name = "";
		for(Account account : this.allAccounts) {
			if(account.getEmail().equals(user))
				name = account.getVorname() + " " + account.getNachname();
		}
		return name;
	}
	
	// Methode um den Chatverlauf zwischen dem aktuellen ChatPartner zu erzeugen
	public String getChatverlauf() {
		String html = "";
		for(Nachricht nachricht : this.alleNachrichten.get(this.aktChatPartner)) {
			   if(this.aktChatPartner.equals(nachricht.getSender()))
				   html += "<div class='bg-light rounded p-2 mb-2 message-left'>" + nachricht.getText() + "</div>";
			   else
				   html += "<div class='bg-primary text-white rounded p-2 mb-2 message-right'>" + nachricht.getText() + "</div>";
		   }
		return html;
	}
	
	public String getChatSeitenanzeige() {
		String html = "";
		
		for(int i = 0; i < this.aktChatReihenfolge.length; i++) {
			if(this.aktChatPartner.equals(this.aktChatReihenfolge[i]))
				html += "<a href='./NachrichtenAppl.jsp?action=switch&user=" + this.aktChatReihenfolge[i] + "' class='list-group-item list-group-item-action active'>" 
					 + this.getNameFromUser(this.aktChatReihenfolge[i]) + "</a>";
			else
				html += "<a href='./NachrichtenAppl.jsp?action=switch&user=" + this.aktChatReihenfolge[i] + "' class='list-group-item list-group-item-action'>" 
						 + this.getNameFromUser(this.aktChatReihenfolge[i]) + "</a>";
				
		}
		
		return html;
	}
	
	// Abschnitt Getter und Setter
	
	
	


	public String getAktChatPartner() {
		return aktChatPartner;
	}
	public void setAktChatPartner(String aktChatPartner) {
		this.aktChatPartner = aktChatPartner;
	}
	public Account getUser() {
		return this.user;
	}
	public void setUser(Account user) {
		this.user = user;
	}
	public String getVorname() {
		return user.getVorname();
	}
	public void setVorname(String vorname) {
		user.setVorname(vorname);
	}
	public String getNachname() {
		return user.getNachname();
	}
	public void setNachname(String nachname) {
		user.setNachname(nachname);
	}
	public String getEmail() {
		return user.getEmail();
	}
	public void setEmail(String email) {
		user.setEmail(email);
	}
	public String getActive() {
		return user.getActive();
	}
	public void setActive(String active) {
		user.setActive(active);
	}
	public String getAdmin() {
		return user.getAdmin();
	}
	public void setAdmin(String admin) {
		user.setAdmin(admin);
	}
	public boolean getLogedIn() {
		return user.isLogedIn();
	}
	public void setLogedIn(boolean logedIn) {
		user.setLogedIn(logedIn);
	}

	public String getAktuelleSeite() {
		return aktuelleSeite;
	}


	public void setAktuelleSeite(String aktuelleSeite) {
		this.aktuelleSeite = aktuelleSeite;
	}
	public void setLoginSuccess(boolean loginSuccess) {
	    this.loginSuccess = loginSuccess;
	}

	public boolean getLoginSuccess() {
	    return loginSuccess;
	}
}
