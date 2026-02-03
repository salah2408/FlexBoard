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
		String sql = "select email, vorname, nachname, active, admin from account where email = ?";
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
}
