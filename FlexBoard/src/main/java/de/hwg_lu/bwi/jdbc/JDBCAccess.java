package de.hwg_lu.bwi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class JDBCAccess {
	
	String dbDrivername;
	String dbProt;
	String dbServer;
	String dbPort;
	String dbName;
	String dbUserid;
	String dbPassword;
	String dbSchema;
	
	Connection dbConn; //erstmal null

	public JDBCAccess() throws ClassNotFoundException, SQLException {
		this.setDBParameter();
		this.createConnection();
		this.setSchema();
	}
	
	public abstract void setDBParameter();

	public void createConnection() throws ClassNotFoundException, SQLException {
		Class.forName(dbDrivername);
		String dbURL = dbProt + "://" + dbServer + ":" + dbPort + "/" + dbName;
		this.dbConn = DriverManager.getConnection(
					dbURL,
					dbUserid,
					dbPassword
				);
	}
	public abstract void setSchema() throws ClassNotFoundException, SQLException;
	
	public Connection getConnection() {
		return this.dbConn;
	}
}
