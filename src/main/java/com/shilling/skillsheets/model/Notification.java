package com.shilling.skillsheets.model;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Optional;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public abstract class Notification {
	
	public static enum Action {
		OK,
		ACCEPT,
		REJECT,
		CONFIRM,
		DENY
	}
	
	public static class Info extends Notification {
		
		private static final Action[] acts = new Action[] {
				Action.OK
		};
		
		public Info (
				@Nullable Integer id,
				@Nullable String timestamp,
				String msg) {
			super (id, timestamp, msg, Info.acts);
		}
		
		public Info (String msg) {
			this (null, null, msg);
		}
		
	}
	
	public static class Request extends Notification {
		
		private static final Action[] acts = new Action[] {
				Action.ACCEPT,
				Action.REJECT
		};
		
		public Request (
				@Nullable Integer id,
				@Nullable String timestamp,
				String msg) {
			super (id, timestamp, msg, Request.acts);
		}
		
		public Request (String msg) {
			this (null, null, msg);
		}
		
	}
	
	public static class Confirmation extends Notification {
		
		private static final Action[] acts = new Action[] {
				Action.CONFIRM,
				Action.DENY
		};
		
		public Confirmation (
				@Nullable Integer id,
				@Nullable String timestamp,
				String msg) {
			super (id, timestamp, msg, Confirmation.acts);
		}
		
		public Confirmation (String msg) {
			this (null, null, msg);
		}
		
	}
	
	@JsonCreator
	public static Notification create (
			@JsonProperty ("id") @Nullable Integer id,
			@JsonProperty ("timestamp") @Nullable String timestamp,
			@JsonProperty ("text") String msg, 
			@JsonProperty("actions") Notification.Action...acts) {
		
		Preconditions.checkNotNull(msg);
		Preconditions.checkNotNull(acts);
		
		if (acts.equals(Info.acts))
			return new Info (id, timestamp, msg);
		if (acts.equals(Request.acts))
			return new Request (id, timestamp, msg);
		if (acts.equals(Confirmation.acts))
			return new Confirmation (id, timestamp, msg);
		
		throw new IllegalArgumentException ("Cannot form a message for the actions " + acts);
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
		return Notification.create (id, this.getTimestamp(), this.getMessage(), this.getAction());
	}
}
