package com.shilling.skillsheets.api.controllers;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
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
import com.shilling.skillsheets.model.SkillSheet;
import com.shilling.skillsheets.model.User;

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
	
	private User teacher = new User.Builder("id").setTeacher(true).build();
	private User student = new User.Builder("id").setTeacher(false).build();
	
	private SkillSheet mockSkillSheet = new SkillSheet();
	
	/*
	 * TESTS AS STUDENT
	 */
	
	@Test
	public void testCreateAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.UNAUTHORIZED, status);
	}
	
	@Test
	public void testReadAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		Mockito.when(this.skillSheetService.read(Mockito.anyObject())).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		JSONAssert.assertEquals(
				"[{name:null,numberOfSkills:0,visible:null,availableFrom:null,availableUntil:null,dueDate:null,averageGrade:null,grade:0}]", 
				result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testReadUuidAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(), Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		JSONAssert.assertEquals(
				"{name:null,numberOfSkills:0,visible:null,availableFrom:null,availableUntil:null,dueDate:null,averageGrade:null,grade:0}", 
				result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testUpdateAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.UNAUTHORIZED, status);
	}
	
	@Test
	public void testDeleteAsStudent() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.UNAUTHORIZED, status);
	}
	
	/*
	 * TESTS AS TEACHER
	 */
	
	@Test
	public void testCreateAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.teacher));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.CREATED, status);
		
		JSONAssert.assertEquals(
				"{name:null,numberOfSkills:0,visible:false,availableFrom:null,availableUntil:null,dueDate:null,averageGrade:0,grade:null}", 
				result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testReadAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.teacher));
		Mockito.when(this.skillSheetService.read(Mockito.anyObject())).thenReturn(Collections.singleton(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		JSONAssert.assertEquals(
				"[{name:null,numberOfSkills:0,visible:false,availableFrom:null,availableUntil:null,dueDate:null,averageGrade:0,grade:null}]", 
				result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testReadUuidAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.teacher));
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(), Mockito.anyString())).thenReturn(Optional.of(this.mockSkillSheet));
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets/testuuid")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
		
		JSONAssert.assertEquals(
				"{name:null,numberOfSkills:0,visible:false,availableFrom:null,availableUntil:null,dueDate:null,averageGrade:0,grade:null}", 
				result.getResponse().getContentAsString(), false);
	}
	
	@Test
	public void testUpdateAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.teacher));
		Mockito.when(this.skillSheetService.update(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(true);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
	}
	
	@Test
	public void testDeleteAsTeacher() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.teacher));
		Mockito.when(this.skillSheetService.delete(Mockito.anyObject(), Mockito.anyString())).thenReturn(true);

		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets")
				.header("Id-Token", "teacher")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.OK, status);
	}
	
	/*
	 * NOT FOUND UUID TESTS
	 */
	
	@Test
	public void testReadNotFound() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		Mockito.when(this.skillSheetService.read(Mockito.anyObject(),  Mockito.anyString())).thenReturn(Optional.empty());
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void testUpdateNotFound() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		Mockito.when(this.skillSheetService.update(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}
	
	@Test
	public void testDeleteNotFound() throws Exception {
		Mockito.when(this.userService.fromToken(Mockito.anyString())).thenReturn(Optional.of(this.student));
		Mockito.when(this.skillSheetService.update(Mockito.anyObject(), Mockito.anyString(), Mockito.anyObject())).thenReturn(false);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/api/skillsheets")
				.header("Id-Token", "student")
				.accept(MediaType.APPLICATION_JSON);
		
		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		HttpStatus status = HttpStatus.valueOf(result.getResponse().getStatus());
		
		assertEquals (HttpStatus.NOT_FOUND, status);
	}

}
