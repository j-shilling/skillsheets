package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.shilling.skillsheets.api.model.ResourceModel;

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
	 * Get the UUID of the user who owns this resource. Only
	 * the user can delete this resource permanently. A resource
	 * created automatically by the server will not have an
	 * owner.
	 */
	public Optional<UUID> getOwner() throws IOException;
	
	/**
	 * Set the UUID of the user who owns this resource. Only
	 * the user can delete this resource permanently. A resource
	 * created automatically by the server will not have an
	 * owner.
	 */
	public void setOwner (UUID uuid) throws IOException;
	
	/**
	 * Set the UUID of the user who owns this resource. Only
	 * the user can delete this resource permanently. A resource
	 * created automatically by the server will not have an
	 * owner.
	 */
	default public void setOwner (User user) throws IOException {
		this.setOwner (user.getUuid());
	}
	
	/**
	 * Get the display name of this resource. This may not
	 * exist, in which case the return will be Optional.empty()
	 */
	public Optional<String> getName() throws IOException;
	
	/**
	 * Set the display name of this resource. If name is set to
	 * <tt>null</tt> then the previous name will simply be 
	 * deleted.
	 */
	public void setName(@Nullable String name) throws IOException;
	
	/**
	 * Returns <tt>true</tt> if the user of the specified UUID
	 * has permission to edit this resource.
	 */
	public boolean canEdit(UUID uuid) throws IOException;
	
	/**
	 * Returns <tt>true</tt> if the has permission to edit this resource.
	 */
	default public boolean canEdit (User user) throws IOException {
		return this.canEdit(user.getUuid());
	}
	
	/**
	 * Adds a user to the list of accounts which can edit this resource.
	 */
	public void addEditor (UUID uuid) throws IOException;
	
	/**
	 * Adds a user to the list of accounts which can edit this resource.
	 */
	default public void addEditor (User user) throws IOException {
		this.addEditor(user.getUuid());
	}
        
        /**
         * Removes editing permission from a user
         */
        public void delEditor (UUID uuid) throws IOException;
        
        /**
         * Removes editing permission from a user
         */
        default public void delEditor (User user) throws IOException {
            this.delEditor(user.getUuid());
        }
	
	/**
	 * Returns <tt>true</tt> if the specified user has permission to view this resource.
	 */
	public boolean canView (UUID uuid) throws IOException;
	
	/**
	 * Returns <tt>true</tt> if the specified user has permission to view this resource.
	 */
	default public boolean canView (User user) throws IOException {
		return this.canView(user.getUuid());
	}
	
	/**
	 * Gives the specified account permission to view this resource.
	 */
	public void addViewer (UUID uuid) throws IOException;
	
	/**
	 * Gives the specified account permission to view this resource.
	 */
	default public void addViewer (User user) throws IOException {
		this.addViewer(user.getUuid());
	}
        
        /**
         * Removes viewing permission from a user
         */
        public void delViewer (UUID uuid) throws IOException;
        
        /**
         * Removes editing permission from a user
         */
        default public void delViewer (User user) throws IOException {
            this.delViewer(user.getUuid());
        }
	
	/**
	 * Delete this Resource from persistent storage.
	 */
	public void delete();
	
	/**
	 * Get a Model object of this resource which can be serialized to JSON
	 * and sent to the client.
	 */
	public ResourceModel getModel(UUID uuid);
	
	/**
	 * Get a Model object of this resource which can be serialized to JSON
	 * and sent to the client.
	 */
	default public ResourceModel getModel (User user) {
		return this.getModel(user.getUuid());
	}
	
	public void copy (Resource other) throws IOException;
	
}
