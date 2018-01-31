package com.shilling.skillsheets.dao.local;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserDao;

public class LocalUserDaoTest {
	
	private final File userdir =
			Paths.get(System.getProperty("user.dir"), "localusertest").toFile();
	
	private UserDao dao;
	
	@Before
	public void setUp () {
		this.dao = new LocalUserDao (this.userdir);	
	}
	
	@After
	public void tearDown () {
		String children[] = this.userdir.list();
		for (String child : children) {
			new File (this.userdir.getPath(), child).delete();
		}
		this.userdir.delete();
	}

	@Test
	public void testLocalUserDao() {
		assertNotNull (dao);
		assertTrue (this.userdir.exists());
		assertTrue (this.userdir.isDirectory());
	}

	@Test
	public void testCreateWithId() throws IOException {
		User user = this.dao.createWithId("testid");
		Optional<String> id = user.getId();
		
		assertTrue (id.isPresent());
		assertEquals ("testid", id.get());
		
		assertFalse (user.getEmail().isPresent());
		assertFalse (user.getName().isPresent());
		
		File file = 
				Paths.get(this.userdir.getPath(), user.getUuid().toString() + ".user.xml").toFile();
		assertTrue (file.exists());
		assertTrue (file.isFile());
	}

	@Test
	public void testCreateWithEmail() throws IOException {
		User user = this.dao.createWithEmail("test@email.com");
		Optional<String> email = user.getEmail();
		
		assertTrue (email.isPresent());
		assertEquals ("test@email.com", email.get());
		
		assertFalse (user.getId().isPresent());
		assertFalse (user.getName().isPresent());
		
		File file = 
				Paths.get(this.userdir.getPath(), user.getUuid().toString() + ".user.xml").toFile();
		assertTrue (file.exists());
		assertTrue (file.isFile());
	}
	
	@Test
	public void testIdChanged() throws IOException {
		User user = this.dao.createWithEmail("test@email.com");
		user.setId("id");
		
		assertEquals (Optional.of(user), this.dao.read("id"));
	}
	
	@Test
	public void testEmailChanged() throws IOException {
		User user = this.dao.createWithId("test");
		user.setEmail("test@mail.com");
		
		assertEquals (Optional.of(user), this.dao.read("test@mail.com"));
	}

	@Test
	public void testRead() throws IOException {
		User id = this.dao.createWithId("testid");
		User email = this.dao.createWithEmail("test@email.com");
		
		assertTrue (this.dao.read("testid").isPresent());
		assertEquals (id, this.dao.read("testid").get());
		assertTrue (this.dao.read("test@email.com").isPresent());
		assertEquals (email, this.dao.read("test@email.com").get());
		assertFalse (this.dao.read("badid").isPresent());
	}

	@Test
	public void testDeleteDao() throws IOException {
		User user = this.dao.createWithId("testid");
		
		File file = 
				Paths.get(this.userdir.getPath(), user.getUuid().toString() + ".user.xml").toFile();
		assertTrue (file.exists());
		
		this.dao.delete(user);
		
		assertFalse (file.exists());
		assertFalse (this.dao.read("testid").isPresent());
	}
	
	@Test
	public void testDeleteObject() throws IOException {
		User user = this.dao.createWithId("testid");
		
		File file = 
				Paths.get(this.userdir.getPath(), user.getUuid().toString() + ".user.xml").toFile();
		assertTrue (file.exists());
		
		user.delete();
		
		assertFalse (file.exists());
	}

}
