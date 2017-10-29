package com.shilling.skillsheets.dao;

import java.util.Collection;
import java.util.Optional;

import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public interface UserNotifications {
	public boolean saveMessage (User user, Notification notification);
	public Optional<Notification> getMessage (User user, int id);
	public Collection<Notification> getAllMessages (User user);
	public boolean deleteMessage (User user, int id);
	
	default public boolean deleteMessage (User user, Notification notification) {
		if (notification.getId().isPresent()) {
			return this.deleteMessage(user, notification.getId().get());
		} else {
			return false;
		}
	}
}
