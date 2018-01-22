package com.shilling.skillsheets.api.services;

import java.util.Collection;
import java.util.Optional;

import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

/**
 * Service used to perform CRUD operations on 
 * {@link com.shilling.skillsheets.model.SkilSheet}
 * 
 * @author Jake Shilling
 *
 */
public interface SkillSheetService {

	public SkillSheet create (User user);
	public Collection<SkillSheet> read (User user);
	public Optional<SkillSheet> read (User user, String uuid);
	public boolean update (User user, String uuid, SkillSheet skillSheet);
	public boolean delete (User user, String uuid);
	
}
