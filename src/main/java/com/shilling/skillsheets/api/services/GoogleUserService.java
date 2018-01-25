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
	
	@Autowired GoogleUserService (UserDao dao, GoogleIdTokenVerifier verifier) {
		this.logger = LogManager.getLogger(GoogleUserService.class);
		this.dao = dao;
		this.verifier = verifier;
	}
	
	private class UpdateUser implements Runnable {
		
		private final User user;
		private final Payload payload;
		
		private UpdateUser (User user, Payload payload) {
			this.user = user;
			this.payload = payload;
		}

		@Override
		public void run() {
			try {
				GoogleUserService.this.dao.setUid (this.user, this.payload.getSubject());
				GoogleUserService.this.dao.setEmail (this.user, this.payload.getEmail());
				GoogleUserService.this.dao.setName (this.user, (String) this.payload.get ("name"));
			} catch (IOException e) {
				GoogleUserService.this.logger.warn("Could not update user data: " + e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<User> fromToken(String id_token) throws IOException {
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
		
		/* Get information from DAO */
		Optional<User> result = this.dao.read(payload.getSubject());
		if (result.isPresent()) {
			/* This user is already on record. Get local information and
			 * update any information from Google which might have changed.
			 */
			new Thread (new UpdateUser (result.get(), payload));
			User user = new User.Builder(result.get())
							.setName((String) payload.get("name"))
							.setId(payload.getSubject())
							.setEmail(payload.getEmail())
							.build();
			return Optional.of(user);
		} else {
			User user = new User.Builder (this.dao.create())
					.setName((String) payload.get("name"))
					.setId(payload.getSubject())
					.setEmail(payload.getEmail())
					.build();
			return Optional.of(user);
		}
	
	}

}