package com.shilling.skillsheets;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.DriveScopes;
import com.shilling.skillsheets.model.User;

public class GoogleCredentialFactory {
	private final Logger logger;
	private final JsonFactory jsonFactory;
	private final HttpTransport httpTransport;
	private final DataStoreFactory dataStoreFactory;
	private final List<String> scopes;

	private GoogleCredentialFactory() {
		this.logger = LogManager.getLogger(GoogleCredentialFactory.class);
		this.jsonFactory = JacksonFactory.getDefaultInstance();

		HttpTransport httpTransport = null;
		FileDataStoreFactory dataStoreFactory = null;
		try {

			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = 
					new FileDataStoreFactory (new File(System.getProperty("user.home"), ".credentials/skill-sheets"));
			

		} catch (GeneralSecurityException e) {
			this.logger.error(e.getMessage());
		} catch (IOException e) {
			this.logger.error(e.getMessage());
		}

		this.httpTransport = httpTransport;
		this.dataStoreFactory = dataStoreFactory;
		this.scopes = Arrays.asList(DriveScopes.DRIVE_APPDATA);

		Arrays.asList(DriveScopes.DRIVE_APPDATA);
	}

	public Optional<Credential> authorize(final User user) {
		this.logger.traceEntry("Authorizing " + user.toString());
		
		if (!user.getId().isPresent()) {
			this.logger.warn("Cannot authorize without a User ID.");
			this.logger.traceExit("Optional.empty()");
			return Optional.empty();
		}
		
		InputStream in = this.getClass().getResourceAsStream("/client_secret.json");
		
		try {
			GoogleClientSecrets clientSecrets = 
					GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(in));
			GoogleAuthorizationCodeFlow flow =
	                new GoogleAuthorizationCodeFlow.Builder(
	                        this.httpTransport, this.jsonFactory, clientSecrets, this.scopes)
	                .setDataStoreFactory(this.dataStoreFactory)
	                .setAccessType("offline")
	                .build();
			
			this.logger.trace("Checking if credentials are stored for user.");
			Optional<Credential> ret = Optional.ofNullable(flow.loadCredential(user.getId().get()));
			if (ret.isPresent()) {
				this.logger.traceExit("Credentials were already stored.");
				return ret;
			}
			this.logger.trace("Credentials were not stored.");
			
			if (!user.getAuthCode().isPresent()) {
				this.logger.warn("Cannot request credentials without an authorization code from the client.");
				this.logger.traceExit("Optional.empty()");
				return Optional.empty();
			}
			
			this.logger.trace("Executing request for credentials.");
			TokenRequest request = flow.newTokenRequest(user.getAuthCode().get())
					.setRedirectUri("http://localhost:8080/oauth2callback");
			TokenResponse response = request.execute(); 
			
			ret = Optional
					.ofNullable(flow.createAndStoreCredential(response, user.getId().get()));
			this.logger.traceExit("Credentials received: " + ret.isPresent());
			return ret;
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return Optional.empty();
		}
	}
}
