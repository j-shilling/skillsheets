package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public interface UserGroup extends Resource {

	public Collection<UUID> getMembers() throws IOException;
	public void addMember (UUID uuid) throws IOException;
	default public void addMember (User user) throws IOException {
		this.addMember(user.getUuid());
	}
	
}
