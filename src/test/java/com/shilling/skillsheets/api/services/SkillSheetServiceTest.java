package com.shilling.skillsheets.api.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.SkillSheetDao;
import com.shilling.skillsheets.dao.User;

@RunWith(SpringRunner.class)
public class SkillSheetServiceTest {
	
	@MockBean
	SkillSheetDao skillsheets;
	
	@Mock
	User mockUser;
	@Mock
	SkillSheet mockSkillSheet;
	
	SkillSheetService service;
	UUID uuid = UUID.randomUUID();
	
	@Before
	public void setUp () {
		this.service = new SkillSheetServiceImpl (this.skillsheets);
	}

	@Test
	public void testCreate() throws IOException {
		Mockito.when(this.skillsheets.create()).thenReturn(this.mockSkillSheet);
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		
		SkillSheet sheet = this.service.create(this.mockUser);
		
		assertEquals (this.mockSkillSheet, sheet);
		
		Mockito.verify(this.mockSkillSheet, Mockito.times(1)).addTeacher(this.mockUser);
		Mockito.verify(this.mockUser, Mockito.times(1)).addSkillSheet(this.mockSkillSheet);
	}

	@Test
	public void testRead() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(false);
		Mockito.when(this.mockUser.getSkillSheets()).thenReturn(Collections.singleton(this.uuid));
		
		Collection<SkillSheet> result = this.service.read(this.mockUser);
		
		assertEquals (1, result.size());
		assertEquals (this.mockSkillSheet, result.toArray()[0]);
	}
	
	@Test
	public void testReadEmpty() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(false);
		Mockito.when(this.mockUser.getSkillSheets()).thenReturn(Collections.emptyList());
		
		Collection<SkillSheet> result = this.service.read(this.mockUser);
		
		assertTrue (result.isEmpty());
	}

	@Test
	public void testReadUuidTeacher() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, this.uuid);
		assertTrue (result.isPresent());
		assertEquals (this.mockSkillSheet, result.get());
	}
	
	@Test
	public void testReadUuidStudent() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(false);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(true);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, this.uuid);
		assertTrue (result.isPresent());
		assertEquals (this.mockSkillSheet, result.get());
	}
	
	@Test
	public void testReadUuidNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(false);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, this.uuid);
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testReadUuidNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.empty());
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		Mockito.when(this.mockSkillSheet.isStudent(this.mockUser)).thenReturn(false);
		
		Optional<SkillSheet> result = this.service.read(this.mockUser, this.uuid);
		assertFalse (result.isPresent());
	}

	@Test
	public void testDelete() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		
		assertTrue (this.service.delete(this.mockUser, this.uuid));
		
		Mockito.verify(this.skillsheets, Mockito.times(1)).delete(this.mockSkillSheet);
	}
	
	@Test
	public void testDeleteNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.empty());
		
		assertFalse (this.service.delete(this.mockUser, this.uuid));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).delete(this.mockSkillSheet);
	}
	
	@Test
	public void testDeleteNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(false);
		
		assertFalse (this.service.delete(this.mockUser, this.uuid));
		
		Mockito.verify(this.skillsheets, Mockito.times(0)).delete(this.mockSkillSheet);
	}

	@Test
	public void testSetName() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(true);
		
		assertTrue (this.service.setName(this.mockUser, this.uuid, "name"));
		
		Mockito.verify(this.mockSkillSheet, Mockito.times(1)).setName(Mockito.anyString());
	}
	
	@Test
	public void testSetNameNotFound() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.empty());
		
		assertFalse (this.service.setName(this.mockUser, this.uuid, "name"));
		
		Mockito.verify(this.mockSkillSheet, Mockito.times(0)).setName(Mockito.anyString());
	}
	
	@Test
	public void testSetNameNotVisible() throws IOException {
		Mockito.when(this.skillsheets.read(this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		Mockito.when(this.mockSkillSheet.isTeacher(this.mockUser)).thenReturn(false);
		
		assertFalse (this.service.setName(this.mockUser, this.uuid, "name"));
		
		Mockito.verify(this.mockSkillSheet, Mockito.times(0)).setName(Mockito.anyString());
	}

}
