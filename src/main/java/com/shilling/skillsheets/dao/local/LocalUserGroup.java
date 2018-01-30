package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.shilling.skillsheets.api.model.ResourceModel;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserGroup;

public class LocalUserGroup extends LocalResource<LocalUserGroup.Data> implements UserGroup {
	
	class Data {
		
	}

	public LocalUserGroup(UUID uuid, File file) {
		super(uuid, file);
	}

	@Override
	public boolean canWrite(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canRead(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ResourceModel getModel(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<String> getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UUID> getMembers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Data initial() {
		// TODO Auto-generated method stub
		return null;
	}

}
