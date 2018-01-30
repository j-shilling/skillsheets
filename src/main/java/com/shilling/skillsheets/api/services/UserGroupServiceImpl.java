package com.shilling.skillsheets.api.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserGroup;

public class UserGroupServiceImpl implements UserGroupService {

	@Override
	public UserGroup create(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<UserGroup> read(User user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<UserGroup> read(User user, UUID uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean delete(User user, UUID uuid) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setName(User user, UUID uuid, String name) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean addMember(User user, UUID uuid, String member) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delMember(User user, UUID uuid, String member) {
		// TODO Auto-generated method stub
		return false;
	}

}
