package de.hwg_lu.bwi520.beans;

public class MessageBean {
	String msg;
	String action;
	
	public MessageBean() {
		this.msg = "Willkommen am BWI 520 FlexBoard";
		this.action = "Viel spaß beim durchstöbern";
	}
	
	
	public void setRegMessage() {
		this.msg = "Willkommen zum FlexBoard";
		this.action = "Bitte Registrieren sie sich";
	}


	
	
	
	
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}	
}
