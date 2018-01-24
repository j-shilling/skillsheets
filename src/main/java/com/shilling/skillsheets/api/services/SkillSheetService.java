package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

/**
 * Service used to perform CRUD operations on 
 * {@link com.shilling.skillsheets.model.SkilSheet}
 * 
 * @author Jake Shilling
 *
 */
public interface SkillSheetService {

	/**
	 * Creates a new SkillSheet and sets the creating account
	 * as a teacher for it.
	 * 
	 * @param user			User creating this SkillSheet
	 * @return				This newly created item
	 * @throws IOException	May be thrown when new data is being
	 * 						written
	 */
	public SkillSheet create (User user) throws IOException;
	public Collection<SkillSheet> read (User user);
	public Optional<SkillSheet> read (User user, String uuid);
	
	/**
	 * Ensures that all records associated with skillSheet's
	 * UUID match what is contained in the past object.
	 * 
	 * @param user			User performing the update
	 * @param skillSheet	Object containing new information
	 * @return				<tt>true</tt> if a record for this
	 * 						UUID was found.
	 * @throws IOException	May be thrown when new data is being
	 * 						written
	 */
	public boolean update (User user, SkillSheet skillSheet) throws IOException;
	
	/**
	 * Delete any record with a match UUID
	 * 
	 * @param user			User performing the delete
	 * @param uuid			UUID to look up
	 * @return				<tt>true</tt> if a record for this
	 * 						UUID was found.
	 * @throws IOException	May be thrown when new data is being
	 * 						written
	 */
	public boolean delete (User user, String uuid) throws IOException;
	
}
