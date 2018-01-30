package com.shilling.skillsheets.api.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserGroup;

public interface UserGroupService {

	UserGroup create (User user);

	Collection<UserGroup> read (User user);

	Optional<UserGroup> read (User user, UUID uuid);

	boolean delete(User user, UUID uuid);

	boolean setName(User user, UUID uuid, String name);

	boolean addMember(User user, UUID uuid, String member);

	boolean delMember(User user, UUID uuid, String member);

}
