package de.hwg_lu.bwi520.classes;

public class Account {
	String vorname;
	String nachname;
	String email;
	String passwort;
	
	
	public Account() {
		this.vorname = "";
		this.nachname = "";
		this.email = "";
		this.passwort = "";
	}
	
	public Account(String vorname, String nachname, String email, String passwort) {
		super();
		this.vorname = vorname;
		this.nachname = nachname;
		this.email = email;
		this.passwort = passwort;
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
	public String getPasswort() {
		return passwort;
	}
	public void setPasswort(String passwort) {
		this.passwort = passwort;
	}
}
