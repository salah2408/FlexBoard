package de.hwg_lu.bwi520.classes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Nachricht {
	String text;
	LocalDate datum;
	LocalTime uhrzeit;
	LocalDateTime zeitpunkt;
	String sender;
	String empfaenger;
	
	public Nachricht(String text, LocalDate datum, LocalTime uhrzeit, LocalDateTime zeitpunkt, String sender, String empfaenger) {
		this.text = text;
		this.datum = datum;
		this.uhrzeit = uhrzeit;
		this.zeitpunkt = zeitpunkt;
		this.sender = sender;
		this.empfaenger = empfaenger;
	}


	
	
	
	
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public LocalDate getDatum() {
		return datum;
	}
	public void setDatum(LocalDate datum) {
		this.datum = datum;
	}
	public LocalTime getUhrzeit() {
		return uhrzeit;
	}
	public void setUhrzeit(LocalTime uhrzeit) {
		this.uhrzeit = uhrzeit;
	}
	public LocalDateTime getZeitpunkt() {
		return zeitpunkt;
	}
	public void setZeitpunkt(LocalDateTime zeitpunkt) {
		this.zeitpunkt = zeitpunkt;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getEmpfaenger() {
		return empfaenger;
	}
	public void setEmpfaenger(String empfaenger) {
		this.empfaenger = empfaenger;
	}
}
