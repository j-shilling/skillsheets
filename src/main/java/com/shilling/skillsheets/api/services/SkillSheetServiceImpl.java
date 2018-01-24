package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.SkillSheetDao;
import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

@Service
public class SkillSheetServiceImpl implements SkillSheetService {
	
	private final Logger logger;
	private final SkillSheetDao dao;
	
	@Autowired
	private SkillSheetServiceImpl (SkillSheetDao dao) {
		this.logger = LogManager.getLogger(SkillSheetServiceImpl.class);
		this.dao = dao;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SkillSheet create(User user) throws IOException {
		Preconditions.checkNotNull(user);
		Preconditions.checkArgument(user.isTeacher());
		
		this.logger.traceEntry("Creating new SkillSheet for " + user);
		
		SkillSheet skillsheet = new SkillSheet.Builder()
				.addTeacher(user)
				.build();
				
		this.dao.create(skillsheet);
		return skillsheet;
	}

	@Override
	public Collection<SkillSheet> read(User user) {
		Preconditions.checkNotNull(user);
		return null;
	}

	@Override
	public Optional<SkillSheet> read(User user, String uuid) {
		Preconditions.checkNotNull(user);
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean update(User user, SkillSheet skillSheet) throws IOException {
		Preconditions.checkNotNull(user);
		Preconditions.checkNotNull(skillSheet);
		Preconditions.checkArgument(user.isTeacher());
		Preconditions.checkArgument(skillSheet.isTeacher(user));
		
		try {
			this.dao.update(skillSheet);
		} catch (IOException e) {
			throw e;
		} catch (NoSuchElementException e) {
			return false;
		}
		
		return true;
	}

	@Override
	public boolean delete(User user, String uuid) throws IOException {
		Preconditions.checkNotNull(user);
		Preconditions.checkArgument(user.isTeacher());
		
		try {
			this.dao.delete(uuid);
		} catch (IOException e) {
			throw e;
		} catch (NoSuchElementException e) {
			return false;
		}
		
		return true;
	}

}
