package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.api.client.util.Preconditions;
import com.shilling.skillsheets.dao.ModelEncoder;
import com.shilling.skillsheets.model.User;

public class LocalFiles {

	private final Logger logger;
	private final File basedir;
	private final String fileExtension;

	@Autowired
	private LocalFiles(ModelEncoder encoder) {
		Preconditions.checkNotNull(encoder);

		this.logger = LogManager.getLogger(LocalFiles.class);
		this.basedir = new File(System.getProperty("user.home"), ".skillsheets");
		this.basedir.mkdir();

		String extension = encoder.getFileExtension();
		Preconditions.checkNotNull(extension);
		Preconditions.checkArgument(!extension.isEmpty());

		if (extension.charAt(0) == '.')
			this.fileExtension = extension;
		else
			this.fileExtension = ".".concat(extension);
	}

	private File getUserDir(User user) {
		this.logger.traceEntry("Finding dir for " + user);

		File ret = new File(this.basedir, user.getId());
		ret.mkdir();
		return ret;
	}

	public File getFile(User user, Class<?> klass) {
		this.logger.traceEntry("Getting " + klass.getSimpleName() + " file for " + user);

		File ret = new File(this.getUserDir(user), klass.getSimpleName().toLowerCase() + this.fileExtension);

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
