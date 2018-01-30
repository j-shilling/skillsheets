package com.shilling.skillsheets.dao;

import java.io.IOException;

/**
 * Interface to check user permissions
 * 
 * @author Jake Shilling
 *
 */
public interface UserEditableResource extends Resource {
	
	/**
	 * Checks whether a given user can edit this resource.
	 * 
	 * @param user		User account to check
	 * @return			<tt>true</tt> is user can edit.
	 * @throws IOException 
	 */
	public boolean canWrite (User user) throws IOException;

}
