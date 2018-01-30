package com.shilling.skillsheets.dao;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserGroup extends UserEditableResource, UserReadableResource {

	public Optional<String> getName();
	public Collection<UUID> getMembers();
	
}
