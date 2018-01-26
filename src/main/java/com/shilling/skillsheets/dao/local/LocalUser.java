package com.shilling.skillsheets.dao.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.User;
import com.shilling.skillsheets.model.SkillSheet;

/**
 * Represents a user account saved in local file storage.
 * 
 * @author Jake Shilling
 *
 */
class LocalUser implements User {

	private final Logger logger = LogManager.getLogger(LocalUser.class);
	private final File file;
	private final LocalUserDao dao;
	
	public LocalUser (LocalUserDao dao, File file) {
		Preconditions.checkNotNull(file, "This user does not have a file");
		Preconditions.checkArgument(file.exists(), "This user's file does not exist");
		Preconditions.checkArgument(file.isFile(), "This user's file is not a normal file");
		
		this.file = file;
		this.dao = dao;
	}
	
	private File getFile () {
		Preconditions.checkState(this.getFile().exists(), "This user has been deleted");
		return this.getFile();
	}
	
	private Properties getProperties () throws IOException {
		Properties ret = new Properties();
		
		try {
			ret.load(new FileInputStream (this.getFile()));
		} catch (IOException e) {
			throw new IOException ("Could not load properties file", e);
		}
		
		return ret;
	}
	
	public synchronized void setProperty (String key, String value) {
		
		try {
			Properties props = this.getProperties();
			props.setProperty(key, value);
			props.store(new FileWriter (this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}

	}
	
	public synchronized void addProperty (String key, String item) {
		
		try {
			Properties props = this.getProperties();
			String value = props.getProperty(key) + "," + item;
			props.setProperty(key, value);
			props.store(new FileWriter (this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized boolean isTeacher() throws IOException {
		return Boolean.valueOf(this.getProperties().getProperty("teacher"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setName(String name) {

		new Thread (() -> {
				LocalUser.this.setProperty("name", name);
		}).start();
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getName() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("name"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setId(String id) {

		try {
			Optional<String> old = this.getId();
			if (old.isPresent())
				this.dao.updateKey(old.get(), id);
		} catch (IOException e) {
			this.logger.error("Error trying to read id: " + e.getMessage());
		}
		
		new Thread (() -> {
			LocalUser.this.setProperty("id", id);
		}).start();
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getId() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("id"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setEmail(String email) {

		try {
			Optional<String> old = this.getEmail();
			if (old.isPresent())
				this.dao.updateKey(old.get(), email);
		} catch (IOException e) {
			this.logger.error("Error trying to read email: " + e.getMessage());
		}
		
		new Thread (() -> {
			LocalUser.this.setProperty("email", email);
		}).start();
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Optional<String> getEmail() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("email"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User addSkillSheet(SkillSheet skillsheet) {
		new Thread (() -> {
			LocalUser.this.addProperty("skillsheets", skillsheet.getUuid());
		}).start();
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized Collection<String> getSkillSheets() throws IOException {
		String values = this.getProperties().getProperty("skillsheets");
		return new HashSet<>(Arrays.asList(values.split(",")));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void delete() {
		this.getFile().delete();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object obj) {
		if (obj instanceof LocalUser)
			return this.file.equals(((LocalUser) obj).file);
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode () {
		return Objects.hash(this.file);
	}

}
