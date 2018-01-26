package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;

import javax.annotation.Nullable;

import com.shilling.skillsheets.model.SkillSheet;

/**
 * Manages persistent storage of {@link SkillSheet}s.
 * 
 * @author Jake Shilling
 *
 */
public interface SkillSheetDao {
	
	/**
	 * Create a new entry in storage and build a 
	 * {@link SkillSheet} to represent it.
	 * 
	 * @return				A newly created {@link SkillSheet}
	 * @throws IOException	If the new entry could not be written
	 */
	public SkillSheet create () throws IOException;
	
	/**
	 * Create an instance of {@link SKillSheet} by reading from
	 * an entry in storage.
	 * 
	 * @param uuid			The UUID to look up
	 * @return				The new SkillSheet or Optional.empty() if
	 * 						the UUID could not be found.
	 * @throws IOException	If the storage could not be read.
	 */
	public Optional<SkillSheet> read(String uuid) throws IOException;
	
	/**
	 * Delete an entry from permanent storage.
	 * 
	 * @param skillSheet	The object to be deleted
	 * @throws IOException  If the change could not be written
	 */
	public void delete (SkillSheet skillSheet) throws IOException;
	
	/**
	 * Save a association between a user and a skill sheet signifying
	 * the user's permission to edit the sheet.
	 * 
	 * @param skillsheet	The editable item
	 * @param user			The user account
	 * @throws IOException	If the association could not be written
	 */
	public void addTeacher(SkillSheet skillsheet, User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a teacher.
	 * 
	 * @param skillSheet		SkillSheet to look up
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isTeacher(SkillSheet skillSheet, User user) throws IOException;
	
	/**
	 * Read the association saved for a particular SkillSheet to see
	 * if a user has been defined as a student.
	 * 
	 * @param skillSheet		SkillSheet to look up
	 * @param user				User to look for
	 * @return					<tt>true</tt> if the association is found
	 * @throws IOException		If the storage could not be read
	 */
	public boolean isStudents(SkillSheet skillSheet, User user) throws IOException;
	
	/**
	 * Set the display name for a given SkillSheet in storage.
	 * 
	 * @param skillSheet		SkillSheet in question
	 * @param name				New name; may be null
	 * @throws IOException		If the storage could not be written
	 */
	public void setName(SkillSheet skillSheet, @Nullable String name) throws IOException;

}
