package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

/**
 * Common interface for all DAO objects.
 * 
 * @author Jake Shilling
 *
 * @param <T>  The Resource Interface this object produces
 */
public interface ResourceDao<T extends Resource> {

	/** Create a new instance the resource and save it */
	public T create () throws IOException;
	
	/** Find a previously created resource */
	public Optional<T> read (UUID uuid) throws IOException;
        
        /** Removes a UUID from this DAO's records. Does not
         *  delete the resource from permanent storage, just
         *  removes what was necessary to look up the records.
         * 
         *  @see Resource#delete
         */
        public void delete (UUID uuid) throws IOException;
	
	/** Removes a UUID from this DAO's records. Does not
         *  delete the resource from permanent storage, just
         *  removes what was necessary to look up the records.
         * 
         *  @see Resource#delete
         */
        default public void delete (T resource) throws IOException {
            this.delete (resource.getUuid());
        }
	
}
