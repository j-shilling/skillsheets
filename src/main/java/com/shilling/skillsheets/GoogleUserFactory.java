package com.shilling.skillsheets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.shilling.skillsheets.model.TokenId;
import com.shilling.skillsheets.model.User;

/**
 * A class to validated Google's Token ID from the client and get
 * user information.
 * 
 * @author Jake
 *
 */
public class GoogleUserFactory {
	
	private final String client_id =
		"407997016708-o3kmbrmnodmqtfmvp2j0hsu9uvh9ittn.apps.googleusercontent.com";
	private final Logger logger =
			LogManager.getLogger(GoogleUserFactory.class);
	
	/**
	 * Get a user from a given token id string.
	 * 
	 * @param tokenid	The Token ID from the client.
	 * @return			The user account if found; an Optional.empty() otherwise.
	 */
	public Optional<User> getUser (TokenId tokenid) {
		this.logger.traceEntry();
		
		GoogleIdTokenVerifier verifier 
			= new GoogleIdTokenVerifier.Builder (new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(this.client_id))
				.build();
		
		GoogleIdToken token = null;
		
		try {
			token = verifier.verify(tokenid.toString());
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
			return Optional.empty();
		
		Payload payload = token.getPayload();
		User user = new User.Builder()
				.setId(payload.getSubject())
				.setEmail(payload.getEmail())
				.setName((String) payload.get("name"))
				.setFamilyName((String) payload.get("family_name"))
				.setFirstName((String) payload.get("given_name"))
				.setImageUrl(tokenid.getImageUrl())
				.build();
		
		this.logger.traceExit(user.toString());
		return Optional.of(user);
	}

}
