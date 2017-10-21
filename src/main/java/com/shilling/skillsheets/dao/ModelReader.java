package com.shilling.skillsheets.dao;

import java.io.InputStream;

import com.shilling.skillsheets.model.User;

public interface ModelReader {
	InputStream getNotificationsInputStream (User user);
}
