package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.UUID;

public interface UserGroup extends Resource {

	public Collection<UUID> getMembers() throws IOException;
	
}
