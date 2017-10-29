package com.shilling.skillsheets.dao;

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

	@Override
	public void saveMessage(User user, Notification notification) {
		this.logger.traceEntry("Sending message to " + user);
		
		Optional<Notifications> prev = this.reader.read(user, Notifications.class);
		Notifications notes = null;
		if (prev.isPresent())
			notes = prev.get();
		else
			notes = new Notifications();
		
		notes.add(notification);
		
		this.logger.traceExit("Successful: " + this.writer.write(user, notes));
	}

	@Override
	public Notification getMessage(User user, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMessage(User user, int id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Notification[] getAllMessages(User user) {
		return new Notification[0];
	}

	@Override
	public void deleteMessage(User user, Notification notification) {
		// TODO Auto-generated method stub
		
	}



}
