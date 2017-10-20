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
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.shilling.skillsheets.model.User;

public class GoogleDriveFactory {

	private final Logger logger;
	private final JsonFactory jsonFactory;
	private final HttpTransport httpTransport;
	private final DataStoreFactory dataStoreFactory;
	private final List<String> scopes;

	private GoogleDriveFactory() {
		this.logger = LogManager.getLogger(GoogleDriveFactory.class);
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
		this.dataStoreFactory = MemoryDataStoreFactory.getDefaultInstance();
		this.scopes = Arrays.asList(DriveScopes.DRIVE_APPDATA);

		Arrays.asList(DriveScopes.DRIVE_APPDATA);
	}

	private Optional<Credential> authorize(final User user) {
		if (!user.getAuthCode().isPresent())
			return Optional.empty();
		
		InputStream in = this.getClass().getResourceAsStream("/client_secret.json");
		
		try {
			GoogleClientSecrets clientSecrets = 
					GoogleClientSecrets.load(this.jsonFactory, new InputStreamReader(in));
			GoogleTokenResponse response = 
					new GoogleAuthorizationCodeTokenRequest(
							this.httpTransport,
							this.jsonFactory,
							"https://www.googleapis.com/oauth2/v4/token",
							clientSecrets.getDetails().getClientId(),
				            clientSecrets.getDetails().getClientSecret(),
				            user.getAuthCode().get(),
				            "").execute();
			
			return Optional
					.ofNullable(new GoogleCredential().setAccessToken(response.getAccessToken()));
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return Optional.empty();
		}
	}

	public Optional<Drive> getDrive(final User user) {

		Optional<Credential> credential = this.authorize(user);
		if (!credential.isPresent()) {
			this.logger.error("Could not get a Credential instance.");
			return Optional.empty();
		}

		return Optional.of(new Drive.Builder(this.httpTransport, this.jsonFactory, credential.get())
				.setApplicationName("Skill Sheets").build());

	}
}
