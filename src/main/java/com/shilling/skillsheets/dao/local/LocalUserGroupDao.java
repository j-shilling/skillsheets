package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.util.UUID;

import com.shilling.skillsheets.dao.UserGroup;

public class LocalUserGroupDao extends LocalDao<UserGroup> {

	protected LocalUserGroupDao(File dir) {
		super(dir);
	}

	@Override
	protected String getFileExtension() {
		return "group.xml";
	}

	@Override
	protected UserGroup getInstance(UUID uuid, File file) {
		return new LocalUserGroup (uuid, file);
	}

}
