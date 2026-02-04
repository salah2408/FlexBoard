package de.hwg_lu.bwi520.datenbank;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.hwg_lu.bwi.jdbc.PostgreSQLAccess;

public class AccountTest {

	Connection dbConn;
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		AccountTest myObj = new AccountTest();
		myObj.doSomething();
	}

	
	public void doSomething() throws ClassNotFoundException, SQLException {
		this.dbConn = new PostgreSQLAccess().getConnection();
		this.createTableAccount();
		this.showAllAccounts();
//		this.deleteAllAccounts();
	}
	
	public void createTableAccount() throws ClassNotFoundException, SQLException {
		String sql = "CREATE TABLE IF NOT EXISTS account("
				+ "email VARCHAR(64) PRIMARY KEY, "
				+ "vorname VARCHAR(32) NOT NULL, "
				+ "nachname VARCHAR(32) NOT NULL, "
				+ "passwort VARCHAR(32) NOT NULL, "
				+ "active VARCHAR(3) NOT NULL, "
				+ "admin VARCHAR(3) NOT NULL);";
		
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Tabelle Account müsste jetzt existieren");
		
	}
	
	public void showAllAccounts() throws SQLException {
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
			
			System.out.println(email + "||" + vorname + "||" + nachname + "||" + passwort + "||" + active + "||" + admin + "||");
		}
	}
	
	public void deleteAllAccounts() throws SQLException {
		String sql = "DELETE FROM account";
		PreparedStatement prep = dbConn.prepareStatement(sql);
		prep.executeUpdate();
		System.out.println("Alle Accounts müssten geloescht sein");
	}
}
