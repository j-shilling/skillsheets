package com.shilling.skillsheets.dao;

import java.io.IOException;

import javax.annotation.Nullable;

public interface SkillSheet {

	public String getUuid();
	
	/**
	 * Save a association between a user and a skill sheet signifying
	 * the user's permission to edit the sheet.
	 * 
	 * @param skillsheet	The editable item
	 * @param user			The user account
	 * @throws IOException	If the association could not be written
	 */
	public void addTeacher(User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a teacher.
	 * 
	 * @param skillSheet		SkillSheet to look up
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isTeacher(User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a student.
	 * 
	 * @param skillSheet		SkillSheet to look up
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isStudent(User user) throws IOException;
	
	/**
	 * Set the display name for a given SkillSheet in storage.
	 * 
	 * @param skillSheet		SkillSheet in question
	 * @param name				New name; may be null
	 * @throws IOException		If the storage could not be written
	 */
	public void setName(@Nullable String name) throws IOException;
	
}
