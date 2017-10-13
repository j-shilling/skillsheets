package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.User;
import com.shilling.skillsheets.model.UserViewOptions;

public class UserSettingsImpl implements UserSettings {

	@Override
	public UserViewOptions getUserViewOption(User user) {
		return UserViewOptions.STUDENT;
	}

}
