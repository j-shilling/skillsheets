package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.SkillSheetDao;

/**
 * @author Jake Shilling
 *
 */
public class LocalSkillSheetDao implements SkillSheetDao {
	
	private static final String EXTENSION = ".skillsheet.xml";
	
	private final File dir;
	private final Map<UUID, SkillSheet> skillsheets;
	
	LocalSkillSheetDao (File dir) {
		if (!dir.exists())
			dir.mkdirs();
		
		Preconditions.checkArgument(dir.isDirectory());
		
		this.dir = dir;
		this.skillsheets = new HashMap<>();
		
		File[] files = this.dir.listFiles((parent, name) -> name.endsWith(EXTENSION));
		for (File file : files) {
			String name = file.getName().substring(0, file.getName().indexOf(EXTENSION));
			UUID uuid = UUID.fromString(name);
			
			SkillSheet sheet = new LocalSkillSheet (uuid, file);
			this.skillsheets.put(uuid, sheet);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SkillSheet create() throws IOException {
		UUID uuid = UUID.randomUUID();
		File file = Paths.get(this.dir.getAbsolutePath(), uuid.toString() + EXTENSION).toFile();
		
		file.createNewFile();
		
		SkillSheet sheet = new LocalSkillSheet (uuid, file);
		this.skillsheets.put(uuid, sheet);
		
		return sheet;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<SkillSheet> read(String uuid) throws IOException {
		return Optional.ofNullable(this.skillsheets.get(UUID.fromString(uuid)));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(SkillSheet skillsheet) throws IOException {
		Preconditions.checkNotNull(skillsheet);
		
		SkillSheet sheet = this.skillsheets.remove(UUID.fromString(skillsheet.getUuid()));
		if (sheet != null)
			sheet.delete();
	}

}
