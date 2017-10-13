package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.User;

public class UserNotificationsImpl implements UserNotifications {

	@Override
	public int getPendingNotificationCount(User user) {
		return 5;
	}

}
