package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.shilling.skillsheets.dao.SkillSheetDao;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.model.SkillSheet;

@Service
public class SkillSheetServiceImpl implements SkillSheetService {
	
	private final Logger logger;
	private final SkillSheetDao skillsheets;
	
	@Autowired 
	SkillSheetServiceImpl (SkillSheetDao skillsheets) {
		this.logger = LogManager.getLogger(SkillSheetServiceImpl.class);
		this.skillsheets = skillsheets;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SkillSheet create(User user) throws IOException {
		Preconditions.checkNotNull(user);
		Preconditions.checkArgument(user.isTeacher());
		
		this.logger.traceEntry("Creating new SkillSheet for " + user);
				
		try {
			SkillSheet skillsheet = this.skillsheets.create();
			this.skillsheets.addTeacher (skillsheet, user);
			user.addSkillSheet (skillsheet);
			return skillsheet;
		} catch (IOException e) {
			this.logger.warn("Could not create new SkillSheet");
			throw e;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<SkillSheet> read(User user) throws IOException {
		Preconditions.checkNotNull(user);
		
		Collection<SkillSheet> ret = new HashSet<>();
		Iterable<String> uuids = user.getSkillSheets ();
		for (String uuid : uuids) {
			Optional<SkillSheet> result = this.read(user, uuid);
			if (result.isPresent())
				ret.add(result.get());
		}
		
		return new ImmutableSet.Builder<SkillSheet>().addAll(ret).build();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<SkillSheet> read(User user, String uuid)  throws IOException {
		Preconditions.checkNotNull(user);
		
		Optional<SkillSheet> result = this.skillsheets.read (uuid);
		if (!result.isPresent())
			return Optional.empty();
		
		/* Check visibility */
		if (this.skillsheets.isTeacher(result.get(), user)) {
			/* User is a teacher on this SkillSheet */
			return result;
		}
		
		if (this.skillsheets.isStudents(result.get(), user)) {
			/* User is a student on this SkillSheet */
			return result;
		}
		
		/* This skill sheet is not visible to the user */
		return Optional.empty();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delete(User user, String uuid) throws IOException {
		Preconditions.checkNotNull(user);
		
		Optional<SkillSheet> result = this.skillsheets.read (uuid);
		if (!result.isPresent())
			return false;
		
		/* Check visibility */
		if (this.skillsheets.isTeacher(result.get(), user)) {
			/* User is a teacher on this SkillSheet */
			this.skillsheets.delete(result.get());
			return true;
		} else {
			/* User cannot delete this SkillSheet */
			return false;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setName(User user, String uuid, String name) throws IOException {
		Preconditions.checkNotNull(user);
		
		Optional<SkillSheet> result = this.skillsheets.read (uuid);
		if (!result.isPresent())
			return false;
		
		/* Check visibility */
		if (this.skillsheets.isTeacher(result.get(), user)) {
			/* User is a teacher on this SkillSheet */
			this.skillsheets.setName (result.get(), name);
			return true;
		} else {
			/* User cannot delete this SkillSheet */
			return false;
		}
	}

}
