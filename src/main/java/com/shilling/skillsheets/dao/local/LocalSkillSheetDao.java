/**
 * 
 */
package com.shilling.skillsheets.dao.local;

import java.io.IOException;
import java.util.Optional;

import com.shilling.skillsheets.dao.SkillSheetDao;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.model.SkillSheet;

/**
 * @author Jake Shilling
 *
 */
public class LocalSkillSheetDao implements SkillSheetDao {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SkillSheet create() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<SkillSheet> read(String uuid) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(SkillSheet skillSheet) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addTeacher(SkillSheet skillsheet, User user) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTeacher(SkillSheet skillSheet, User user) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isStudents(SkillSheet skillSheet, User user) throws IOException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(SkillSheet skillSheet, String name) throws IOException {
		// TODO Auto-generated method stub

	}

}
