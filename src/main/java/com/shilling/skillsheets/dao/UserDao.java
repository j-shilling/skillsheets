package com.shilling.skillsheets.dao;

import java.util.Optional;

import com.shilling.skillsheets.model.User;

public interface UserDao {

	public boolean create (User user);
	public Optional<User> read (String id);
	public boolean update (User user);
	public boolean delete (User user);
	
}
