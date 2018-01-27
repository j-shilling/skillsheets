/**
 * 
 */
package com.shilling.skillsheets.dao.local;

import java.io.IOException;

import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * @author Jake Shilling
 *
 */
class LocalSkillSheet implements SkillSheet {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUuid() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTeacher(User user) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTeacher(User user) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStudent(User user) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(String name) throws IOException {
		// TODO Auto-generated method stub

	}

}
