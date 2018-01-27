package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;

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
	
}
