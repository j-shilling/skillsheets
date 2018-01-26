package com.shilling.skillsheets.api.services;

import static org.junit.Assert.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserDao;

@RunWith(SpringRunner.class)
public class GoogleUserServiceTest {
	
	@MockBean
	private GoogleIdTokenVerifier verifier;
	@MockBean
	private UserDao dao;
	
	private UserService service;
	
	@Before
	public void setUp () {
		this.service = new GoogleUserService (this.dao, this.verifier);
	}

	@Test
	public void testNullToken() throws Exception {
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

}
