package com.shilling.skillsheets.api.controllers;

import java.util.Collections;
import java.util.Optional;

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
	private SkillSheetService skillSheetService;
	@MockBean
	private UserService userService;
	
	@Mock
	private User mockUser;
	@Mock
	private SkillSheet mockSkillSheet;
	
	/*
	 * TESTS AS STUDENT
	 */
	
	@Test
	public void testCreateAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times(0)).create(Mockito.anyObject());
	}
	
	@Test
	public void testReadAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		Mockito.when(this.skillSheetService.read(Mockito.anyObject())).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.timeout(1))
			.read(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadUuidAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(), Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.timeout(1))
			.read(Mockito.eq(this.mockUser), Mockito.eq("testuuid"));
	}
	
	@Test
	public void testSetNameAsStudent () throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "student")
				.content("newname".getBytes());
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times(0))
			.setName(Mockito.any(), Mockito.any(), Mockito.any());
	}
	
	@Test
	public void testDeleteAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.FORBIDDEN, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times(0))
			.delete (Mockito.any(), Mockito.any());
	}
	
	/*
	 * TESTS AS TEACHER
	 */
	
	@Test
	public void testCreateAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.create(Mockito.anyObject())).thenReturn(this.mockSkillSheet);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.CREATED, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times (1))
			.create(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.read(Mockito.anyObject())).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times (1))
			.read(Mockito.eq(this.mockUser));
	}
	
	@Test
	public void testReadUuidAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(), Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times (1))
			.read(Mockito.eq(this.mockUser), Mockito.eq("testuuid"));
	}
	
	@Test
	public void testSetNameAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.setName(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(true);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "teacher")
				.content("newname".getBytes());
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times(1))
			.setName(Mockito.anyObject(), Mockito.eq("testuuid"), Mockito.eq("newname"));
	}
	
	@Test
	public void testSetNameNullAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.setName(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(true);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets/testuuid/name")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.header("Id-Token", "teacher");
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times(1))
			.setName(Mockito.anyObject(), Mockito.eq("testuuid"), Mockito.eq(null));
	}
	
	@Test
	public void testDeleteAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.delete(Mockito.anyObject(), Mockito.anyString())).thenReturn(true);

		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		Mockito.verify(this.skillSheetService, Mockito.times (1))
			.delete (Mockito.eq(this.mockUser), Mockito.eq("testuuid"));
	}
	
	/*
	 * NOT FOUND UUID TESTS
	 */
	
	@Test
	public void testReadNotFound() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(),  Mockito.anyString())).thenReturn(Optional.empty());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void testSetNameNotFound() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.setName(Mockito.anyObject(), Mockito.anyString(), Mockito.anyString())).thenReturn(false);
		
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
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.mockUser));
		Mockito.when(this.mockUser.isTeacher()).thenReturn(true);
		Mockito.when(this.skillSheetService.delete(Mockito.anyObject(), Mockito.anyString())).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}

}
