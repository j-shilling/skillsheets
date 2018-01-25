package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

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
	
	public void setName (User user, String name) throws IOException;
	public void setEmail (User user, String email) throws IOException;
	public void setUid (User user, String id) throws IOException;
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	public void delete (User user);
	public void addSkillSheet(User user, SkillSheet skillsheet);
	public Collection<String> getSkillSheets(User user);
	
}
