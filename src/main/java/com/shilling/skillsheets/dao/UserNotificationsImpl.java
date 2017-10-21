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
	private final ModelWriter writer;
	
	@Autowired
	private UserNotificationsImpl(ModelWriter writer) {
		this.logger = LogManager.getLogger(UserNotificationsImpl.class);
		this.writer = writer;
	}

	@Override
	public int getNextMessageId(User user) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void saveMessage(User user, Notification notification) {
		this.logger.traceEntry("Sending message to " + user);
		boolean ret = this.writer.write(user, notification);
		this.logger.traceExit("Successful: " + ret);
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

	@Override
	public Notification[] getAllMessages(User user) {
		return new Notification[0];
	}



}
