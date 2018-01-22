package com.shilling.skillsheets.api.services;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.shilling.skillsheets.dao.UserDao;
import com.shilling.skillsheets.model.User;

/**
 * Implementation of {@link UserService} for Google Accounts
 * 
 * @author Jake Shilling
 *
 */
@Service
public class GoogleUserService implements UserService {

	private final Logger logger;
	private final UserDao dao;
	private final GoogleIdTokenVerifier verifier;
	
	@Autowired
	private GoogleUserService (UserDao dao, GoogleIdTokenVerifier verifier) {
		this.logger = LogManager.getLogger(GoogleUserService.class);
		this.dao = dao;
		this.verifier = verifier;
	}
	
	private class UpdateUser implements Runnable {
		
		private final User user;
		
		private UpdateUser (User user) {
			this.user = user;
		}

		@Override
		public void run() {
			GoogleUserService.this.dao.update(this.user);
		}
	}
	
	private class CreateUser implements Runnable {
		
		private final User user;
		
		private CreateUser (User user) {
			this.user = user;
		}

		@Override
		public void run() {
			GoogleUserService.this.dao.create(user);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> fromToken(String id_token) {
		
		/* Verify Id Token */
		GoogleIdToken token = null;

		try {
			token = verifier.verify(id_token);
		} catch (GeneralSecurityException e) {
			this.logger.error(e.getMessage());
			this.logger.traceExit("Returning Optional.empty()");
			return Optional.empty();
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			this.logger.traceExit("Returning Optional.empty()");
			return Optional.empty();
		}

		if (token == null)
			return Optional.empty(); // token could not be verified

		/* Get information from token */
		Payload payload = token.getPayload();
		User user = new User.Builder()
				.setId(payload.getSubject())
				.setEmail(payload.getEmail())
				.setName((String) payload.get("name"))
				.setFamilyName((String) payload.get("family_name"))
				.setFirstName((String) payload.get("given_name"))
				.build();
		
		/* Get information from DAO */
		Optional<User> result = this.dao.read(user.getId().get());
		if (!result.isPresent()) {
			/* This user is already on record. Get local information and
			 * update any information from Google which might have changed.
			 */
			user = new User.Builder(user)
					.setTeacher(result.get().isTeacher())
					.build();
			new Thread (new UpdateUser (user)).start();
		} else {
			/* This user is logging in for the first time;
			 * add them to the DB in the background. */
			new Thread (new CreateUser (user)).start();
		}
		
		return Optional.of(user);
	
	}

}