package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.User;

public interface UserNotifications {
	public int getPendingNotificationCount(User user);
}
