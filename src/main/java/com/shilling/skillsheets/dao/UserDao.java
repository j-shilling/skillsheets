package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;

public interface UserDao extends ResourceDao<User> {
	
	public User createWithId(String id) throws IOException;
	public User createWithEmail(String email) throws IOException;
	public Optional<User> read(String id) throws IOException;
	
}
