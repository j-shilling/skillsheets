package com.shilling.skillsheets.model;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
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
		public static Action create (@JsonProperty ("value") int value) {
			for (Action act : Action.values())
				if (act.getValue() == value)
					return act;
			
			return null;
		}
	}
	
	private final Logger logger;
	
	private final Integer id;
	private final Instant timestamp;
	private final String msg;
	private final Notification.Action[] acts;
	
	@JsonCreator
	public Notification (
			@JsonProperty ("id") @Nullable Integer id,
			@JsonProperty ("timestamp") @Nullable String timestamp,
			@JsonProperty ("text") String msg, 
			@JsonProperty("actions") Notification.Action...acts) {
		Preconditions.checkNotNull(msg);
		Preconditions.checkNotNull(acts);
		
		this.logger = LogManager.getLogger(Notification.class);
		
		this.id = id;
		this.msg = msg;
		this.acts = acts;
		
		Instant time = null;
		
		if (timestamp != null) {
			try {
				time = Instant.parse(timestamp);
			} catch (DateTimeParseException e) {
				this.logger.error("Could not parse timestamp \""
						+ timestamp + "\"");
				this.logger.error(e.getMessage());
			}
		}
		
		if (time == null)
			this.timestamp = Instant.now();
		else
			this.timestamp = time;
	}
	
	public Notification (String msg) {
		this (null, null, msg, new Notification.Action[] {
				Action.OK
		});
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
	public Optional<Integer> getId() {
		return Optional.ofNullable(this.id);
	}
	
	@JsonProperty ("timestamp")
	public String getTimestamp() {
		return this.timestamp.toString();
	}
	
	public Notification setId(int id) {
		return new Notification (id, this.getTimestamp(), this.getMessage(), this.getAction());
	}
}
