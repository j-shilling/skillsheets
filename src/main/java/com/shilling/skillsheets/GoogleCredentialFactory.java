package com.shilling.skillsheets;

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
		this.logger = LogManager.getLogger(GoogleDriveFactory.class);
		this.jsonFactory = JacksonFactory.getDefaultInstance();

		HttpTransport httpTransport = null;
		FileDataStoreFactory dataStoreFactory = null;
		try {

			httpTransport = GoogleNetHttpTransport.newTrustedTransport();
			dataStoreFactory = new FileDataStoreFactory (new java.io.File(System.getProperty("user.home"), ".credentials/skill-sheets"));
			

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
		if (!user.getId().isPresent())
			return Optional.empty();
		
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
			
			Optional<Credential> ret = Optional.ofNullable(flow.loadCredential(user.getId().get()));
			if (ret.isPresent())
				return ret;
			
			if (!user.getAuthCode().isPresent())
				return Optional.empty();
			
			/*GoogleTokenResponse response = 
					new GoogleAuthorizationCodeTokenRequest(
							this.httpTransport,
							this.jsonFactory,
							"https://www.googleapis.com/oauth2/v4/token",
							clientSecrets.getDetails().getClientId(),
				            clientSecrets.getDetails().getClientSecret(),
				            user.getAuthCode().get(),
				            "").execute();*/
			TokenResponse response = flow.newTokenRequest(user.getAuthCode().get())
					.setRedirectUri("http://localhost:8080/login")
					.execute(); 
			
			return Optional
				//	.ofNullable(new GoogleCredential().setAccessToken(response.getAccessToken()));
					.ofNullable(flow.createAndStoreCredential(response, user.getId().get()));
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return Optional.empty();
		}
	}
}
