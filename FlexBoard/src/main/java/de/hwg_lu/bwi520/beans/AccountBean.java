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
	
	Account user;
	HashMap<String, Vector<Nachricht>> alleNachrichten;
	
	String aktChatPartner;
	
	public AccountBean() throws ClassNotFoundException, SQLException {
		this.allAccounts = new Vector<Account>();
		this.dbConn = new PostgreSQLAccess().getConnection(); 
		
		this.user = new Account();
		this.alleNachrichten = new HashMap<String, Vector<Nachricht>>();
		
		this.aktChatPartner = "";
		this.readAllAccountsFromDB();
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

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Abschnitt Nachrichten / Posteingang
	
	
	
	
	// Methode um vom aktuell eingelogten User alle Chatverläufe zu lesen (in Reihenfolge: Alt -> neu) und in eine Hashmap zu spiechern mit allen wichtigen informationen
	public void readAlleNachrichtenFromDB() throws SQLException {
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


	            System.out.println(sender + " -> " + empfaenger + " || " + datum + uhrzeit + " || " + nachricht);
	            
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
	       this.setAktChatPartner(this.searchAktuellsterChat()); 
	}
	
	//Methode um den aktuellsten CHat zu identifizieren. Dieser soll später automatisch geöffnet sein beim Laden des Posteingangs
	public String searchAktuellsterChat() {
		String aktChat = "";
		
		// Einen zufälligen Chat als aktChat auswählen, da HashMaps nicht nach der Reihe gespeichert werden
		for(String user: this.alleNachrichten.keySet()) {
			aktChat = user;
			break;
		}
		
		// Suche nach aktuellstem Chat
		for(String user : this.alleNachrichten.keySet()) {
			Vector<Nachricht> nachrichten = this.alleNachrichten.get(user);
			Vector<Nachricht> nachrichtenAktChat = this.alleNachrichten.get(aktChat);
			// Die aktuellsten Nachrichten werden raus genommen um verglichen zu werden
			LocalDateTime zeitUser = nachrichten.get(nachrichten.size()-1).getZeitpunkt();
			LocalDateTime zeitAktChat = nachrichtenAktChat.get(nachrichtenAktChat.size()-1).getZeitpunkt();
			
			// Die Nachricht des aktuellen User ist aktueller
			if(zeitAktChat.isBefore(zeitUser)) {
				aktChat = user;
			}
			
		}
		System.out.println(aktChat);
		
		
		return aktChat;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// Abschnitt getHtml
	
	
	
	public String getNavbarHtml() {
		String html = "<nav class='navbar navbar-expand-lg bg-body-tertiary'>"
				+ "  <div class='container-fluid'>"
				+ "    <a class='navbar-brand' href='./NavbarAppl.jsp?action=zurHomepage'>FlexBoard</a>";
				
		
		if(!user.isLogedIn()) {
			html += "    <div class='d-flex align-items-center ms-auto me-2 order-lg-3'>"
					+ "      <a class='nav-link px-2' href='./NavbarAppl.jsp?action=zumLogin'>Login</a>"
					+ "      <span class='text-muted'>|</span>"
					+ "      <a class='nav-link px-2' href='./NavbarAppl.jsp?action=zurReg'>Registrieren</a>"
					+ "    </div>";
		}
			html += "    <button class='navbar-toggler' type='button' data-bs-toggle='collapse' data-bs-target='#navbarSupportedContent'>"
				+ "      <span class='navbar-toggler-icon'></span>"
				+ "    </button>"
				+ "    <div class='collapse navbar-collapse' id='navbarSupportedContent'>"
				+ "      <ul class='navbar-nav me-auto mb-2 mb-lg-0'>"
				+ "        <li class='nav-item'>"
				+ "          <a class='nav-link active' href='./NavbarAppl.jsp?action=zumInserieren'>Inserieren</a>"
				+ "        </li>"
				+ "        <li class='nav-item'>"
				+ "          <a class='nav-link active' href='./NavbarAppl.jsp?action=zurSuche'>Jetzt finden</a>"
				+ "        </li>"
				+ "        <li class='nav-item'>"
				+ "          <a class='nav-link active' href='./NavbarAppl.jsp?action=zurPost'>Posteingang</a>"
				+ "        </li>"
				+ "      </ul>"
				+ "    </div>"
				+ "  </div>"
				+ "</nav>";
		
		
		return html;
	}
	
	
	public String getNachrichtenHtml() {
		String html = "<div class='container-fluid chat-container'>"
			    + "<div class='row h-100'>"
			    + "<div class='col-12 col-md-4 col-lg-3 chat-list p-0'>"
			    + "<div class='list-group list-group-flush'>";
			    
		
			    html += "<a href='#' class='list-group-item list-group-item-action active'>Max Mustermann</a>"
			    + "<a href='#' class='list-group-item list-group-item-action'>Anna Schmidt</a>"
			    + "<a href='#' class='list-group-item list-group-item-action'>Peter Müller</a>"
			    + "<a href='#' class='list-group-item list-group-item-action'>Julia Weber</a>";
			    
			    html += "</div>"
			    + "</div>"
			    + "<div class='col-12 col-md-8 col-lg-9 d-flex flex-column p-0'>"
			    + "<div class='border-bottom p-3 fw-bold'>Max Mustermann</div>"
			    + "<div class='chat-messages flex-grow-1'>"
			    + "<div class='bg-light rounded p-2 mb-2 message-left'>Hallo! Wie geht’s?</div>"
			    + "<div class='bg-primary text-white rounded p-2 mb-2 message-right'>Alles gut 😊 und bei dir?</div>"
			    + "<div class='bg-light rounded p-2 mb-2 message-left'>Super! Hast du kurz Zeit?</div>"
			    + "</div>"
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
	
	
	
	
	
	
	
	
	
	
	
	// Abschnitt Getter und Setter
	
	
	
	public String getAktChatPartner(){
		return this.aktChatPartner;
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
}
