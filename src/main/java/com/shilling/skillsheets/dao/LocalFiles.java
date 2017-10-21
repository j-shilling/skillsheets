package com.shilling.skillsheets.dao;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.shilling.skillsheets.model.User;

public class LocalFiles {

	private final Logger logger;
	private final File basedir;
	
	private LocalFiles () {
		this.logger = LogManager.getLogger(LocalFiles.class);
		this.basedir = new File (System.getProperty("user.home"), ".skillsheets");
		this.basedir.mkdir();
	}
	
	private File getUserDir (User user) {
		this.logger.traceEntry("Finding dir for " + user);
		
		if (user.getId().isPresent()) {
			File ret = new File (this.basedir, user.getId().get());
			ret.mkdir();
			return ret;
		} else {
			return this.basedir;
		}
	}
	
	public File notifications(User user) {
		this.logger.traceEntry("Getting notifications file for " + user);
		
		File ret = new File (this.getUserDir(user), "/notifications.xml");
		
		try {
			ret.createNewFile();
		} catch (IOException e) {
			this.logger.error("Could not create " + ret + ": " + e.getMessage());
			return ret;
		}
		
		this.logger.traceExit(ret.toString());
		return ret;
	}
}
