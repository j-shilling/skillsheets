package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public interface ModelWriter {
	public boolean write (User user, Notification notification);
}
