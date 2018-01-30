package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.util.UUID;

import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.SkillSheetDao;

/**
 * @author Jake Shilling
 *
 */
public class LocalSkillSheetDao extends LocalDao<SkillSheet> implements SkillSheetDao {

	protected LocalSkillSheetDao(File dir) {
		super(dir);
	}

	@Override
	protected String getFileExtension() {
		return ".sheet.xml";
	}

	@Override
	protected SkillSheet getInstance(UUID uuid, File file) {
		return new LocalSkillSheet (uuid, file);
	}

}
