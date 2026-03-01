package de.hwg_lu.bwi520.classes;

import java.sql.Date;
import java.time.LocalDate;

import org.json.JSONObject;

public class Listing {
	String id;
	String userid;
	int catid;
	String title;
	String descr;
	String city;
	String status;
	Date date;
	JSONObject detailsJson;
	
	
	public Listing(String userid, int catid, String title, String descr, String city, String status, Date date, JSONObject detailsJson) {
		this.userid = userid;
		this.catid = catid;
		this.title = title;
		this.descr = descr;
		this.city = city;
		this.status = status;
		this.date = date;
		this.detailsJson = detailsJson;
	}
	
	public String getKategorieDetailsHtml() {
		String html = "";
		
		if(this.catid == 1) {
			
		}
		
		
		
		return html;
	}
}
