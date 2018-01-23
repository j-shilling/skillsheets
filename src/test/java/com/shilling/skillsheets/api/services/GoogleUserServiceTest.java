package com.shilling.skillsheets.api.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.shilling.skillsheets.dao.UserDao;
import com.shilling.skillsheets.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleUserServiceTest {
	
	@MockBean
	private GoogleIdTokenVerifier verifier;
	@MockBean
	private UserDao dao;
	
	@Autowired
	private UserService service;
	
	private final User fromGoogle = new User.Builder()
			.setId("id")
			.setEmail("some@email.com")
			.setFamilyName("Name")
			.setFirstName("Mr")
			.setName("Mr Name")
			.setTeacher(false)
			.build();
	private final User fromDao = new User.Builder(this.fromGoogle)
			.setFirstName("Mrs.")
			.setName("Mrs. Name")
			.setTeacher(true)
			.build();

	@Test
	public void testNullToken() {
		Optional<User> result = this.service.fromToken(null);
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testSecurityException() throws Exception {
		Mockito.when(this.verifier.verify(Mockito.anyString())).thenThrow(new GeneralSecurityException());
		Optional<User> result = this.service.fromToken("token");
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testIOException() throws Exception {
		Mockito.when(this.verifier.verify(Mockito.anyString())).thenThrow(new IOException());
		Optional<User> result = this.service.fromToken("token");
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testNotVerified() throws Exception {
		Mockito.when(this.verifier.verify(Mockito.anyString())).thenReturn(null);
		Optional<User> result = this.service.fromToken("token");
		assertFalse (result.isPresent());
	}
	
	@Test
	public void testNewUser() throws Exception {
		GoogleUserService spy = (GoogleUserService) Mockito.spy(this.service);
		Mockito.doReturn(Optional.of(this.fromGoogle)).when(spy).parseIdToken(Mockito.anyString());
		Mockito.when(this.dao.read(Mockito.anyString())).thenReturn(Optional.empty());
		
		Optional<User> result = spy.fromToken("token");
		assertTrue (result.isPresent());
		assertEquals (this.fromGoogle.getId().get(), result.get().getId().get());
		assertEquals (this.fromGoogle.getEmail().get(), result.get().getEmail().get());
		assertEquals (this.fromGoogle.getName().get(), result.get().getName().get());
		assertEquals (this.fromGoogle.getFirstName().get(), result.get().getFirstName().get());
		assertEquals (this.fromGoogle.getFamilyName().get(), result.get().getFamilyName().get());
		assertEquals (this.fromGoogle.isTeacher(), result.get().isTeacher());
	}
	
	@Test
	public void testUpdateUser() throws Exception {
		GoogleUserService spy = (GoogleUserService) Mockito.spy(this.service);
		Mockito.doReturn(Optional.of(this.fromGoogle)).when(spy).parseIdToken(Mockito.anyString());
		Mockito.when(this.dao.read(Mockito.anyString())).thenReturn(Optional.of(this.fromDao));
		
		Optional<User> result = spy.fromToken("token");
		assertTrue (result.isPresent());
		assertEquals (this.fromGoogle.getId().get(), result.get().getId().get());
		assertEquals (this.fromGoogle.getEmail().get(), result.get().getEmail().get());
		assertEquals (this.fromGoogle.getName().get(), result.get().getName().get());
		assertEquals (this.fromGoogle.getFirstName().get(), result.get().getFirstName().get());
		assertEquals (this.fromGoogle.getFamilyName().get(), result.get().getFamilyName().get());
		assertEquals (this.fromDao.isTeacher(), result.get().isTeacher());
	}

}
