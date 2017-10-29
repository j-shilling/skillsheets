package com.shilling.skillsheets.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.repackaged.com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.NONE)
public class Notifications {
	
	private final Logger logger;
	private final Map<Integer, Notification> notifications;
	
	public Notifications() {
		this.logger = LogManager.getLogger(Notifications.class);
		this.notifications = new HashMap<>();
	}
	
	@JsonCreator
	public Notifications(@JsonProperty("notifications") Iterable<Notification> notifications) {
		this();
		
		Preconditions.checkNotNull(notifications);
		
		for (Notification e: notifications)
			this.add(e);
	}
	
	public boolean add (final Notification notification) {
		Preconditions.checkNotNull(notification);
		
		if (notification.getId().isPresent()) {
			if (this.notifications.containsKey(notification.getId().get())) {
				this.logger.warn("Notification ID is not unique; rejecting message.");
				return false;
			} else {
				this.notifications.put(notification.getId().get(), notification);
				return true;
			}
		} else {
			int i = 0;
			while (this.notifications.containsKey(i)
					&& i < Integer.MAX_VALUE)
				i ++;
			
			if (i == Integer.MAX_VALUE) {
				this.logger.warn("This instants of Notifications is full.");
				return false;
			}
			
			this.notifications.put(i, notification.setId(i));
			return true;
		}
		
	}
	
	public Optional<Notification> get (final int i) {
		return Optional.ofNullable(this.notifications.get(i));
	}
	
	public boolean del (final int i) {
		return this.notifications.remove(i) != null;
	}
	
	public boolean del (final Notification notification) {
		if (notification.getId().isPresent())
			return this.del(notification.getId().get());
		else
			return false;
	}
	
	@JsonProperty ("notifications")
	public List<Notification> getNotifications() {
		return new ImmutableList.Builder<Notification>()
				.addAll(this.notifications.values())
				.build();
	}

}
