package com.shilling.skillsheets.dao.local;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.SkillSheetDao;

public class LocalSkillSheetDaoTest {
	
	private final File dir =
			Paths.get(System.getProperty("user.dir"), "localskillsheettest").toFile();
	
	private SkillSheetDao dao = null;
	
	@Before
	public void setUp() {
		this.dao = new LocalSkillSheetDao (this.dir);
	}
	
	@After
	public void tearDown() {
		File[] children = this.dir.listFiles();
		for (File file : children)
			file.delete();
		this.dir.delete();
	}

	@Test
	public void testLocalSkillSheetDao() {
		assertTrue (this.dir.isDirectory());
		assertNotNull (this.dao);
	}

	@Test
	public void testCreate() throws IOException {
		SkillSheet sheet = this.dao.create();
		File file = Paths.get(this.dir.getAbsolutePath(), 
				sheet.getUuid() + LocalSkillSheetDao.EXTENSION).toFile();
		assertTrue (file.isFile());
	}

	@Test
	public void testRead() throws IOException {
		SkillSheet sheet1 = this.dao.create();
		SkillSheet sheet2 = this.dao.create();
		
		assertEquals (Optional.of(sheet1), this.dao.read(sheet1.getUuid()));
		assertEquals (Optional.of(sheet2), this.dao.read(sheet2.getUuid()));
		assertEquals (Optional.empty(), this.dao.read(UUID.randomUUID().toString()));
	}

	@Test
	public void testDelete() throws IOException {
		SkillSheet sheet = this.dao.create();
		File file = Paths.get(this.dir.getAbsolutePath(), 
				sheet.getUuid() + LocalSkillSheetDao.EXTENSION).toFile();
		assertTrue (file.isFile());
		
		this.dao.delete(sheet);
		assertFalse (this.dao.read(sheet.getUuid()).isPresent());
		assertFalse (file.exists());
	}

}
