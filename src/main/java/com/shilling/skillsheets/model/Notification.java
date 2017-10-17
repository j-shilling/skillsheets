package com.shilling.skillsheets.model;

public class Notification {
	private final String msg;
	
	public Notification (String msg) {
		this.msg = msg;
	}
	
	public String getMessage() {
		return this.msg;
	}
}
