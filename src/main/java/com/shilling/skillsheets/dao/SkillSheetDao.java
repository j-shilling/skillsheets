package com.shilling.skillsheets.dao;

import java.io.IOException;
import java.util.NoSuchElementException;

import com.shilling.skillsheets.model.SkillSheet;

public interface SkillSheetDao {
	
	public void create (SkillSheet skillsheet) throws IOException;
	public void update (SkillSheet skillsheet) throws IOException, NoSuchElementException;
	public void delete (String uuid) throws IOException, NoSuchElementException;

}
