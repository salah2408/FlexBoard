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
	
	public AccountBean() throws ClassNotFoundException, SQLException {
		this.allAccounts = new Vector<Account>();
		this.dbConn = new PostgreSQLAccess().getConnection(); 
	}
	
	public boolean saveAccount(String email, String vorname, String nachname, String passwort) throws SQLException {
		if(this.checkAccountExists(email))
			return false;
		
		try {
			String sql ="insert into account (email, vorname, nachname, passwort, acitve, admin) "
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
}
