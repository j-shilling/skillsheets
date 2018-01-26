package com.shilling.skillsheets.dao;

import java.util.Collection;
import com.shilling.skillsheets.model.SkillSheet;

/**
 * A class to model user account information.
 * 
 * @author Jake Shilling
 *
 */
public interface User {
	
	public boolean isTeacher();

	public User setName(String string);

	public User setId(String subject);

	public User setEmail(String email);

	public User addSkillSheet(SkillSheet skillsheet);

	public Collection<String> getSkillSheets();

}
