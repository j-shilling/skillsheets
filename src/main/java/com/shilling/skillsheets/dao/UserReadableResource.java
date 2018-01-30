package com.shilling.skillsheets.dao;

import java.io.IOException;

import com.shilling.skillsheets.api.model.ResourceModel;

/**
 * Common interface for resources visible to users.
 * 
 * @author Jake Shilling
 *
 */
public interface UserReadableResource extends Resource {
	
	/**
	 * Checks for the read permissions of a given user.
	 * 
	 * @param user		User account to check
	 * @throws IOException 
	 */
	public boolean canRead (User user) throws IOException;
	
	/**
	 * Get a serializable model of this resource to sent
	 * to the client.
	 * 
	 * @param user		User account requesting the model
	 */
	public ResourceModel getModel (User user);

}
