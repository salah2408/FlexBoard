package de.hwg_lu.bwi520.beans;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;
import de.hwg_lu.bwi520.classes.Account;

public class AccountBean {
	Vector<Account> allAccounts;
	Connection dbConn;
	
	String vorname;
	String nachname;
	String email;
	String active;
	String admin;
	
	boolean logedIn;
	
	public AccountBean() throws ClassNotFoundException, SQLException {
		this.allAccounts = new Vector<Account>();
		this.dbConn = new PostgreSQLAccess().getConnection(); 
		
		this.vorname = "";
		this.nachname = "";
		this.email = "";
		this.active = "";
		this.admin = "";
		
		this.logedIn = false;
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
				this.email = email;
				this.vorname = dbRes.getString("vorname");
				this.nachname = dbRes.getString("nachname");
				this.active = dbRes.getString("active");
				this.admin = dbRes.getString("admin");
				this.logedIn = true;
				return true;
			}
			else
				return false;
		}
		else
			return false;
		
	}

	
	public String getNavbarHtml() {
		String html = "<nav class='navbar navbar-expand-lg bg-body-tertiary'>"
				+ "  <div class='container-fluid'>"
				+ "    <a class='navbar-brand' href='./NavbarAppl.jsp?action=zurHomepage'>FlexBoard</a>";
				
		
		if(!this.logedIn) {
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
	
	
	
	
	
	public String getVorname() {
		return vorname;
	}
	public void setVorname(String vorname) {
		this.vorname = vorname;
	}
	public String getNachname() {
		return nachname;
	}
	public void setNachname(String nachname) {
		this.nachname = nachname;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public String getAdmin() {
		return admin;
	}
	public void setAdmin(String admin) {
		this.admin = admin;
	}
	public boolean getLogedIn() {
		return logedIn;
	}
	public void setLogedIn(boolean logedIn) {
		this.logedIn = logedIn;
	}
}
