package com.shilling.skillsheets.dao;

import java.io.IOException;

public interface SkillSheet extends Resource {
	
	/**
	 * Save a association between a user and a skill sheet signifying
	 * the user's permission to edit the sheet.
	 * 
	 * @param user			The user account
	 * @throws IOException	If the association could not be written
	 */
	public SkillSheet addTeacher(User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a teacher.
	 * 
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isTeacher(User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a student.
	 * 
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isStudent(User user) throws IOException;
	
}
