package de.hwg_lu.bwi520.beans;

public class WeiterleitungsBean {
	String link;
	
	public WeiterleitungsBean() {
		this.link = "./HomepageView.jsp";
	}

	
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
