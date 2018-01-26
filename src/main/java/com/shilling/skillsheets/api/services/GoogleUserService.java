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
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.dao.UserDao;

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
	GoogleUserService (UserDao dao, GoogleIdTokenVerifier verifier) {
		this.logger = LogManager.getLogger(GoogleUserService.class);
		this.dao = dao;
		this.verifier = verifier;
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
			User user = result.get()
				.setName((String) payload.get("name"))
				.setId(payload.getSubject())
				.setEmail(payload.getEmail());
			
			return Optional.of(user);
		} else {
			User user = this.dao.create()
				.setName((String) payload.get("name"))
				.setId(payload.getSubject())
				.setEmail(payload.getEmail());
			
			return Optional.of(user);
		}
	
	}

}