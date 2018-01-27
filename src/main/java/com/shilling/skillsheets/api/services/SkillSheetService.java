package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * Service used to perform CRUD operations on 
 * {@link com.shilling.skillsheets.model.SkilSheet}
 * 
 * @author Jake Shilling
 *
 */
@Service
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
	
	/**
	 * Read and return all SkillSheets available to a user.
	 * 
	 * @param user			User performing the operation
	 * @return				Collection of results. May be empty, but not
	 * 						null.
	 * @throws IOException	If an error occurred while reading.
	 */
	public Collection<SkillSheet> read (User user) throws IOException;
	
	/**
	 * Read and return the SkillSheet with a given UUID.
	 * 
	 * @param user			User making the requests. The requested
	 * 						SkillSheet must be visible to the user.
	 * @param uuid			Identifier for the SkillSheet
	 * @return				The result or Optional.empty()
	 * @throws IOException	If an error occured while reading
	 */
	public Optional<SkillSheet> read (User user, String uuid) throws IOException;
	
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
	
	/**
	 * Sets the display name of the specified SkillSheet. This name may
	 * be null. The old name will be replaced.
	 * 
	 * @param user			User performing the update
	 * @param uuid			UUID of the SkillSheet
	 * @param name			Desired Name
	 * @return				<tt>true</tt>if the corresponding SkillSheet was found
	 * @throws IOException	If an error occured saving the updated
	 */
	public boolean setName(User user, String uuid, String name) throws IOException;
	
}
