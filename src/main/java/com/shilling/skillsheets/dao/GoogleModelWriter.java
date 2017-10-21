package com.shilling.skillsheets.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.shilling.skillsheets.GoogleDriveFactory;
import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public class GoogleModelWriter implements ModelWriter {
	
	private final Logger logger;
	private final GoogleDriveFactory driveFactory;
	private final ModelEncoder encoder;
	
	@Autowired
	private GoogleModelWriter (GoogleDriveFactory driveFactory,
			ModelEncoder encoder) {
		this.logger = LogManager.getLogger(GoogleModelWriter.class);
		this.driveFactory = driveFactory;
		this.encoder = encoder;
	}

	@Override
	public boolean write(User user, Notification notification) {
		this.logger.traceEntry("Saving " + notification);
		
		this.logger.trace("Encoding object.");
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		if (!this.encoder.encode(notification, null, out)) {
			this.logger.error("Could not encode object.");
			return false;
		}
		
		this.logger.trace("Trying to access Drive API.");
		Optional<Drive> result = this.driveFactory.getDrive(user);
		if (!result.isPresent()) {
			this.logger.error("Could not get Drive instance");
			return false;
		}
		
		Drive drive = result.get();
		
		File fileMetadata = new File();
		fileMetadata.setName("notifications.xml");
		fileMetadata.setParents(Collections.singletonList("appDataFolder"));
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		InputStreamContent content = new InputStreamContent("text/xml", in);
		
		try {
			drive.files().create(fileMetadata, content)
					.setFields("id")
					.execute();
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			return false;
		}
		
		return true;
	}

}
