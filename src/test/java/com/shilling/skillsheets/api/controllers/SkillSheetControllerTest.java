package com.shilling.skillsheets.api.controllers;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.shilling.skillsheets.api.services.SkillSheetService;
import com.shilling.skillsheets.api.services.UserService;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@WebMvcTest(value = SkillSheetController.class, secure = false)
public class SkillSheetControllerTest extends TestCase {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private SkillSheetService service;
	@MockBean
	private UserService users;
	@MockBean
	private UuidValidator uuids;
	
	@Mock
	private SkillSheet mockSkillSheet;
	@Mock
	private User mockUser;
	
	private UUID uuid = UUID.randomUUID();
	
	@Before
	public void setUp() throws IOException {
		Mockito.when(this.users.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.uuids.validate(Mockito.anyString())).thenReturn(this.uuid);
	}
	
	/*
	 * TESTS AS STUDENT
	 */
	
	@Test
	public void testCreateAsStudent() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.service, Mockito.times(0)).create(Mockito.anyObject());
	}
	
	@Test
	public void testReadAsStudent() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		Mockito.when(this.service.read(this.mockUser)).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times(1))
			.read(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadUuidAsStudent() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		Mockito.when(this.service.read(this.mockUser, this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times(1))
			.read(this.mockUser, this.uuid);
	}
	
	@Test
	public void testSetNameAsStudent () throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "student")
				.content("newname".getBytes());
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.service, Mockito.times(0))
			.setName(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	@Test
	public void testDeleteAsStudent() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.service, Mockito.times(0))
			.delete (Mockito.any(), Mockito.any());
	}
	
	/*
	 * TESTS AS TEACHER
	 */
	
	@Test
	public void testCreateAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.create(this.mockUser)).thenReturn(this.mockSkillSheet);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.CREATED, status);
		
		Mockito.verify(this.service, Mockito.times (1))
			.create(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.read(this.mockUser)).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times (1))
			.read(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadUuidAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.read(this.mockUser, this.uuid)).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times (1))
			.read(this.mockUser, this.uuid);
	}
	
	@Test
	public void testSetNameAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.setName(Mockito.eq(this.mockUser), Mockito.eq(this.uuid), Mockito.anyString())).thenReturn(true);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "teacher")
				.content("newname".getBytes());
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times(1))
			.setName(Mockito.eq(this.mockUser), Mockito.eq(this.uuid), Mockito.eq("newname"));
	}
	
	@Test
	public void testSetNameNullAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.setName(this.mockUser, this.uuid, null)).thenReturn(true);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "teacher");
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times(1))
			.setName(Mockito.eq(this.mockUser), Mockito.eq(this.uuid), Mockito.eq(null));
	}
	
	@Test
	public void testDeleteAsTeacher() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.delete(this.mockUser, this.uuid)).thenReturn(true);

		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.service, Mockito.times (1))
			.delete (this.mockUser, this.uuid);
	}
	
	/*
	 * NOT FOUND UUID TESTS
	 */
	
	@Test
	public void testReadNotFound() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.read(this.mockUser,  this.uuid)).thenReturn(Optional.empty());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void testSetNameNotFound() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.setName(Mockito.eq(this.mockUser), Mockito.eq(this.uuid), Mockito.anyString())).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "teacher")
				.content("newname".getBytes());
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void testDeleteNotFound() throws Exception {
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.service.delete(this.mockUser, this.uuid)).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}

}
