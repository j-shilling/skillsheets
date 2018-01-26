package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;

/**
 * Manages persistent storage of {@link User}s.
 * 
 * @author Jake Shilling
 *
 */
public interface UserDao {

	/**
	 * Creates a new User entry
	 * 
	 * @return			A new instance of User corresponding to
	 * 					the stored record
	 * @throws IOException	if the storage could not be written
	 */
	public User create () throws IOException;
	
	/**
	 * Creates an instance of User from a stored User entry.
	 * 
	 * @param id		An identifier for the entry
	 * @return			New user or Optional.empty()
	 * @throws IOException	if the storage could not be read
	 */
	public Optional<User> read (String id) throws IOException;
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public void delete (User user);
	
}
