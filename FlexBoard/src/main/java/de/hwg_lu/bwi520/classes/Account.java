package de.hwg_lu.bwi520.classes;

public class Account {
	String vorname;
	String nachname;
	String email;
	String active;
	String admin;
	boolean logedIn;
	
	
	public Account(String vorname, String nachname, String email, String active, String admin, boolean logedIn) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.active = active;
		this.admin = admin;
		this.logedIn = logedIn;
	}
	
	public Account() {
		this.vorname = "";
		this.nachname = "";
		this.email = "";
		this.active = "";
		this.admin = "";
		this.logedIn = false;
	}
	
	
	public Account(String vorname, String nachname, String email, String active, String admin) {
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.active = active;
		this.admin = admin;
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
	public boolean isLogedIn() {
		return logedIn;
	}
	public void setLogedIn(boolean logedIn) {
		this.logedIn = logedIn;
	}
}
