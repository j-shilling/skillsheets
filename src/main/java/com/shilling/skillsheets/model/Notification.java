package com.shilling.skillsheets.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {
	
	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public static enum Action {
		OK ("Ok", 0, "w3-blue"),
		ACCEPT ("Accept", 1, "w3-green"),
		REJECT ("Reject", 2, "w3-red");
		
		private final String text;
		private final int value;
		private final String color;
		
		private Action (String text, int value, String color) {
			this.text = text;
			this.value = value;
			this.color = color;
		}
		
		@JsonProperty ("text")
		public String getText() {
			return this.text;
		}
		
		@JsonProperty ("value")
		public int getValue() {
			return this.value;
		}
		
		@JsonProperty ("color")
		public String getColor() {
			return this.color;
		}
		
		@Override
		public String toString() {
			return this.getText();
		}
		
		@JsonCreator
		public static Action create (int value) {
			for (Action act : Action.values())
				if (act.getValue() == value)
					return act;
			
			return null;
		}
	}
	
	private final int id;
	private final String msg;
	private final Notification.Action[] acts;
	
	@JsonCreator
	public Notification (
			@JsonProperty ("id") int id,
			@JsonProperty ("text") String msg, 
			@JsonProperty("actions") Notification.Action...acts) {
		this.msg = msg;
		this.acts = acts;
		this.id = id;
	}
	
	@JsonProperty ("text")
	public String getMessage() {
		return this.msg;
	}
	
	@JsonProperty ("actions") 
	public Action[] getAction() {
		return this.acts;
	}
	
	@JsonProperty ("id")
	public int getId() {
		return this.id;
	}
}
