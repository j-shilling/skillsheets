package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

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
	
	public Optional<UUID> getOwner() throws IOException;
	
	public void setOwner (UUID uuid) throws IOException;
	
	default public void setOwner (User user) throws IOException {
		this.setOwner (user.getUuid());
	}
	
	public Optional<String> getName() throws IOException;
	
	public void setName(@Nullable String name) throws IOException;
	
	public boolean canEdit(UUID uuid) throws IOException;
	
	default public boolean canEdit (User user) throws IOException {
		return this.canEdit(user.getUuid());
	}
	
	public void addEditor (UUID uuid) throws IOException;
	
	default public void addEditor (User user) throws IOException {
		this.addEditor(user.getUuid());
	}
	
	public boolean canView (UUID uuid) throws IOException;
	
	default public boolean canView (User user) throws IOException {
		return this.canView(user.getUuid());
	}
	
	public void addViewer (UUID uuid) throws IOException;
	
	
	default public void addViewer (User user) throws IOException {
		this.addViewer(user.getUuid());
	}
	
	/**
	 * Delete this Resource from persistent storage
	 */
	public void delete();
	
}
