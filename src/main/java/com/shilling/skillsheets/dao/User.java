package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import javax.annotation.Nullable;

import com.shilling.skillsheets.model.SkillSheet;

/**
 * Represents an entry for a user in persistent storage.
 * 
 * @author Jake Shilling
 *
 */
public interface User {
	
	/**
	 * Check whether this user has a teacher account.
	 * 
	 * @return
	 * @throws IOException 
	 */
	public boolean isTeacher() throws IOException;

	/**
	 * Updates the name of this user in storage.
	 * 
	 * @param string	The new value to be saved. Nullable.
	 * @return			<tt>this</tt> to enable method chaining
	 */
	public User setName(@Nullable String string);
	
	public Optional<String> getName () throws IOException;

	/**
	 * Updates the id of this user in storage.
	 * 
	 * @param string	The new value to be saved. Nullable.
	 * @return			<tt>this</tt> to enable method chaining
	 */
	public User setId(@Nullable String subject);
	
	public Optional<String> getId() throws IOException;

	/**
	 * Updates the email of this user in storage.
	 * 
	 * @param string	The new value to be saved. Nullable.
	 * @return			<tt>this</tt> to enable method chaining
	 */
	public User setEmail(@Nullable String email);
	
	public Optional<String> getEmail() throws IOException;

	/**
	 * Declares in storage that a SkillSheet is visible to this
	 * user.
	 * 
	 * @param SkillSheet	The visible SkillSheet
	 * @return				<tt>this</tt> to enable method chaining
	 */
	public User addSkillSheet(SkillSheet skillsheet);

	/**
	 * Get a collection of UUIDs corresponding to the SkillSheets
	 * added with {@link User#addSkillSheet}.
	 * 
	 * @return				A collection of results. May be empty;
	 * 						may not be null.
	 * @throws IOException 
	 */
	public Collection<String> getSkillSheets() throws IOException;
	
	/**
	 * Delete this user's information
	 */
	public void delete();

}
