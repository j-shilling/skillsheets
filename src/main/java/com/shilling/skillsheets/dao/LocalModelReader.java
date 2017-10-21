package com.shilling.skillsheets.dao;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shilling.skillsheets.model.User;

public class LocalModelReader implements ModelReader {
	
	private final Logger logger;
	private final LocalFiles files;
	
	@Autowired
	private LocalModelReader (LocalFiles files) {
		this.logger = LogManager.getLogger(LocalModelReader.class);
		this.files = files;
	}

	@Override
	public InputStream getNotificationsInputStream(User user) {
		this.logger.traceEntry("Getting notification stream for " + user);
		File file = this.files.notifications(user);
		
		try (RandomAccessFile in = new RandomAccessFile(file, "rw")) {
			
			FileLock lock = in.getChannel().lock();
			byte[] arr = new byte[(int)in.length()];
			in.readFully(arr);
			lock.release();
			in.close();
			
			ByteArrayInputStream ret = new ByteArrayInputStream(arr);
			return ret;
			
		} catch (IOException e) {
			this.logger.error(e.getMessage());
			byte[] empty = new byte[0];
			return new ByteArrayInputStream(empty);
		}
	}

}
