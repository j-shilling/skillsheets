package com.shilling.skillsheets;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.shilling.skillsheets.model.User;

public class GoogleDriveFactory {

	private final Logger logger;
	private final GoogleCredentialFactory credentials;
	private final JsonFactory jsonFactory;
	private final HttpTransport httpTransport;
	
	@Autowired
	private GoogleDriveFactory(GoogleCredentialFactory credentials) {
		this.logger = LogManager.getLogger(GoogleDriveFactory.class);
		this.credentials = credentials;
		
		this.jsonFactory = JacksonFactory.getDefaultInstance();

		HttpTransport httpTransport = null;
		try {

			httpTransport = GoogleNetHttpTransport.newTrustedTransport();	

		} catch (GeneralSecurityException e) {
			this.logger.error(e.getMessage());
		} catch (IOException e) {
			this.logger.error(e.getMessage());
		}

		this.httpTransport = httpTransport;
	}

	public Optional<Drive> getDrive(final User user) {

		this.logger.traceEntry("Trying to get a Drive service object.");
		Optional<Credential> credential = this.credentials.authorize(user);
		if (!credential.isPresent()) {
			this.logger.error("Could not get a Credential instance.");
			return Optional.empty();
		}

		Optional<Drive> ret = Optional.of(new Drive.Builder(this.httpTransport, this.jsonFactory, credential.get())
				.setApplicationName("Skill Sheets").build());
		
		this.logger.traceExit("Successful: " + ret.isPresent());
		return ret;
	}
}
