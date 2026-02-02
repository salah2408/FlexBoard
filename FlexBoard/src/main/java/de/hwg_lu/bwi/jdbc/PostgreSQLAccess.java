package de.hwg_lu.bwi.jdbc;

import java.sql.SQLException;

public class PostgreSQLAccess extends JDBCAccess {

	public PostgreSQLAccess() throws ClassNotFoundException, SQLException {
		super(); //Aufruf JDBCAccess(): setParm, createConn, setSchema
	}

	public void setDBParameter() {
		this.dbDrivername = "org.postgresql.Driver";
		this.dbProt       = "jdbc:postgresql";
		this.dbServer     = "143.93.200.243";
		this.dbPort       = "5435";
		this.dbName       = "BWUEBDB";
		this.dbUserid     = "user1";
		this.dbPassword   = "pgusers";
		this.dbSchema     = "bwi520_638584";
	}
	public void setSchema() throws ClassNotFoundException, SQLException {
		String sql = "SET Schema '" + dbSchema + "'";
		System.out.println(sql);
		this.dbConn.prepareStatement(sql).executeUpdate();
		System.out.println("Schema " + dbSchema + " gesetzt");
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new PostgreSQLAccess();
	}
}
