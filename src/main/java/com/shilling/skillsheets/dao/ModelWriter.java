package com.shilling.skillsheets.dao;

import com.shilling.skillsheets.model.User;

public interface ModelWriter {
	public boolean write (User user, Object obj);
}
