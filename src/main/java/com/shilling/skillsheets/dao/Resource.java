package com.shilling.skillsheets.dao;

import java.util.UUID;

/**
 * Common interface for resources in persistent storage.
 * 
 * @author Jake Shilling
 *
 */
public interface Resource {
	
	/**
	 * @return A UUID for this resource
	 */
	public UUID getUuid();
	
	/**
	 * Delete this Resource from persistent storage
	 */
	public void delete();
	
}
