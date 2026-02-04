package de.hwg_lu.bwi520.messages;

public class RegMessage {
	String msg;
	String action;
	
	public RegMessage() {
		this.msg = "Willkommen zum BWI 520 FlexBoard";
		this.action = "Konto erstellen";
	}
	
	
	public void setRegMessage() {
		this.msg = "Willkommen zum BWI 520 FlexBoard";
		this.action = "Konto erstellen";
	}

	public void setAccountExists(String email) {
		this.msg = "Der Account mit der Email: " + email + " existiert bereits";
		this.action = "Bitte melden sie sich an";
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
