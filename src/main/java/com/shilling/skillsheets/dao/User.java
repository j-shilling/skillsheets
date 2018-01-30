package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

/**
 * Represents an entry for a user in persistent storage.
 * 
 * @author Jake Shilling
 *
 */
public interface User extends Resource {
	
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
	 * @throws IOException 
	 */
	public User setName(@Nullable String string) throws IOException;
	
	public Optional<String> getName () throws IOException;

	/**
	 * Updates the id of this user in storage.
	 * 
	 * @param string	The new value to be saved. Nullable.
	 * @return			<tt>this</tt> to enable method chaining
	 * @throws IOException 
	 */
	public User setId(@Nullable String subject) throws IOException;
	
	public Optional<String> getId() throws IOException;

	/**
	 * Updates the email of this user in storage.
	 * 
	 * @param string	The new value to be saved. Nullable.
	 * @return			<tt>this</tt> to enable method chaining
	 * @throws IOException 
	 */
	public User setEmail(@Nullable String email) throws IOException;
	
	public Optional<String> getEmail() throws IOException;

	public User addSkillSheet(UUID uuid) throws IOException;
	
	public User delSkillSheet(UUID uuid) throws IOException;
	
	default public User addSkillSheet (SkillSheet resource)  throws IOException {
		this.addSkillSheet(resource.getUuid());
		return this;
	}
	
	default public User delSkillSheet (SkillSheet resource)  throws IOException {
		this.delSkillSheet(resource.getUuid());
		return this;
	}

	/**
	 * Get a collection of UUIDs corresponding to the SkillSheets
	 * added with {@link User#addSkillSheet}.
	 * 
	 * @return				A collection of results. May be empty;
	 * 						may not be null.
	 * @throws IOException 
	 */
	public Collection<UUID> getSkillSheets() throws IOException;
	
	public User addUserGroup(UUID uuid) throws IOException;
	
	public User delUserGroup(UUID uuid) throws IOException;
	
	default public User addUserGroup (UserGroup resource) throws IOException {
		this.addUserGroup(resource.getUuid());
		return this;
	}
	
	default public User delUserGroup (UserGroup resource) throws IOException {
		this.delUserGroup(resource.getUuid());
		return this;
	}
	
	public Collection<UUID> getUserGroups () throws IOException;

}
