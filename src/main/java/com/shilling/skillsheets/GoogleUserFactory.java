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
import com.shilling.skillsheets.model.Tokens;
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
	public Optional<User> getUser (Tokens tokens) {
		this.logger.traceEntry();
		
		User user = null;
		if (tokens.getIdToken().isPresent()) {
			String id_token = tokens.getIdToken().get();
			
			GoogleIdTokenVerifier verifier 
			= new GoogleIdTokenVerifier.Builder (new NetHttpTransport(), new JacksonFactory())
				.setAudience(Collections.singletonList(this.client_id))
				.build();
			
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
				return Optional.empty();
			
			Payload payload = token.getPayload();
			
			user = new User.Builder(payload.getSubject())
						.setEmail(payload.getEmail())
						.setName((String) payload.get("name"))
						.setFamilyName((String) payload.get("family_name"))
						.setFirstName((String) payload.get("given_name"))
						.build();
		}
		
		Optional<User> ret = Optional.ofNullable(user);
		this.logger.traceExit(ret.toString());
		return ret;
	}

}
