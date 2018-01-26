package com.shilling.skillsheets.api.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.shilling.skillsheets.dao.SkillSheetDao;
import com.shilling.skillsheets.dao.UserDao;
import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

@RunWith(SpringRunner.class)
public class SkillSheetServiceTest {
	
	@MockBean
	SkillSheetDao skillsheets;
	@MockBean
	UserDao users;
	
	SkillSheetService service;
	SkillSheet mockSkillSheet = new SkillSheet.Builder().build();
	User mockUser = new User.Builder().setTeacher(true).build();
	
	@Before
	public void setUp () {
		this.service = new SkillSheetServiceImpl (this.skillsheets, this.users);
	}

	@Test
	public void testCreate() throws IOException {
		Mockito.when(this.skillsheets.create()).thenReturn(this.mockSkillSheet);
		SkillSheet sheet = this.service.create(this.mockUser);
		
		assertEquals (this.mockSkillSheet, sheet);
		
		Mockito.verify(this.skillsheets, Mockito.times(1)).addTeacher(this.mockSkillSheet, this.mockUser);
		Mockito.verify(this.users, Mockito.times(1)).addSkillSheet(this.mockUser, this.mockSkillSheet);
	}

	@Test
	public void testRead() throws IOException {
		Mockito.when(this.skillsheets.read("test")).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		Mockito.when(this.users.getSkillSheets(this.mockUser)).thenReturn(Collections.singleton("test"));
		
		Collection<SkillSheet> result = this.service.read(this.mockUser);
		
		assertEquals (1, result.size());
		assertEquals (this.mockSkillSheet, result.toArray()[0]);
	}
	
	@Test
	public void testReadEmpty() throws IOException {
		Mockito.when(this.skillsheets.read("test")).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		Mockito.when(this.users.getSkillSheets(this.mockUser)).thenReturn(Collections.emptyList());
		
		Collection<SkillSheet> result = this.service.read(this.mockUser);
		
		assertTrue (result.isEmpty());
	}

	@Test
	public void testReadUuidTeacher() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, "test");
		assertTrue (result.isPresent());
		assertEquals (this.mockSkillSheet, result.get());
	}
	
	@Test
	public void testReadUuidStudent() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, "test");
		assertTrue (result.isPresent());
		assertEquals (this.mockSkillSheet, result.get());
	}
	
	@Test
	public void testReadUuidNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, "test");
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testReadUuidNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.empty());
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		Mockito.when(this.skillsheets.isStudents(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, "test");
		assertFalse (result.isPresent());
	}

	@Test
	public void testDelete() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		
		assertTrue (this.service.delete(this.mockUser, "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(1)).delete(this.mockSkillSheet);
	}
	
	@Test
	public void testDeleteNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertFalse (this.service.delete(this.mockUser, "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).delete(this.mockSkillSheet);
	}
	
	@Test
	public void testDeleteNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		
		assertFalse (this.service.delete(this.mockUser, "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).delete(this.mockSkillSheet);
	}

	@Test
	public void testSetName() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(true);
		
		assertTrue (this.service.setName(this.mockUser, "test", "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(1)).setName(this.mockSkillSheet, "test");
	}
	
	@Test
	public void testSetNameNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.empty());
		
		assertFalse (this.service.setName(this.mockUser, "test", "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).setName(this.mockSkillSheet, "test");
	}
	
	@Test
	public void testSetNameNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.skillsheets.isTeacher(this.mockSkillSheet, this.mockUser)).thenReturn(false);
		
		assertFalse (this.service.setName(this.mockUser, "test", "test"));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).setName(this.mockSkillSheet, "test");
	}

}
