package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public interface UserNotifications {
	public int getNextMessageId(User user);
	public void saveMessage (User user, Notification notification);
	public Notification getMessage (User user, int id);
	public void deleteMessage (User user, int id);
	
	default void deleteMessage (User user, Notification notification) {
		this.deleteMessage(user, notification.getId());
	}
}
