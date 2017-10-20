package com.shilling.skillsheets.dao;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.shilling.skillsheets.GoogleDriveFactory;
import com.shilling.skillsheets.model.Notification;
import com.shilling.skillsheets.model.User;

public class UserNotificationsImpl implements UserNotifications {
	
	private final Logger logger;
	private final GoogleDriveFactory driveFactory;
	
	@Autowired
	private UserNotificationsImpl(GoogleDriveFactory driveFactory) {
		this.logger = LogManager.getLogger(UserNotificationsImpl.class);
		this.driveFactory = driveFactory;
	}

	@Override
	public int getNextMessageId(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void saveMessage(User user, Notification notification) {
		
		this.logger.traceEntry();
		
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("notifications");
		Element message = root.addElement("notification")
				.addAttribute("id", Integer.toString(notification.getId()));
		
		message.addElement("text").addText(notification.getMessage());
		for (Notification.Action action : notification.getAction())
			message.addElement("Action").addText(Integer.toString(action.getValue()));
		
		OutputFormat format = OutputFormat.createPrettyPrint();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			XMLWriter writer = new XMLWriter (out, format);
			writer.write(doc);
			
			InputStream in = new ByteArrayInputStream(out.toByteArray());
			InputStreamContent content = new InputStreamContent("text/xml", in);
			
			Optional<Drive> result = this.driveFactory.getDrive(user);
			if (!result.isPresent()) {
				this.logger.error("Could not get Drive instance");
				return;
			}
			
			Drive drive = result.get();
			
			File fileMetadata = new File();
			fileMetadata.setName("notifications.xml");
			fileMetadata.setParents(Collections.singletonList("appDataFolder"));
			
			drive.files().create(fileMetadata, content)
					.setFields("id")
					.execute();
			
		} catch (UnsupportedEncodingException e) {
			this.logger.error(e.getMessage());
		} catch (IOException e) {
			this.logger.error(e.getMessage());
		}
		
	}

	@Override
	public Notification getMessage(User user, int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteMessage(User user, int id) {
		// TODO Auto-generated method stub
		
	}



}
