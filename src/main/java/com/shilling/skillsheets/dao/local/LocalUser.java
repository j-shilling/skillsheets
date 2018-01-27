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
import java.util.UUID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Preconditions;
import com.shilling.skillsheets.dao.SkillSheet;
import com.shilling.skillsheets.dao.User;

/**
 * Represents a user account saved in local file storage.
 * 
 * @author Jake Shilling
 *
 */
class LocalUser implements User {

	private final Logger logger = LogManager.getLogger(LocalUser.class);
	private final UUID uuid;
	private final File file;
	private final LocalUserDao dao;

	public LocalUser(LocalUserDao dao, File file, UUID uuid) {
		Preconditions.checkNotNull(file, "This user does not have a file");
		Preconditions.checkArgument(file.exists(), "This user's file does not exist");
		Preconditions.checkArgument(file.isFile(), "This user's file is not a normal file");

		this.uuid = uuid;
		this.file = file;
		this.dao = dao;
	}

	private File getFile() {
		Preconditions.checkState(this.file.exists(), "This user has been deleted");
		return this.file;
	}

	private Properties getProperties() throws IOException {
		Properties ret = new Properties();

		try {
			ret.load(new FileInputStream(this.getFile()));
		} catch (IOException e) {
			throw new IOException("Could not load properties file", e);
		}

		return ret;
	}

	private void setProperty(String key, String value) {

		try {
			Properties props = this.getProperties();
			props.setProperty(key, value);
			props.store(new FileWriter(this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}

	}

	private void addProperty(String key, String item) {

		try {
			Properties props = this.getProperties();
			String value = props.getProperty(key) + "," + item;
			props.setProperty(key, value);
			props.store(new FileWriter(this.getFile()), "Setting " + key + " to " + value);
		} catch (IOException e) {
			this.logger.error("Could not update user: " + e.getMessage());
			return;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isTeacher() throws IOException {
			return Boolean.valueOf(this.getProperties().getProperty("teacher"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User setName(String name) {
		this.setProperty("name", name);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getName() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("name"));
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	@Override
	public User setId(String id) throws IOException {
		
		this.dao.updateKey(this.getProperties().getProperty("id"), id, LocalUser.this);
		this.setProperty("id", id);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getId() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("id"));
	}

	/**
	 * {@inheritDoc}
	 * @throws IOException 
	 */
	@Override
	public User setEmail(String email) throws IOException {
		this.dao.updateKey(this.getProperties().getProperty("email"), email, this);
		this.setProperty("email", email);
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<String> getEmail() throws IOException {
		return Optional.ofNullable(this.getProperties().getProperty("email"));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public User addSkillSheet(SkillSheet skillsheet) {
		addProperty("skillsheets", skillsheet.getUuid());
		
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Collection<String> getSkillSheets() throws IOException {
		String values = this.getProperties().getProperty("skillsheets");
		return new HashSet<>(Arrays.asList(values.split(",")));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete() {
		this.getFile().delete();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LocalUser)
			return this.uuid.equals(((LocalUser) obj).uuid);
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(this.uuid);
	}

	@Override
	public UUID getUuid() {
		return this.uuid;
	}

}
