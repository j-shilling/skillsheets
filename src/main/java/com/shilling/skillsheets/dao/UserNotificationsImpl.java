package com.shilling.skillsheets.dao;

import java.util.Collection;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.Notifications;
import com.shilling.skillsheets.model.User;

public class UserNotificationsImpl implements UserNotifications {
	
	private final Logger logger;
	private final ModelWriter writer;
	private final ModelReader reader;
	
	@Autowired
	private UserNotificationsImpl(ModelWriter writer, ModelReader reader) {
		this.logger = LogManager.getLogger(UserNotificationsImpl.class);
		this.writer = writer;
		this.reader = reader;
	}
	
	private Notifications getNotifications(User user) {
		this.logger.traceEntry("Reading current notifications for " + user);
		
		Optional<Notifications> prev = this.reader.read(user, Notifications.class);
		if (prev.isPresent()) {
			this.logger.traceExit("Found notifications.");
			return prev.get();
		} else {
			this.logger.traceExit("No notifications found.");
			return new Notifications();
		}
	}

	@Override
	public boolean saveMessage(User user, Notification notification) {
		this.logger.traceEntry("Sending message to " + user);
		
		Notifications notes = this.getNotifications(user);
		notes.add(notification);
		
		boolean ret = this.writer.write(user, notes);
		this.logger.traceExit("Successful: " + ret);
		return ret;
	}

	@Override
	public Optional<Notification> getMessage(User user, int id) {
		this.logger.traceEntry("Getting notification " + id + " for " + user);
		Optional<Notification> ret = this.getNotifications(user).get(id);
		this.logger.traceExit("Notification found: " + ret.isPresent());
		return ret;
	}
	
	@Override
	public Collection<Notification> getAllMessages(User user) {
		this.logger.traceEntry("Getting all notifications for " + user);
		Collection<Notification> ret = this.getNotifications(user).getNotifications();
		this.logger.traceExit("Found " + ret.size() + " notifications.");
		return ret;
	}

	@Override
	public boolean deleteMessage(User user, int id) {
		this.logger.traceEntry("Deleting notification " + id + " for " + user);
		
		Notifications notes = this.getNotifications(user);
		boolean ret = notes.del(id);
		
		if (ret) {
			this.logger.trace("Saving notifications.");
			ret = this.writer.write(user, notes);
			this.logger.traceExit("Successful: " + ret);
			return ret;
		} else {
			this.logger.traceExit("Could not delete notification.");
			return false;
		}
	}

}
