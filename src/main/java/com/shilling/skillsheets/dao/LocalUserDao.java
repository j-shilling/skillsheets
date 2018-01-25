package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

public class LocalUserDao implements UserDao {

	@Override
	public User create() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> read(String id) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setName(User user, String name) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEmail(User user, String email) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setUid(User user, String id) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSkillSheet(User user, SkillSheet skillsheet) {
		// TODO Auto-generated method stub

	}

	@Override
	public Collection<String> getSkillSheets(User user) {
		// TODO Auto-generated method stub
		return null;
	}

}
