package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.User;
import com.shilling.skillsheets.model.UserViewOptions;

public interface UserSettings {
	UserViewOptions getUserViewOption (User user);
}
