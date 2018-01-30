package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

import com.shilling.skillsheets.dao.UserGroup;

public class LocalUserGroup extends LocalResource<LocalUserGroup.Data> implements UserGroup {
	
	class Data extends LocalResource.Data {
		
		private Collection<UUID> members = new HashSet<>();
		
		public Collection<UUID> getMembers () {
			return this.members;
		}
	}

	public LocalUserGroup(UUID uuid, File file) {
		super(uuid, file);
	}

	@Override
	public Collection<UUID> getMembers() throws IOException {
		return this.read().getMembers();
	}

	@Override
	protected Data initial() {
		return new Data();
	}

}
